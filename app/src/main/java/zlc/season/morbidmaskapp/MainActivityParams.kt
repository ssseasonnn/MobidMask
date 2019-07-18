package zlc.season.morbidmaskapp

class MainActivityParams private constructor(activity: MainActivity) {
    var test = ""

    companion object {
        fun of(activity: MainActivity): MainActivityParams {
            return MainActivityParams(activity)
        }
    }

    init {
        test = activity.intent.getStringExtra("test") ?: ""
    }
}