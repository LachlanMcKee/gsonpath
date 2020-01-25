package gsonpath.adapter.standard.interf

import com.squareup.javapoet.TypeName
import gsonpath.generator.processingExceptionMatcher
import gsonpath.util.MethodElementContent
import gsonpath.util.ParameterElementContent
import gsonpath.util.TypeHandler
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import org.mockito.Mockito.`when` as whenever

class InterfaceModelMetadataFactoryTest {
    @JvmField
    @Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Mock
    private lateinit var typeHandler: TypeHandler
    private val element = mock(TypeElement::class.java)
    private lateinit var interfaceModelMetadataFactory: InterfaceModelMetadataFactory

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        interfaceModelMetadataFactory = InterfaceModelMetadataFactory(typeHandler)
    }

    @Test
    fun givenNoMethods_thenExpectNoMetadata() {
        val createMetadata = executeCreateMetadata(emptyList())
        Assert.assertEquals(emptyList<InterfaceModelMetadata>(), createMetadata)
    }

    @Test
    fun givenMethodWithVoidReturnType_thenExpectProcessingException() {
        testInvalidReturnType(ReturnType.VOID)
    }

    @Test
    fun givenMethodWithParameters_thenExpectProcessingException() {
        val methodData = setup(ReturnType.VALID, "ignoredName", listOf(mock(ParameterElementContent::class.java)))

        exceptionRule.expect(`is`(processingExceptionMatcher(methodData.element,
                "Gson Path interface methods must not have parameters")))
        executeCreateMetadata(listOf(methodData))
    }

    @Test
    fun givenMethodNameWithNoUppercaseCharacters_thenFieldNameWithSameName() {
        testFieldName("lowercase", "lowercase")
    }

    @Test
    fun givenMethodNameWithUppercaseCharacters_thenFieldNameWithoutPrefixName() {
        testFieldName("getValue", "value")
    }

    @Test
    fun givenMethodNameWithMultipleUppercaseCharacters_thenFieldNameWithoutPrefixName() {
        testFieldName("getValueAtIndex", "valueAtIndex")
    }

    private fun testFieldName(methodName: String, fieldName: String) {
        val methodData = setup(ReturnType.VALID, methodName = methodName)
        val createMetadata = executeCreateMetadata(listOf(methodData))

        val expectedMetadata = InterfaceModelMetadata(TypeName.INT, fieldName, methodData.element,
                methodName, methodData.returnTypeMirror)
        Assert.assertEquals(listOf(expectedMetadata), createMetadata)
    }

    private fun testInvalidReturnType(returnType: ReturnType) {
        val methodData = setup(returnType, "ignoredName")
        exceptionRule.expect(`is`(processingExceptionMatcher(methodData.element,
                "Gson Path interface methods must have a return type")))
        executeCreateMetadata(listOf(methodData))
    }

    private fun setup(
            returnType: ReturnType,
            methodName: String,
            parameters: List<ParameterElementContent> = emptyList()): MethodElementContent {

        val modelElement = mock(ExecutableElement::class.java)
        val returnTypeMirror = mock(TypeMirror::class.java)

        return MethodElementContent(
                element = modelElement,
                methodName = methodName,
                returnTypeMirror = returnTypeMirror,
                returnTypeName = returnType.typeName,
                parameterElementContents = parameters
        )
    }

    private fun executeCreateMetadata(methodData: List<MethodElementContent>): List<InterfaceModelMetadata> {
        whenever(typeHandler.getMethods(element)).thenReturn(methodData)
        return interfaceModelMetadataFactory.createMetadata(element)
    }

    private sealed class ReturnType(val typeName: TypeName) {
        object VOID : ReturnType(TypeName.VOID)
        object VALID : ReturnType(TypeName.INT)
    }
}