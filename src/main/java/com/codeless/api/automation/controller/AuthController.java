package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.AUTH_RESOURCE;

import com.codeless.api.automation.constant.PropertyKey;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.service.UsernameStorageService;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(AUTH_RESOURCE)
@RequiredArgsConstructor
public class AuthController {

  private final UsernameStorageService userStorageService;

  @PostMapping(value = "/sign-in")
  public void signIn(HttpServletRequest httpServletRequest) {
    verifySignIn(httpServletRequest);
    userStorageService.addUsername(httpServletRequest.getSession().getId(),
        httpServletRequest.getParameter(PropertyKey.EMAIL));
  }

  @DeleteMapping(value = "/sign-out")
  public void signOut(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    SecurityContextHolder.clearContext();
    if (session != null) {
      userStorageService.removeUsername(session.getId());
      session.invalidate();
    }
  }

  private void verifySignIn(HttpServletRequest httpServletRequest) {
    if (Objects.isNull(httpServletRequest.getParameter(PropertyKey.EMAIL))) {
      throw new ApiException("Email should be present in request.", HttpStatus.BAD_REQUEST.value());
    }
    if (Objects.isNull(httpServletRequest.getParameter(PropertyKey.PASSWORD))) {
      throw new ApiException("Password should be present in request.",
          HttpStatus.BAD_REQUEST.value());
    }
  }

}
