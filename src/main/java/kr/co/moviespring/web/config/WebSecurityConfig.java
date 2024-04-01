//package kr.co.moviespring.web.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // 포스트 요청을 보낼때 csrf를 방지하고자 토큰을 비교하는 설정을 끈다
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/user/**").hasAnyRole("MEMBER","ADMIN") //역할별 권한설정, 멤버와 관리자 모두 접속가능
//                        .requestMatchers("/admin/**").hasRole("ADMIN")//관리자만 접속가능
//                        .anyRequest().permitAll())
//                .formLogin((form) -> form
//                        .loginPage("/user/signin")
//                        .permitAll())
//        .logout((logout) -> logout
//        .logoutUrl("/user/logout")
//        .logoutSuccessUrl("/index")
//        .permitAll());
//
//        return http.build();
//    }
//
////    @Bean
////    public UserDetailsService userDetailsService() {
////        UserDetails user = User.builder()
////                .username("admin")
////                .password("{noop}1234") //비번을 넣으려면 암호화를 해줘서 넣어야 한다 ,그게 아니라면 noop을 포함해서 비번을 작성
////                .roles("MEMBER", "ADMIN")
////                .build();
////
////        UserDetails user2 = User.builder()
////                .username("dragon")
////                .password("{noop}222")
////                .roles("MEMBER")
////                .build();
////        return new InMemoryUserDetailsManager(user);
////    }
//
//}
