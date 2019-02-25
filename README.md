# JavaChatSystem
# 简单的Java聊天室

## Description
## 项目描述

+ Support group chat and DM(direct message),just a simple chat system.
+ 支持群聊、私聊的简单聊天系统

## Related Technology
## 相关技术

+ Java
+ Socket
+ Thread

## System Features
## 系统功能

+ Sign in and sign out
+ 注册及退出
+ DM(direct message)
+ 私聊消息
+ Group chat
+ 群聊

## 项目实现

+ 服务端：采用线程池调度执行服务器与客户端业务处理逻辑
+ 客户端：采用读写线程，分别处理交互与服务器数据接收

## 项目总结
+ 熟悉项目的开发流程（需求，分析，技术选择，设计，编码，测试，发布）
+ 掌握了Java的网络编程的常用的API和步骤
+ 提升了多线程的优点的认识熟练使用
+ 加深maven工具的理解和掌握

## 优化与扩展
+ 优化
    + 参数严格校验
    + 异常信息处理
    + 用户体验（信息展示）
+ 扩展
    + 数据存储到存储引擎（MySQL）
    + 注册信息存储
    + 增加登录功能（用户名+密码）
    + 历史消息存储

