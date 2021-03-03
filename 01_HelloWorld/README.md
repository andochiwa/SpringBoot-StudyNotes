# 基础

## 入门

### 创建主程序

* @SpringBootApplication

  告知主程序这是一个springboot应用

* @RestController

  整合了@Controller和@ResponseBody

随后直接运行主程序即可

## 依赖管理

父项目做依赖管理时，不用指定版本号

springboot会自动引入所需的包，引入方式spring-boot-starter
-*

spring-boot-starter-web继承了web所需的所有依赖，包括Tomcat

## 容器功能
### 组件添加
1. @Configuration proxy
   
    标志一个类为配置类

    * full模式
      
        保证每个bean方法返回的组件都是单例
      因为需要检查是否有组件，效率会比较低
      
    * lite模式 
      
        保证每个bean方法返回的组件都是新创建的 
        不用检查所以效率较高
    
    组件依赖必须使用Full模式默认。其他默认是Lite模式

2. @Component、@Controller、@Service、@Repository

    用spring的注解去创建

3. @ComponentScan、@Import

    放入class类扫描创建，默认组件名字为全类名

4. @ConditionalOnBean, @ConditionalOnMissionBean

   * 条件装配

   满足Conditional指定的条件，则进行组件注入

### 导入xml配置文件

使用@ImportResource("classpath:*.xml")来配置

### 配置绑定
如何使用Java读取到properties文件中的内容，并且把它封装到JavaBean中

#### @ConfigurationProperties
可以从springboot的properties配置文件中获取信息，直接导入







