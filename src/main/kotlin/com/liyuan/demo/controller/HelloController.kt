package com.liyuan.demo.controller

import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @Author:LiYuan
 * @description:
 * @Date:Create in 19:23 2018/7/5
 * @Modified By:
 */
@RestController
class HelloController {

    @ApiOperation(value = "hello", notes = "hello", httpMethod = "GET")
    @GetMapping("/hello")
    fun hello(): Mono<String> {
        return Mono.just("hello")
    }
}