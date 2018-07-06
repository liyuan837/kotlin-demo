package com.liyuan.demo.dao

import com.liyuan.demo.dao.inner.JpaMongoTemplate
import com.liyuan.demo.domain.po.hero.HeroPo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

/**
 * @Author:LiYuan
 * @description:
 * @Date:Create in 10:58 2018/7/6
 * @Modified By:
 */
@Repository
interface HeroRepository : ReactiveMongoRepository<HeroPo, String> {

}

@Component
class HeroTemplate : JpaMongoTemplate<HeroPo>()