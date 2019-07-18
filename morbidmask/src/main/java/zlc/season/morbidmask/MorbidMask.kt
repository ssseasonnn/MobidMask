package zlc.season.morbidmask

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Val(
    val key: String,
    val type: Int = string
)


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Var(
    val key: String,
    val type: Int = string
)

@Var("aaa", int)
class Test {
    val test: String = ""
}

//enum class Type{
//    Byte,Short,Int,Long,Float,Double,Char,Boolean,String
//}
const val byte = 0
const val short = 1
const val int = 2
const val long = 3
const val float = 4
const val double = 5
const val char = 6
const val boolean = 7
const val string = 8


