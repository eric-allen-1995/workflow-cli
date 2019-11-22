集成flowable-modeler

#### 加入依赖
> 选用这个依赖的原因是因为，app 依赖 包含 rest，logic，conf 的依赖
```xml
<!-- app 依赖 包含 rest，logic，conf -->
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-ui-modeler-app</artifactId>
    <version>6.4.1</version>
</dependency>
```
> 或者也可以直接用 rest，logic，conf 的依赖
```xml
<!-- modeler依赖 rest，logic，conf -->
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-ui-modeler-rest</artifactId>
    <version>6.4.1</version>
</dependency>
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-ui-modeler-logic</artifactId>
    <version>6.4.1</version>
</dependency>
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-ui-modeler-conf</artifactId>
    <version>6.4.1</version>
</dependency>
```

#### 重写`RemoteAccountResource`,`SecurityUtils`,`SecurityConfiguration`
> 项目中的org目录是为了保持包名不变,在SpringBoot启动时,不去加载依赖中的配置,而是加载自己重写后的文件
```shell script
org.flowable.ui.common.rest.idm.remote
|-
```





#### 启动类修改
> 在`@SpringBootApplication`注解中排除`SecurityAutoConfiguration`和`ManagementWebSecurityAutoConfiguration`
```java
@MapperScan()
// 移除 Security 自动配置
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class FlowableApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }
}
```


#### 常见问题
> 项目启动后报错,提示`org.mybatis.logging.LoggerFactory`
- 检查pom依赖是否有`slf4j-log4j12`和`slf4j-api`,且已配置`log4j.properties`
- 检查pom依赖中的`spring-boot-starter-web`是否已排除`spring-boot-starter-logging`
- 如果使用的是MybatisPlus,需添加`mybatis-plus-extension`依赖