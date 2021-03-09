# 用Thymeleaf构建后台管理项目

password: 123456

## 1、拦截器

### 1、实现HandlerInterceptor 接口

```java
public class Interceptor implements HandlerInterceptor {
}
```



### 2、配置类中实现WebMvcConfigurer接口，重写addInterceptors方法，配置拦截器

```java
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }
}
```



### 3、拦截器原理

1、根据当前请求，找到**HandlerExecutionChain【**可以处理请求的handler以及handler的所有 拦截器】

2、先来**顺序执行** 所有拦截器的 preHandle方法

- 1、如果当前拦截器prehandler返回为true。则执行下一个拦截器的preHandle
- 2、如果当前拦截器返回为false。直接   倒序执行所有已经执行了的拦截器的  afterCompletion；

**3、如果任何一个拦截器返回false。直接跳出不执行目标方法**

**4、所有拦截器都返回True。执行目标方法**

**5、倒序执行所有拦截器的postHandle方法。**

**6、前面的步骤有任何异常都会直接倒序触发** afterCompletion

7、页面成功渲染完成以后，也会倒序触发 afterCompletion



![image-20210308031157283](C:\Users\10660\AppData\Roaming\Typora\typora-user-images\image-20210308031157283.png)



## 2、文件上传

### 1、页面表单

```html
<form method="post" action="/upload" enctype="multipart/form-data">
    <div class="form-group">
        <label for="exampleInputFile">Header image</label>
        <input type="file" name="headerImg" id="exampleInputFile">
        <p class="help-block">Example block-level help text here.</p>
    </div>
    <div class="form-group">
        <label for="exampleInputFile">Other</label>
        <input type="file" name="photos" multiple>
        <p class="help-block">Example block-level help text here.</p>
    </div>
</form>
```



### 2、文件上传代码

```java
    /**
     *  获取上传的文件用 @RequestPart + MultiPartFile || MultiPartFIle[]
     *  @SneakyThrows 处理模板的try-catch, 不用再自己写
     */
    @SneakyThrows
    @PostMapping("/upload")
    public String upload(@RequestParam("username") String username,
                         @RequestParam("email") String email,
                         @RequestPart("headerImg") MultipartFile headerImg,
                         @RequestPart("photos") MultipartFile[] photos) {
        log.info("username={}, email={}, headerImg={}, photos={}", username,
                email, headerImg.getSize(), photos.length);
        if (!headerImg.isEmpty()) {
            // 保存到文件服务器，OSS服务器等
//            headerImg.getInputStream();
            // 获取名字
            String filename = headerImg.getOriginalFilename();
            headerImg.transferTo(new File("E:\\" + filename));
        }

        if (photos.length != 0) {
            for (MultipartFile photo : photos) {
                if (!photo.isEmpty()) {
                    String filename = photo.getOriginalFilename();
                    photo.transferTo(new File("E:\\" + filename));
                }
            }
        }
        return "main";
    }
```

