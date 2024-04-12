package engine.security

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {

    @Bean
    fun userDetailsService(): UserDetailsService {
        return AppUserDetailsImpl()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService())
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { matcherRegistry ->
                matcherRegistry
                    .requestMatchers(HttpMethod.GET, "/api/quizzes").hasRole("USER")
                    .requestMatchers(HttpMethod.POST,"/api/quizzes").hasRole("USER")
                    .requestMatchers(HttpMethod.POST,"/api/quizzes/**").hasRole("USER")
                    .requestMatchers(HttpMethod.GET,"/api/quizzes/**").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/quizzes/**").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                    .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .anyRequest().permitAll()
            }
            .httpBasic(Customizer.withDefaults())
            .csrf().disable()
            .cors().disable()
            .formLogin(Customizer.withDefaults())
            .headers().frameOptions().disable()

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}

