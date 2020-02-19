package gsonpath.errors;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;
import gsonpath.extension.annotation.Size;
import gsonpath.safe.GsonSafeList;

import java.util.List;
import java.util.Map;

@AutoGsonAdapter
class GsonErrorTestModel {

    ValuesWrapper singleValue;

    List<ValuesWrapper> listValue;

    ValuesWrapper[] arrayValue;

    Map<String, ValuesWrapper> mapValue;

    @AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE)
    static class ValuesWrapper {
        GsonSafeList<StrictTextModel> values;
    }

    @AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE)
    static class StrictTextModel {
        @Size(min = 1, max = 5)
        public String text;
    }
}
