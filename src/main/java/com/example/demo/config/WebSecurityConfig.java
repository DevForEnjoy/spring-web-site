package com.example.demo.config;

import com.example.demo.domain.Role;
import com.example.demo.servise.UserServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServise userServise;
    private DataSource dataSource;

    private Role role;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("a")
                .roles("ADMIN")
                .password("{noop}p")
                .and()
                .withUser("al")
                .password("{noop}pass")
                .roles("USER");

        auth.userDetailsService(userServise)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] staticResources = {
                "/css/**",
                "/img/**",
                "/fonts/**",
                "/scripts/**",
        };

        http.authorizeRequests().antMatchers(staticResources).permitAll();

        http.authorizeRequests()
                .antMatchers("/", "/registration", "/home","/logout").permitAll()
                .antMatchers("/logout").hasAnyAuthority()
                .antMatchers("/index").hasAnyRole("USER","ADMIN")
                .antMatchers("/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();

    }

}
