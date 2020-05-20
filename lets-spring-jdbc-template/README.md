# Spring JdbcTemplate

引入spring-boot-starter-jdbc, h2 database 和 spring-boot-starter-web 作为快速原型测试。

编辑 `src\main\resources\application.yml`，开启h2 web console。
```yml
spring:
  datasource:
    url: jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
    schema: classpath:IScream.sql
    data:
    initialization-mode: always
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
  h2:
    console:
      path: "/h2-console"
      enabled: true
```
启动后留意控制台输出
```bash
H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:mydb'
```
访问 http://localhost:8080/h2-console/ 并填写正确的jdbc路径。

![](images\h2-console.png)

[代码示例](\src\main\java\io\github\wdpm\App.java)