package ru.otus.hw12.servlet.admin;

import ru.otus.hw12.services.template.TemplateProcessor;
import ru.otus.hw12.servlet.BaseServlet;
import ru.otus.hw12.servlet.ContentType;
import ru.otus.hw12.servlet.TemplatePage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminServlet extends BaseServlet {
    private final TemplateProcessor templateProcessor;

    public AdminServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        noParamsResponse(response, ContentType.TEXT_HTML, templateProcessor, TemplatePage.ADMIN);
    }
}
