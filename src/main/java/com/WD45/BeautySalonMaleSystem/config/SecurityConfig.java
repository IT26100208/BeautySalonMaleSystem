package com.WD45.BeautySalonMaleSystem.config;

import com.WD45.BeautySalonMaleSystem.entity.Admin;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;
import com.WD45.BeautySalonMaleSystem.service.CustomUserDetailsService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService) {

        this.userDetailsService = userDetailsService;
    }



    // PASSWORD ENCODER


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }



    // DEFAULT ADMIN


    @Bean
    public CommandLineRunner initAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Read the temporary reset password from
            // your computer's environment.
            String resetPassword =
                    System.getenv("SALON_ADMIN_RESET_PASSWORD");

            // Only run the reset when explicitly requested.
            if (resetPassword == null || resetPassword.isBlank()) {
                return;
            }

            if (resetPassword.length() < 12) {
                throw new IllegalArgumentException(
                        "Reset password must be at least 12 characters."
                );
            }

            // Find the existing admin account.
            Admin admin = userRepository
                    .findByEmail("admin@salon.com")
                    .filter(user -> user instanceof Admin)
                    .map(user -> (Admin) user)
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Admin account not found."
                            )
                    );

            // Encrypt the new password using BCrypt.
            String encodedPassword =
                    passwordEncoder.encode(resetPassword);

            // Replace the old password with the encoded one.
            admin.setPassword(encodedPassword);

            // Save the updated admin.
            userRepository.save(admin);

            System.out.println(
                    "Admin password reset successfully!"
            );
        };
    }



    // AUTHENTICATION PROVIDER


    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }



    // LOGIN SUCCESS


    @Bean
    public AuthenticationSuccessHandler
    authenticationSuccessHandler() {

        return (request, response, authentication) -> {

            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (authentication.getAuthorities()
                    .stream()
                    .anyMatch(a ->
                            a.getAuthority()
                                    .equals("ROLE_CUSTOMER"))) {

                response.sendRedirect("/customer");

            } else {

                response.sendRedirect("/login?error");
            }
        };
    }
    // SECURITY


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // AUTHORIZATION


                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()

                        // ADMIN
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/dashboard/services/**")
                        .hasRole("ADMIN")

                        // CUSTOMER
                        .requestMatchers(
                                "/customer/**"
                        )
                        .hasRole("CUSTOMER")

                        // SERVICES
                        .requestMatchers("/services/**")
                        .permitAll()

                        //PUBLIC BROWSING
                        .requestMatchers(
                                "/settings",
                                "/settings/**"
                        ).authenticated()

                        //FEEDBACK
                        .requestMatchers("/customer/feedback/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/admin/feedback/**").hasRole("ADMIN")

                        // EVERYTHING ELSE
                        .anyRequest()
                        .authenticated()
                )



                // LOGIN


                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .usernameParameter("email")

                        .passwordParameter("password")

                        .successHandler(
                                authenticationSuccessHandler()
                        )

                        .failureUrl(
                                "/login?error"
                        )

                        .permitAll()
                )



                // LOGOUT


                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl(
                                "/login?logout"
                        )

                        .invalidateHttpSession(true)

                        .clearAuthentication(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                );

        return http.build();
    }
}