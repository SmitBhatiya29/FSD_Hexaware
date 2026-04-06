package com.springboot.easypay.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

//    private final UserService userService;
    private final JwtFilter jwtFilter;
    /** This was my Phase-1 In-Memory Auth Manager */
    /*
    @Bean
    public UserDetailsService users() {
        UserDetails customer1 = User.builder()
                .username("harry")
                .password("{noop}potter")
                .authorities("CUSTOMER")
                .build();
        UserDetails customer2 = User.builder()
                .username("ronald")
                .password("{noop}weasley")
                .authorities("CUSTOMER")
                .build();
        UserDetails executive1 = User.builder()
                .username("hermione")
                .password("{noop}granger")
                .authorities("EXECUTIVE")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .authorities("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(customer1,customer2,executive1, admin);
    }
*/
    @Bean
    public SecurityFilterChain bankingSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/employee/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/login").permitAll()
                        .requestMatchers("/api/hr/**").hasAuthority("HR")
                        .requestMatchers(HttpMethod.POST, "/api/salary-structure/add").hasAuthority("HR")
                        .requestMatchers(HttpMethod.PUT, "/api/salary-structure/update/**").hasAuthority("HR")


                        .requestMatchers("/api/manager/**").hasAuthority("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/timesheets/pending").hasAuthority("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/timesheets/status/**").hasAuthority("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/payroll/team-summary/**").hasAuthority("MANAGER")

                        .requestMatchers(HttpMethod.POST, "/api/benefits/add").hasAnyAuthority("HR","PAYROLL_PROCESSOR")
                        .requestMatchers(HttpMethod.POST, "/api/payroll/generate/**").hasAnyAuthority("HR", "PAYROLL_PROCESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/payroll/drafts").hasAnyAuthority("HR", "PAYROLL_PROCESSOR")
                        .requestMatchers(HttpMethod.PUT, "/api/payroll/process/**").hasAnyAuthority("HR", "PAYROLL_PROCESSOR")

                        .requestMatchers("/api/employee/**").hasAnyAuthority("EMPLOYEE", "MANAGER", "HR")
                        .requestMatchers("/api/leave-request/**").hasAnyAuthority("EMPLOYEE", "MANAGER", "HR")
                        .requestMatchers(HttpMethod.POST, "/api/timesheets/submit").hasAnyAuthority("EMPLOYEE", "MANAGER", "HR")
                        .requestMatchers(HttpMethod.GET, "/api/salary-structure/get/**").hasAnyAuthority("EMPLOYEE", "MANAGER", "HR")
                        .requestMatchers(HttpMethod.GET, "/api/payroll/pay-stub/**").hasAnyAuthority("EMPLOYEE", "MANAGER", "HR")
                        .requestMatchers(HttpMethod.POST, "/api/employee-benefits/assign").hasAnyAuthority( "PAYROLL_PROCESSOR", "HR")

                        .anyRequest().authenticated()

                );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            UserDetailsService userDetailsService,
//            PasswordEncoder passwordEncoder
//    )  {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//        return new ProviderManager(daoAuthenticationProvider);
//    }




}