## git更新
## 使用bootstrap快速搭建页面
    https://v3.bootcss.com/css/#buttons-tags
### 使用github登陆 ：
    当用户访问社区执行登陆操作时，会调用authorize接口访问github，github会直接返回我们携带的redirect-uri(携带code),当社区接收到时，会带着code调用github的access_token，如果github验证成功的话，会返回access_token,然后再用access_token调用github的user api(用户接口),然后把获取的数据保存到数据库，然后返回用户登陆成功。
### 使用Mybatis使用SQL语句

### 使用String MVC的Interceptor做登陆拦截

###实现发布问题，查询自己发布，修改问题，查看问题

##脚本
```bash
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
