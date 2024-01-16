package com.codeless.api.automation.service;

public interface UsernameStorageService {

  String addUsername(String sessionId, String user);

  String getUsername(String sessionId);

  String removeUsername(String sessionId);
}

