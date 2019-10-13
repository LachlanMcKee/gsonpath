Change Log
===========

Version 3.6.0 *(2019-10-13)*
----------------------------

* Improvement: Added a new `gsonpath-kt` library that will contain Kotlin specific features.
* Improvement: Added `GsonResultList` and `GsonSafeArrayList`. [#214](../../issues/217)
     * `GsonSafeArrayList` is a list that has an interlying type adapter that remove any invalid elements. It is quite similar to `@RemoveInvalidElements`, however it can also be used when a list is the root element of the array (something `@RemoveInvalidElements` cannot do)
     * `GsonResultList` is Kotlin only and uses a sealed class (`GsonResult`) to either return a `Success` or `Failure` result. 
     * To use these types you need to register the correct type adapter. If you use Kotlin, use `GsonPathTypeAdapterFactoryKt` (also ensure you include the `gsonpath-kt` library), otherwise use `GsonPathTypeAdapterFactory`.

Version 3.5.0 *(2019-10-05)*
----------------------------

* Improvement: `@GsonSubType` has been completely rewriten to offer a much more powerful API. See issue [#214](../../issues/214) for details. 


Version 3.4.0 *(2019-06-19)*
----------------------------

* Improvement: `@GsonSubType` now supports null values. ([#210](../../issues/210)) 

This is change in behaviour, as previously a null or missing value for a `subTypeKey` would have thrown an exception saying this was not allowed. Now that it is allowed, it is possible that elements may leak into an array/list.

The main benefit is that it is now possible for `stringValueSubtypes` to handle situations where the `subTypeKey` value is null, such as:

```java
@GsonSubtype(
        subTypeKey = "type",
        defaultType = Type1.class,
        stringValueSubtypes = {
                @GsonSubtype.StringValueSubtype(value = GsonSubtype.StringValueSubtype.NULL_STRING, subtype = TypeNull.class)
        }
)
``` 

Version 3.3.0 *(2019-04-07)*
----------------------------

* Improvement: `@GsonSubType` now works as a top-level annotation in conjunction with `@AutoGsonAdapter`. This will allow sub-types to be used when it is the root JSON element.

e.g.
```java
@GsonSubtype(
        subTypeKey = "is_int",
        booleanValueSubtypes = {
                @GsonSubtype.BooleanValueSubtype(value = true, subtype = DirectlyAnnotatedSubType.Type1.class),
                @GsonSubtype.BooleanValueSubtype(value = false, subtype = DirectlyAnnotatedSubType.Type2.class)
        }
)
public abstract class DirectlyAnnotatedSubType {
    String name;

     public class Type1 extends DirectlyAnnotatedSubType {
        int intTest;
    }

     public class Type2 extends DirectlyAnnotatedSubType {
        double doubleTest;
    }
}
```

This can be used to deserialize the following JSON:
```json
{
  "name": "example1",
  "is_int": true,
  "intTest": 0
}
```

or:
```json
{
  "name": "example2",
  "is_int": false,
  "doubleTest": 0.0
}
```
 
* Improvement: `@AutoGsonAdapter` now works with enums.

e.g.
```java
@AutoGsonAdapter
class Type {
    TestEnum[] value;
}

@AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
enum TestEnum {
    VALUE_ABC,
    VALUE_DEF,
    @SerializedName("custom")
    VALUE_GHI,
    VALUE_1
}
```

Can be used to parse the following JSON:

```json
{
  "value": [
    "value-abc",
    "value-def",
    "custom",
    "value-1"
  ]
}
```

Version 3.2.0 *(2019-03-18)*
----------------------------

* Fix: The new GsonSubType changes made in 3.1.0 would fail if enclosed within a final class. This no longer occurs.
* Fix / breaking change: The generated `TypeAdapter` will now delegate the writing of a field to the `TypeAdapter` belonging to the instance's type, rather than the defined type. This means in situations where inheritance is used, the concrete implementation will be used for writing, rather than the base type. For reads, there is no change in behaviour.

e.g.
 ```java
 class Type {}
 
 @AutoGsonAdapter
 class Type1 extends Type {}
 
 @AutoGsonAdapter
 class SubTypesExample {
     Type item;
 }
 ```
 
 And a Kotlin example would be:

 ```kotlin
 sealed class Type {
     @AutoGsonAdapter
     data class Type1(val value: String) : Type()
     
     @AutoGsonAdapter
     data class Type2(val value: Int) : Type()
 }
 ```

Version 3.1.0 *(2019-02-23)*
----------------------------

* Fix: Prevent a potential issue which causes extensions to not function.
* Improvement: Merged existing extensions into the core library, please remove reference to the old extensions when updating. The only dependencies you will need now are:

 ```gradle
 compile 'net.lachlanmckee:gsonpath:x.x.x'
 apt 'net.lachlanmckee:gsonpath-compiler:x.x.x'
 ```

* Improvement: Created `RemoveInvalidElements` extension. When used with arrays/lists it will remove any invalid elements rather than throwing an exception.
* Improvement: `@GsonSubType` now works with non-primitive, non-final types as well as arrays/lists. An example is as follows:

 ```java
 @AutoGsonAdapter
 class SubTypesExample {
     @GsonSubtype(
             subTypeKey = "type",
             stringValueSubtypes = {
                     @GsonSubtype.StringValueSubtype(key = "type1", subtype = Type1.class),
                     @GsonSubtype.StringValueSubtype(key = "type2", subtype = Type2.class)
             }
     )
     Type item;
 }
 ```
     
This may be helpful when used with Kotlin sealed classes:

Sealed Class:

 ```kotlin
 sealed class Type {
     @AutoGsonAdapter
     data class Type1(val value: String) : Type()
     
     @AutoGsonAdapter
     data class Type2(val value: Int) : Type()
 }
 ```

GsonSubType Pojo:

 ```java
 @AutoGsonAdapter
 class SubTypesExample {
     @GsonSubtype(
             subTypeKey = "type",
             stringValueSubtypes = {
                     @GsonSubtype.StringValueSubtype(key = "type1", subtype = Type1.class),
                     @GsonSubtype.StringValueSubtype(key = "type2", subtype = Type2.class)
             }
     )
     Type item;
 }
 ```

Version 3.0.1 *(2018-11-27)*
----------------------------

* Fix: GsonSubType will no longer remove the `subTypeKey` property. This made it impossible serialize the POJO. ([#132](../../issues/174)) 

Version 3.0.0 *(2018-11-18)*
----------------------------

* Improvement: Added array support. Examples of this are as follows:

     ```java
     @AutoGsonAdapter
     class ArrayExample {
         @SerializedName("test1[1]")
         int plainArray;
     
         @SerializedName("test2[2].child")
         int arrayWithNestedObject;
     
         @SerializedName("test2[2].child2")
         int arrayWithNestedObject2;
     
         @SerializedName("test3[3].child[1]")
         int arrayWithNestedArray;
     
         @SerializedName("test4.child[1]")
         int objectWithNestedArray;
     }
     ```

* Improvement: Annotated interfaces are now able to also write as well as read. Previously only reading was possible.
* Fix: GsonSubType no longer fails if any other fields are defined before in the class. ([#132](../../pull/142))
* Fix: Annotation inheritance now works correctly with interfaces. ([#132](../../issues/153))

Version 2.4.2 *(2018-07-30)*
----------------------------

* Improvement/Bug: Annotations are now also read from the POJO's getter method if it does not exist on the property. See issue [#132](../../issues/137) for details.
     * Inherited method annotations are also used. This is very useful for abstract methods that do not duplicate their annotations when overriding the method.
     * This behaviour was changed to provide better support for sealed classes within Kotlin.

Version 2.4.1 *(2018-05-15)*
----------------------------

* Fix: Resolved a defect causing Kotlin classes to not respect `fieldValidationType`, as it incorrectly reports that the class has a default value. See issue [#132](../../issues/132) for details.

Version 2.4.0 *(2018-05-14)*
----------------------------

* Improvement: Replaced `GsonPathDefaultConfiguration` with a much more useful annotation inheritance. See issue [#124](../../issues/124) for details.
     * Note that this is a breaking change. If you were previously using `GsonPathDefaultConfiguration`, you will need to change your implementation.
* Improvement: Passing more metadata to extensions to make their validation more useful.
* Improvement: Annotations are now retained at runtime.
     * This fixes an issue with Kotlin classes discarding the annotations.

Version 2.3.2 *(2018-04-12)*
----------------------------

* Fix: Raise a compilation error when using SerializedName.alternate as it is not supported.
* Fix: Fixed an issue causing 2.3.1 to not find `hasDefaultValue` in certain circumstances.

Version 2.3.1 *(2018-03-07)*
----------------------------

* New: Added a `Generated` annotation to all generated type adapters.
* New: Allowed annotated interfaces to use default and static methods (Java 8+).
* New: Support for all primitive types.
* Fix: Long, Float and Double now respect `longSerializationPolicy` and `serializeSpecialFloatingPointValues` from the gson instance.
* Fix: Non-null fields with a default value will no longer throw an exception if the value is missing when deserializing. i.e:

     ```java
     @AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL)
     public class TestValidateWithDefaultValue {
         @NonNull
         public Integer mandatoryWithDefault = 0;
     }
     ```

Version 2.3.0 *(2017-10-15)*
----------------------------

* New: Added support for Kotlin data classes.
     * This has been introduced by guessing the constructor of a Java class. Note, this will have a potential behavioural change if your existing POJOs annotated with `@AutoGsonAdapter` have constructors with parameters.
     * If you plan on using this feature for regular Java POJOs, you must ensure that the order of the constructor parameters are in the same order as the fields themselves.
     * Kotlin data classes are generated in a predictable format, hence why this approach works.
* New: Removed support for annotating interfaces that extend `list` or `collection. Refer to the original feature in version 1.8.0.
* Fix: Fixed a potential crash with `@GsonSubtype` when the `subTypeKey` field was missing or null.

Version 2.2.0 *(2017-03-12)*
----------------------------

* New: Added extension support (Proposed within Issue [#87](../../issues/87))
     * This allows other libraries to extend the existing field validation.
     * The first extension implementation adds validation for fields annotated with [Android Support Library annotations](https://developer.android.com/reference/android/support/annotation/package-summary.html) annotations. This library can be found [here](https://github.com/LachlanMcKee/gsonpath-extensions-android).
          * This library adds support for validating `@FloatRange`, `@IntRange`, `@StringDef` and `@IntDef`
          * To ensure that the library works as expected, use the 'apt' gradle dependency command.

Version 2.1.0 *(2017-02-26)*
----------------------------

* New: Added polymorphism within the type adapters by introducing a new annotation called `GsonSubtype` (Promposed within Issue [#78](../../issues/78))
     * This adds subtyping to the generated `TypeAdapters`.
     * Some example usages are as follows:

     ```java
     @AutoGsonAdapter
     class SubTypesExample {
         @GsonSubtype(
                 subTypeKey = "type",
                 stringValueSubtypes = {
                         @GsonSubtype.StringValueSubtype(key = "type1", subtype = Type1.class),
                         @GsonSubtype.StringValueSubtype(key = "type2", subtype = Type2.class)
                 }
         )
         Type[] items;
     }
     ```

     Or potentially:

     ```java
     @AutoGsonAdapter
     class SubTypesExample {
         @GsonSubtype(
                 subTypeKey = "type",
                 defaultType = TypeDefault.class,
                 subTypeFailureOutcome = GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE,
                 stringValueSubtypes = {
                         @GsonSubtype.StringValueSubtype(key = "type1", subtype = Type1.class),
                         @GsonSubtype.StringValueSubtype(key = "type2", subtype = Type2.class)
                 }
         )
         Type[] items;
     }
     ```

Version 2.0.0 *(2017-02-18)*
----------------------------

* New: Promoted the changes from the 2.0.0-beta2 into a release build.
     * The documentation has been improved, and the compiler errors are more informative.
     * This build (unlike the betas) also includes the changes from 1.8.1.
     * See the changes below for further details.

Version 1.8.1 *(2017-02-18)*
----------------------------

* Fix: Generics are now properly supported. Please let me know if you find any unexpected issues. (Issue [#69](../../issues/69))
* Fix: Fixed a variable naming issues in concrete classes generated for interfaces. This should ensure that conflicts are substiantially less likely. (Issue [#69](../../issues/74))

Version 2.0.0-beta2 *(2017-02-14)*
----------------------------

* Fix: The TypeAdapterFactory generated via the `@AutoGsonAdapterFactory` now has a public constructor
     * This was causing issues in projects that use proguard.

Version 2.0.0-beta1 *(2017-02-13)*
----------------------------

* New: Added multi-module support. Previously the library would not allow gsonpath to be used with a library.
     * This is a breaking API change, to use the library a new `@AutoGsonAdapterFactory` must be used.

     Previously, the factory was added to Gson as follows:

     `builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory())`

     The following step is now required beforehand:

     ```java
     @AutoGsonAdapterFactory
     public interface TestGsonTypeFactory extends TypeAdapterFactory {
     }
     ```

     Also, notice that the `createTypeAdapterFactory` now requires a class as an argument.

     `builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))`

     The advantage of this change is that multiple `@AutoGsonAdapterFactory` annotations can be used.
     *Note: Only one `@AutoGsonAdapterFactory` annotation may be used per module/project*

Version 1.8.0 *(2017-01-08)*
----------------------------

* New: Compiler has been rewritten in Kotlin. This should help improve future code quality.
* New: Special version of annotated interfaces may now be created that extend `List` or `Collection`
     * This allows developers to directly access a JSON array without creating a container object. Visit the [interfaces guide](guides/interfaces.md) for further details
* Removed: The `@AutoGsonArrayStreamer` annotation has been removed as it has been superseded by the feature above.

Version 1.7.0 *(2016-12-06)*
----------------------------

* Fix: All method annotations defined within interfaces annotated with `@AutoGsonAdapter` are now copied to the concrete implementation methods.
* New: Classes and interfaces annotated with `@AutoGsonAdapter` can now be package private

Version 1.6.3 *(2016-11-09)*
----------------------------

* Fix: Added Java 7 target and source compatibility for all libraries.

Version 1.6.2 *(2016-10-22)*
----------------------------

* Fix: The `equals` method generated for interfaces annotated with `@AutoGsonAdapter` now tests for equality correctly.
* New: Added `toString` method genration for interfaces annotated with `@AutoGsonAdapter`.

Version 1.6.1 *(2016-07-28)*
----------------------------

* Fix: The `FlattenJson` annotation no longer causes the processor to fail when used on interfaces. (Issue [#47](../../issues/47))
* Fix: Interfaces annotated with `@AutoGsonAdapter` now always correctly order their fields (Issue [#45](../../issues/45))
     * When using Json Path notation, if fields with the same parent were not grouped together, the constructor was being called with arguments in the wrong order.

Version 1.6.0 *(2016-07-28)*
----------------------------

* New: Added text substitution support for all fields and methods annotated with the `@SerializedName` annotation.
     * See the [path substitution guide](guides/path_substitution.md) for details.

Version 1.5.3 *(2016-07-28)*
----------------------------

* Fix: The `FlattenJson` annotation may now be used on methods.
     * Since the addition of interface support, it made sense to change this behaviour.

Version 1.5.2 *(2016-07-27)*
----------------------------

* New: Interfaces annotated with `@AutoGsonAdapter` now generate `equals` and `hashCode` implementations within the generated POJO.
     * All fields within the generated POJO are inspected.
          * It is currently not possible to exclude fields from inspection
     * The generated methods are Java 6 compatible as the code does not use the Java 7 `Objects.hash()` method.

Version 1.5.1 *(2016-07-26)*
----------------------------

* Fix: Changed `JsonFieldMissingException` to extend the Gson `JsonParseException` class to more easily prevent possible uncaught exceptions.
* Fix: Interfaces annotated with `@AutoGsonAdapter` now correctly work with inheritance.
* Fix: Interfaces annotated with `@AutoGsonAdapter` are now stricter about their implementation. (See [interfaces guide](guides/interfaces.md) for details surrounding interface design)
     * Methods that do not specify a return type will now fail gracefully.
     * Methods that specify parameters will now fail gracefully.

Version 1.5.0 *(2016-07-03)*
----------------------------

* New: The `@AutoGsonAdapter` annotation can now be used with interfaces.
     * The library generates an immutable POJO which is constructed within the generated Gson `TypeAdapter` class.
     * To learn more about this feature, visit the [interfaces guide](guides/interfaces.md).

Version 1.4.1 *(2016-07-03)*
----------------------------

* Fix: The `VALIDATE_EXPLICIT_NON_NULL` value for the `FieldValidationType` enum now correctly treats primitives as `@NonNull`. This behaviour now matches the documentation.
* Fix: Using an unsupported primitive type within an `@AutoGsonAdapter` annotated class now throws a more informative error. The primitives supported by Gson are: `boolean`, `int`, `long`, `double`.

Version 1.4.0 *(2016-05-26)*
----------------------------

 * New: Made changes to the mandatory field feature to make it more useful. The changes are described below:
     * Removed `@Mandatory` and `@Optional`. Instead the annotation processor will attempt to find any `@Nullable` or `Nonnull` (as well as other permutations such as `NonNull`, `NotNull` and `Notnull`) annotations, and use those instead.
     * The `@AutoGsonAdapter` annotation property `fieldPolicy` has been renamed to `fieldValidationType` to be more clear.
     * The fieldValidationType enum values have the same behaviour as before, however they have been renamed to be clearer as well:
        * NO_VALIDATION - No fields are validated, and the Gson parser should never raise exceptions for missing content.
        * VALIDATE_EXPLICIT_NON_NULL - Any Objects marked with `@NonNull` (or similar), or primitives should fail if the value does not exist within the JSON.
        * VALIDATE_ALL_EXCEPT_NULLABLE - All fields will be treated as `@NonNull`, and should fail when value is not found, unless the field is annotation with `@Nullable` (except for primitives).
 * New: Added a default configuration concept for the `@AutoGsonAdapter` annotation.
     * Allows developers to set default values for the annotation if they are unhappy with the default values provided within the `@AutoGsonAdapter` annotation.
        * Some developers may not like the '.' character being used as a delimiter. With this new feature a developer can change this delimiter throughout the entire application using the default configuration instead of changing every single `@AutoGsonAdapter` annotation.
     * To use this feature, developers must:
        * Annotate a class with the new `@GsonPathDefaultConfiguration` annotation and specify their desired default values
        * Set the `defaultConfiguration` property within the `@AutoGsonAdapter` annotation to point to this class on every usage of `@AutoGsonAdapter` that wishes to use these defaults.
     * See the annotation javadoc for further details.

Version 1.3.0 *(2016-05-14)*
----------------------------

 * New: Added writing support to the generated `TypeAdapter` classes.
    * The `@AutoGsonAdapter` annotation has a new `serializeNulls` property which specifies whether nulls are written to the document. (The same way Gson handles this)
 * New: Added a mandatory field concept which will raise exceptions for missing JSON content
    * Two annotations were added to solve this issue: `@Mandatory` and `@Optional`
        * Since these annotations can be used on primitives, it didn't make sense to use `@Nullable` and `Nonnull` at this stage.
    * The `@AutoGsonAdapter` annotation has a new `fieldPolicy` property which specifies how the generated `TypeAdapter` handles mandatory content. It is an enum with the following values:
        * NEVER_FAIL - No fields are mandatory, and the Gson parser should never raise exceptions for missing content.
        * FAIL_MANDATORY - Any field marked with `@Mandatory` should fail if the value does not exist within the JSON.
        * FAIL_ALL_EXCEPT_OPTIONAL - All fields will be treated as `@Mandatory`, and should fail when content is not found, unless the field is annotation with `@Optional`.
 * Fix: Annotation processor messages are clearer and inform when it has started and completed.
 * Fix: Better error handling for invalid scenarios, such as duplicate JSON field names.
 
