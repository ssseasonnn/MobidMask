package zlc.season.morbidmaskrecipe

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import zlc.season.morbidmask.Val
import zlc.season.morbidmask.Var
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
class MorbidMaskRecipe : AbstractProcessor() {
    lateinit var messager: Messager
    lateinit var elements: Elements
    var filer: Filer? = null


    val paramsInfoMap = mutableMapOf<ClassName, List<ParamsInfo>>()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        elements = processingEnv.elementUtils
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
        val vals = roundEnv.getElementsAnnotatedWith(Val::class.java)
        val vars = roundEnv.getElementsAnnotatedWith(Var::class.java)

        for (element in vals) {
            val targetPackageName = elements.getPackageOf(element).qualifiedName.toString()
            val targetClassName = element.simpleName.toString() + "Params"

            printWaring(targetPackageName)
            printWaring(targetClassName)

            val targetClass = ClassName(targetPackageName, targetClassName)
            printWaring(targetClass.canonicalName)

            element.getAnnotation()

        }
        return false
    }

    private fun printError(message: String) {
        messager.printMessage(Diagnostic.Kind.ERROR, message)
    }

    private fun printWaring(waring: String) {
        messager.printMessage(Diagnostic.Kind.WARNING, waring)
    }
}