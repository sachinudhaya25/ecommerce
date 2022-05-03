package com.ecommerce.training.config;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;






@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private final HttpMessageConverter<String> messageConverter;

	private final ObjectMapper mapper;

	public AuthEntryPointJwt(ObjectMapper mapper) {
	        this.messageConverter = new StringHttpMessageConverter();
	        this.mapper = mapper;
	 }
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ApiError apiError = new ApiError(UNAUTHORIZED);
		apiError.setMessage(authException.getMessage());
        apiError.setDebugMessage(authException.getMessage());
		logger.error("Unauthorized error: {}", authException.getMessage());
		ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
	    outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
	    messageConverter.write(mapper.writeValueAsString(apiError), MediaType.APPLICATION_JSON, outputMessage);
	}
	
}
