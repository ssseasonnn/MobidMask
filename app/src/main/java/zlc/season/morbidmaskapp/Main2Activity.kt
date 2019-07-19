package zlc.season.morbidmaskapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

open class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val bundle = intent.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                val value = bundle.get(key)
                Log.d(
                    "TAG", String.format(
                        "%s %s (%s)", key,
                        value!!.toString(), value.javaClass.getName()
                    )
                )
            }
        }
    }
}
