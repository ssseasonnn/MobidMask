package zlc.season.morbidmaskapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

class CustomEntity(
    val id: Int,
    val content: String
) {
    override fun toString(): String {
        return "[$id,$content]"
    }
}

@Parcelize
class ParcelableEntity(
    val foo: String = "",
    val bar: String = ""
) : Parcelable {
    override fun toString(): String {
        return "[$foo,$bar]"
    }
}


class SerializableEntity(
    val foo: String = "",
    val bar: String = ""
) : Serializable {
    override fun toString(): String {
        return "[$foo,$bar]"
    }
}
