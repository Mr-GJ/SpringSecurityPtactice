package com.bootsecurity.bootsecurity.Controller;

import com.bootsecurity.bootsecurity.config.MyAuthenticationFailureHandler;
import com.bootsecurity.bootsecurity.config.MyAuthenticationSuccessHandler;
import com.bootsecurity.bootsecurity.config.MyExpireSessionStrategy;
import com.bootsecurity.bootsecurity.config.MyUserDetailsService;
import com.bootsecurity.bootsecurity.config.auth.ImageCodeIdentify.KaptchaCodeFilter;
import com.bootsecurity.bootsecurity.config.auth.MylogoutSuccessHandler;
import com.bootsecurity.bootsecurity.config.auth.smscode.SmsCodeSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private DataSource dataSource;
    @Resource
    private MylogoutSuccessHandler mylogoutSuccessHandler;
    @Resource
    private KaptchaCodeFilter kaptchaCodeFilter;
    @Resource
    private SmsCodeSecurityConfig smsCodeSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(kaptchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
//                .logoutSuccessUrl("/aftersignout.html")
                .logoutUrl("/signout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(mylogoutSuccessHandler)
                .and()
                .rememberMe()
                .tokenValiditySeconds(24*60*60)
                .tokenRepository(persistentTokenRepository()).and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login.html")
                .usernameParameter("uname")
                .passwordParameter("pword")
                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/index")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/smslogin","/smscode","/login.html", "/login","/aftersignout.html","/kaptcha").permitAll()
                .antMatchers("/index").authenticated()
                .anyRequest().access("@rabcService.haspermission(request,authentication)")
//                .antMatchers("/biz1", "/biz2").hasAnyAuthority("ROLE_user", "ROLE_admin")
////                .antMatchers("/syslog","/sysuser").hasAnyRole("admin")
//                .antMatchers("/syslog").hasAuthority("/sys_log")
//                .antMatchers("/sysuser").hasAuthority("/sys_user")
//                .anyRequest().authenticated()
                .and().apply(smsCodeSecurityConfig)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login.html")
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(new MyExpireSessionStrategy());


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder().encode("123456"))
//                .roles("user")
//                .and()
//                .withUser("admin")
//                .password(passwordEncoder().encode("123456"))
////                .roles("admin")
//                .authorities("sys:log","sys:user")
//                .and()
//                .passwordEncoder(passwordEncoder());
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "fonts/**", "/img/**", "/js/**");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

}
