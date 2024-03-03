package com.quyen.hust.config;

import com.quyen.hust.security.AuthTokenFilter;
import com.quyen.hust.security.AuthenticationEntryPointJwt;
import com.quyen.hust.security.CustomUserDetailsService;
import com.quyen.hust.statics.Roles;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    private final AuthenticationEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //authentication
                .antMatchers(HttpMethod.POST, "/api/v1/authentication/login", "/api/v1/authentication/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/authentication/refresh-token", "/api/v1/authentication/logout").authenticated()
                //manage account
                .antMatchers(HttpMethod.PUT, "/api/v1/accounts/**").hasAuthority(Roles.ADMIN.getCode())
                //manage email sending

                //manage discount code
                .antMatchers(HttpMethod.GET, "/api/v1/discount-codes/**").hasAuthority(Roles.ADMIN.getCode())
                .antMatchers(HttpMethod.POST, "/api/v1/discount-codes/**").hasAuthority(Roles.ADMIN.getCode())
                .antMatchers(HttpMethod.PUT, "/api/v1/discount-codes/**").hasAuthority(Roles.ADMIN.getCode())
                .antMatchers(HttpMethod.DELETE, "/api/v1/discount-codes/**").hasAuthority(Roles.ADMIN.getCode())
                //manage training field
                .antMatchers(HttpMethod.GET, "/api/v1/training-fields/**").hasAuthority(Roles.ADMIN.getCode())
                .antMatchers(HttpMethod.POST, "/api/v1/training-fields/**").hasAuthority(Roles.ADMIN.getCode())
                .antMatchers(HttpMethod.PUT, "/api/v1/training-fields/**").hasAuthority(Roles.ADMIN.getCode())
                .antMatchers(HttpMethod.DELETE, "/api/v1/training-fields/**").hasAuthority(Roles.ADMIN.getCode())
                //manage course
                .antMatchers(HttpMethod.POST, "/api/v1/courses/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.PUT, "/api/v1/courses/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.DELETE, "/api/v1/courses/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                //manage section
                .antMatchers(HttpMethod.POST, "/api/v1/sections/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.GET, "/api/v1/sections/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.PUT, "/api/v1/sections/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.DELETE, "/api/v1/sections/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                //manage lesson
                .antMatchers(HttpMethod.POST, "/api/v1/lessons/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.GET, "/api/v1/lessons/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.PUT, "/api/v1/lessons").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.DELETE, "/api/v1/lessons/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                //manage quiz
                .antMatchers(HttpMethod.POST, "/api/v1/quizzes/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.PUT, "/api/v1/quizzes/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.DELETE, "/api/v1/quizzes/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                //manage dashboard
                .antMatchers(HttpMethod.GET, "/api/v1/courses/analysis/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.POST, "/api/v1/courses/analysis/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.PUT, "/api/v1/courses/analysis/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())
                .antMatchers(HttpMethod.DELETE, "/api/v1/courses/analysis/**").hasAnyAuthority(Roles.ADMIN.getCode(), Roles.TEACHER.getCode())

                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .exceptionHandling(
                        configurer -> configurer.accessDeniedPage("/403")
                ).headers().frameOptions().sameOrigin();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new EncodingFilter(), ChannelProcessingFilter.class);
    }

}
