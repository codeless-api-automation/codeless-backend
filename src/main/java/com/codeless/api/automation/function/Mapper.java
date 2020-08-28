package com.codeless.api.automation.function;

public interface Mapper<T, S> {

  S map(T source);
}
