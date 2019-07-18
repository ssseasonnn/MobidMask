package zlc.season.morbidmaskapp

class MainTestActivityParams private constructor(activity: MainActivity) {
    var test = ""

    companion object {
        fun of(activity: MainActivity): MainTestActivityParams {
            return MainTestActivityParams(activity)
        }
    }

    init {
        test = activity.intent.getStringExtra("test") ?: ""
    }
}