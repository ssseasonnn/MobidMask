package zlc.season.morbidmask

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Params(
    vararg val value: Val = [Val("param")]
)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class MutableParams(
    vararg val value: Var = [Var("param")]
)

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


