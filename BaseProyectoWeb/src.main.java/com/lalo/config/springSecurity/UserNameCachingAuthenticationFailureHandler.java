package com.lalo.config.springSecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class UserNameCachingAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private String usernameParameter = "app_username";
	private String passwordParameter = "app_password";
	private boolean postOnly = true;
	public UserNameCachingAuthenticationFailureHandler(){super("/login.xhtml?error=x");}
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
    	if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Método de autentificación no soportado: " + request.getMethod());
		}
        super.onAuthenticationFailure(request, response, exception);
        System.out.println(obtainUsername(request));
        obtainPassword(request);
    }
    protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}
}
