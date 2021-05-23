package com.codeless.api.automation.controller.fillter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
    response.setHeader("Access-Control-Allow-Methods",
        "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
    response.setHeader("Access-Control-Allow-Headers", "X-Auth-Token");
    response.setHeader("Access-Control-Allow-Headers",
        "Content-Type, Accept, Authorization, X-Auth-Token");
    response.setHeader("Access-Control-Expose-Headers", "X-Auth-Token");
    response.setHeader("Access-Control-Max-Age", "3600");
    filterChain.doFilter(servletRequest, servletResponse);
  }
}
