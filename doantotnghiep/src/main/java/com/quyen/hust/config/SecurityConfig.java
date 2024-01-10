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
//                .antMatchers(HttpMethod.POST, "/authentication/login", "/authentication/signup").permitAll()
//                .antMatchers(HttpMethod.POST, "/authentication/refresh-token", "/authentication/logout").authenticated()
//                .antMatchers(HttpMethod.GET, "/users", "/users/{id}").hasAnyAuthority(Roles.USER.toString(), Roles.ADMIN.toString())
//                .antMatchers(HttpMethod.POST, "/users").hasAnyAuthority(Roles.ADMIN.toString())
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .exceptionHandling(
                        configurer -> configurer.accessDeniedPage("/403")
                ).headers().frameOptions().sameOrigin();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
