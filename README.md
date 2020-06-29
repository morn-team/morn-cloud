
**中文** | [English](https://github.com/pigxcloud/pig/blob/master/README.md)

<p align="center">
 <img src="https://img.shields.io/badge/Pig-2.7-success.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Avue-2.5-green.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Spring%20Cloud-Hoxto.SR4-blue.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.3.RELEASE-blue.svg" alt="Downloads">
</p>

- 基于 Spring Cloud Hoxton 、Spring Boot 2.3、 OAuth2 的RBAC权限管理系统  
- 基于数据驱动视图的理念封装 element-ui，即使没有 vue 的使用经验也能快速上手
- 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持
- 提供 lambda 、stream api 、webflux 的生产实践

<a href="http://pig4cloud.com/doc/pig" target="_blank">部署文档</a> | <a target="_blank" href="https://avuejs.com"> 前端解决方案</a>  
   
#### 核心依赖 

依赖 | 版本
---|---
Spring Boot |  2.3.0.RELEASE  
Spring Cloud | Hoxton.SR4   
Spring Security OAuth2 | 2.3.6
Mybatis Plus | 3.3.1
hutool | 5.3.5
Avue | 2.5.2

#### 模块说明

```lua
pig-ui  -- https://gitee.com/log4j/pig-ui

morn-cloud
├── morn-auth -- 授权服务提供[3000]
├── morn-codegen -- 图形化代码生成[5002]
└── morn-common -- 系统公共模块 
     ├── morn-common-core -- 公共工具类核心包
     ├── morn-common-datasource -- 动态数据源包
     ├── morn-common-log -- 日志服务
     ├── morn-common-mybatis -- mybatis 扩展封装
     ├── morn-common-security -- 安全工具类
     └── morn-common-swagger -- 接口文档
├── morn-discovery -- Nacos Server[8848]
├── morn-gateway -- Spring Cloud Gateway网关[9999]
├── morn-monitor -- Spring Boot Admin监控 [5001]
└── morn-upms -- 通用用户权限管理模块
     └── morn-upms-api -- 通用用户权限管理系统公共api模块
     └── morn-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
	 
```

#### Hosts

启动项目前，修改本地`host`文件

```
127.0.0.1   morn-mysql
127.0.0.1   morn-redis
127.0.0.1   morn-discovery
127.0.0.1   morn-gateway
```