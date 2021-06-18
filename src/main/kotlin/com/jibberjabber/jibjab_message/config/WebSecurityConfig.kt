package com.jibberjabber.jibjab_message.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig
@Autowired constructor(private val unauthorizedHandler: AuthEntryPoint) : WebSecurityConfigurerAdapter() {

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter()
    }

    override
    fun configure(http: HttpSecurity) {
//        http.csrf().disable()
//        http.authorizeRequests().antMatchers("/").permitAll()
        http
            .csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers("/ws/**").permitAll()
            // SWAGGER CONFIG
            .antMatchers("/v3/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/ui","/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated().and()
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .headers().xssProtection().and().contentSecurityPolicy("script-src 'self'")
    }
}

//
//@Configuration
//class WebMvcConfig : WebMvcConfigurer {
//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("*")
//            .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
//            .maxAge(MAX_AGE_SECS)
//    }
//
//    companion object {
//        private const val MAX_AGE_SECS: Long = 3600
//    }
//}

//@Configuration
//class SecurityConfig {
//    @Bean
//    fun corsFilter(): CorsFilter {
//        val source = UrlBasedCorsConfigurationSource()
//        val config = CorsConfiguration()
//        config.allowCredentials = true
//        config.addAllowedOrigin("http://localhost:3000")
//        config.addAllowedHeader("*")
//        config.addAllowedMethod("OPTIONS")
//        config.addAllowedMethod("HEAD")
//        config.addAllowedMethod("GET")
//        config.addAllowedMethod("PUT")
//        config.addAllowedMethod("POST")
//        config.addAllowedMethod("DELETE")
//        config.addAllowedMethod("PATCH")
//        source.registerCorsConfiguration("/**", config)
//        return CorsFilter(source)
//    }
//}