package gsonpath.adapter.standard.extension.range.intrange;

import gsonpath.AutoGsonAdapter;

public interface IntRangeModel {

    interface BaseModel<T> {
        T getValue();
    }

    @AutoGsonAdapter
    interface IntModel extends BaseModel<Integer> {
        @android.support.annotation.IntRange(from = 0, to = 5)
        Integer getValue();
    }

    @AutoGsonAdapter
    interface LongModel extends BaseModel<Long> {
        @gsonpath.extension.annotation.IntRange(from = 0, to = 5)
        Long getValue();
    }
}

