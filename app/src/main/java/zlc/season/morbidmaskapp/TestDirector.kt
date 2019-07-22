package zlc.season.morbidmaskapp

import android.content.Intent

class TestDirector private constructor() {
    val intent = Intent()

    fun byteParam(byte: Byte): TestDirector {
        intent.putExtra("byteParam", byte)
        return this
    }

    fun get(): Intent {
        return intent
    }

    companion object {
        fun of(): TestDirector {
            return TestDirector()
        }
    }
}