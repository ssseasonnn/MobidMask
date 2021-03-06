package zlc.season.morbidmaskapp

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import kotlinx.android.synthetic.main.dialog_test.*
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
class TestDialogFragment : DialogFragment() {
    private val params by lazy { TestDialogFragmentParams.of(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_test, container, false)
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

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }
}