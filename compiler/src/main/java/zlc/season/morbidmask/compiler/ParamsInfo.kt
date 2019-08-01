package zlc.season.morbidmask.compiler

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.type.TypeMirror

class ParamsInfo(
    val key: String,
    val typeMirror: TypeMirror,
    val className: ClassName,
    val isMutable: Boolean
)