# 基础

## 1 入门

### 1.1 创建主程序

* @SpringBootApplication

  告知主程序这是一个springboot应用

* @RestController

  整合了@Controller和@ResponseBody

随后直接运行主程序即可

## 2 依赖管理

父项目做依赖管理时，不用指定版本号

springboot会自动引入所需的包，引入方式spring-boot-starter -*

spring-boot-starter-web继承了web所需的所有依赖，包括Tomcat

## 3 容器功能

### 3.1 组件添加

#### 3.1.1 @Configuration

标志一个类为配置类

 * full模式

   保证每个bean方法返回的组件都是单例 因为需要检查是否有组件，效率会比较低

 * lite模式

   保证每个bean方法返回的组件都是新创建的 不用检查所以效率较高

组件依赖必须使用Full模式默认。其他默认是Lite模式

#### 3.1.2 @Component、@Controller、@Service、@Repository

用spring的注解去创建

#### 3.1.3 @ComponentScan、@Import

放入class类扫描创建，默认组件名字为全类名

#### 3.1.4 @ConditionalOnBean, @ConditionalOnMissionBean

* 条件装配

满足Conditional指定的条件，则进行组件注入

### 3.2 导入xml配置文件

使用@ImportResource("classpath:*.xml")来配置

### 3.3 配置绑定

如何使用Java读取到properties文件中的内容，并且把它封装到JavaBean中

#### 3.3.1 @ConfigurationProperties

可以从springboot的properties配置文件中获取信息，直接导入

## 4 最佳实践

- 引入场景依赖

- - https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-starter

- 查看自动配置了哪些（选做）

- - 自己分析，引入场景对应的自动配置一般都生效了
  - 配置文件中**debug=true**开启自动配置报告。**Negative**（不生效）**Positive**（生效）

- 是否需要修改

- - 参照文档修改配置项

- - - https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html#common-application-properties
    - 自己分析。xxxxProperties绑定了配置文件的哪些。

- - 自定义加入或者替换组件

- - - @Bean、@Component。。。

- - 自定义器  **XXXXXCustomizer**；

  - ### ......

## 5 小技巧

### 5.1 Lombok

在maven中导入依赖

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

* **@Data** 可以在编译时自动生成get+set+equals+hashCode+toString方法
* **@NoArgsConstructor** 生成无参构造器
* **@AllArgsConstructor** 生成全参构造器

### 5.2 dev-tools

热部署（自动重启）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```



