package zlc.season.morbidmaskrecipe

import com.google.auto.service.AutoService
import zlc.season.morbidmask.MutableParams
import zlc.season.morbidmask.Params
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
        types.add(Params::class.java.canonicalName)
        types.add(MutableParams::class.java.canonicalName)
        return types
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }


    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        "process".logw()

        val paramsElements = roundEnv.getElementsAnnotatedWith(Params::class.java)
        paramsElements.forEach {
            val paramsAnnotation = it.getAnnotation(Params::class.java)
            val valList = paramsAnnotation.value
            valList.forEach { each ->
                val paramsInfo = ParamsInfo(each.key, each.type, false)
                saveParamsInfo(it, paramsInfo)
            }
        }

        val mutableParamsElements = roundEnv.getElementsAnnotatedWith(MutableParams::class.java)
        mutableParamsElements.forEach {
            val mutableParamsAnnotation = it.getAnnotation(MutableParams::class.java)
            val varList = mutableParamsAnnotation.value
            varList.forEach { each ->
                val paramsInfo = ParamsInfo(each.key, each.type, true)
                saveParamsInfo(it, paramsInfo)
            }
        }


        paramsInfoMap.forEach { (element, paramsInfoList) ->
            val typeMirror = element.asType()

            val subtypeOfType = typeMirror.isSubType("android.app.Activity")

            val file = ParamsFileGenerator(element, paramsInfoList).generate()
            val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            file.writeTo(File(kaptKotlinGeneratedDir))
        }
        return false
    }

    private fun saveParamsInfo(element: Element, paramsInfo: ParamsInfo) {
        var list = paramsInfoMap[element]
        if (list == null) {
            val newList = mutableListOf<ParamsInfo>()
            list = newList
            paramsInfoMap[element] = newList
        }

        if (list.find { it.key == paramsInfo.key } != null) {
            "Found duplicate key [${paramsInfo.key}] on class ${element.simpleName}".loge()
        }
        list.add(paramsInfo)
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