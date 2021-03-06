package zlc.season.morbidmask.compiler

import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

fun String.mapClassName(): ClassName {
    return when (this) {
        "byte", "java.lang.Byte" -> BYTE
        "short", "java.lang.Short" -> SHORT
        "int", "java.lang.Integer" -> INT
        "long", "java.lang.Long" -> LONG
        "float", "java.lang.Float" -> FLOAT
        "double", "java.lang.Double" -> DOUBLE
        "char", "java.lang.Character" -> CHAR
        "boolean", "java.lang.Boolean" -> BOOLEAN
        "java.lang.String" -> STRING
        else -> ClassName.bestGuess(this)
    }
}

fun ClassName.isBasic(): Boolean {
    return when (this) {
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        CHAR,
        BOOLEAN,
        STRING -> true
        else -> false
    }
}

fun ClassName.getDefaultValue(): String {
    return when (this) {
        BYTE -> "0"
        SHORT -> "0"
        INT -> "0"
        LONG -> "0L"
        FLOAT -> "0f"
        DOUBLE -> "0.0"
        CHAR -> "'0'"
        BOOLEAN -> "false"
        STRING -> "\"\""
        else -> ""
    }
}

fun Element.isActivity(): Boolean {
    return if (this is TypeElement) {
        val typeMirror = this.asType()
        typeMirror.isSubType("android.app.Activity")
    } else {
        false
    }
}

fun Element.isFragment(): Boolean {
    return if (this is TypeElement) {
        val typeMirror = this.asType()
        typeMirror.isSubType("androidx.fragment.app.Fragment")
    } else {
        false
    }
}

fun Element.isDialogFragment(): Boolean {
    return if (this is TypeElement) {
        val typeMirror = this.asType()
        typeMirror.isSubType("androidx.fragment.app.DialogFragment")
    } else {
        false
    }
}

fun TypeMirror.isParcelable(): Boolean {
    return isSubType("android.os.Parcelable")
}

fun TypeMirror.isSerializable(): Boolean {
    return isSubType("java.io.Serializable")
}

fun TypeMirror.isSubType(otherType: String): Boolean {
    if (isTypeEqual(otherType)) {
        return true
    }
    if (kind != TypeKind.DECLARED) {
        return false
    }
    val declaredType = this as DeclaredType
    val typeArguments = declaredType.typeArguments
    if (typeArguments.size > 0) {
        val typeString = StringBuilder(declaredType.asElement().toString())
        typeString.append('<')
        for (i in typeArguments.indices) {
            if (i > 0) {
                typeString.append(',')
            }
            typeString.append('?')
        }
        typeString.append('>')
        if (typeString.toString() == otherType) {
            return true
        }
    }
    val element = declaredType.asElement() as TypeElement
    val superType = element.superclass
    if (superType.isSubType(otherType)) {
        return true
    }
    for (interfaceType in element.interfaces) {
        if (interfaceType.isSubType(otherType)) {
            return true
        }
    }
    return false
}

private fun TypeMirror.isTypeEqual(otherType: String): Boolean {
    return otherType == toString()
}