package gsonpath.vanilla;

public class PeopleModelVanilla {
    public People[] people;

    public static class People {
        public Person person;
    }

    public static class Person {
        public Names names;
    }

    public static class Names {
        public String first;
        public String last;
    }
}
