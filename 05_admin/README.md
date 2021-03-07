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
- @ResponseStatus+自定义异常 ；底层是 ResponseStatusExceptionResolver ，把responsestatus注解的信息底层调用 response.sendError(statusCode, resolvedReason)；tomcat发送的/error
- Spring底层的异常，如 参数类型转换异常；DefaultHandlerExceptionResolver 处理框架底层的异常。

- - response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); 
  - ![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606114118010-f4aaf5ee-2747-4402-bc82-08321b2490ed.png)

- 自定义实现 HandlerExceptionResolver 处理异常；可以作为默认的全局异常处理规则

- - ![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606114688649-e6502134-88b3-48db-a463-04c23eddedc7.png)

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

# 