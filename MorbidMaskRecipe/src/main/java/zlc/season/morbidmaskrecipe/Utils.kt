package zlc.season.morbidmaskrecipe

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

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
        typeMirror.isSubType("android.support.v4.app.Fragment")
    } else {
        false
    }
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
    val element = declaredType.asElement() as? TypeElement ?: return false
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