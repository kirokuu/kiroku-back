package com.example.kiroku.config;



import com.example.kiroku.login.service.OAuth2SuccessHandler;
import com.example.kiroku.login.service.impl.CustomOauth2UserServiceImpl;
import com.example.kiroku.security.filter.AuthTokenFilter;
import com.example.kiroku.security.jwt.AuthEntryPointJwt;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.domain.type.UserType;
import com.example.kiroku.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;

    private final CustomOauth2UserServiceImpl customOauth2UserService;
    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error, favicon.ico
                .requestMatchers("/error", "/favicon.ico","/home",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html");
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

        http.formLogin(form -> form.disable());
        //엔드포인트에서 권한체크에 실패하였을시 에러핸들링
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        //http.httpBasic(withDefaults());
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                        .sameOrigin()
                )
        );

        http.csrf(csrf -> csrf.disable());
        http.oauth2Login(oauth ->oauth.userInfoEndpoint(endpoint->
                endpoint.userService(customOauth2UserService)).
                successHandler(oAuth2SuccessHandler));
        //jwt토큰필터
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean //TODO test용
    public CommandLineRunner initData(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
                User user1 = User.createUser(
                        "user1@gmail.com",
                        encoder.encode("12345678"),
                        "010-1234-0001",
                        UserType.ROLE_USER);
                repo.save(user1);

                User user2 = User.createUser(
                        "user2@gmail.com",
                        encoder.encode(
                                "12345678"),
                        "010-1234-0001",
                        UserType.ROLE_USER);
                repo.save(user2);
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
