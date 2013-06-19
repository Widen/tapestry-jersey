package com.financeactive.tapestry.jersey.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public interface JerseyRequestFilter {

    boolean service(JerseyEndPoint endpoint, HttpServletRequest request, HttpServletResponse response, JerseyRequestHandler handler) throws IOException;

}
