package com.mskim.place_searching.configuration.security;

import com.mskim.place_searching.auth.AuthRepository;
import com.mskim.place_searching.auth.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthRepository authRepository;

    @Autowired
    public SecurityConfigurer(AuthRepository authRepository) {
        super();
        this.authRepository = authRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
                Member member = authRepository.findByAccount(account)
                                                .orElseThrow(() -> new UsernameNotFoundException(account));
                System.out.println("===================> Member: " + member);
                Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
                grantedAuthorities.add(new SimpleGrantedAuthority("user"));

                return new User(member.getAccount(), member.getPassword(), grantedAuthorities);
            }
        }).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/auth/sign_in",
                         "/h2/**", "/favicon.ico").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().usernameParameter("id") // Parameter is received from view
                        .passwordParameter("password")
                        .loginPage("/auth/sign_in")
                        .loginProcessingUrl("/auth/validation") // Request receiving  from form submit
                        .successHandler(new SignInSuccessHandler("/"));
//                        .and().exceptionHandling().authenticationEntryPoint();
//                        .failureForwardUrl("/error");
    }
}
