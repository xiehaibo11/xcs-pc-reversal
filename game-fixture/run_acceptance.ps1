# run_acceptance.ps1 - game client runtime acceptance in local fixture env.
# ASCII-only script (avoids PS5.1 codepage mojibake); Unicode matched via [char] codes.
# Flow: start fixtures -> generate sconfig (MakeSconfig, real jar) -> launch client exe
#       -> UIA-click login button -> collect evidence (error.log/requests.log/udp.log/screenshots)
#       -> kill client (<=100s lifetime) -> kill fixtures.
# Red lines: bind loopback only; never touch 8.137.196.28; client lifetime <=120s.
param(
  [string]$RepoRoot = 'F:\xcs-pc-reversal',
  [string]$SandboxDir = 'F:\xcs-pc-reversal\.sandbox-game',
  [int]$ObserveSec = 30,
  [int]$MaxClientLifeSec = 100,
  [switch]$RejectIM,
  [switch]$PushTiles
)
$ErrorActionPreference = 'Continue'
$FixtureDir = Join-Path $RepoRoot 'game-fixture'
$RuntimeDir = Join-Path $FixtureDir 'runtime'
$ToolsDir = Join-Path $RepoRoot 'tools'
$JarsDir = Join-Path $RepoRoot 'game-jars'
$EvDir = Join-Path $FixtureDir 'evidence'
New-Item -ItemType Directory -Force -Path $RuntimeDir, $EvDir | Out-Null

function Log($m) {
  $ts = Get-Date -Format 'HH:mm:ss.fff'
  $line = "[$ts] $m"
  Write-Host $line
  try { [System.IO.File]::AppendAllText((Join-Path $RuntimeDir 'acceptance.log'), "$line`r`n", [System.Text.Encoding]::UTF8) } catch { Write-Host 'LOG-WRITE-FAIL' }
}
function Kill-Tree([int]$ProcId, [string]$Label) {
  try { taskkill /PID $ProcId /T /F 2>$null | Out-Null; Log "KILLED $Label pid=$ProcId" } catch { Log "KILL-FAIL $Label pid=$ProcId : $_" }
}

# ---- 0. defensive cleanup of leftovers from previous runs ----
Get-Process -Name 'java', 'javaw' -ErrorAction SilentlyContinue | Where-Object { $_.Path -like "$RepoRoot*" -or $_.CommandLine -like '*MakeSconfig*' } | Stop-Process -Force -ErrorAction SilentlyContinue
Remove-Item (Join-Path $RuntimeDir '*.txt') -Force -ErrorAction SilentlyContinue

$swTotal = [System.Diagnostics.Stopwatch]::StartNew()
$runStart = Get-Date
Log "=== game client acceptance run start (repo=$RepoRoot) ==="

# ---- 1. start UDP fixture ----
$udpArgs = "`"$FixtureDir\udp_server.py`" --runtime-dir `"$RuntimeDir`""
if ($RejectIM) { $udpArgs = "$udpArgs --reject-login" }
if ($PushTiles) { $udpArgs = "$udpArgs --push-tile-round --push-delay 3" }
$udpProc = Start-Process -FilePath 'python' -ArgumentList $udpArgs -PassThru -WindowStyle Hidden -RedirectStandardOutput (Join-Path $RuntimeDir 'udp_stdout.log') -RedirectStandardError (Join-Path $RuntimeDir 'udp_stderr.log')
Log "udp_server.py started pid=$($udpProc.Id) rejectIM=$RejectIM pushTiles=$PushTiles"

# ---- 2. start HTTP fixture ----
$httpArgs = "`"$FixtureDir\game_server.py`" --runtime-dir `"$RuntimeDir`""
if ($PushTiles) { $httpArgs = "$httpArgs --mj-configs --yizhuo-sample" }
$httpProc = Start-Process -FilePath 'python' -ArgumentList $httpArgs -PassThru -WindowStyle Hidden -RedirectStandardOutput (Join-Path $RuntimeDir 'http_stdout.log') -RedirectStandardError (Join-Path $RuntimeDir 'http_stderr.log')
Log "game_server.py started pid=$($httpProc.Id) mjConfigs=$PushTiles yizhuoSample=$PushTiles"

