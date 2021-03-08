# 06、数据访问

## 1、SQL

### 1、数据源的自动配置-**HikariDataSource**

#### 1、导入JDBC场景

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jdbc</artifactId>
        </dependency>
        
```

### ![image-20210309002949580](C:\Users\10660\AppData\Roaming\Typora\typora-user-images\image-20210309002949580.png)

为什么导入JDBC场景，官方不导入数据库驱动？**因为官方不知道我们接下要操作什么数据库**。

```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```

springboot已经对mysql做了版本支持

### 2、分析自动配置

#### 1、自动配置的类

- DataSourceAutoConfiguration ： 数据源的自动配置

- - 修改数据源相关的配置：**spring.datasource**
  - **数据库连接池的配置，是自己容器中没有DataSource才自动配置的**
  - 底层配置好的连接池是：**HikariDataSource**

```java
    @Configuration(proxyBeanMethods = false)
    @Conditional(PooledDataSourceCondition.class)
    @ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
    @Import({ DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Tomcat.class,
            DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.OracleUcp.class,
            DataSourceConfiguration.Generic.class, DataSourceJmxConfiguration.class })
    protected static class PooledDataSourceConfiguration
```

- DataSourceTransactionManagerAutoConfiguration： 事务管理器的自动配置
- JdbcTemplateAutoConfiguration： **JdbcTemplate的自动配置，可以来对数据库进行crud**

- - 可以修改这个配置项@ConfigurationProperties(prefix = **"spring.jdbc"**) 来修改JdbcTemplate
  - @Bean@Primary   JdbcTemplate；容器中有这个组件

- JndiDataSourceAutoConfiguration： jndi的自动配置
- XADataSourceAutoConfiguration： 分布式事务相关





### 3、修改配置项

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test?serverTimezone=Japan
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 4、测试

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        String sql = "select * from book";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(data -> data.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        }));
    }
}
```



## 2、使用Druid数据源

### 1、druid官方github地址

https://github.com/alibaba/druid

整合第三方技术的两种方式

- 自定义
- 找starter

### 2、自定义方式

#### 1、创建数据源

```xml
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.2.5</version>
        </dependency>
```



#### 2、StatViewServlet

> StatViewServlet的用途包括：
>
> - 提供监控信息展示的html页面
> - 提供监控信息的JSON API

```xml
    <servlet>
        <servlet-name>DruidStatView</servlet-name>
        <servlet-class>com.alibaba.druid.support.http.StatViewServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>DruidStatView</servlet-name>
        <url-pattern>/druid/*</url-pattern>
    </servlet-mapping>
```



#### 3、StatFilter

> 用于统计监控信息；如SQL监控、URI监控

```xml
<property name="filters" value="stat,slf4j" />
```

系统中所有filter：

| 别名          | Filter类名                                              |
| ------------- | ------------------------------------------------------- |
| default       | com.alibaba.druid.filter.stat.StatFilter                |
| stat          | com.alibaba.druid.filter.stat.StatFilter                |
| mergeStat     | com.alibaba.druid.filter.stat.MergeStatFilter           |
| encoding      | com.alibaba.druid.filter.encoding.EncodingConvertFilter |
| log4j         | com.alibaba.druid.filter.logging.Log4jFilter            |
| log4j2        | com.alibaba.druid.filter.logging.Log4j2Filter           |
| slf4j         | com.alibaba.druid.filter.logging.Slf4jLogFilter         |
| commonlogging | com.alibaba.druid.filter.logging.CommonsLogFilter       |

**慢SQL记录配置**

```xml
<bean id="stat-filter" class="com.alibaba.druid.filter.stat.StatFilter">
    <property name="slowSqlMillis" value="10000" />
    <property name="logSlowSql" value="true" />
</bean>

使用 slowSqlMillis 定义慢SQL的时长
```

### 3、使用官方starter方式

#### 1、引入druid-starter

```xml
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.2.5</version>
        </dependency>
```

#### 2、分析自动配置

- 扩展配置项 **spring.datasource.druid**
- DruidSpringAopConfiguration.class,  监控SpringBean的；配置项：**spring.datasource.druid.aop-patterns**
- DruidStatViewServletConfiguration.**class**, 监控页的配置：**spring.datasource.druid.stat-view-servlet；默认开启**
-  DruidWebStatFilterConfiguration.**class**, web监控配置；**spring.datasource.druid.web-stat-filter；默认开启**
- DruidFilterConfiguration.**class**}) 所有Druid自己filter的配置

```java
private static final String FILTER_STAT_PREFIX = "spring.datasource.druid.filter.stat";
private static final String FILTER_CONFIG_PREFIX = "spring.datasource.druid.filter.config";
private static final String FILTER_ENCODING_PREFIX = "spring.datasource.druid.filter.encoding";
private static final String FILTER_SLF4J_PREFIX = "spring.datasource.druid.filter.slf4j";
private static final String FILTER_LOG4J_PREFIX = "spring.datasource.druid.filter.log4j";
private static final String FILTER_LOG4J2_PREFIX = "spring.datasource.druid.filter.log4j2";
private static final String FILTER_COMMONS_LOG_PREFIX = "spring.datasource.druid.filter.commons-log";
private static final String FILTER_WALL_PREFIX = "spring.datasource.druid.filter.wall";
```



