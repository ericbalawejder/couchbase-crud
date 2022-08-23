package org.couchbase.quickstart.exception;

public class ProfileException extends RuntimeException {

  public ProfileException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProfileException(String message) {
    super(message);
  }

  public ProfileException(Throwable cause) {
    super(cause);
  }

}
