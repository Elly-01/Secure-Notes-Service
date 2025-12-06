package com.agora.Secure_Notes_Service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    @Value("${notes.api.token}")
    private String validToken;

/**
 * The function `doFilterInternal` checks for a valid Authorization header
 * with a Bearer token and allows access based on the token validity.
 * 
 * @param request The `request` parameter in the `doFilterInternal` method
 * represents the HTTP request that the servlet container receives from the
 * client. It contains information such as the request URL, headers,
 * parameters, and body. The `HttpServletRequest` class provides methods to
 * access and manipulate this information within the servlet or filter
 * @param response The `response` parameter in the `doFilterInternal`
 * method is of type `HttpServletResponse`. It represents the response that
 * the servlet sends back to the client. This response object contains
 * information such as the status code, headers, and body of the response
 * that will be sent back to the client who
 * @param filterChain The `filterChain` parameter in the `doFilterInternal`
 * method is an object that represents a chain of filters to be applied to
 * a request for a servlet. It allows multiple filters to be applied in a
 * specific order before the request reaches the servlet or resource. The
 * `filterChain` object provides
 */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

//        Debug
//        System.out.println("VALID TOKEN: ");
//        System.out.println(validToken);
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (request.getRequestURI().startsWith("/h2-console")) {
                filterChain.doFilter(request, response);
                return;
            }
            if (request.getRequestURI().startsWith("/health")) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Authorization header");
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer "

        if (!token.equals(validToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
