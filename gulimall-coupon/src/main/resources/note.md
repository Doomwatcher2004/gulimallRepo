## 如何使用nacos来作为配置中心
1. 引入相关的依赖
```xml
       <!--        nacos 服务注册与发现-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!--        nacos 配置中心来做配置管理-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
```
2. 创建优先级的配置文件
``bootstrap.properties``
>配置地址
>
```properties
spring.application.name=gulimall-coupon
 spring.cloud.nacos.config.server-addr=127.0.0.1:8848
```
3. 给配置中心添加一个配置
data id (默认是以服务名+properties): gulimall-coupon.properties
4. 之后就可以在nacos添加配置了
5. nacos如何动态刷新 
``@RefreshScope``:刷新配置
配置中心优先级相对高
其次才是应用服务的配置

### nacos的操作
1、 命名空间：配置隔离
   默认： public：默认新增的所有配置环境都在public里面
   1、开发，测试，生产，不同环境来做不同的配置
   注意 ： 记得配置 bootstrap.properties 是使用哪一个命名空间
   2、 每一个微服务之间互相隔离配置，每一个微服务都创建自己的命名空间，之加载自己的命名空间下的所属配置
2、 配置集：
 所有配置的集合
3、配置集id
   配置集id 就是 data id
4、配置分组id
   默认所有的配置集都属于 default_group
   
每个微服务都创建自己的命名空间 每个空间再根据分组来区分环境

### 加载多个配置集
拆分配置文件 来达到分类查看
微服务配置任何信息 都可以放在配置中心
只需要再bootstrap.properties中声明加载配置中心的那些配置即可
springboot中的所有注解都可以使用
```properties
spring.application.name=gulimall-coupon
spring.cloud.nacos.config.server-addr=127.0.0.1:8848
spring.cloud.nacos.config.namespace=7a56a59c-d73f-43ca-9070-e0cf52f1d497
spring.cloud.nacos.config.group=prop
#加载多个不同的配置文件
spring.cloud.nacos.config.ext-config[0].data-id=datasouce.yaml
spring.cloud.nacos.config.ext-config[0].group=prop
spring.cloud.nacos.config.ext-config[0].refresh=true
spring.cloud.nacos.config.ext-config[1].data-id=mybatis.yaml
spring.cloud.nacos.config.ext-config[1].group=prop
spring.cloud.nacos.config.ext-config[1].refresh=true
spring.cloud.nacos.config.ext-config[2].data-id=outher.yaml
spring.cloud.nacos.config.ext-config[2].group=prop
spring.cloud.nacos.config.ext-config[2].refresh=true
```
