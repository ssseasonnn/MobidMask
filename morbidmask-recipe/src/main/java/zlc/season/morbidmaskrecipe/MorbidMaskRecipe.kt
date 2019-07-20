package zlc.season.morbidmaskrecipe

import com.google.auto.service.AutoService
import zlc.season.morbidmask.MutableParams
import zlc.season.morbidmask.Params
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic


@AutoService(Processor::class)
class MorbidMaskRecipe : AbstractProcessor() {

    private val paramsInfoMap = mutableMapOf<Element, MutableList<ParamsInfo>>()

    override fun getSupportedAnnotationTypes(): Set<String> {
        val types = LinkedHashSet<String>()
        types.add(Params::class.java.canonicalName)
        types.add(MutableParams::class.java.canonicalName)
        return types
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val paramsElements = roundEnv.getElementsAnnotatedWith(Params::class.java)
        paramsElements.forEach {
            val paramsAnnotation = it.getAnnotation(Params::class.java)
            val valList = paramsAnnotation.value
            valList.forEach { each ->
                // trick to get annotation class value
                try {
                    each.type
                } catch (exception: MirroredTypeException) {
                    val typeString = exception.typeMirror.toString()
                    val paramsInfo = ParamsInfo(each.key, typeString.mapClassName(), false)
                    saveParamsInfo(it, paramsInfo)
                }
            }
        }

        val mutableParamsElements = roundEnv.getElementsAnnotatedWith(MutableParams::class.java)
        mutableParamsElements.forEach {
            val mutableParamsAnnotation = it.getAnnotation(MutableParams::class.java)
            val varList = mutableParamsAnnotation.value
            varList.forEach { each ->
                // trick to get annotation class value
                try {
                    each.type
                } catch (exception: MirroredTypeException) {
                    val typeString = exception.typeMirror.toString()
                    val paramsInfo = ParamsInfo(each.key, typeString.mapClassName(), true)
                    saveParamsInfo(it, paramsInfo)
                }
            }
        }

        paramsInfoMap.forEach { (element, paramsInfoList) ->
            val fileSpec = ParamsFileGenerator(element, paramsInfoList).generate()
            val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]

            if (kaptKotlinGeneratedDir == null) {
                "kapt kotlin generate dir is not found!".loge()
            } else {
                fileSpec.writeTo(File(kaptKotlinGeneratedDir))
            }
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
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, this)
    }

    private fun String.logw() {
        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, this)
    }

    private fun String.loge() {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, this)
    }
}