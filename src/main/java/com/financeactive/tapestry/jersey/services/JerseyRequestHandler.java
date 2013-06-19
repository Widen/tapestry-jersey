package com.financeactive.tapestry.jersey.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public interface JerseyRequestHandler {

    boolean service(JerseyEndPoint endpoint, HttpServletRequest request, HttpServletResponse response) throws IOException;

}
