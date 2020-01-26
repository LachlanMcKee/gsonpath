package gsonpath.adapter.standard.model

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution
import gsonpath.model.FieldInfo
import gsonpath.util.FieldNamingPolicyMapper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

class FieldPathFetcherTest {
    @Mock
    lateinit var serializedNameFetcher: SerializedNameFetcher
    @Mock
    lateinit var fieldNamingPolicyMapper: FieldNamingPolicyMapper

    lateinit var fieldPathFetcher: FieldPathFetcher

    private val fieldInfo = mock(FieldInfo::class.java)
    private val fieldName = "foo"
    private val delimiter = '.'
    private val fieldNamingPolicy = FieldNamingPolicy.IDENTITY

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        fieldPathFetcher = FieldPathFetcher(serializedNameFetcher, fieldNamingPolicyMapper)

        whenever(fieldInfo.fieldName).thenReturn(fieldName)
    }

    @Test
    fun testNoSerializedName() {
        givenNamingPolicyIs("value")

        val metadata = givenMetadataIs(emptyList())
        assertFieldPath(metadata, FieldPath.Standard("value"))
    }

    @Test
    fun testSerializedNameWithNoSubstitutions() {
        whenever(serializedNameFetcher.getSerializedName(fieldInfo, delimiter))
                .thenReturn("value")

        val metadata = givenMetadataIs(emptyList())
        assertFieldPath(metadata, FieldPath.Standard("value"))
    }

    @Test
    fun testSerializedNameWithSubstitutions() {
        whenever(serializedNameFetcher.getSerializedName(fieldInfo, delimiter))
                .thenReturn("{FIRST}{SECOND}")

        val metadata = givenMetadataIs(listOf(
                mock(PathSubstitution::class.java).apply {
                    whenever(original).thenReturn("FIRST")
                    whenever(replacement).thenReturn("FOO")
                },
                mock(PathSubstitution::class.java).apply {
                    whenever(original).thenReturn("SECOND")
                    whenever(replacement).thenReturn("BAR")
                }))
        assertFieldPath(metadata, FieldPath.Standard("FOOBAR"))
    }

    @Test
    fun testNameWithDelimiters() {
        whenever(serializedNameFetcher.getSerializedName(fieldInfo, delimiter))
                .thenReturn("foo.bar")

        val metadata = givenMetadataIs(emptyList())
        assertFieldPath(metadata, FieldPath.Nested("foo.bar"))
    }

    @Test
    fun testNameEndingWithDelimiter() {
        whenever(serializedNameFetcher.getSerializedName(fieldInfo, delimiter))
                .thenReturn("bar.")

        val metadata = givenMetadataIs(emptyList())
        assertFieldPath(metadata, FieldPath.Nested("bar.foo"))
    }

    private fun givenNamingPolicyIs(result: String) {
        whenever(fieldNamingPolicyMapper.applyFieldNamingPolicy(fieldNamingPolicy, fieldName))
                .thenReturn(result)
    }

    private fun givenMetadataIs(pathSubstitutions: List<PathSubstitution>): GsonObjectMetadata {
        return GsonObjectMetadata(delimiter, fieldNamingPolicy, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, pathSubstitutions)
    }

    private fun assertFieldPath(metadata: GsonObjectMetadata, expected: FieldPath) {
        assertEquals(expected, fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
    }
}