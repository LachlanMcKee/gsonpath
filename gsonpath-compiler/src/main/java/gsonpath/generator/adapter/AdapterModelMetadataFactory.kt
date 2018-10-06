package gsonpath.generator.adapter

import com.squareup.javapoet.ClassName
import gsonpath.AutoGsonAdapter
import gsonpath.compiler.generateClassName
import gsonpath.generator.adapter.properties.AutoGsonAdapterPropertiesFactory
import gsonpath.generator.adapter.read.ReadParams
import gsonpath.generator.adapter.write.WriteParams
import gsonpath.generator.interf.ModelInterfaceGenerator
import gsonpath.model.*
import gsonpath.util.TypeHandler
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType

class AdapterModelMetadataFactory(
        private val fieldInfoFactory: FieldInfoFactory,
        private val gsonObjectTreeFactory: GsonObjectTreeFactory,
        private val typeHandler: TypeHandler,
        private val modelInterfaceGenerator: ModelInterfaceGenerator) {

    fun createMetadata(modelElement: TypeElement,
                       autoGsonAnnotation: AutoGsonAdapter): AdapterModelMetadata {

        val modelClassName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(modelClassName.packageName(),
                generateClassName(modelClassName, "GsonTypeAdapter"))

        val concreteClassName: ClassName
        val fieldInfoList: List<FieldInfo>
        val isModelInterface = modelElement.kind.isInterface

        val properties = AutoGsonAdapterPropertiesFactory().create(autoGsonAnnotation, isModelInterface)

        val requiresConstructorInjection: Boolean =
                if (isModelInterface) {
                    true
                } else {
                    findNonEmptyConstructor(modelElement) != null
                }

        if (!isModelInterface) {
            concreteClassName = modelClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromElement(
                    modelElement,
                    properties.fieldsRequireAnnotation,
                    requiresConstructorInjection)

        } else {
            val interfaceInfo = modelInterfaceGenerator.handle(modelElement)
            concreteClassName = interfaceInfo.parentClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromInterface(interfaceInfo)
        }

        val rootGsonObject = gsonObjectTreeFactory
                .createGsonObject(fieldInfoList, properties.rootField,
                        GsonObjectMetadata(properties.flattenDelimiter,
                                properties.gsonFieldNamingPolicy,
                                properties.gsonFieldValidationType,
                                properties.pathSubstitutions))

        val flattenedFields = gsonObjectTreeFactory.getFlattenedFieldsFromGsonObject(rootGsonObject)
        flattenedFields.forEach {
            AdapterAnnotationValidator.validateFieldAnnotations(it.fieldInfo)
        }
        val mandatoryInfoMap = MandatoryFieldInfoFactory().createMandatoryFieldsFromGsonObject(rootGsonObject)

        val readParams = ReadParams(
                baseElement = modelClassName,
                concreteElement = concreteClassName,
                requiresConstructorInjection = requiresConstructorInjection,
                mandatoryInfoMap = mandatoryInfoMap,
                rootElements = rootGsonObject,
                flattenedFields = flattenedFields)

        val writeParams = WriteParams(
                elementClassName = modelClassName,
                rootElements = rootGsonObject,
                serializeNulls = properties.serializeNulls)

        return AdapterModelMetadata(
                modelClassName,
                adapterClassName,
                isModelInterface,
                rootGsonObject,
                mandatoryInfoMap,
                readParams,
                writeParams)
    }

    /**
     * Finds a constructor within the input [TypeElement] that has at least one argument.
     *
     * @param modelElement the model being searched.
     */
    private fun findNonEmptyConstructor(modelElement: TypeElement): ExecutableType? {
        return typeHandler.getAllMembers(modelElement)
                .asSequence()
                .filter { it.kind == ElementKind.CONSTRUCTOR }
                .map { (it.asType() as ExecutableType) }
                .find { it.parameterTypes.size > 0 }
    }
}