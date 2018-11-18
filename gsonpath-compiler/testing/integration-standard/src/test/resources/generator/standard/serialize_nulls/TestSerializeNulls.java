package generator.standard.class_annotations.serialize_nulls;

import java.lang.String;
import java.lang.Integer;
import java.lang.Double;
import java.lang.Boolean;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter(serializeNulls = true)
public class TestSerializeNulls {
    public int value1;
    public double value2;
    public boolean value3;
    public String value4;
    public Integer value5;
    public Double value6;
    public Boolean value7;
}