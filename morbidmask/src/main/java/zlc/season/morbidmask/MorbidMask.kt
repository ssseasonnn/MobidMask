package zlc.season.morbidmask

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Val(
    val key: String,
    val type: KClass<*> = String::class
)


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Var(
    val key: String,
    val type: KClass<*> = String::class
)

@Val("aaa")
class Test {

}