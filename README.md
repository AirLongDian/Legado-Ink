<p align="center">
  <img src="docs/icon4.svg" width="128" height="128" alt="Legado Ink Logo">
</p>

<h1 align="center">阅读·墨水屏 / Legado Ink</h1>


<p align="center">
  面向 Android 墨水屏设备的 Legado 优化版
</p>

<p align="center">
  <img alt="Android 4.4+" src="https://img.shields.io/badge/Android-4.4%2B-3DDC84?logo=android&logoColor=white">
  <img alt="E-Ink" src="https://img.shields.io/badge/Display-E--Ink-568FCC">
  <a href="LICENSE"><img alt="GPL-3.0" src="https://img.shields.io/badge/License-GPL--3.0-blue.svg"></a>
</p>

Legado Ink 基于 Legado 修改，目标是在墨水屏设备（包括老旧设备与低性能设备）上获得更直接的操作反馈、更少的闪烁与残影，以及低规格设备上更好的优化与性能。

本项目并非 Legado 官方版本。

## 主要变化

- 裁剪主题系统，仅保留水墨屏E-INK一套主题并略作调整
- 移除正文翻页的动画渲染与等待，大幅降低无动画翻页延迟与渲染负荷，并移除不适合墨水屏设备的其它模式。
- 移除界面动画过渡、涟漪、惯性滚动等不适合墨水屏且会导致残影的动态效果。
- 裁剪不适合水墨屏的部分样式设置（仍保留字号、字体、字重、行距、段距、缩进和页边距等排版能力）
- 移除墨水屏设备用不到的本地 Web 服务、网页管理端、WebSocket 和 MCP模块。
- 完整保留 WebDAV、书源、网络导入、本地书籍和阅读进度等核心功能。

## 兼容性

| 项目 | 要求 |
| --- | --- |
| 最低系统 | Android 4.4（API 19） |
| 目标设备 | 墨水屏阅读器 |
| 编译 SDK | Android SDK 30 |
| JDK | JDK 11 |



## 上游与许可

- 上游项目：[Wingsnail/legado-android-sdk19](https://github.com/Wingsnail/legado-android-sdk19)
- 本项目按照 [GNU General Public License v3.0](LICENSE) 发布。
- 第三方组件继续遵循各自的开源许可证。
