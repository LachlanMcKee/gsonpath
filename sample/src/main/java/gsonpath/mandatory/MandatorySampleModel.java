package gsonpath.mandatory;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE)
class MandatorySampleModel {
    int test;
}
