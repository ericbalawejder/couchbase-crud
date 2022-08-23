package org.couchbase.quickstart.response;

public class ProfileResponse {

  private final int status;
  private final String message;
  private final long timeStamp;

  public ProfileResponse(int status, String message, long timeStamp) {
    this.status = status;
    this.message = message;
    this.timeStamp = timeStamp;
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

}
