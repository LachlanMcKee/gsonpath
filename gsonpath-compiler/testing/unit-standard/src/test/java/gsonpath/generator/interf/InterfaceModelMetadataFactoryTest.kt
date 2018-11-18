package gsonpath.generator.interf

import com.squareup.javapoet.TypeName
import gsonpath.generator.processingExceptionMatcher
import gsonpath.util.MethodElementContent
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
import javax.lang.model.element.Element
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
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
    fun givenMethodWithNullReturnType_thenExpectProcessingException() {
        testInvalidReturnType(ReturnType.NULL)
    }

    @Test
    fun givenMethodWithVoidReturnType_thenExpectProcessingException() {
        testInvalidReturnType(ReturnType.VOID)
    }

    @Test
    fun givenMethodWithParameters_thenExpectProcessingException() {
        val methodData = setup(ReturnType.VALID, listOf(mock(TypeMirror::class.java)))

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
                methodName, methodData.generifiedElement.returnType)
        Assert.assertEquals(listOf(expectedMetadata), createMetadata)
    }

    private fun testInvalidReturnType(returnType: ReturnType) {
        val methodData = setup(returnType)
        exceptionRule.expect(`is`(processingExceptionMatcher(methodData.element,
                "Gson Path interface methods must have a return type")))
        executeCreateMetadata(listOf(methodData))
    }

    private fun setup(returnType: ReturnType, parameterTypes: List<TypeMirror> = emptyList(), methodName: String? = null): MethodElementContent {
        val modelElement = mock(Element::class.java)
        val generifiedElement = mock(ExecutableType::class.java)
        val returnTypeMirror = mock(TypeMirror::class.java)

        whenever(generifiedElement.returnType).thenReturn(returnTypeMirror)
        whenever(typeHandler.getTypeName(returnTypeMirror)).thenReturn(returnType.typeName)

        val methodElementAsType = mock(ExecutableType::class.java)
        whenever(methodElementAsType.parameterTypes).thenReturn(parameterTypes)
        whenever(modelElement.asType()).thenReturn(methodElementAsType)

        val name = mock(Name::class.java)
        whenever(name.toString()).thenReturn(methodName)
        whenever(modelElement.simpleName).thenReturn(name)

        return MethodElementContent(
                element = modelElement,
                generifiedElement = generifiedElement
        )
    }

    private fun executeCreateMetadata(methodData: List<MethodElementContent>): List<InterfaceModelMetadata> {
        whenever(typeHandler.getMethods(element)).thenReturn(methodData)
        return interfaceModelMetadataFactory.createMetadata(element)
    }

    private sealed class ReturnType(val typeName: TypeName?) {
        object NULL : ReturnType(null)
        object VOID : ReturnType(TypeName.VOID)
        object VALID : ReturnType(TypeName.INT)
    }
}