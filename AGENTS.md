# Repository Guidelines

## Project Structure & Module Organization

- `analysis/xcs-cs112/` and `analysis/distinguish-cs112/`: editable WPF reconstructions targeting `net8.0-windows`, x64. `distinguish.Common`, `distinguish.Model`, and `distinguish.ViewModel` separate networking, models, and UI logic; XAML lives under `distinguish/`.
- `analysis/xcs-cs/`, `analysis/distinguish-cs/`, original ZIPs, and extracted applications: reference evidence; preserve originals.
- `game-project/src/quesheng/`: reconstructed JavaFX sources; `game-project/resources/` contains FXML, CSS, and images. Ignored `game-src/` and `game-jars/` provide decompiled references and dependencies.
- `PC端/fixture_server/`: local PC backend and Python self-tests (moved 2026-11-12; old `fixture_server/` paths in older reports refer here). `game-fixture/`: game client HTTP(8081 on 127.0.0.3) + MobileIMSDK UDP 7901 fixture, with `tools/MakeSconfig.java` sconfig generator. `scripts/` contains environment checks; `docs/` contains evidence, repair notes, and the roadmap.

## Build, Test, and Development Commands

Run from the repository root on Windows:

```powershell
dotnet build analysis/xcs-cs112/xcs.csproj -c Release
dotnet build analysis/distinguish-cs112/distinguish.csproj -c Release
.\game-project\build.cmd
python -m pip install websockets cryptography
python PC端/fixture_server/server.py
python PC端/fixture_server/selftest.py
pwsh -File scripts/Test-ReverseEnvironment.ps1
```

The first two commands build PC clients. The Java build requires JDK 21, `game-jars/`, and `.sandbox-game/游戏端/jre-9/lib/modules`; it packages `game-project/out/quesheng.jar`. The fixture serves local HTTP/WebSocket traffic; self-tests start it when needed. The PowerShell check requires version 7.2+ and writes `.tools/reverse-environment.json`.

## Coding Style & Naming Conventions

Match surrounding formatting: tabs in C#, four spaces in Java, Python, and PowerShell. Preserve original namespaces, class names, protocol field spellings, resource paths, and XAML bindings. Keep mechanical decompiler repairs separate from intentional behavior changes. No shared formatter configuration is checked in; avoid unrelated reformatting. Preserve existing batch-file encoding when editing Chinese paths.

## Testing Guidelines

Testing uses custom Python self-tests and PowerShell probes; no coverage threshold is configured. Extend `PC端/fixture_server/selftest.py` using `test_<number>_<scenario>` functions. Cover successful and rejected protocol requests, RSA exchange, WebSocket/QoS, and image decoding. Build affected clients; report GUI validation separately from compilation. Documentation-only changes require command/path and Markdown checks.

## Commit & Pull Request Guidelines

History currently contains one descriptive Chinese commit; no enforced prefix convention exists. Use concise, scoped subjects. PRs should explain changed behavior, cite source evidence, link relevant issues, list validation results, and include screenshots for UI changes. Update relevant status documents and preserve unrelated working-tree changes.

## Configuration & Evidence Handling

Keep tests on the default `127.0.0.2` fixture: HTTP 8081, images 80, WebSocket 18889. Game UDP 7901 requires separate validation. Build without launching original executables. Exclude credentials, local INI settings, captures, logs, and generated outputs from commits; respect `.gitignore`.
