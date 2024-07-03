package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/login", "/join").permitAll()
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/my/**").hasAnyRole("USER", "ADMIN")
//                .requestMatchers("/admin").hasRole("ADMIN")
//                .anyRequest().authenticated() //authenticated : 로그인한 사용자 가능
//        );

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
        );

        //formLogin방식
//        http.formLogin((auth) -> auth.loginPage("/login")
//                .loginProcessingUrl("/loginProc").permitAll()
//        );
        
        //httpBasic방식
        http.httpBasic(Customizer.withDefaults());


        //디폴트는 enable
//        http.csrf((auth) -> auth.disable());

        http.sessionManagement((auth) -> auth
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true));

        //세션 고정 보호(해킹으로부터 안전)
        http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());

        //로그아웃을 위한 설정
        http.logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));

        return http.build();
    }

    //Inmemory방식으로 회원 추가
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.builder()
//                .username("user1")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
}
