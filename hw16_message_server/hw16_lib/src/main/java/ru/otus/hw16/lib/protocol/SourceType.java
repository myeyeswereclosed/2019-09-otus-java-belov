package ru.otus.hw16.lib.protocol;

public enum SourceType {
        FRONTEND("frontend"),
        DB_SERVICE("dbService"),
        MESSAGE_SYSTEM("messageSystem")
    ;

    private final String type;

    SourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean isFrontend(String type) {
        return FRONTEND.type.equals(type);
    }

    public static boolean isDbService(String type) {
        return DB_SERVICE.type.equals(type);
    }
}
