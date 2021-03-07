# 用Thymeleaf构建后台管理项目

password: 123456

## 6、拦截器

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



## 7、文件上传

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

- - **1、请求进来使用文件上传解析器判断（**isMultipart**）并封装（**resolveMultipart，**返回**MultipartHttpServletRequest**）文件上传请求**
  - **2、参数解析器来解析请求中的文件内容封装成MultipartFile**
  - **3、将request中文件信息封装为一个Map；**MultiValueMap<String, MultipartFile>

**FileCopyUtils**。实现文件流的拷贝



![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1605847414866-32b6cc9c-5191-4052-92eb-069d652dfbf9.png)

# 