# ---- 3. wait for fixtures to publish their bind IPs ----
$httpIp = $null
for ($i = 0; $i -lt 20; $i++) {
  Start-Sleep -Milliseconds 500
  $f = Join-Path $RuntimeDir 'http_ip.txt'
  if (Test-Path $f) { $httpIp = (Get-Content $f -Raw).Trim(); break }
}
if (-not $httpIp) { Log 'FATAL: http_ip.txt not published'; Kill-Tree $httpProc.Id 'http'; Kill-Tree $udpProc.Id 'udp'; exit 2 }
Log "HTTP fixture bound at $httpIp`:8081"
$udpIp = '127.0.0.1'
$uf = Join-Path $RuntimeDir 'udp_ip.txt'
if (Test-Path $uf) { $udpIp = (Get-Content $uf -Raw).Trim() }
Log "UDP fixture bound at $udpIp`:7901"

# ---- 4. generate sconfig.config via real jar classes, place into client dir ----
$clientDir = $null
foreach ($cand in @($SandboxDir, (Join-Path $SandboxDir ([char]0x6E38 + [char]0x620F + [char]0x7AEF)))) {
  if (Test-Path (Join-Path $cand '*.exe')) { $clientDir = $cand; break }
}
if (-not $clientDir) { $clientDir = $SandboxDir }
$exeFile = Get-ChildItem -Path $clientDir -Filter '*.exe' | Select-Object -First 1
if (-not $exeFile) { Log "FATAL: no exe in $clientDir"; exit 3 }
Log "client dir: $clientDir ; exe: $($exeFile.Name)"

$sconfigPath = Join-Path $clientDir 'sconfig.config'
$javaOut = & java -cp "$ToolsDir;$JarsDir\QueshengDaPai.jar" MakeSconfig gen $sconfigPath $httpIp 'XCS232' $env:GF_PASS 2>&1
Log "MakeSconfig: $($javaOut -join ' | ')"
if (-not (Test-Path $sconfigPath)) { Log 'FATAL: sconfig.config not generated'; exit 4 }

