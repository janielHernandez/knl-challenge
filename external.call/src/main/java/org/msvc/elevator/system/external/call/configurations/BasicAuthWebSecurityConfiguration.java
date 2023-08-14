package org.msvc.elevator.system.external.call.configurations;

import com.elevator.system.common.constants.BuildingAttributes;
import org.msvc.elevator.system.external.call.api.controllers.PublicElevatorCallController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class BasicAuthWebSecurityConfiguration{


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .csrf().disable()
                .authorizeRequests()
                .requestMatchers(getForbiddenFloors()).authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic();
        return http.build();
    }

    private String[] getForbiddenFloors() {

        String urlAllows[] = new String[]{
            PublicElevatorCallController.URL.concat("/")
                    .concat(String.valueOf(BuildingAttributes.BASEMENT_FLOOR)),
            PublicElevatorCallController.URL.concat("/")
                    .concat(String.valueOf(BuildingAttributes.MAX_FLOOR))
        };
        return urlAllows;

    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("root")
                .password(passwordEncoder().encode("sasa"))
                .roles("USER_ROLE")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

        @Bean
        public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
