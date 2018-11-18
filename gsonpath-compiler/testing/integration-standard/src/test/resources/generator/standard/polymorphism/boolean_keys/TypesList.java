package generator.standard.polymorphism.boolean_keys;

import gsonpath.AutoGsonAdapter;

import generator.standard.polymorphism.Type;

@AutoGsonAdapter
class TypesList {
    @TypeGsonSubType
    Type[] items;
}
