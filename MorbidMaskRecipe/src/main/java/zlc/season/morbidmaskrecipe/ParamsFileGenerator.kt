package zlc.season.morbidmaskrecipe

import com.google.auto.common.MoreElements
import com.squareup.kotlinpoet.*
import zlc.season.morbidmask.*
import javax.lang.model.element.Element

class ParamsFileGenerator(
    private val element: Element,
    private val paramList: List<ParamsInfo>
) {

    fun generate(): FileSpec {
        val packageName = MoreElements.getPackage(element).qualifiedName.toString()
        val originClassName = element.simpleName.toString()
        val paramClassName = originClassName + "Params"

        val originClass = ClassName(packageName, originClassName)
        val paramClass = ClassName(packageName, paramClassName)

        val bundleClass = ClassName("android.os", "Bundle")

        val paramClassBuilder = TypeSpec.classBuilder(paramClass.simpleName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addModifiers(KModifier.PRIVATE)
                    .addParameter("bundle", bundleClass.copy(nullable = true))
                    .build()
            )
            .addType(companionObject(originClass, paramClass))

        paramList.forEach {
            paramClassBuilder.addProperty(
                PropertySpec.builder(it.key, it.className())
                    .mutable(it.isMutable)
                    .initializer(it.initializer())
                    .build()
            )
        }

        return FileSpec.builder(packageName, paramClassName)
            .addType(paramClassBuilder.build())
            .build()
    }

    private fun ParamsInfo.className(): ClassName {
        return ClassName("kotlin", type)
    }

    private fun ParamsInfo.initializer(): String {
        val defaultValue = when (type) {
            byte -> "0"
            short -> "0"
            int -> "0"
            long -> "0L"
            float -> "0f"
            double -> "0"
            char -> "''"
            boolean -> "false"
            string -> "\"\""
            else -> ""
        }
        return """
            bundle?.get$type("$key") ?: $defaultValue
        """.trimIndent()
    }

    /**
     *  generate companion code:
     *
     *    companion object {
     *        fun of(activity: MainActivity): MainActivityParams {
     *            return MainActivityParams(activity)
     *        }
     *    }
     */
    private fun companionObject(originClass: ClassName, paramClass: ClassName): TypeSpec {
        return TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder("of")
                    .addParameter("activity", originClass)
                    .addStatement("return ${paramClass.simpleName}(activity.intent.extras)")
                    .returns(paramClass)
                    .build()
            )
            .build()
    }

    private fun companion(block: () -> Unit) {

    }
}