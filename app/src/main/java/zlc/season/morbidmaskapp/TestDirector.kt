package zlc.season.morbidmaskapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager

class TestDirector private constructor(private val context: Context) {
    private val intent = Intent(context, TestActivity::class.java)

    fun byteParam(byte: Byte): TestDirector {
        intent.putExtra("byteParam", byte)
        return this
    }

    fun stringParam(string: String): TestDirector {
        intent.putExtra("stringParam", string)
        return this
    }

    fun get(): Intent {
        return intent
    }

    fun direct() {
        context.startActivity(intent)
    }

    companion object {
        fun of(context: Context): TestDirector {
            return TestDirector(context)
        }
    }
}

class FragmentDirector private constructor(
    private val fragmentManager: FragmentManager
) {

    val bundle = Bundle()

    fun byteParam(byte: Byte): FragmentDirector {
        bundle.putByte("byteParam", byte)
        return this
    }

    fun get(): TestDialogFragment {
        return TestDialogFragment()
            .apply { arguments = bundle }
    }

    fun direct(tag: String = ""): TestDialogFragment {
        return TestDialogFragment()
            .apply { arguments = bundle }
            .also { it.show(fragmentManager, tag) }
    }

    fun direct(block: (TestDialogFragment) -> Unit = {}): TestDialogFragment {
        return TestDialogFragment()
            .apply { arguments = bundle }
            .also {
                block(it)
            }
    }

    companion object {
        fun of(fragmentManager: FragmentManager): FragmentDirector {
            return FragmentDirector(fragmentManager)
        }
    }
}