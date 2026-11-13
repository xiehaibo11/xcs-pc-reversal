#requires -Version 7.2
<#
.SYNOPSIS
Read-only checks for this project's Windows reverse engineering environment.
.DESCRIPTION
Does not launch samples, enable a proxy, attach a debugger, or capture traffic.
GUI tools are checked for presence; CLI tools must pass a bounded probe.
Results are written beneath the ignored .tools directory by default.
#>
[CmdletBinding()]
param(
    [string]$ToolsRoot = 'F:\逆向工具',
    [string]$DevRoot = 'F:\Dev',
    [string]$ReportPath = (Join-Path $PSScriptRoot '..\.tools\reverse-environment.json')
)

$ErrorActionPreference = 'Stop'
$rows = [System.Collections.Generic.List[object]]::new()
$localPrograms = Join-Path $env:LOCALAPPDATA 'Programs'
$jdk = Join-Path $DevRoot 'SDKs\JDK21\bin'
$debuggers = Join-Path ${env:ProgramFiles(x86)} 'Windows Kits\10\Debuggers'
$projectRoot = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))

function Add-Probe {
    param(
        [string]$Name,
        [string]$Category,
        [string]$Path,
        [string[]]$ProbeArgs = @(),
        [string]$Expected = '',
        [int[]]$AcceptedExitCodes = @(0),
        [switch]$PresenceOnly
    )

    $row = [ordered]@{
        Name = $Name; Category = $Category; Path = $Path
        Status = 'Missing'; ExecutableFileVersion = $null; ExitCode = $null; Detail = ''
    }
    if (Test-Path -LiteralPath $Path -PathType Leaf) {
        $row.ExecutableFileVersion = (Get-Item -LiteralPath $Path).VersionInfo.FileVersion
        if ($PresenceOnly) {
            $row.Status = 'Present'
            $row.Detail = 'File present; GUI interaction was not tested.'
        } else {
            $process = [System.Diagnostics.Process]::new()
            try {
                $process.StartInfo.FileName = $Path
                foreach ($arg in $ProbeArgs) { $process.StartInfo.ArgumentList.Add($arg) }
                $process.StartInfo.UseShellExecute = $false
                $process.StartInfo.CreateNoWindow = $true
                $process.StartInfo.RedirectStandardOutput = $true
                $process.StartInfo.RedirectStandardError = $true
                $process.StartInfo.Environment['JAVA_HOME'] = Split-Path $jdk
                if (-not $process.Start()) { throw 'Failed to start probe' }
                $stdout = $process.StandardOutput.ReadToEndAsync()
                $stderr = $process.StandardError.ReadToEndAsync()
                if (-not $process.WaitForExit(30000)) {
                    $process.Kill($true)
                    $process.WaitForExit()
                    throw 'Probe exceeded 30 seconds'
                }
                $output = ($stdout.GetAwaiter().GetResult() + "`n" + $stderr.GetAwaiter().GetResult()).Trim()
                $row.ExitCode = $process.ExitCode
                $row.Detail = $output
                $row.Status = if ($process.ExitCode -in $AcceptedExitCodes -and
                    (-not $Expected -or $output -match $Expected)) { 'Verified' } else { 'Failed' }
            } catch {
                $row.Status = 'Failed'
                $row.Detail = $_.Exception.Message
            } finally {
                $process.Dispose()
            }
        }
    }
    $rows.Add([pscustomobject]$row)
}