修改上传文件限制

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 100MB
```

### 3、自动配置原理

文件上传自动配置类**-MultipartAutoConfiguration-MultipartProperties**

- 自动配置好了 **StandardServletMultipartResolver  【文件上传解析器】**
- **原理步骤**

- - **1、请求进来使用文件上传解析器判断（**isMultipart）**并封装resolveMultipar返回MultipartHttpServletRequest文件上传请求**
  - **2、参数解析器来解析请求中的文件内容封装成MultipartFile**
  - **3、将request中文件信息封装为一个Map；**MultiValueMap<String, MultipartFile>

**FileCopyUtils**。实现文件流的拷贝



![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1605847414866-32b6cc9c-5191-4052-92eb-069d652dfbf9.png)



## 3、异常处理

### 1、错误处理

#### 1、默认规则

- 默认情况下，**Spring Boot提供`/error`处理所有错误的映射**

- 对于机器客户端，它将生成JSON响应，其中包含错误，HTTP状态和异常消息的详细信息。对于浏览器客户端，响应一个“ whitelabel”错误视图，以HTML格式呈现相同的数据

- ![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606024421363-77083c34-0b0e-4698-bb72-42da351d3944.png)![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606024616835-bc491bf0-c3b1-4ac3-b886-d4ff3c9874ce.png)

- 要对其进行自定义，添加`View`解析为`error`

- 要完全替换默认行为，可以实现 `ErrorController `并注册该类型的Bean定义，或添加`ErrorAttributes类型的组件`以使用现有机制但替换其内容。

- **error/下的4xx，5xx页面会被自动解析**；

  

#### 2、定制错误处理逻辑

- 自定义错误页

- - error/404.html  error/5xx.html；**有精确的错误状态码页面就匹配精确，没有就找 4xx.html；如果都没有就触发白页**

- @ControllerAdvice+@ExceptionHandler处理全局异常；底层是 ExceptionHandlerExceptionResolver 支持的
- @ResponseStatus+自定义异常 ；底层是 **ResponseStatusExceptionResolver** ，把responsestatus注解的信息底层调用 response.sendError(statusCode, resolvedReason)；这个是tomcat发送的/error
- Spring底层的异常，如 参数类型转换异常；DefaultHandlerExceptionResolver 处理框架底层的异常。

- - response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); 

- 自定义实现 HandlerExceptionResolver 处理异常；可以作为默认的全局异常处理规则

- ErrorViewResolver  实现自定义处理异常；

- - response.sendError 。error请求就会转给controller
  - 你的异常没有任何人能处理。tomcat底层 response.sendError。error请求就会转给controller
  - basicErrorController 要去的页面地址是 ErrorViewResolver  ；





#### 3、异常处理自动配置原理

- ErrorMvcAutoConfiguration  自动配置异常处理规则

- - 容器中的组件：类型：DefaultErrorAttributes -> id：errorAttributes

- - - public class DefaultErrorAttributesimplements ErrorAttributes, HandlerExceptionResolver
    - DefaultErrorAttributes：定义错误页面中可以包含哪些数据。
    - ![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606044430037-8d599e30-1679-407c-96b7-4df345848fa4.png)
    - ![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606044487738-8cb1dcda-08c5-4104-a634-b2468512e60f.png)

- - 容器中的组件：类型：BasicErrorController --> id：basicErrorController（json+白页 适配响应）

- - - 处理默认 /error 路径的请求；页面响应 new ModelAndView("error", model)；
    - 容器中有组件 View->id是error；（响应默认错误页）
    - 容器中放组件 BeanNameViewResolver（视图解析器）；按照返回的视图名作为组件的id去容器中找View对象。

- - 容器中的组件：类型：DefaultErrorViewResolver -> id：conventionErrorViewResolver

- - - **如果发生错误，会以HTTP的状态码 作为视图页地址（viewName），找到真正的页面**
    - **error/404、5xx.html**



如果想要返回页面；就会找error视图【**StaticView**】。(默认是一个白页)

![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606043870164-3770e116-344f-448e-8bff-8f32438edc9a.png)

写出去json

![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606043904074-50b7f088-2d2b-4da5-85e2-0a756da74dca.png) 默认错误页，名字为error 

所以既能写json也能写错误页



#### 4、异常处理步骤流程

1、执行目标方法，目标方法运行期间有任何异常都会被(doDispatcher)catch、而且标志当前请求结束；并且用 **dispatchException** 封装

2、进入视图解析流程

processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);

3、**mv = processHandlerException**；处理handler发生的异常，处理完成返回ModelAndView；

- 1、遍历所有的 handlerExceptionResolvers，看谁能处理当前异常【**HandlerExceptionResolver**处理器异常解析器】
- ![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606047252166-ce71c3a1-0e0e-4499-90f4-6d80014ca19f.png) HandlerExceptionResolver接口中就一个方法，可以自定义，但也必须返回ModelAndView
- 2、系统默认的  异常解析器；
- **![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606047109161-c68a46c1-202a-4db1-bbeb-23fcae49bbe9.png)**

- - 1、**DefaultErrorAttributes**先来处理异常。把异常信息保存到rrequest域，并且返回null；
  - 2、默认没有任何类能处理异常，所以异常会被抛出

- - - 1、如果没有任何类能处理，最终底层(浏览器)就会发送 /error 请求。会被底层的BasicErrorController处理
    - 2、解析错误视图；遍历所有的  ErrorViewResolver  看谁能解析。
    - **![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606047900473-e31c1dc3-7a5f-4f70-97de-5203429781fa.png)**
    - 3、默认的 DefaultErrorViewResolver ,作用是把响应状态码作为错误页的地址，拼接成error/500.html 
    - 4、模板引擎最终响应这个页面 error/500.html 

## 4、Web原生组件注入（Servlet、Filter、Listener）

### 1、使用Servlet API

@ServletComponentScan(basePackages = "com.github.admin.servlet")  :指定原生Servlet组件都放在哪里

@WebServlet(urlPatterns = "/my")：效果：直接响应，没有经过Spring的拦截器

@WebFilter(urlPatterns={"/css/\*","/images/\*"}) 过滤器使用

@WebListener 监听器使用

推荐可以这种方式；



扩展：DispatchServlet 如何注册进来

- 容器中自动配置了  DispatcherServlet  属性绑定到 WebMvcProperties；对应的配置文件配置项是 spring.mvc。
- 通过 ServletRegistrationBean<DispatcherServlet> 把 DispatcherServlet  配置进来。
- 默认映射的路径是 / 。

![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606284869220-8b63d54b-39c4-40f6-b226-f5f095ef9304.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_14%2Ctext_YXRndWlndS5jb20g5bCa56GF6LC3%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)

Tomcat-Servlet；

多个Servlet都能处理到同一层路径，精确优选原则（因此优先来到/my路径,而不是先到/路径,因此不会走Spring流程,而是走Tomcat流程,故/my路径不会被拦截器拦截）

A： /my/

B： /my/1



### 2、使用RegistrationBean（spring低层提供）

`ServletRegistrationBean` `FilterRegistrationBean` `ServletListenerRegistrationBean`

```java
@Configuration
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean<MyServlet> myServlet() {
        MyServlet servlet = new MyServlet();
        return new ServletRegistrationBean<>(servlet, "/my", "/my02");
    }

    @Bean
    public FilterRegistrationBean<MyFilter> myFilter() {
//        return new FilterRegistrationBean<>(new MyFilter(), myServlet());
        FilterRegistrationBean<MyFilter> filter = new FilterRegistrationBean<>(new MyFilter());
        filter.setUrlPatterns(Arrays.asList("/css/*", "/images/*"));
        return filter;
    }

    @Bean
    public ServletListenerRegistrationBean<MyListener> myListener() {
        return new ServletListenerRegistrationBean<>(new MyListener());
    }
}
```

## 5、嵌入式Servlet容器

### 1、切换嵌入式Servlet容器

- 默认支持的webServer

- - `Tomcat`, `Jetty` or `Undertow`
  - `ServletWebServerApplicationContext` 容器启动寻找`ServletWebServerFactory` 并引导创建服务器

- 切换服务器

![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606280937533-504d0889-b893-4a01-af68-2fc31ffce9fc.png)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

- 原理

- - SpringBoot应用启动发现当前是Web应用。web场景包-导入tomcat
  - web应用会创建一个web版的ioc容器 `ServletWebServerApplicationContext` 
  - `ServletWebServerApplicationContext`  启动的时候寻找 `ServletWebServerFactory`（Servlet 的web服务器工厂---> Servlet 的web服务器）
  - SpringBoot底层默认有很多的WebServer工厂；`TomcatServletWebServerFactory`, `JettyServletWebServerFactory`, or `UndertowServletWebServerFactory`
  - 底层直接会有一个自动配置类。`ServletWebServerFactoryAutoConfiguration`
  - `ServletWebServerFactoryAutoConfiguration`导入了`ServletWebServerFactoryConfiguration`（配置类）
  - `ServletWebServerFactoryConfiguration` 配置类 根据动态判断系统中到底导入了那个Web服务器的包。（默认是web-starter导入tomcat包），容器中就有 `TomcatServletWebServerFactory`
  - `TomcatServletWebServerFactory` 创建出Tomcat服务器并启动；`TomcatWebServer` 的构造器拥有初始化方法initialize---this.tomcat.start();
  - **内嵌服务器，就是手动把启动服务器的代码调用（tomcat核心jar包存在才能启动）**

### 2、定制Servlet容器

- 实现  WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> 

- - 把配置文件的值和`ServletWebServerFactory` 进行绑定

- 修改配置文件 server.xxx
- 直接自定义 ConfigurableServletWebServerFactory 

`xxxxxCustomizer`：定制化器，可以改变xxxx的默认规则

```java
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomizationBean implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        server.setPort(9000);
    }

}
```

## 6、定制化原理

### 1、定制化的常见方式 

- 修改配置文件；
- xxxxx**Customizer**来定制化；
- 编写自定义的配置类  xxxConfiguration；+ @Bean替换、增加容器中默认组件；视图解析器 
- **Web应用 编写一个配置类实现 WebMvcConfigurer 即可定制化web功能；+ @Bean给容器中再扩展一些组件**

```java
@Configuration
public class AdminWebConfig implements WebMvcConfigurer
```

- @EnableWebMvc + WebMvcConfigurer —— @Bean  **可以全面接管SpringMVC**，所有规则全部自己重新配置； 实现定制和扩展功能**（慎用）**

- - 原理
  - 1、WebMvcAutoConfiguration  默认的SpringMVC的自动配置功能类。静态资源、欢迎页.....
  - 2、一旦使用 `@EnableWebMvc` 、会 @Import(DelegatingWebMvcConfiguration.class)
  - 3、DelegatingWebMvcConfiguration 的 作用，**只保证SpringMVC最基本的使用**

- - - 把所有系统中的 WebMvcConfigurer 拿过来。所有功能的定制都是这些 WebMvcConfigurer  合起来一起生效
    - 自动配置了一些非常底层的组件。RequestMappingHandlerMapping、这些组件依赖的组件都是从容器中获取
    - public class DelegatingWebMvcConfiguration **extends WebMvcConfigurationSupport**

- - 4、WebMvcAutoConfiguration 里面的配置要能生效 有这个条件  @ConditionalOnMissingBean(**WebMvcConfigurationSupport.class**)
  - 5、@EnableWebMvc  导致了 @Import(DelegatingWebMvcConfiguration.class) 所以WebMvcAutoConfiguration  没有生效。



## 7、指标监控

### 1、SpringBoot Actuator

#### 1、简介

未来每一个微服务在云上部署以后，我们都需要对其进行监控、追踪、审计、控制等。SpringBoot就抽取了Actuator场景，使得我们每个微服务快速引用即可获得生产级别的应用监控、审计等功能。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### 2、1.x与2.x的不同



![image-20210310032541532](C:\Users\10660\AppData\Roaming\Typora\typora-user-images\image-20210310032541532.png)

#### 3、如何使用

- 引入场景
- 访问 http://localhost:8080/actuator/**
- 暴露所有监控信息为HTTP

```yaml
management:
  endpoints:
    enabled-by-default: true #暴露所有端点信息
    web:
      exposure:
        include: '*'  #以web方式暴露
