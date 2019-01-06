package gsonpath.generator.extension.empty;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.EmptyToNull;

import java.util.Collection;
import java.util.Map;

public interface EmptyModel {

    interface BaseStringModel {
        @EmptyToNull
        String getValue();
    }

    interface BaseMapModel {
        @EmptyToNull
        Map<String, String> getValue();
    }

    interface BaseCollectionModel {
        @EmptyToNull
        Collection<String> getValue();
    }

    interface BaseArrayModel {
        @EmptyToNull
        String[] getValue();
    }

    interface Nullable {
        @AutoGsonAdapter
        interface StringModel extends BaseStringModel {
        }

        @AutoGsonAdapter
        interface MapModel extends BaseMapModel {
        }

        @AutoGsonAdapter
        interface CollectionModel extends BaseCollectionModel {
        }

        @AutoGsonAdapter
        interface ArrayModel extends BaseArrayModel {
        }
    }

    interface NonNull {
        @AutoGsonAdapter
        interface StringModel extends BaseStringModel {
            @EmptyToNull
            @gsonpath.NonNull
            String getValue();
        }

        @AutoGsonAdapter
        interface MapModel extends BaseMapModel {
            @EmptyToNull
            @gsonpath.NonNull
            Map<String, String> getValue();
        }

        @AutoGsonAdapter
        interface CollectionModel extends BaseCollectionModel {
            @EmptyToNull
            @gsonpath.NonNull
            Collection<String> getValue();
        }

        @AutoGsonAdapter
        interface ArrayModel extends BaseArrayModel {
            @EmptyToNull
            @gsonpath.NonNull
            String[] getValue();
        }
    }

}
