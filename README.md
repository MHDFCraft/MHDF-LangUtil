<p align="center">
   <img src="./Logo.png" width="200px" height="200px" alt="MHDF-LangUtil">
</p>

<div align="center">

# MHDF-LangUtil

_✨一款便携性的MC原版语言文件处理库✨_

_✨轻量 便携 快捷✨_

</div>

<p align="center">
    <a href="https://github.com/MHDFCraft/MHDF-LangUtil/issues">
        <img src="https://img.shields.io/github/issues/MHDFCraft/MHDF-LangUtil?style=flat-square" alt="issues">
    </a>
    <a href="https://github.com/MHDFCraft/MHDF-LangUtil/blob/main/LICENSE">
        <img src="https://img.shields.io/github/license/MHDFCraft/MHDF-LangUtil?style=flat-square" alt="license">
    </a>
    <a href="https://qm.qq.com/cgi-bin/qm/qr?k=T047YB6lHNMMcMuVlK_hGBcT5HNESxMA&jump_from=webapi&authKey=0/IFGIO6xLjjHB2YKF7laLxkKWbtWbDhb1lt//m7GgbElJSWdRZ8RjbWzSsufkO6">
        <img src="https://img.shields.io/badge/QQ群-129139830-brightgreen?style=flat-square" alt="qq-group">
    </a>
</p>

<div align="center">
    <a href="https://github.com/MHDFCraft/MHDF-LangUtil/pulse">
        <img src="https://repobeats.axiom.co/api/embed/e58f3e1358766291db33ba451d3e90be99811f4f.svg" alt="pulse">
    </a>
</div>

## 使用方法

1. 将本库导入至您的项目
2. 创建一个LangAPI实例就像这样:
   ```java
   LangAPI langAPI = new LangAPI(this, new File(getDataFolder(), "minecraftLang"));
   ```
3. 然后下载并重载语言文件
   ```java
   LangManager langManager = langAPI.getLangManager();
   langManager.downloadLang(); 
   langManager.reloadLang();
   ```
4. 然后就可以像这样获取一个物品实例的中文名称了
   ```java
   langManager.getItemName(itemStack);
   ```

## 贡献者

<a href="https://github.com/MHDFCraft/MHDF-LangUtil/graphs/contributors">
  <img src="https://stg.contrib.rocks/image?repo=MHDFCraft/MHDF-LangUtil" alt="contributors"/>
</a>

## 精神支柱

- [Xiao-MoMi](https://github.com/Xiao-MoMi)

## Star

[![Stargazers over time](https://starchart.cc/MHDFCraft/MHDF-LangUtil.svg?variant=adaptive)](https://starchart.cc/MHDFCraft/MHDF-LangUtil)

## 友链

<div>
    <a href="https://plugin.mhdf.cn/">插件文档</a>
    <a href="https://www.mhdf.cn/">梦回东方</a>
    <a href="https://www.yuque.com/xiaoyutang-ayhvn/rnr4ym/">鱼酱の萌新开服教程</a>
</div>
