# PC 端（xcs 电脑图传 2.0.15 / 鑫财神图传PC 2.0.14）总目录

| 子目录 | 内容 |
|---|---|
| `原始包/` | 两个原始 zip（xcs电脑图传(新).zip=2.0.15、鑫财神图传PC.zip=2.0.14） |
| `原始包解压/` | 解压后的原始样本（xcs.exe / distinguish.exe + res 音效） |
| `测试副本/` | 真机验证用副本：xcs-2015（还原版构建）、dist-2014、orig-2015（原版对照）；外部 INI 夹具 |
| `fixture_server/` | PC 协议 fixture（HTTP 8081 + 图片 80 + WS 18889，合约对照原始反编译），`python server.py` / `python selftest.py`（82 项） |

反编译基准与可编译工程在 `analysis/`（evidence 链被审计报告大量引用，保持原路径）：
- `analysis/xcs-cs`（2.0.15 原始反编译 + baml-strings 忠实性基准）、`analysis/xcs-cs112`（2.0.15 还原工程）
- `analysis/distinguish-cs`（2.0.14 原始反编译）、`analysis/distinguish-cs112`（2.0.14 还原工程）

文档：`docs/逆向①②`、`docs/A2`、`docs/A4`、`docs/UI设计①`、`docs/功能验证报告.md`。
测试工具：`.tools/invoke-btn.ps1`、`dump-buttons.ps1`、`read-dialog.ps1`、`invoke-idx.ps1`；
上一会话冒烟/全测记录：`.tools/smoke-ui/`（fulltest.ps1 + 日志）。
测试账号见 `docs/设备登录.md`（真实服务器，单点登录互踢；凭据文件按 AGENTS.md 政策不入库）。
