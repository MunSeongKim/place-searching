package com.mskim.place_searching.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(SignInFailureHandler.class);
    private static final String FAILURE_PARAMETER_KEY = "error";
    private static final String FAILURE_PARAMETER_VALUE = "true";
    private String usernameKey;
    private RequestCache requestCache = new HttpSessionRequestCache();

    public SignInFailureHandler(String defaultFailureUrl, String usernameKey) {
        super.setDefaultFailureUrl(this.generateFailureRedirectUrl(defaultFailureUrl));
        this.usernameKey = usernameKey;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);

        logger.info("---------> USERNAME_KEY: " + this.usernameKey);
        logger.info("---------> parameter: " + request.getParameter(this.usernameKey));
        request.setAttribute(this.usernameKey, request.getParameter(this.usernameKey));
//        request.setAttribute(); TODO: Error Message 작업 필요

        super.onAuthenticationFailure(request, response, exception);
    }

    private String generateFailureRedirectUrl(String failureUrl) {
        return failureUrl + "?" + FAILURE_PARAMETER_KEY + "=" + FAILURE_PARAMETER_VALUE;
    }
}
