package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("select username, password, active from users where username=?")
                .authoritiesByUsernameQuery("select u.username,ur.roles from users u inner join user_role ur on u.id = ur.user_id where u.username=?");

        auth.inMemoryAuthentication()
                .withUser("a")
                .roles("ADMIN")
                .password("{noop}p")
                .and()
                .withUser("al")
                .password("{noop}pass")
                .roles("USER");
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
