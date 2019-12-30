package zlc.season.morbidmaskapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_test.*
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
    Val("stringParam", String::class),
    Val("customParam", CustomEntity::class),
    Val("parcelable", ParcelableEntity::class),
    Val("serializable", SerializableEntity::class)
)
class TestFragment : Fragment() {
    private val params by lazy { TestFragmentParams.of(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }
}
