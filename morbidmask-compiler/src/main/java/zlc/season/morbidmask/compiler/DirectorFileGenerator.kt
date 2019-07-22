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
class DirectorFileGenerator(
    private val element: Element,
    private val paramList: List<ParamsInfo>
) {

    val packageName = MoreElements.getPackage(element).qualifiedName.toString()
    val originClassName = element.simpleName.toString()
    val directorClassName = originClassName + "Director"

    val originClass = ClassName(packageName, originClassName)
    val directorClass = ClassName(packageName, directorClassName)

    val intentClass = ClassName("android.content", "Intent")
    val bundleClass = ClassName("android.os", "Bundle")

    private val contextClass = ClassName("android.content", "Context")

    fun generate(): FileSpec {
        return when {
            element.isActivity() -> generateActivityDirector()
            element.isFragment() -> generateFragmentDirector()
            else -> throw IllegalStateException("MobidMask not support this class: [$originClassName]!")
        }
    }

    private fun generateActivityDirector(): FileSpec {
        val classBuilder = TypeSpec.classBuilder(directorClass.simpleName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addModifiers(KModifier.PRIVATE)
                    .addParameter("context", contextClass)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("context", contextClass)
                    .initializer("context")
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addType(initActivityTypeSpec(directorClass))

        classBuilder.addProperty(
            PropertySpec.builder("intent", intentClass)
                .initializer("Intent(context,$originClassName::class.java)")
                .build()
        )

        paramList.forEach {
            val funSpec = FunSpec.builder(it.key)
                .addParameter("key", it.className)
                .addStatement("intent.putExtra(\"${it.key}\",key)")
                .addStatement("return this")
                .returns(directorClass)
                .build()
            classBuilder.addFunction(funSpec)
        }

        val getFunSpec = FunSpec.builder("get")
            .addStatement("return intent")
            .returns(intentClass)
            .build()
        classBuilder.addFunction(getFunSpec)

        val directFunSpec = FunSpec.builder("direct")
            .addStatement("context.startActivity(intent)")
            .build()
        classBuilder.addFunction(directFunSpec)

        return FileSpec.builder(packageName, directorClassName)
            .addType(classBuilder.build())
            .build()
    }

    private fun generateFragmentDirector(): FileSpec {
        val classBuilder = TypeSpec.classBuilder(directorClass.simpleName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addType(initFragmentTypeSpec(directorClass))

        classBuilder.addProperty(
            PropertySpec.builder("bundle", bundleClass)
                .initializer("Bundle()")
                .build()
        )

        paramList.forEach {
            val paramSetter = FunSpec.builder(it.key)
                .addParameter("key", it.className)
                .addStatement("bundle.put${it.className.simpleName}(\"${it.key}\",key)")
                .addStatement("return this")
                .returns(directorClass)
                .build()
            classBuilder.addFunction(paramSetter)
        }

        val getFunSpec = FunSpec.builder("get")
            .addStatement(
                """return $originClassName()
                .apply{ arguments = bundle }
                """.trimIndent()
            )
            .returns(originClass)
            .build()
        classBuilder.addFunction(getFunSpec)

        val typeName = LambdaTypeName.get(
            parameters = *arrayOf(originClass),
            returnType = UNIT
        )
        val directFunSpec = FunSpec.builder("direct")
            .addParameter(
                ParameterSpec.builder("block", typeName).defaultValue("{}").build()
            )
            .addStatement(
                """return $originClassName()
                .apply{ arguments = bundle }
                .also { block(it)}
                """.trimIndent()
            )
            .build()
        classBuilder.addFunction(directFunSpec)

        return FileSpec.builder(packageName, directorClassName)
            .addType(classBuilder.build())
            .build()
    }


    private fun ParamsInfo.initializer(): String {
        return if (className.isBasic()) {
            """bundle?.get${className.simpleName}("$key") ?: ${className.getDefaultValue()}
            """.trimIndent()
        } else {
            val gsonClass = ClassName("com.google.gson", "Gson")
            """$gsonClass().fromJson(bundle?.getString("$key") ?: "{}", ${className.simpleName}::class.java)
            """.trimIndent()
        }
    }


    /**
     *  generate code:
     *
     *    companion object {
     *        fun of(activity: MainActivity): MainActivityParams {
     *            return MainActivityParams(activity.intent.extras)
     *        }
     *    }
     */
    private fun initActivityTypeSpec(directorClass: ClassName): TypeSpec {
        return TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder("of")
                    .addParameter("context", contextClass)
                    .addStatement("return ${directorClass.simpleName}(context)")
                    .returns(directorClass)
                    .build()
            )
            .build()
    }

    /**
     *  generate code:
     *
     *    companion object {
     *        fun of(fragment: MainFragment): MainFragmentParams {
     *            return MainFragmentParams(fragment.arguments)
     *        }
     *    }
     */
    private fun initFragmentTypeSpec(directorClass: ClassName): TypeSpec {
        return TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder("of")
                    .addStatement("return ${directorClass.simpleName}()")
                    .returns(directorClass)
                    .build()
            )
            .build()
    }
}