package higmsa.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/**").permitAll()  // "/users/**" 경로는 인증 없이 접근 허용
                        .requestMatchers("/**").permitAll()  // "/users/**" 경로는 인증 없이 접근 허용
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                );
        return http.build();
    }
}


//package higmsa.userservice.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfiguration {
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/users/**").permitAll() // "/users/**" 경로는 인증 없이 접근 허용
//                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
//                );
//
//        return http.build();
//    }
//}
//
//
//
//
