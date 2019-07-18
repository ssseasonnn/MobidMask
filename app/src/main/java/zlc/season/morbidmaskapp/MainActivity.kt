package zlc.season.morbidmaskapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import zlc.season.morbidmask.Val

@Val("test")
class MainActivity : AppCompatActivity() {

    val params by lazy { MainTestActivityParams.of(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val test = params.test

        MainActivityParams.of(this).test
    }
}
