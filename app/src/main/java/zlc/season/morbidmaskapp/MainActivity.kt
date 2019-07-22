package zlc.season.morbidmaskapp

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TestActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_activity.setOnClickListener {
            val intent = TestDirector.of()
                .byteParam(1)
                .get()

            startActivity(intent)
        }

        btn_fragment.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragment_container, TestFragment())
            fragmentTransaction.commit()
        }

        btn_dialog.setOnClickListener {
            val dialog = TestDialogFragment()
            dialog.show(supportFragmentManager, "")
        }
    }
}
