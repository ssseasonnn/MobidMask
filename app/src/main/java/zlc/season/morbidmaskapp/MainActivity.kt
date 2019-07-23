package zlc.season.morbidmaskapp

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TestActivity() {

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
                .test("This is a mutable param")
                .direct()
        }

        btn_fragment.setOnClickListener {

            TestFragmentDirector.of()
                .stringParam("hhhhh")
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
