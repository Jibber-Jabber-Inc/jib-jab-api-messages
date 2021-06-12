package com.jibberjabber.jibjab_message.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(message: String?) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String?) : RuntimeException(message)