package zlc.season.morbidmask.compiler

import com.squareup.kotlinpoet.ClassName

class ParamsInfo(
    val key: String,
    val className: ClassName,
    val isMutable: Boolean
)