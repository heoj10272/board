package com.heoj10272.config;

import com.heoj10272.security.handler.BoardLoginSuccessHandler;
import com.heoj10272.security.service.BoardUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BoardUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
//                .antMatchers("/board/list").permitAll()
//                .antMatchers("/board/member").hasRole("USER");

        http.formLogin().successHandler(successHandler()); //인가/인증에 문제시 로그인 화면
        http.csrf().disable();
        http.logout().logoutSuccessUrl("/board/list");
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailsService);  //7days
    }

    @Bean
    public BoardLoginSuccessHandler successHandler() {
        return new BoardLoginSuccessHandler();
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication().withUser("user1") //사용자 계정은 user1
//                .password("$2a$10$Bw56CkKwQbKEtXjNOHH8huzOc2GY19nbrV7cvjeqY.fHhXa9WtNya") //1111 패스워드 인코딩
//                .roles("USER");
//
//    }


}