# ---- 5. launch the game client ----
$swClient = [System.Diagnostics.Stopwatch]::StartNew()
$gameProc = Start-Process -FilePath $exeFile.FullName -WorkingDirectory $clientDir -PassThru
Log "client exe launched pid=$($gameProc.Id) (lifetime cap ${MaxClientLifeSec}s)"
# external watchdog: enforce <=120s red line even if THIS script hangs
$wdScript = Join-Path $RuntimeDir 'watchdog.ps1'
Set-Content -Path $wdScript -Encoding UTF8 -Value "param([int]\$P,[int]\$Sec) Start-Sleep -Seconds \$Sec; \$p = Get-Process -Id \$P -ErrorAction SilentlyContinue; if (\$p) { taskkill /PID \$P /T /F 2>\$null | Out-Null }"
$wdProc = Start-Process -FilePath 'powershell' -ArgumentList '-NoProfile', '-ExecutionPolicy', 'Bypass', '-File', "`"$wdScript`"", '-P', "$($gameProc.Id)", '-Sec', "$($MaxClientLifeSec + 20)" -PassThru -WindowStyle Hidden
Log "watchdog pid=$($wdProc.Id) kills client at $($MaxClientLifeSec + 20)s if still alive"

Add-Type -AssemblyName UIAutomationClient
Add-Type -AssemblyName UIAutomationTypes
$auto = [System.Windows.Automation.AutomationElement]
$btnCond = New-Object System.Windows.Automation.PropertyCondition($auto::ControlTypeProperty, [System.Windows.Automation.ControlType]::Button)

# helper: screenshot full screen (bring game window to front first to avoid occlusion)
Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing
function Shot([string]$Name) {
  try {
    # only activate when the client has a real top-level window (guards against AppActivate hang)
    try {
      $winCond = New-Object System.Windows.Automation.PropertyCondition($auto::ProcessIdProperty, $gameProc.Id)
      $curWins = $auto::RootElement.FindAll([System.Windows.Automation.TreeScope]::Children, $winCond)
      $titled = $false
      foreach ($cw in $curWins) { if ($cw.Current.Name -and $cw.Current.Name.Length -gt 0) { $titled = $true; break } }
      if ($titled) {
        $wshell2 = New-Object -ComObject WScript.Shell
        $null = $wshell2.AppActivate($gameProc.Id)
        Start-Sleep -Milliseconds 700
      } else { Log "ACTIVATE-SKIP ${Name}: no titled window" }
    } catch { Log "ACTIVATE-FAIL ${Name}: $_" }
    $b = New-Object System.Drawing.Bitmap([System.Windows.Forms.SystemInformation]::VirtualScreen.Width, [System.Windows.Forms.SystemInformation]::VirtualScreen.Height)
    $g = [System.Drawing.Graphics]::FromImage($b)
    $g.CopyFromScreen(0, 0, 0, 0, $b.Size)
    $p = Join-Path $EvDir $Name
    $b.Save($p, [System.Drawing.Imaging.ImageFormat]::Png)
    $g.Dispose(); $b.Dispose()
    Log "SCREENSHOT $p"
  } catch { Log "SCREENSHOT-FAIL ${Name}: $_" }
}

# ---- 6. wait for login window, then UIA-invoke the login button ----
$loginClicked = $false
$sawRealWindow = $false
$uiaNames = ''
$deadline = $swClient.Elapsed.TotalSeconds + 70
while ($swClient.Elapsed.TotalSeconds -lt $deadline) {
  if ($swClient.Elapsed.TotalSeconds -gt $MaxClientLifeSec) { break }
  Start-Sleep -Seconds 2
  $wins = $auto::RootElement.FindAll([System.Windows.Automation.TreeScope]::Children, (New-Object System.Windows.Automation.PropertyCondition($auto::ProcessIdProperty, $gameProc.Id)))
  if ($wins.Count -eq 0) { continue }
  foreach ($w in $wins) {
    if ($w.Current.Name -and $w.Current.Name.Length -gt 0) { $sawRealWindow = $true }
    $btns = $w.FindAll([System.Windows.Automation.TreeScope]::Descendants, $btnCond)
    $names = @()
    for ($i = 0; $i -lt $btns.Count; $i++) { $names += $btns.Item($i).Current.Name }
    $uiaNames = ($names -join ' / ')
    Log "UIA window '$($w.Current.Name)' class='$($w.Current.ClassName)' buttons=[$uiaNames]"
    $deng = [char]0x767B  # 'DENG' first char of the login button caption
    for ($i = 0; $i -lt $btns.Count; $i++) {
      $nm = $btns.Item($i).Current.Name
      if ($nm -and $nm.Contains($deng)) {
        try {
          $btns.Item($i).GetCurrentPattern([System.Windows.Automation.InvokePattern]::Pattern).Invoke()
          $loginClicked = $true
          Log "UIA-INVOKE login button idx=$i name='$nm'"
        } catch { Log "UIA-INVOKE-FAIL: $_" }
        break
      }
    }
    if ($loginClicked) { break }
  }
  if ($loginClicked) { break }
}
if (-not $loginClicked -and $sawRealWindow) {
  Log 'UIA login button not found but real window seen -> fallback SendKeys SPACE/ENTER (login.requestFocus() in code)'
  try {
    $wshell = New-Object -ComObject WScript.Shell
    $null = $wshell.AppActivate($gameProc.Id)
    Start-Sleep -Milliseconds 300
    Add-Type -AssemblyName System.Windows.Forms
    [System.Windows.Forms.SendKeys]::SendWait(' ')
    Start-Sleep -Milliseconds 800
    [System.Windows.Forms.SendKeys]::SendWait('{ENTER}')
    $loginClicked = $true
    Log 'SendKeys SPACE+ENTER sent'
  } catch { Log "SENDKEYS-FAIL: $_" }
} elseif (-not $loginClicked) {
  Log 'no real client window appeared - skip SendKeys (client startup slow/hung; watchdog will enforce cap)'
}
Log "login click phase done clicked=$loginClicked at client-life=$([int]$swClient.Elapsed.TotalSeconds)s"

# ---- 7. observe phase: screenshots + process alive checks ----
# UIA helper: invoke a client button whose Name contains given chars (used for 获取)
function Invoke-ClientButton([int[]]$CodePoints, [string]$Label) {
  $findCond = New-Object System.Windows.Automation.PropertyCondition($auto::ProcessIdProperty, $gameProc.Id)
  $wins2 = $auto::RootElement.FindAll([System.Windows.Automation.TreeScope]::Children, $findCond)
  foreach ($w2 in $wins2) {
    $btns2 = $w2.FindAll([System.Windows.Automation.TreeScope]::Descendants, $btnCond)
    for ($i = 0; $i -lt $btns2.Count; $i++) {
      $nm = $btns2.Item($i).Current.Name
      if ($nm) {
        $match = $true
        foreach ($cp in $CodePoints) { if (-not $nm.Contains([char]$cp)) { $match = $false; break } }
        if ($match) {
          try {
            $btns2.Item($i).GetCurrentPattern([System.Windows.Automation.InvokePattern]::Pattern).Invoke()
            Log "UIA-INVOKE $Label button idx=$i name='$nm'"
            return $true
          } catch { Log "UIA-INVOKE-FAIL ${Label}: $_"; return $false }
        }
      }
    }
  }
  Log "UIA button not found: $Label"
  return $false
}

if ($PushTiles) {
  # Run C: push branch - bracket the chain with 3 screenshots
  Start-Sleep -Seconds 6      # login settles; push lands ~3s after IM login ACK
  Shot 'runC_1_pre_push.png'
  Start-Sleep -Seconds 9      # push sent + client jiance HTTP pull done
  Shot 'runC_2_post_push_counts.png'
  $huoqu = @(0x83B7, 0x53D6)  # 'HUO QU' - fetch button text
  $null = Invoke-ClientButton $huoqu 'HUOQU'
  Start-Sleep -Seconds 9      # huoqu -> yiZhuo/get -> begingamge -> displayWeinaPai
  Shot 'runC_3_post_render_tiles.png'
  $alive = $false
  try { $alive = -not $gameProc.HasExited } catch {}
  Log "PushTiles phase done clientAlive=$alive life=$([int]$swClient.Elapsed.TotalSeconds)s"
} else {
$shotAt = @()
$shotAt += [Math]::Min($ObserveSec, [Math]::Max(3, $MaxClientLifeSec - 15))
if ($ObserveSec -gt 12) { $shotAt += [Math]::Min($ObserveSec + 12, $MaxClientLifeSec - 5) }
$swObserve = [System.Diagnostics.Stopwatch]::StartNew()
while ($swObserve.Elapsed.TotalSeconds -lt $ObserveSec) {
  Start-Sleep -Seconds 2
  $alive = $false
  try { $alive = -not $gameProc.HasExited } catch {}
  $life = $swClient.Elapsed.TotalSeconds
  foreach ($t in $shotAt) {
    if ($swObserve.Elapsed.TotalSeconds -ge $t -and $swObserve.Elapsed.TotalSeconds -lt ($t + 2)) {
      Shot ("game_{0:d3}s.png" -f [int]$life)
    }
  }
  if ($life -gt $MaxClientLifeSec) { Log "client life cap reached (${life}s)"; break }
}
Shot 'game_final.png'
$stillAlive = $false
try { $stillAlive = -not $gameProc.HasExited } catch {}
Log "after observe: clientAlive=$stillAlive life=$([int]$swClient.Elapsed.TotalSeconds)s"
}

# ---- 8. kill client + fixtures ----
Kill-Tree $gameProc.Id 'client-exe'
Start-Sleep -Seconds 1
Get-Process -Name 'java', 'javaw' -ErrorAction SilentlyContinue | Where-Object { $_.StartTime -gt $runStart } | ForEach-Object { Log "stray java pid=$($_.Id) -> kill"; Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue }
Kill-Tree $httpProc.Id 'http-fixture'
Kill-Tree $udpProc.Id 'udp-fixture'
try { Stop-Process -Id $wdProc.Id -Force -ErrorAction SilentlyContinue; Log "KILLED watchdog pid=$($wdProc.Id)" } catch {}

# ---- 9. evidence summary ----
Log '--- error.log (tail) ---'
$errLog = Join-Path $clientDir 'error.log'
if (Test-Path $errLog) { Get-Content $errLog -Tail 40 | ForEach-Object { Log "  ERR| $_" } } else { Log '  (no error.log)' }
Log '--- requests.log (all) ---'
$rl = Join-Path $RuntimeDir 'requests.log'
if (Test-Path $rl) { Get-Content $rl | ForEach-Object { Log "  HTTP| $_" } } else { Log '  (no requests.log)' }
Log '--- udp.log (tail 60) ---'
$ul = Join-Path $RuntimeDir 'udp.log'
if (Test-Path $ul) { Get-Content $ul -Tail 60 | ForEach-Object { Log "  UDP| $_" } } else { Log '  (no udp.log)' }
Copy-Item $rl, $ul -Destination $EvDir -Force -ErrorAction SilentlyContinue
Copy-Item $errLog -Destination (Join-Path $EvDir 'error.log') -Force -ErrorAction SilentlyContinue
Log "=== acceptance run end (total $([int]$swTotal.Elapsed.TotalSeconds)s) ==="
