package zlc.season.morbidmaskapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_test.*
import zlc.season.morbidmask.annotation.MutableParams
import zlc.season.morbidmask.annotation.Params
import zlc.season.morbidmask.annotation.Val
import zlc.season.morbidmask.annotation.Var

@Params(
    Val("byteParam", Byte::class),
    Val("shortParam", Short::class),
    Val("intParam", Int::class),
    Val("longParam", Long::class),
    Val("floatParam", Float::class),
    Val("doubleParam", Double::class),
    Val("charParam", Char::class),
    Val("booleanParam", Boolean::class),
    Val("stringParam", String::class),
    Val("customParam", CustomEntity::class),
    Val("parcelable", ParcelableEntity::class),
    Val("serializable", SerializableEntity::class)
)
@MutableParams(
    Var("test", String::class),
    Var("test1", Boolean::class)
)
class TestActivity : AppCompatActivity() {

    private val params by lazy { TestActivityParams.of(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Log.d("TAG", params.stringParam)
        Log.d("TAG", params.booleanParam.toString())
        Log.d("TAG", params.intParam.toString())
        val content = """
            Params:
            byteParams = ${params.byteParam}
            shortParam = ${params.shortParam}
            intParam = ${params.intParam}
            longParam = ${params.longParam}
            floatParam = ${params.floatParam}
            doubleParam = ${params.doubleParam}
            charParam = ${params.charParam}
            booleanParam = ${params.booleanParam}
            stringParam = ${params.stringParam}
            customParam = ${params.customParam}
             parcelable = ${params.parcelable}
             serializable = ${params.serializable}
        """.trimIndent()

        tv_content.text = content

        //change params
        params.test = "123"
        params.test1 = true
    }
}
