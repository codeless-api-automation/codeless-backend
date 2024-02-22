package com.codeless.api.automation.storage;

import com.codeless.api.automation.entity.User;
import io.github.bucket4j.Bucket;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RateLimitData {

  private User user;
  private Bucket bucket;
}
