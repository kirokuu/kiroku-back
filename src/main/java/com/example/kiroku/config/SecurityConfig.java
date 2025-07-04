package com.example.kiroku.config;



import com.example.kiroku.com.security.filter.AuthTokenFilter;
import com.example.kiroku.com.security.jwt.AuthEntryPointJwt;
import com.example.kiroku.login.domain.User;
import com.example.kiroku.login.domain.type.UserType;
import com.example.kiroku.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/**").permitAll()
                        .anyRequest().authenticated());
        //세션대신 jwt토큰을 사용하므로 세션생성을 막는다.
        http.sessionManagement(
                session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
        );

        http.formLogin(form -> form.
                loginPage("/login")
                .loginProcessingUrl("/login")           // 로그인 폼 제출(POST) 처리 URL
                .defaultSuccessUrl("/", true)           // 로그인 성공 시 이동할 기본 페이지
                .failureUrl("/login?error=true")
                .permitAll());
        // 3) 로그아웃 설정 (선택)
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .permitAll()
        );

        //엔드포인트에서 권한체크에 실패하였을시 에러핸들링
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.httpBasic(withDefaults());
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                        .sameOrigin()
                )
        );

        http.csrf(csrf -> csrf.disable());

        //jwt토큰필터
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public CommandLineRunner initData(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                User user1 = User.createUser("user1",
                        encoder.encode("password1"),
                        "010-1234-0001",
                        UserType.ROLE_USER);
                repo.save(user1);

                User user2 = User.createUser("user1",
                        encoder.encode(
                                "password1"),
                        "010-1234-0001",
                        UserType.ROLE_USER);
                repo.save(user2);

            }

        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
