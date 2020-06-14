package com.data.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 这个重载方法是设置安全策略的，可以理解为哪些拦截哪些不拦截，触发什么情况的时候进行拦截等
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*******************************************************************************************************/
        //配置：首页可以访问，但是功能页需要对应的权限
        http
                .authorizeRequests()//授权请求
                .antMatchers("/").permitAll()//对指定的匹配（“/”）允许全部
                .antMatchers("/level1/**").hasRole("vip1")//对指定的请求添加角色要求
                .antMatchers("/level2/**").hasRole("vip2")//对指定的请求添加角色要求
                .antMatchers("/level3/**").hasRole("vip3")//对指定的请求添加角色要求
        ;
        /*******************************************************************************************************/
        http.formLogin();//配置首页，如果无权限的话会跳转到它写好的首页里面去
        /*******************************************************************************************************/
        http
                .logout()
                .deleteCookies("remove")//删除cookies
                .invalidateHttpSession(true)//使得session无效
                .logoutSuccessUrl("/")//登出后跳转到哪里
        ;//配置注销页
        /*******************************************************************************************************/
        //super.configure(http);
    }

    /**
     * 这个重载方法是设置认证的,查看源码发现可以从内存或者数据库中认证;
     * inMemoryAuthentication   在内存中验证
     * passwordEncoder          不允许明文密码防止反编译，所以用特定的编码格式，这里选用了其中一种；
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*******************************************************************************************************/
        auth
                .inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("root").password(new BCryptPasswordEncoder().encode("root")).roles("vip1", "vip2", "vip3").and()
                .withUser("chengfeng").password(new BCryptPasswordEncoder().encode("chengfeng")).roles("vip1", "vip2").and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("guest")).roles("vip1")
        ;
        /*******************************************************************************************************/

        //super.configure(auth);
    }
}