function Find-Executable([string]$Name) {
    $command = Get-Command $Name -CommandType Application -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($command) { return $command.Source }
    return (Join-Path $ToolsRoot ('missing\' + $Name))
}

Add-Probe '.NET SDK' 'Runtime' (Find-Executable 'dotnet.exe') @('--list-sdks') '\d+\.\d+'
Add-Probe '.NET 8 Desktop Runtime x64' 'Runtime' (Find-Executable 'dotnet.exe') @('--list-runtimes') 'Microsoft.WindowsDesktop.App 8\.'
Add-Probe 'Java 21' 'Runtime' (Join-Path $jdk 'java.exe') @('-version') 'version "21\.'
Add-Probe 'javap' 'Java' (Join-Path $jdk 'javap.exe') @('-version') '21\.'
Add-Probe '7-Zip' 'Archive' (Join-Path $DevRoot 'Tools\7zip\7z.exe') @('i') '7-Zip'
$ilspy = Join-Path $env:USERPROFILE '.dotnet\tools\ilspycmd.exe'
if (-not (Test-Path -LiteralPath $ilspy)) {
    # Newer tool packages can expose a native binary through a .cmd shim.
    $shim = Join-Path $env:USERPROFILE '.dotnet\tools\ilspycmd.cmd'
    if (Test-Path -LiteralPath $shim) {
        $contents = Get-Content -LiteralPath $shim -Raw
        $candidate = [regex]::Match($contents, '"([^"]+\.exe)"')
        if ($candidate.Success) {
            $ilspy = $candidate.Groups[1].Value.Replace('%~dp0', (Split-Path $shim) + '\')
        }
    }
}
Add-Probe 'ILSpy CLI' '.NET' $ilspy @('--version') 'ilspycmd:'
Add-Probe 'ILSpy GUI' '.NET' (Join-Path $localPrograms 'ILSpy\ILSpy.exe') -PresenceOnly
Add-Probe 'dnSpyEx GUI' '.NET' (Join-Path $ToolsRoot 'dnSpyEx\dnSpy.exe') -PresenceOnly
Add-Probe 'dnSpyEx CLI' '.NET' (Join-Path $ToolsRoot 'dnSpyEx\dnSpy.Console.exe') @('-t', 'distinguish.Common.Constant', (Join-Path $projectRoot 'PC端\原始包解压\xcs电脑图传\xcs.exe')) 'PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_LOGIN = 6'
Add-Probe 'CFR' 'Java' (Join-Path $jdk 'java.exe') @('-jar', (Join-Path $ToolsRoot 'CFR\cfr.jar'), '--version') 'CFR 0\.152'
Add-Probe 'JD-GUI' 'Java' (Join-Path $ToolsRoot 'JD-GUI\jd-gui-windows-1.6.6\jd-gui.exe') -PresenceOnly
Add-Probe 'JADX' 'Android' (Join-Path $jdk 'java.exe') @('-cp', (Join-Path $ToolsRoot 'JADX\lib\jadx-gui-1.5.6-all.jar'), 'jadx.cli.JadxCLI', '--version') '\d+\.\d+'
Add-Probe 'APKTool' 'Android' (Join-Path $jdk 'java.exe') @('-jar', (Join-Path $ToolsRoot 'APKTool\apktool.jar'), '--version') '\d+\.\d+'
Add-Probe 'ADB' 'Android' (Join-Path $DevRoot 'SDKs\Android\platform-tools\adb.exe') @('version') 'Android Debug Bridge'
Add-Probe 'Frida' 'Instrumentation' (Join-Path $ToolsRoot 'PythonEnv\Scripts\frida.exe') @('--version') '\d+\.\d+'
Add-Probe 'Objection' 'Instrumentation' (Join-Path $ToolsRoot 'PythonEnv\Scripts\objection.exe') @('version') 'objection:'
Add-Probe 'Androguard' 'Android' (Join-Path $ToolsRoot 'PythonEnv\Scripts\androguard.exe') @('--version') 'androguard'
Add-Probe 'Python binary analysis modules' 'Binary' (Join-Path $ToolsRoot 'PythonEnv\Scripts\python.exe') @('-c', 'import capstone, unicorn, keystone, lief, pefile, elftools; print("binary modules OK")') 'binary modules OK'
Add-Probe 'Detect It Easy CLI' 'Binary' (Join-Path $ToolsRoot 'Detect-It-Easy\die\diec.exe') @('--version') '\d+\.\d+'
Add-Probe 'Ghidra launcher' 'Native' (Join-Path $DevRoot 'Tools\Ghidra\ghidra_12.1.4_PUBLIC\ghidraRun.bat') -PresenceOnly
Add-Probe 'x64dbg' 'Native' (Join-Path $ToolsRoot 'x64dbg\release\x64\x64dbg.exe') -PresenceOnly
Add-Probe 'x32dbg' 'Native' (Join-Path $ToolsRoot 'x64dbg\release\x32\x32dbg.exe') -PresenceOnly
Add-Probe 'ImHex' 'Binary' (Join-Path $ToolsRoot 'ImHex\imhex.exe') -PresenceOnly
Add-Probe 'Wireshark' 'Network' (Join-Path $ToolsRoot 'Wireshark\Wireshark.exe') @('--version') 'Wireshark'
Add-Probe 'TShark' 'Network' (Join-Path $ToolsRoot 'Wireshark\tshark.exe') @('--version') 'TShark'
Add-Probe 'Npcap capture interfaces' 'Network' (Join-Path $ToolsRoot 'Wireshark\dumpcap.exe') @('-D') 'NPF_Loopback'
Add-Probe 'Fiddler Classic' 'Network' (Join-Path $localPrograms 'Fiddler\Fiddler.exe') -PresenceOnly
foreach ($tool in @(
    @('Process Monitor', 'ProcessMonitor\Procmon64.exe'),
    @('Process Explorer', 'ProcessExplorer\procexp64.exe'),
    @('ProcDump', 'Procdump\procdump64.exe'),
    @('TCPView', 'TCPView\tcpview64.exe'),
    @('DebugView', 'DebugView\Dbgview64.exe'),
    @('VMMap', 'VMMap\vmmap64.exe'),
    @('Autoruns', 'Autoruns\Autoruns64.exe')
)) {
    Add-Probe $tool[0] 'Sysinternals' (Join-Path $ToolsRoot ('Sysinternals\' + $tool[1])) -PresenceOnly
}
Add-Probe 'WinDbg classic x64' 'Native' (Join-Path $debuggers 'x64\windbg.exe') -PresenceOnly
Add-Probe 'WinDbg classic x86' 'Native' (Join-Path $debuggers 'x86\windbg.exe') -PresenceOnly
Add-Probe 'CDB x64' 'Native' (Join-Path $debuggers 'x64\cdb.exe') @('-version') 'cdb version'
Add-Probe 'CDB x86' 'Native' (Join-Path $debuggers 'x86\cdb.exe') @('-version') 'cdb version'

$report = [ordered]@{
    CheckedAt = (Get-Date).ToString('o')
    OS = [System.Runtime.InteropServices.RuntimeInformation]::OSDescription
    ToolsRoot = $ToolsRoot
    Note = 'Verified = CLI probe passed. Present = file exists, GUI not tested. ExecutableFileVersion refers to the probe executable (e.g. java.exe), not a JAR tool. Detail contains actual tool output. No samples executed.'
    Tools = $rows.ToArray()
}
$absoluteReport = [System.IO.Path]::GetFullPath($ReportPath)
New-Item -ItemType Directory -Path (Split-Path $absoluteReport) -Force | Out-Null
$report | ConvertTo-Json -Depth 6 | Set-Content -LiteralPath $absoluteReport -Encoding utf8
$rows | Select-Object Name, Status, ExecutableFileVersion | Format-Table -AutoSize
Write-Output "Report: $absoluteReport"
if ($rows.Where({ $_.Status -in 'Missing', 'Failed' }).Count) { exit 1 }
