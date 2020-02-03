package gsonpath;

public interface GsonPathListener {
    void onListElementIgnored(Exception exception);

    void onDefaultEnum(Class clazz, String unexpectedValue);
}