```

- 测试

http://localhost:8080/actuator/beans

http://localhost:8080/actuator/configprops

http://localhost:8080/actuator/metrics

http://localhost:8080/actuator/metrics/jvm.gc.pause

[http://localhost:8080/actuator/](http://localhost:8080/actuator/metrics)endpointName/detailPath
。。。。。。



#### 4、[可视化](https://github.com/codecentric/spring-boot-admin)



### 2、Actuator Endpoint

#### 1、最常使用的端点



| ID                 | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| `auditevents`      | 暴露当前应用程序的审核事件信息。需要一个`AuditEventRepository组件`。 |
| `beans`            | 显示应用程序中所有Spring Bean的完整列表。                    |
| `caches`           | 暴露可用的缓存。                                             |
| `conditions`       | 显示自动配置的所有条件信息，包括匹配或不匹配的原因。         |
| `configprops`      | 显示所有`@ConfigurationProperties`。                         |
| `env`              | 暴露Spring的属性`ConfigurableEnvironment`                    |
| `flyway`           | 显示已应用的所有Flyway数据库迁移。 需要一个或多个`Flyway`组件。 |
| `health`           | 显示应用程序运行状况信息。                                   |
| `httptrace`        | 显示HTTP跟踪信息（默认情况下，最近100个HTTP请求-响应）。需要一个`HttpTraceRepository`组件。 |
| `info`             | 显示应用程序信息。                                           |
| `integrationgraph` | 显示Spring `integrationgraph` 。需要依赖`spring-integration-core`。 |
| `loggers`          | 显示和修改应用程序中日志的配置。                             |
| `liquibase`        | 显示已应用的所有Liquibase数据库迁移。需要一个或多个`Liquibase`组件。 |
| `metrics`          | 显示当前应用程序的“指标”信息。                               |
| `mappings`         | 显示所有`@RequestMapping`路径列表。                          |
| `scheduledtasks`   | 显示应用程序中的计划任务。                                   |
| `sessions`         | 允许从Spring Session支持的会话存储中检索和删除用户会话。需要使用Spring Session的基于Servlet的Web应用程序。 |
| `shutdown`         | 使应用程序正常关闭。默认禁用。                               |
| `startup`          | 显示由`ApplicationStartup`收集的启动步骤数据。需要使用`SpringApplication`进行配置`BufferingApplicationStartup`。 |
| `threaddump`       | 执行线程转储。                                               |





如果您的应用程序是Web应用程序（Spring MVC，Spring WebFlux或Jersey），则可以使用以下附加端点：

| ID           | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| `heapdump`   | 返回`hprof`堆转储文件。                                      |
| `jolokia`    | 通过HTTP暴露JMX bean（需要引入Jolokia，不适用于WebFlux）。需要引入依赖`jolokia-core`。 |
| `logfile`    | 返回日志文件的内容（如果已设置`logging.file.name`或`logging.file.path`属性）。支持使用HTTP`Range`标头来检索部分日志文件的内容。 |
| `prometheus` | 以Prometheus服务器可以抓取的格式公开指标。需要依赖`micrometer-registry-prometheus`。 |





最常用的Endpoint

- **Health：监控状况**
- **Metrics：运行时指标**
- **Loggers：日志记录**





#### 2、Health Endpoint

健康检查端点，我们一般用于在云平台，平台会定时的检查应用的健康状况，我们就需要Health Endpoint可以为平台返回当前应用的一系列组件健康状况的集合。

重要的几点：

- health endpoint返回的结果，应该是一系列健康检查后的一个汇总报告
- 很多的健康检查默认已经自动配置好了，比如：数据库、redis等
- 可以很容易的添加自定义的健康检查机制

#### 3、Metrics Endpoint

提供详细的、层级的、空间指标信息，这些信息可以被pull（主动推送）或者push（被动获取）方式得到；

- 通过Metrics对接多种监控系统
- 简化核心Metrics开发
- 添加自定义Metrics或者扩展已有Metrics

#### 4、管理Endpoints

##### 1、开启与禁用Endpoints

- 默认所有的Endpoint除过shutdown都是开启的。
- 需要开启或者禁用某个Endpoint。配置模式为  management.endpoint.**<endpointName>**.enabled = true

```yaml
management:
  endpoint:
    beans:
      enabled: true
