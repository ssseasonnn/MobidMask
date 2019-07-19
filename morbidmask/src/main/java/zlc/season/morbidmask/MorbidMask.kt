package zlc.season.morbidmask

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Params(
    vararg val value: Val
)


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class MutableParams(
    vararg val value: Var
)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Val(
    val key: String,
    val type: String = string
)


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Var(
    val key: String,
    val type: String = string
)

@Params(
    Val("123"),
    Val("sdf")
)
@Var("aaa", string)
class Test {
    val test: String = ""
}

const val byte = "Byte"
const val short = "Short"
const val int = "Int"
const val long = "Long"
const val float = "Float"
const val double = "Double"
const val char = "Char"
const val boolean = "Boolean"
const val string = "String"


