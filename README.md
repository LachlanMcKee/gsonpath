# Gson Path

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.lachlanmckee/gsonpath/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.lachlanmckee/gsonpath) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-gsonpath-green.svg?style=true)](https://android-arsenal.com/details/1/4191)

An annotation processor library which generates gson type adapters at compile time which also use basic JsonPath functionality.

## Benefits
The benefits of this library are as follows:

#### Statically generated Gson Type Adapters
By statically generating Gson Type Adapters, the majority of reflection used by the Gson library can be removed. This greatly improves performance and removes code obfuscation issues.

#### JsonPath syntax
JsonPath syntax can be used to reduce the number of POJOs required to parse a JSON file. See the example section for more details.

This allows for easier integration with other libraries which rely on a flat class structure (such as DBFlow).

#### POJO immutability via interfaces
You are given the option of using immutable POJOs based off annotated interfaces.

These POJOs are similar to AutoValue, however you do not need to reference the concrete implementation as the Type Adapter creates it on your behalf.

See the [interfaces guide](guides/interfaces.md) for further details.

#### JsonPath - Path Substitutions
You can reduce the amount of repetition when creating POJOs using Path Substitutions by using straightforward string replacement functionality within the `AutoGsonAdapter` annotation.

See the [path substitution guide](guides/path_substitution.md) for further details.

#### Optional client side validation
Add optional client side validation to your json using `@Nullable` and `NonNull` annotations to add mandatory field constraints.

The client side valiation can also be enhanced through extensions. These extensions are separate annotation processors that register to be notified whenever a field with a specific annotation is encountered.

##### Notable extensions
[Android Support Annotation validation](https://github.com/LachlanMcKee/gsonpath-extensions-android)

## Example
Given the following JSON:

```json
{
   "person": {
      "names": {
         "first": "Lachlan",
         "last": "McKee"
      }
   }
}
```

We can deserialize the content with a single class by using Gson Path. The following class demonstrates the annotations required to create a type adapter which can correctly read the content.

```java
@AutoGsonAdapter(rootField = "person.names")
public class PersonModel {
   @SerializedName("first")
   String firstName;
   
   @SerializedName("last")
   String lastName;
}
```

We could also write it as follows (to reduce the number of annotations required):

```java
@AutoGsonAdapter(rootField = "person.names")
public class PersonModel {
   String first;
   String last;
}
```

## Setup
The following steps are required to use the generated `TypeAdapters` within your project.

#### AutoGsonAdapterFactory
Create a type adapter factory by annotating an interface as follows:

```java
package com.example;
 
@AutoGsonAdapterFactory
public interface ExampleGsonTypeFactory extends TypeAdapterFactory {
}
```

Gson Path can be used across multiple modules by defining a factory within each. 

*Note: Only one `@AutoGsonAdapterFactory` annotation may be used per module/project. If you do this accidentally, the annotation processor will raise a helpful error.*

#### AutoGsonAdapter
Create any number of type adapters by annotating a class or interface as follows:

```java
package com.example;
 
@AutoGsonAdapter
public class ExampleModel {
    String value;
}
```

or

```java
package com.example;
 
@AutoGsonAdapter
public interface ExampleModel {
    String getValue();
}
```

#### Gson Integration
For each type adapter factory interface, register it with your Gson builder as follows:

```java
return new GsonBuilder()
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(ExampleGsonTypeFactory.class))
                .create();
```

## Proguard
To use proguard within your project, you must add the generated type adapter factory. Using the example above, this would be:

```
-keep public class com.example.ExampleGsonTypeFactoryImpl
```

## Download
This library is available on Maven, you can add it to your project using the following gradle dependencies:

```gradle
compile 'net.lachlanmckee:gsonpath:3.7.1'
apt 'net.lachlanmckee:gsonpath-compiler:3.7.1'

compile 'net.lachlanmckee:gsonpath-kt:3.7.1' // an optional Kotlin library 
```