```

- 或者禁用所有的Endpoint然后手动开启指定的Endpoint

```yaml
management:
  endpoints:
    enabled-by-default: false
  endpoint:
    beans:
      enabled: true
    health:
      enabled: true
```



##### 2、暴露Endpoints

支持的暴露方式

- HTTP：默认只暴露**health**和**info** Endpoint
- **JMX**：默认暴露所有Endpoint
- 除过health和info，剩下的Endpoint都应该进行保护访问。如果引入SpringSecurity，则会默认配置安全访问规则

| ID                 | JMX  | Web  |
| ------------------ | ---- | ---- |
| `auditevents`      | Yes  | No   |
| `beans`            | Yes  | No   |
| `caches`           | Yes  | No   |
| `conditions`       | Yes  | No   |
| `configprops`      | Yes  | No   |
| `env`              | Yes  | No   |
| `flyway`           | Yes  | No   |
| `health`           | Yes  | Yes  |
| `heapdump`         | N/A  | No   |
| `httptrace`        | Yes  | No   |
| `info`             | Yes  | Yes  |
| `integrationgraph` | Yes  | No   |
| `jolokia`          | N/A  | No   |
| `logfile`          | N/A  | No   |
| `loggers`          | Yes  | No   |
| `liquibase`        | Yes  | No   |
| `metrics`          | Yes  | No   |
| `mappings`         | Yes  | No   |
| `prometheus`       | N/A  | No   |
| `scheduledtasks`   | Yes  | No   |
| `sessions`         | Yes  | No   |
| `shutdown`         | Yes  | No   |
| `startup`          | Yes  | No   |
| `threaddump`       | Yes  | No   |



### 3、定制 Endpoint

#### 1、定制 Health 信息

```java
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        int errorCode = check(); // perform some specific health check
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

}

