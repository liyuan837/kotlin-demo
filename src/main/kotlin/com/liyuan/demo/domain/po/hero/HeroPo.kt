package com.liyuan.demo.domain.po.hero

import com.liyuan.demo.annotation.NoArg
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @Author:LiYuan
 * @description:
 * @Date:Create in 10:54 2018/7/6
 * @Modified By:
 */
@NoArg
@Document(collection="hero")
data class HeroPo(
        //主键
        @Id
        var id:String?,

        var name:String?,

        var type:Int?,

        var desc:String
)