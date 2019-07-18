package zlc.season.morbidmaskrecipe

import kotlin.reflect.KClass

class ParamsInfo(
    val key: String,
    val type: KClass<*>,
    val default: Any
)