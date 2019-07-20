package zlc.season.morbidmaskapp

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TestActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_activity.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("a", "123")
            intent.putExtra("b", 1)
            intent.putExtra("c", false)
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
