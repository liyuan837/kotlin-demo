package com.liyuan.demo.controller

import com.liyuan.demo.dao.HeroRepository
import com.liyuan.demo.domain.form.hero.HeroPostForm
import com.liyuan.demo.domain.po.hero.HeroPo
import com.liyuan.demo.util.Result
import com.liyuan.demo.util.getSuccessMono
import com.liyuan.demo.util.transfer
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * @Author:LiYuan
 * @description:
 * @Date:Create in 19:23 2018/7/5
 * @Modified By:
 */
@RestController
@RequestMapping("hero")
class HeroController(private val heroRepository: HeroRepository){

    @ApiOperation(value = "查询人物", notes = "查询人物", httpMethod = "GET")
    @GetMapping("/query")
    fun query(@RequestParam("id") id:String): Mono<Result>{
        return heroRepository.findById(id).map {
            getSuccessMono(it)
        }

    }

    @ApiOperation(value = "保存", notes = "保存", httpMethod = "POST")
    @PostMapping("/add")
    fun add(@RequestBody heroPostForm: HeroPostForm): Mono<Result> {
        var heroPo = transfer<HeroPo>(heroPostForm)
        return heroRepository.save(heroPo).map {
            getSuccessMono(it)
        }
    }

    @ApiOperation(value = "删除", notes="删除",httpMethod = "POST")
    @PostMapping("/delete")
    fun delete(@RequestParam("id") id:String):Mono<Result>{
        return heroRepository.deleteById(id).map {
            getSuccessMono(it)
        }
    }
}