package com.vm.qsmart2api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

/**
 * Created by srinivas.j.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtUtil;

    private static final String header = "[JWT_AUTH_FILTER] ";

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    @Value("${app.debug.required:false}")
    private boolean isLogEnabled;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isLogEnabled) {
                logger.info("{}Enter:doFilterInternal:Request:[{}]", header, request.getRequestURI());
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    logger.info("Header  :[{}]", headerNames.nextElement());
                }
                logger.info("Authorization : [{}]", request.getHeader("Authorization"));
            }
            Authentication authentication = jwtUtil.validateAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            logger.error("{}Excep:doFilterInternal:Error:{}", header, ExceptionUtils.getRootCauseMessage(ex));
        }
        filterChain.doFilter(request, response);
    }
}
