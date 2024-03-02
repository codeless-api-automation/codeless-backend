package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.PROFILE_RESOURCE;

import com.codeless.api.automation.dto.Profile;
import com.codeless.api.automation.service.ProfileService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(PROFILE_RESOURCE)
@Validated
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping
  public Profile getProfile(Principal principal) {
    return profileService.getProfile(principal.getName());
  }
}