#### 3、配置示例

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test?serverTimezone=Japan
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

    druid:
      filters: stat,wall # 底层开启功能，stat（sql监控），wall（防火墙）

      max-active: 12

      stat-view-servlet: # 配置监控页功能
        enabled: true
        login-username: root
        login-password: root
        url-pattern: /druid/*
        reset-enable: false

      web-stat-filter: # 监控web
        exclusions: '*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*' # 可以不写，默认配置
        enabled: true
        url-pattern: /*
      aop-patterns: com.github.boot.* #监控SpringBean

      filter: # 对上面filters里面的stat的详细配置
        stat:
          slow-sql-millis: 1000
          log-slow-sql: true
        wall:
          enabled: true
```

[官方配置](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter)

[配置项列表](https://github.com/alibaba/druid/wiki/DruidDataSource配置属性列表)



## 3、整合MyBatis操作

https://github.com/mybatis

SpringBoot官方的Starter：spring-boot-starter-*

第三方的： *-spring-boot-starter

```xml
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.4</version>
        </dependency>
```

### 1、配置模式

- 全局配置文件
- SqlSessionFactory: 自动配置好了
- SqlSession：自动配置了 SqlSessionTemplate 组合了SqlSession
- @Import(AutoConfiguredMapperScannerRegistrar.**class**）；
- Mapper： 只要我们写的操作MyBatis的接口标准了 @Mapper 就会被自动扫描进来**（所以所有Mapper接口都要@Mapper注解）**

```java
@EnableConfigurationProperties(MybatisProperties.class) ： MyBatis配置项绑定类。
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class })
public class MybatisAutoConfiguration{}

@ConfigurationProperties(prefix = "mybatis")
public class MybatisProperties
```

可以修改配置文件中 mybatis 开始的所有；



```yaml
# 配置mybatis规则
mybatis:
  config-location: classpath:mybatis/mybatis-config.xml  #全局配置文件位置
  mapper-locations: classpath:mybatis/mapper/*.xml  #sql映射文件位置
```





配置 private Configuration configuration; mybatis.configuration下面的所有，就是相当于改mybatis全局配置文件中的值

```yaml
# 配置mybatis的规则
mybatis:
#  config-location: classpath:mybatis/mybatis-config.xml 
  mapper-locations: classpath:mybatis/mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    
 可以不写全局配置文件，所有全局配置文件的配置都放在configuration配置项中即可
```

- 导入mybatis官方starter
- 编写mapper接口。标准@Mapper注解
- 编写sql映射文件并绑定mapper接口
- 在application.yaml中指定Mapper配置文件的位置，以及指定全局配置文件的信息 （建议；**配置在mybatis.configuration**）



### 2、注解模式

```java
@Mapper
public interface CityMapper {

    @Insert("INSERT INTO city (name, state, country) VALUES(#{name}, #{state}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(City city);

    @Select("SELECT id, name, state, country FROM city WHERE id = #{id}")
    City findById(long id);

}
```

### 3、混合模式

```java
@Mapper
public interface CityMapper {

//    @Insert("INSERT INTO city (name, state, country) VALUES(#{name}, #{state}, #{country})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(City city);

    @Select("SELECT id, name, state, country FROM city WHERE id = #{id}")
    City findById(long id);

}
```



**最佳实战：**

- 引入mybatis-starter
- **配置application.yaml中，指定mapper-location位置即可**
- 编写Mapper接口并标注@Mapper注解
- 简单方法直接注解方式
- 复杂方法编写mapper.xml进行绑定映射
- ***@MapperScan("com.atguigu.admin.mapper") 简化，其他的接口就可以不用标注@Mapper注解***



## 4、整合 MyBatis-Plus 完成CRUD

### 1、什么是MyBatis-Plus

[MyBatis-Plus](https://github.com/baomidou/mybatis-plus)（简称 MP）是一个 [MyBatis](http://www.mybatis.org/mybatis-3/) 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

[mybatis plus 官网](https://baomidou.com/)

建议安装 **MybatisX** 插件 

### 2、整合MyBatis-Plus 

```
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.2</version>
        </dependency>
```

自动配置

- MybatisPlusAutoConfiguration 配置类，MybatisPlusProperties 配置项绑定。mybatis-plus：xxx 就是对mybatis-plus的定制
- SqlSessionFactory 自动配置好。底层是容器中默认的数据源
- **mapperLocations 自动配置好了**。有默认值。classpath\*:/mapper/\**/\*.xml；任意包的类路径下的所有mapper文件夹下任意路径下的所有xml都是sql映射文件。  建议以后sql映射文件，放在 mapper下
- 容器中也自动配置好了 SqlSessionTemplate
- @Mapper 标注的接口也会被自动扫描；建议直接 @MapperScan("com.atguigu.admin.mapper") 批量扫描就行

**优点：**

-  只需要我们的Mapper继承 **BaseMapper** 就可以拥有crud能力