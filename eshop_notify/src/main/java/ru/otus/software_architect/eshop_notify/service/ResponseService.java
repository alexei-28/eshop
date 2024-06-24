package ru.otus.software_architect.eshop_notify.service;

import ru.otus.software_architect.eshop_notify.api.Response;

public class ResponseService {

    public static Response getSuccessResponse() {
        return new Response(0, "Success");
    }

    public static Response getErrorResponse(String errorMessage) {
        return new Response(-1, errorMessage);
    }
}
