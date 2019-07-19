package zlc.season.morbidmaskapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import zlc.season.morbidmask.*


@Val("test")
@Params(
    Val("key1", boolean),
    Val("key2")
)
@MutableParams(
    Var("key3"),
    Var("key4"),
    Var("key5", int)
)
class MainActivity : Main2Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

        btn.setOnClickListener {
            val intent = Intent(this, Main2Activity::class.java)
            intent.putExtra("a", "123")
            intent.putExtra("b", 1)
            intent.putExtra("c", false)
            startActivity(intent)
        }
    }
}
