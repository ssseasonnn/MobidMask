package zlc.season.morbidmaskrecipe

import com.google.auto.common.MoreElements
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import zlc.season.morbidmask.Val
import zlc.season.morbidmask.Var
import java.io.File
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
class MorbidMaskRecipe : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    lateinit var messager: Messager
    lateinit var elementUtils: Elements
    var filer: Filer? = null


    val paramsInfoMap = mutableMapOf<Element, MutableList<ParamsInfo>>()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val types = LinkedHashSet<String>()
        types.add(Val::class.java.canonicalName)
        types.add(Var::class.java.canonicalName)
        return types
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }


    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val valElements = roundEnv.getElementsAnnotatedWith(Val::class.java)

        valElements.forEach {
            val annotation = it.getAnnotation(Val::class.java)
            val targetPackage = MoreElements.getPackage(it).qualifiedName.toString()
            val targetName = it.simpleName.toString() + "Params"

            val className = ClassName(targetPackage, targetName)
            val paramsInfo = paramsInfoMap[it]
            if (paramsInfo == null) {
                paramsInfoMap[it] = mutableListOf()
            } else {
                paramsInfo.add(ParamsInfo(annotation.key, annotation.type))
            }
        }

        val varElements = roundEnv.getElementsAnnotatedWith(Var::class.java)
        varElements.forEach {
            val annotation = it.getAnnotation(Var::class.java)
            val paramsInfo = paramsInfoMap[it]
            if (paramsInfo == null) {
                paramsInfoMap[it] = mutableListOf()
            } else {
                paramsInfo.add(ParamsInfo(annotation.key, annotation.type))
            }
        }


        paramsInfoMap.forEach { element, mutableList ->

            val targetPackage = MoreElements.getPackage(element).qualifiedName.toString()
            val targetName = element.simpleName.toString()
            val generateName = targetName + "Params"

            val targetClass = ClassName(targetPackage, targetName)
            val generateClass = ClassName(targetPackage, generateName)

            val companion = TypeSpec.companionObjectBuilder()
                .addFunction(
                    FunSpec.builder("of")
                        .addParameter("activity", targetClass)
                        .addStatement("return $generateName(activity)")
                        .returns(generateClass)
                        .build()
                )
                .build()


            val file = FileSpec.builder(targetPackage, generateName)
                .addType(
                    TypeSpec.classBuilder(generateName)
                        .primaryConstructor(
                            FunSpec.constructorBuilder()
                                .addParameter("activity", targetClass)
                                .build()
                        )
                        .addProperty("test", STRING)
                        .addInitializerBlock(
                            CodeBlock.builder()
                                .add(
                                    """test = activity.intent.getStringExtra("test")?:""
                                    """.trimMargin()
                                )
                                .build()
                        )
                        .addType(companion)
                        .build()
                )
                .build()

            val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            file.writeTo(File(kaptKotlinGeneratedDir))
        }
        return false
    }


    private fun String.logn() {
        messager.printMessage(Diagnostic.Kind.NOTE, this)
    }

    private fun String.logw() {
        messager.printMessage(Diagnostic.Kind.WARNING, this)
    }

    private fun String.loge() {
        messager.printMessage(Diagnostic.Kind.ERROR, this)
    }
}