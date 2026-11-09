<p align="center">
  <img src="docs/icon4.svg" width="128" height="128" alt="Legado Ink Logo">
</p>

<h1 align="center">Legado Ink</h1>

<p align="center">
  面向 Android 墨水屏设备的 Legado 精简适配版
</p>

<p align="center">
  <img alt="Android 8+" src="https://img.shields.io/badge/Android-8.0%2B-3DDC84?logo=android&logoColor=white">
  <img alt="E-Ink" src="https://img.shields.io/badge/Display-E--Ink-568FCC">
  <a href="LICENSE"><img alt="GPL-3.0" src="https://img.shields.io/badge/License-GPL--3.0-blue.svg"></a>
</p>

Legado Ink 基于 [Legado](https://github.com/LegadoTeam/legado) 裁剪，目标是在黑白墨水屏设备上获得更直接的操作反馈、更少的闪烁与残影，以及更简单的阅读界面。

本项目是独立维护的个人分支，并非 Legado 官方版本。

## 主要变化

- 固定使用白底黑字的墨水屏主题，移除日间、夜间及主题配置入口。
- 正文翻页固定为无动画直接切换，移除其他翻页引擎和动画设置。
- 移除全局界面过渡、涟漪、惯性滚动等不适合墨水屏的动态效果。
- 阅读样式只保留一个预设，移除正文颜色、背景颜色和背景图片设置。
- 保留字号、字体、字重、行距、段距、缩进和页边距等排版能力。
- 移除本地 Web 服务、网页管理端、WebSocket 和 MCP。
- 完整保留 WebDAV、书源、网络导入、本地书籍和阅读进度等核心功能。
- 暂时保留朗读功能，后续版本再单独评估是否裁剪。

## 兼容性

| 项目 | 要求 |
| --- | --- |
| 最低系统 | Android 8.0（API 26） |
| 目标设备 | 黑白墨水屏阅读器 |
| 编译 SDK | Android SDK 36 |
| JDK | JDK 21 |

当前依赖的 Rhino 运行时使用了 Android 8 引入的能力，因此本分支不再兼容 Android 7 及更早版本。

## 构建

准备 JDK 21 和 Android SDK 后，在项目根目录执行：

```bash
./gradlew :app:assembleAppDebug
```

Windows PowerShell：

```powershell
.\gradlew.bat :app:assembleAppDebug
```

生成的调试 APK 位于：

```text
app/build/outputs/apk/app/debug/
```

Debug 版本包名为 `com.legado.app.debug`，可与正式版分别安装。自行签名发布时，请先备份现有阅读数据。

## 上游与许可

- 上游项目：[LegadoTeam/legado](https://github.com/LegadoTeam/legado)
- 本项目按照 [GNU General Public License v3.0](LICENSE) 发布。
- 第三方组件继续遵循各自的开源许可证。

感谢 Legado 项目及所有上游开源组件的开发者。
