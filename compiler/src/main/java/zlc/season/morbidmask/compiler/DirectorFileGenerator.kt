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
 *    Val("string", String::class)
 * )
 * class MainActivity
 * ```
 *
 * will generate :
 *
 * ```
 * class MainActivityDirector private constructor(private val context: Context) {
 *      private val intent = Intent(context, MainActivity::class.java)
 *
 *      fun boolean(key: Boolean): MainActivityDirector {
 *          intent.putExtra("boolean", key)
 *
 *          return this
 *      }
 *
 *      fun string(key: String): MainActivityDirector {
 *          intent.putExtra("string",key)
 *          return this
 *      }
 *
 *      companion object {
 *           fun of(context: Context): MainActivityDirector {
 *               return MainActivityDirector(context)
 *           }
 *       }
 * }
 * ```
 */
class DirectorFileGenerator(
    private val element: Element,
    private val paramList: List<ParamsInfo>
) {

    private val packageName = MoreElements.getPackage(element).qualifiedName.toString()
    private val originClassName = element.simpleName.toString()
    private val directorClassName = originClassName + "Director"

    private val originClass = ClassName(packageName, originClassName)
    private val directorClass = ClassName(packageName, directorClassName)

    private val intentClass = ClassName("android.content", "Intent")
    private val bundleClass = ClassName("android.os", "Bundle")

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
            .addType(companionForActivity(directorClass))

        classBuilder.addProperty(
            PropertySpec.builder("intent", intentClass)
                .initializer("Intent(context,$originClassName::class.java)")
                .build()
        )

        paramList.forEach {
            val funSpec = FunSpec.builder(it.key)
                .addParameter("key", it.className)
                .addStatement(customActivityEntityStatement(it))
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

        val typeName = LambdaTypeName.get(
            parameters = *arrayOf(intentClass),
            returnType = UNIT
        )
        val directBlockFunSpec = FunSpec.builder("direct")
            .addParameter(
                ParameterSpec.builder("block", typeName).build()
            )
            .addStatement("block(intent)")
            .build()
        classBuilder.addFunction(directBlockFunSpec)

        return FileSpec.builder(packageName, directorClassName)
            .addType(classBuilder.build())
            .build()
    }

    private fun customActivityEntityStatement(paramsInfo: ParamsInfo): String {
        return when {
            paramsInfo.className.isBasic() ->
                """
                    intent.putExtra("${paramsInfo.key}",key)
                """.trimIndent()
            paramsInfo.typeMirror.isParcelable() ->
                """
                    intent.putExtra("${paramsInfo.key}",key)
                """.trimIndent()
            paramsInfo.typeMirror.isSerializable() ->
                """
                    intent.putExtra("${paramsInfo.key}",key)
                """.trimIndent()
            else ->
                """
                    intent.putExtra("${paramsInfo.key}",com.google.gson.Gson().toJson(key))
                """.trimIndent()
        }
    }

    private fun generateFragmentDirector(): FileSpec {
        val classBuilder = TypeSpec.classBuilder(directorClass.simpleName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addType(companionForFragment(directorClass))

        classBuilder.addProperty(
            PropertySpec.builder("bundle", bundleClass)
                .initializer("Bundle()")
                .build()
        )

        paramList.forEach {
            val paramSetter = FunSpec.builder(it.key)
                .addParameter("key", it.className)
                .addStatement(customFragmentEntityStatement(it))
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

    private fun customFragmentEntityStatement(paramsInfo: ParamsInfo): String {
        return when {
            paramsInfo.className.isBasic() ->
                """
                    bundle.put${paramsInfo.className.simpleName}("${paramsInfo.key}",key)
                """.trimIndent()
            paramsInfo.typeMirror.isParcelable() ->
                """
                    bundle.putParcelable("${paramsInfo.key}",key)                    
                """.trimIndent()
            paramsInfo.typeMirror.isSerializable() ->
                """
                    bundle.putSerializable("${paramsInfo.key}",key)
                """.trimIndent()
            else ->
                """
                    bundle.putString("${paramsInfo.key}",com.google.gson.Gson().toJson(key))
                """.trimIndent()
        }
    }

    private fun companionForActivity(directorClass: ClassName): TypeSpec {
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

    private fun companionForFragment(directorClass: ClassName): TypeSpec {
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