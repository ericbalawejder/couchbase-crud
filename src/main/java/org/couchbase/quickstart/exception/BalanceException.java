package org.couchbase.quickstart.exception;

public class BalanceException extends RuntimeException {

  public BalanceException(String message, Throwable cause) {
    super(message, cause);
  }

  public BalanceException(String message) {
    super(message);
  }

  public BalanceException(Throwable cause) {
    super(cause);
  }

}
