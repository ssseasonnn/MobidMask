package zlc.season.morbidmask.compiler

import com.google.auto.common.MoreElements
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element

/**
 * Example:
 *
 * ```
 * @Params(
 *    Val("boolean", Boolean::class),
 *    Val("string", String::class),
 *    Val("char", Char::class)
 * )
 * class MainActivity
 * ```
 *
 * will generate :
 *
 * ```
 * class MainActivityParams private constructor(bundle: Bundle?) {
 *
 *      val boolean: Boolean = bundle?.getBoolean("boolean") ?: false
 *      val string: String = bundle?.getString("string") ?: ""
 *      val char: Char = bundle?.getChar("char") ?: '0'
 *
 *      companion object {
 *          fun of(activity: MainActivity): MainActivityParams = MainActivityParams(activity.intent.extras)
 *      }
 * }
 * ```
 */
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

        val typeSpec = when {
            element.isActivity() -> companionForActivity(originClass, paramClass)
            element.isFragment() -> companionForFragment(originClass, paramClass)
            else -> throw IllegalStateException("MobidMask not support this class: [$originClassName]!")
        }

        val classBuilder = TypeSpec.classBuilder(paramClass.simpleName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addModifiers(KModifier.PRIVATE)
                    .addParameter("bundle", bundleClass.copy(nullable = true))
                    .build()
            )
            .addType(typeSpec)

        paramList.forEach {
            classBuilder.addProperty(
                PropertySpec.builder(it.key, it.className)
                    .mutable(it.isMutable)
                    .initializer(it.initializer())
                    .build()
            )
        }

        return FileSpec.builder(packageName, paramClassName)
            .addType(classBuilder.build())
            .build()
    }

    private fun ParamsInfo.initializer(): String {
        return when {
            className.isBasic() ->
                """
                    bundle?.get${className.simpleName}("$key") ?: ${className.getDefaultValue()}
                """.trimIndent()
            typeMirror.isParcelable() ->
                """
                    bundle?.getParcelable<${className.simpleName}>("$key") ?: ${className.simpleName}()
                """.trimIndent()
            typeMirror.isSerializable() ->
                """
                    bundle?.getSerializable("$key") as ${className.simpleName}? ?: ${className.simpleName}()
                """.trimIndent()
            else -> {
                val gsonClass = ClassName("com.google.gson", "Gson")
                """$gsonClass().fromJson(bundle?.getString("$key") ?: "{}", ${className.simpleName}::class.java)
                """.trimIndent()
            }
        }
    }

    private fun companionForActivity(originClass: ClassName, paramClass: ClassName): TypeSpec {
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

    private fun companionForFragment(originClass: ClassName, paramClass: ClassName): TypeSpec {
        return TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder("of")
                    .addParameter("fragment", originClass)
                    .addStatement("return ${paramClass.simpleName}(fragment.arguments)")
                    .returns(paramClass)
                    .build()
            )
            .build()
    }
}