构建Health
Health build = Health.down()
                .withDetail("msg", "error service")
                .withDetail("code", "500")
                .withException(new RuntimeException())
                .build();
```

```yaml
management:
    health:
      enabled: true
      show-details: always #总是显示详细信息。可显示每个模块的状态信息
```

```java
@Component
public class MyComHealthIndicator extends AbstractHealthIndicator {

    /**
     * 真实的检查方法
     * @param builder
     * @throws Exception
     */
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        //mongodb。  获取连接进行测试
        Map<String,Object> map = new HashMap<>();
        // 检查完成
        if(1 == 2){
//            builder.up(); //健康
            builder.status(Status.UP);
            map.put("count",1);
            map.put("ms",100);
        }else {
//            builder.down();
            builder.status(Status.OUT_OF_SERVICE);
            map.put("err","连接超时");
            map.put("ms",3000);
        }


        builder.withDetail("code",100)
                .withDetails(map);

    }
}
```

#### 2、定制info信息

常用两种方式

##### 1、编写配置文件

```yaml
info:
  appName: boot-admin
  version: 2.0.1
  mavenProjectName: @project.artifactId@  #使用@@可以获取maven的pom文件值
  mavenProjectVersion: @project.version@
```

##### 2、编写InfoContributor

```java
import java.util.Collections;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class ExampleInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("example",
                Collections.singletonMap("key", "value"));
    }

}
```

http://localhost:8080/actuator/info 会输出以上方式返回的所有info信息

#### 3、定制Metrics信息

##### 1、SpringBoot支持自动适配的Metrics

- JVM metrics, report utilization of:

- - Various memory and buffer pools
  - Statistics related to garbage collection
  - Threads utilization
  - Number of classes loaded/unloaded

- CPU metrics
- File descriptor metrics
- Kafka consumer and producer metrics
- Log4j2 metrics: record the number of events logged to Log4j2 at each level
- Logback metrics: record the number of events logged to Logback at each level
- Uptime metrics: report a gauge for uptime and a fixed gauge representing the application’s absolute start time
- Tomcat metrics (`server.tomcat.mbeanregistry.enabled` must be set to `true` for all Tomcat metrics to be registered)
- [Spring Integration](https://docs.spring.io/spring-integration/docs/5.4.1/reference/html/system-management.html#micrometer-integration) metrics



##### 2、增加定制Metrics

```java
class MyService{
    Counter counter;
    public MyService(MeterRegistry meterRegistry){
         counter = meterRegistry.counter("myservice.method.running.counter");
    }

    public void hello() {
        counter.increment();
    }
}


//也可以使用下面的方式
@Bean
MeterBinder queueSize(Queue queue) {
    return (registry) -> Gauge.builder("queueSize", queue::size).register(registry);
}
```



### 4、定制Endpoint

```java
@Component
@Endpoint(id = "container")
public class DockerEndpoint {


    @ReadOperation
    public Map getDockerInfo(){
        return Collections.singletonMap("info","docker started...");
    }

    @WriteOperation
    private void restartDocker(){
        System.out.println("docker restarted....");
    }

}
```

场景：开发**ReadinessEndpoint**来管理程序是否就绪，或者**Liveness****Endpoint**来管理程序是否存活；

当然，这个也可以直接使用 https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-kubernetes-probes