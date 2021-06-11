package com.jibberjabber.jibjab_posts.utils

import com.jibberjabber.jibjab_message.config.AuthEntryPoint
import com.jibberjabber.jibjab_message.dto.UserInfoDto
import com.jibberjabber.jibjab_posts.exception.BadRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URI


@Component
class UserUtils {

    @Value("\${AUTH_HOST}")
    private val authHost: String = "localhost"

    @Value("\${AUTH_PORT}")
    private val authPort: String = "8080"

    private val logger: Logger = LoggerFactory.getLogger(AuthEntryPoint::class.java)


    fun getTokenUserInformation(): UserInfoDto {
        return SecurityContextHolder.getContext().authentication.principal as UserInfoDto
    }

    fun getUserInfoFromId(userId: String): UserInfoDto? {
        val restTemplate = RestTemplate()
        val getUserUrl = "http://$authHost:$authPort/api/user/users/info/$userId"
        logger.info("Authenticating with: $getUserUrl")
        val getUserUri = URI(getUserUrl)
        val headers = HttpHeaders()
        headers.add("Cookie", "jwt=" + SecurityContextHolder.getContext().authentication.credentials.toString())
        val httpEntity = HttpEntity<String>(headers)
        val response: ResponseEntity<UserInfoDto> =
            restTemplate.exchange(getUserUri, HttpMethod.GET, httpEntity, UserInfoDto::class.java)
        if (response.statusCodeValue != 200) throw BadRequestException("Authentication Server couldn't authenticate jwt")
        val userInfoDto = response.body
        logger.info("Got response: " + response.statusCodeValue + ": " + userInfoDto?.email)
        return userInfoDto
    }

}