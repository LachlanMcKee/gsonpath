package gsonpath.adapter.standard.extension.size;

import gsonpath.AutoGsonAdapter;

import java.util.List;

public interface SizeModel {

    interface BaseModel<T> {
        T getValue();
    }

    interface BaseArrayModel extends BaseModel<Integer[]> {
    }

    interface BaseCollectionModel extends BaseModel<List<Integer>> {
    }

    interface BaseStringModel extends BaseModel<String> {
    }

    interface MinAndMax {
        @AutoGsonAdapter
        interface ArrayModel extends BaseArrayModel {
            @gsonpath.extension.annotation.Size(min = 1, max = 3)
            Integer[] getValue();
        }

        @AutoGsonAdapter
        interface CollectionModel extends BaseCollectionModel {
            @android.support.annotation.Size(min = 1, max = 3)
            List<Integer> getValue();
        }

        @AutoGsonAdapter
        interface StringModel extends BaseStringModel {
            @android.support.annotation.Size(min = 1, max = 3)
            String getValue();
        }
    }

    interface Multiple {
        @AutoGsonAdapter
        interface ArrayModel extends BaseArrayModel {
            @gsonpath.extension.annotation.Size(multiple = 2)
            Integer[] getValue();
        }

        @AutoGsonAdapter
        interface CollectionModel extends BaseCollectionModel {
            @android.support.annotation.Size(multiple = 2)
            List<Integer> getValue();
        }

        @AutoGsonAdapter
        interface StringModel extends BaseStringModel {
            @android.support.annotation.Size(multiple = 2)
            String getValue();
        }
    }

    interface ExactSize {
        @AutoGsonAdapter
        interface ArrayModel extends BaseArrayModel {
            @gsonpath.extension.annotation.Size(value = 1)
            Integer[] getValue();
        }

        @AutoGsonAdapter
        interface CollectionModel extends BaseCollectionModel {
            @android.support.annotation.Size(value = 1)
            List<Integer> getValue();
        }

        @AutoGsonAdapter
        interface StringModel extends BaseStringModel {
            @android.support.annotation.Size(value = 1)
            String getValue();
        }
    }

}

