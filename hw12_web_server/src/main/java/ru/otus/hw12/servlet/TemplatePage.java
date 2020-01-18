package ru.otus.hw12.servlet;

public enum TemplatePage {
        LOGIN("login.html"),
        ADMIN("admin/admin.html"),
        ADD_USER("admin/add_user.html"),
        USERS("admin/users.html"),
        USER_ADDED("admin/user_added.html"),
        USER_NOT_ADDED("admin/user_not_added.html")
    ;

    private String pageFile;

    TemplatePage(String file) {
        pageFile = file;
    }

    public String pageFile() {
        return pageFile;
    }
}
