package com.jibberjabber.jibjab_message.config

import com.jibberjabber.jibjab_message.dto.UserInfoDto
import com.jibberjabber.jibjab_message.exception.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.OncePerRequestFilter
import java.net.URI
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthTokenFilter : OncePerRequestFilter() {

    @Value("\${AUTH_HOST}")
    private val authHost: String = "localhost"

    @Value("\${AUTH_PORT}")
    private val authPort: String = "8080"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (request.cookies == null) throw BadRequestException("jwt not found")
            val jwtCookie: Cookie = request.cookies.first { cookie -> cookie.name == "jwt" }
            val userInfo: UserInfoDto? = sendUserServiceRequest(jwtCookie.value)
            val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(userInfo?.role))
            val authentication = UsernamePasswordAuthenticationToken(userInfo, jwtCookie.value, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            response.sendError(401, "Cannot set user authentication: " + e.message)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun sendUserServiceRequest(jwt: String): UserInfoDto? {
        val restTemplate = RestTemplate()
        val getUserUrl = "http://$authHost:$authPort/api/user/users/loggedUser"
        logger.info("Authenticating with: $getUserUrl")
        val getUserUri = URI(getUserUrl)
        val headers = HttpHeaders()
        headers.add("Cookie", "jwt=$jwt")
        val httpEntity = HttpEntity<String>(headers)
        val response: ResponseEntity<UserInfoDto> = restTemplate.exchange(getUserUri, HttpMethod.GET, httpEntity, UserInfoDto::class.java)
        if (response.statusCodeValue != 200) throw BadRequestException("Authentication Server couldn't authenticate jwt")
        return response.body
    }
}
