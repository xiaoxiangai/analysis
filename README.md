# 小象日志采集--基于SDK埋点的用户行为分析日志采集

#### 介绍
小象电商系统上线后，需要收集用户行为数据，通过大数据实时分析实现电商业务数字化运营。小象智慧基于此强需求研发数字营销云平台产品，使用神策开源的埋点SDK完成终端行为上报，采用Nginx+Flume+kafka实现日志收集，采用Flink+ClickHouse架构实现OLAP的实时分析，同时数据会备份写入HDFS。

本开源项目内容包括nginx环境配置、Flume解密和日志格式处理、将明文数据存放到kafka的Topic下、Flink消费后将埋点数据存入HDFS的关键4步操作。为方便前期埋点的校验调优，在kafka环节，增加了埋点解析数据JSON格式存入MySQL。后续计划增加友盟和其他SDK厂商的埋点处理，以及业务系统日志的采集入库。

#### 软件完整架构
![输入图片说明](https://images.gitee.com/uploads/images/2021/0427/092847_e6c637d8_5325125.png "屏幕截图.png")


#### 项目主要内容

1.  日志接口配置文档（Nginx）
2.  日志收集代码（Flume）
3.  基于SDK的埋点方案

#### 使用说明

1.  小象数字营销云平台
数据分析地址：https://data.xiaoxiangai.com/   用户名密码：xiaoxiang1/123456

2.  小象电商微信端演示
![输入图片说明](https://images.gitee.com/uploads/images/2021/0427/103121_7638f5ba_5325125.png "屏幕截图.png")

3.  在微信小程序商城进行操作，数据会在小象数字营销云平台中动态显示。

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
