package zlc.season.morbidmaskapp

import android.os.Bundle

class MainTestActivityParams
private constructor(bundle: Bundle?) {

    var test = ""

    companion object {
        fun of(activity: MainActivity): MainTestActivityParams {
            return MainTestActivityParams(activity.intent.extras)
        }
    }

    init {


        val byte = bundle?.getByte("") ?: 0
        val short = bundle?.getShort("") ?: 0
        val long = bundle?.getLong("") ?: 0L
        val float = bundle?.getFloat("") ?: 0f
        val string = bundle?.getString("") ?: ""
        val int = bundle?.getInt("") ?: 0

    }
}