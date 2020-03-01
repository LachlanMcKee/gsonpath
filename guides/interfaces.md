Interfaces
=

Gson Path supports using the `AutoGsonAdapter` annotation with interfaces.

The library generates an immutable POJO which is used as the concrete implementation of the interface.

Differences to standard POJO
-
There are a few key differences between standard classes using the `AutoGsonAdapter` annotation, and an interface that you should be aware of. These are:
 * The `GsonFieldValidationType` property can only ever be `VALIDATE_EXPLICIT_NON_NULL` or `VALIDATE_ALL_EXCEPT_NULLABLE`. 
   * This API design decision was made since you are unable to set a default value for primitive return types. 
   * If the `GsonFieldValidationType` is not one of the previously mentioned values, it will be forced to `VALIDATE_EXPLICIT_NON_NULL`. 
 * When designing your interface:
   * Be sure to always specify a return type for methods.
   * Do not add any parameters to your method as this method should acts as a 'getter'.
   * The library creates variable names by stripping the characters until it reaches the first uppercase letter. Be sure to begin the name of the method with a 'get' or 'is' or whatever POJO standard you wish to ensure you don't encounter name collisions. 
   * Be aware that although the generated object POJO itself is immutable, the objects contained within the class may not be. 
 
Sample
-

The following interface:

```java
@AutoGsonAdapter
public interface InterfaceExample {
    Integer getObjectExample();
    int getPrimitiveExample();
}
```

Will generate the following POJO:

```java
public final class InterfaceExample_GsonPathModel implements InterfaceExample {
    private final Integer objectExample;
    private final int primitiveExample;
    
    public InterfaceExample_GsonPathModel(Integer objectExample, int primitiveExample) {
        this.objectExample = objectExample;
        this.primitiveExample = primitiveExample;
    }
    
    @Override
    public Integer getObjectExample() {
        return objectExample;
    }
    
    @Override
    public int getPrimitiveExample() {
        return primitiveExample;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        InterfaceExample_GsonPathModel that = (InterfaceExample_GsonPathModel) o;
        
        if ((objectExample == null || !objectExample.equals(that.objectExample))) return false;
        if (primitiveExample != that.primitiveExample) return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = objectExample != null ? objectExample.hashCode() : 0;
        result = 31 * result + (primitiveExample);
        return result;
    }
}
```

Which will in turn generate the following `TypeAdapter`

```java
public final class InterfaceExample_GsonTypeAdapter extends TypeAdapter<InterfaceExample> {
    private static final int MANDATORY_INDEX_PRIMITIVEEXAMPLE = 0;
    private static final int MANDATORY_FIELDS_SIZE = 1;
    
    private final Gson mGson;
    
    public InterfaceExample_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }
    
    @Override
    public InterfaceExample read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        
        Integer value_objectExample = null;
        int value_primitiveExample = 0;
        
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];
        
        int jsonFieldCounter0 = 0;
        in.beginObject();
        
        while (in.hasNext()) {
            if (jsonFieldCounter0 == 2) {
                in.skipValue();
                continue;
            }
            
            switch (in.nextName()) {
                case "objectExample":
                    jsonFieldCounter0++;
                    
                    value_objectExample = mGson.getAdapter(java.lang.Integer.class).read(in);
                    break;
                    
                case "primitiveExample":
                    jsonFieldCounter0++;
                    
                    Integer value_primitiveExample_safe = mGson.getAdapter(java.lang.Integer.class).read(in);
                    if (value_primitiveExample_safe != null) {
                        value_primitiveExample = value_primitiveExample_safe;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_PRIMITIVEEXAMPLE] = true;
                        
                    } else {
                        throw new gsonpath.exception.JsonFieldMissingException("Mandatory JSON element 'primitiveExample' was null for class 'generator.standard.primitive.InterfaceExample_GsonPathModel'");
                    }
                    break;
                
                default:
                    in.skipValue();
                    break;
            }
        }
        
        in.endObject();
        
        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++) {
            
            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {
                
                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case MANDATORY_INDEX_PRIMITIVEEXAMPLE:
                        fieldName = "primitiveExample";
                        break;
                        
                }
                throw new gsonpath.exception.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class 'generator.standard.primitive.InterfaceExample_GsonPathModel'");
            }
        }
        
        return new InterfaceExample_GsonPathModel(
                value_objectExample,
                value_primitiveExample
        );
    }
    
    @Override
    public void write(JsonWriter out, InterfaceExample value) throws IOException {
    }
}
```