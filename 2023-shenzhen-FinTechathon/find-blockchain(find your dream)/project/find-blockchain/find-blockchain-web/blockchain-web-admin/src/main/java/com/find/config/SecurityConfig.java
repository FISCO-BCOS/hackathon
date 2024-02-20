//package com.find.config;
//
//import com.find.service.MyUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
///**
// * Description:
// * Author: Su
// * Date: 2023/10/30
// */
//
////@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
////    @Autowired
////    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
////
////    @Autowired
////    private AuthenticationConfiguration authenticationConfiguration;
////
////    @Autowired
////    @Qualifier("userDetailsService")
////    UserDetailsService userDetailsService;
////
////    //认证用户名密码
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//////        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
////    }
////
////    @Override
////    protected void configure(HttpSecurity http) throws Exception {
////        //System.out.println("1111111111");
//////        http.cors().and().csrf().disable()
////                http
////                .authorizeRequests()
//////                .mvcMatchers("/user/login").permitAll()
////                .anyRequest().permitAll()
////                .and().formLogin()
//////                .loginPage("/index")//自定义访问登录页面地址
//////                .usernameParameter("username")//自定义用户名、密码变量名
//////                .passwordParameter("password")
////                .successForwardUrl("/home")//认证成功始终跳转的路径.defaultSuccessUrl("/hello") redirect 重定向跳转 但是优先跳转到请求的路径 ，可以传入第二个参数true总是重定向到hello
////                //.failureForwardUrl("/user/toLoginPage")//认证失败 跳转 错误信息在request中,.failureUrl("/login.html")认证失败 redirect跳转 错误信息在session中
//////                .authorizeRequests()
//////                // 测试用资源，需要验证了的用户才能访问
//////                .antMatchers("/res/add").authenticated()
//////                //只有角色为admin的用户才能进行删除
//////                .antMatchers(HttpMethod.DELETE,"/res/delete").hasRole("ADMIN")
//////                // todo:其他请求都放行了
//////                .anyRequest().permitAll()
//////                .and()
//////                .formLogin()
//////                .loginProcessingUrl("/index")
//////                .usernameParameter("username")
//////                .passwordParameter("password")
////                .and()
//////                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
////                // 不需要session
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////        //将验证token的过滤器添加在验证用户名/密码的过滤器之前
////        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
////    }
////
////    // 获取AuthenticationManager（认证管理器），登录时认证使用。
////    @Bean
////    public AuthenticationManager authenticationManager() throws Exception {
////        return this.authenticationConfiguration.getAuthenticationManager();
////    }
////
////    @Override
////    public void configure(WebSecurity web) throws Exception {
////        web.ignoring().antMatchers("/img/*", "/css/*", "/templates/*","/layui/*");
////    }
////
////    @Bean
////    public BCryptPasswordEncoder bCryptPasswordEncoder(){
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    CorsConfigurationSource corsConfigurationSource(){
////        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**",new CorsConfiguration().applyPermitDefaultValues());
////        return source;
////    }
//}
