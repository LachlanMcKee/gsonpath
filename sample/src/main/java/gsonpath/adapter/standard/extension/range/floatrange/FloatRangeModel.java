package gsonpath.adapter.standard.extension.range.floatrange;

import gsonpath.annotation.AutoGsonAdapter;

public interface FloatRangeModel {

    interface BaseModel<T> {
        T getValue();
    }

    interface BaseFloatModel extends BaseModel<Float> {
    }

    interface BaseDoubleModel extends BaseModel<Double> {
    }

    interface Inclusive {
        @AutoGsonAdapter
        interface FloatModel extends BaseFloatModel {
            @android.support.annotation.FloatRange(from = 0, to = 5)
            Float getValue();
        }

        @AutoGsonAdapter
        interface DoubleModel extends BaseDoubleModel {
            @gsonpath.extension.annotation.FloatRange(from = 0, to = 5)
            Double getValue();
        }
    }

    interface Exclusive {
        @AutoGsonAdapter
        interface FloatModel extends BaseFloatModel {
            @android.support.annotation.FloatRange(from = 0, to = 5, fromInclusive = false, toInclusive = false)
            Float getValue();
        }

        @AutoGsonAdapter
        interface DoubleModel extends BaseDoubleModel {
            @gsonpath.extension.annotation.FloatRange(from = 0, to = 5, fromInclusive = false, toInclusive = false)
            Double getValue();
        }
    }
}

