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