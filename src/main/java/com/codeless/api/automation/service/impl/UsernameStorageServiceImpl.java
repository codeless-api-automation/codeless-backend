package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.service.UsernameStorageService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public final class UsernameStorageServiceImpl implements UsernameStorageService {

  private final Map<String, String> storage = new ConcurrentHashMap<>();

  @Override
  public String addUsername(String sessionId, String user) {
    return storage.put(sessionId, user);
  }

  @Override
  public String getUsername(String sessionId) {
    return storage.get(sessionId);
  }

  @Override
  public String removeUsername(String sessionId) {
    return storage.remove(sessionId);
  }
}
