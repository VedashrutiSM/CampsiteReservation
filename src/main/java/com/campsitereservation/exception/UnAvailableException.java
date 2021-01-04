package com.campsitereservation.exception;

public class UnAvailableException extends RuntimeException {
  private String message;
  private String errorReason;
  
  public UnAvailableException() {}

  public UnAvailableException(String message, String errorReason) {
    this.message = message;
    this.errorReason = errorReason;
  }
  
  public UnAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1, String errorReason) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.message = message1;
    this.errorReason = errorReason;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getErrorReason() {
    return errorReason;
  }
}
