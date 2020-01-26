package generator.standard.generics.interfaces;

import java.util.Map;

interface BaseTest<GENERIC_1, GENERIC_2, GENERIC_3> {
    GENERIC_1 getValue1();

    Map<GENERIC_2, GENERIC_3> getValue2();
}