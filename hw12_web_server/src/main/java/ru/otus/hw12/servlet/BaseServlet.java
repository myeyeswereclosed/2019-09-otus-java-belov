package ru.otus.hw12.servlet;

import ru.otus.hw12.services.template.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

abstract public class BaseServlet extends HttpServlet {
    protected String requestParam(HttpServletRequest request, String param) {
        return request.getParameter(param);
    }

    protected void templateResponse(
        HttpServletResponse response,
        ContentType contentType,
        TemplateProcessor processor,
        TemplatePage page,
        Map<String, Object> params
    ) throws IOException  {
        response.setContentType(contentType.getName());
        response
            .getWriter()
            .println(processor.getPage(page.pageFile(), params));
    }

    protected void noParamsResponse(
        HttpServletResponse response,
        ContentType contentType,
        TemplateProcessor processor,
        TemplatePage page
    ) throws IOException {
        templateResponse(response, contentType, processor, page, Collections.emptyMap());
    }
}
