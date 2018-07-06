package com.liyuan.demo.util

import org.springframework.beans.BeanUtils
import org.springframework.beans.BeansException
import org.springframework.beans.FatalBeanException
import org.springframework.util.Assert
import org.springframework.util.ClassUtils
import java.lang.reflect.Modifier
import java.util.*

/**
 * 简便的返回一个复制类
 */
inline fun <reified T> transfer(src: Any?): T {
    val target = T::class.java.newInstance()
    BeanUtils.copyProperties(src!!, target)
    return target
}

/**
 * jpa中经常更新的时候需要null不能覆盖
 */
fun copyPropertiesWithoutNull(source: Any, target: Any) {
    return copyPropertiesWithoutNull(source, target, null, null)
}

/**
 * jpa中剔除了null和id
 */
fun copyPropertiesWithoutNullIgnoreId(source: Any, target: Any) {
    return copyPropertiesWithoutNull(source, target, null, "id")
}


/**
 * 改造的spring中beantutil中复制类
 */
@Throws(BeansException::class)
private fun copyPropertiesWithoutNull(source: Any, target: Any, editable: Class<*>?,
                                      vararg ignoreProperties: String?) {

    Assert.notNull(source, "Source must not be null")
    Assert.notNull(target, "Target must not be null")

    var actualEditable: Class<*> = target.javaClass
    if (editable != null) {
        if (!editable.isInstance(target)) {
            throw IllegalArgumentException("Target class [" + target.javaClass.name +
                    "] not assignable to Editable class [" + editable.name + "]")
        }
        actualEditable = editable
    }
    val targetPds = BeanUtils.getPropertyDescriptors(actualEditable)
    val ignoreList = if (ignoreProperties != null) Arrays.asList(*ignoreProperties) else null

    for (targetPd in targetPds) {
        val writeMethod = targetPd.writeMethod
        if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.name))) {
            val sourcePd = BeanUtils.getPropertyDescriptor(source.javaClass, targetPd.getName())
            if (sourcePd != null) {
                val readMethod = sourcePd.readMethod
                if (readMethod != null && ClassUtils.isAssignable(writeMethod.parameterTypes[0], readMethod.returnType)) {
                    try {
                        if (!Modifier.isPublic(readMethod.declaringClass.modifiers)) {
                            readMethod.isAccessible = true
                        }
                        val value = readMethod.invoke(source) ?: continue
                        if (!Modifier.isPublic(writeMethod.declaringClass.modifiers)) {
                            writeMethod.isAccessible = true
                        }
                        writeMethod.invoke(target, value)
                    } catch (ex: Throwable) {
                        throw FatalBeanException(
                                "Could not copy property '" + targetPd.name + "' from source to target", ex)
                    }

                }
            }
        }
    }
}

/**
 * 测试类
 */
fun main(args: Array<String>) {
//    @NoArg
    data class User1(var name: String, var age: Int?)

//    @NoArg
    data class UserVo1(var name: String, var age: Int?)

    val user = User1("zmy", null)

    val userVo1 = UserVo1("java", 11)

//    val userVo = transferWithoutNull<UserVo1>(user)

    copyPropertiesWithoutNull(user, userVo1)
    println("name: ${userVo1.name} and age: ${userVo1.age}")

}