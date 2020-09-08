package com.codeless.api.automation.mapper;

public interface Mapper<T, S> {

  S map(T source);
}
