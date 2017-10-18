Caravan（Ctrip SOA 开发工具集）
================

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Caravan是携程框架部门SOA常用开发工具的集合，包含配置、熔断等高度重用组件。

更多产品介绍参见[Apollo配置中心介绍](https://github.com/ctripcorp/caravan/wiki/Caravan

# Features
* caravan-common
  * 基本的工具接口和工具类

* caravan-configuration
  * 在程序外部配置源和程序内部配置使用之间的抽象层，隔离配置源的形式和配置使用方式

* caravan-hystrix
  * 服务依赖熔断、隔离限流组件

* caravan-util
  * httpclient、序列化器方面的工具

* caravan-web
  * web开发工具

* caravan-protobuf
  * 可与protobuf-net互通的pb格式，基于pojo对象

* caravan-ribbon
  * 负载均衡工具

* caravan-etcd
  * etcd v2的java client

