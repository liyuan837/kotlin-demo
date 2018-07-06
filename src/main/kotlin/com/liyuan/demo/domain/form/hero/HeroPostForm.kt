package com.liyuan.demo.domain.form.hero

import com.liyuan.demo.annotation.NoArg

/**
 * @Author:LiYuan
 * @description:
 * @Date:Create in 13:36 2018/7/6
 * @Modified By:
 */
@NoArg
class HeroPostForm(
        var name:String?,
        var type:Int?,
        var desc:String?
)