package zlc.season.morbidmaskapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_activity.setOnClickListener {
            TestActivityDirector.of(this)
                .byteParam(1)
                .shortParam(123)
                .intParam(1123123123)
                .longParam(123123123123123123)
                .booleanParam(true)
                .floatParam(123f)
                .stringParam("This is string param")
                .customParam(CustomEntity(999, "Custom entity content"))
                .direct()
        }

        btn_fragment.setOnClickListener {

            TestFragmentDirector.of()
                .stringParam("hhhhh")
                .customParam(CustomEntity(999, "Custom entity content"))
                .direct {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.add(R.id.fragment_container, it)
                    fragmentTransaction.commit()
                }
        }

        btn_dialog.setOnClickListener {
            TestDialogFragmentDirector.of()
                .stringParam("asdf")
                .direct {
                    it.show(supportFragmentManager, "")
                }
        }
    }
}
