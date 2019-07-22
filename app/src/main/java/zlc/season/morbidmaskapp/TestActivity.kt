package zlc.season.morbidmaskapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*
import zlc.season.morbidmask.annotation.Params
import zlc.season.morbidmask.annotation.Val

@Params(
    Val("byteParam", Byte::class),
    Val("shortParam", Short::class),
    Val("intParam", Int::class),
    Val("longParam", Long::class),
    Val("floatParam", Float::class),
    Val("doubleParam", Double::class),
    Val("charParam", Char::class),
    Val("booleanParam", Boolean::class),
    Val("stringParam", String::class)
)
open class TestActivity : AppCompatActivity() {

    private val params by lazy { TestActivityParams.of(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

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
        """.trimIndent()

        tv_content.text = content
    }
}
