package org.couchbase.quickstart.controllers;

import org.couchbase.quickstart.exception.BalanceException;
import org.couchbase.quickstart.exception.ProfileException;
import org.couchbase.quickstart.response.ProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.couchbase.client.core.error.DocumentNotFoundException;

@ControllerAdvice
public class ProfileExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ProfileResponse> handleProfileException(ProfileException exc) {
    final ProfileResponse error = new ProfileResponse(
        HttpStatus.BAD_REQUEST.value(), exc.getMessage(), System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
  }

  @ExceptionHandler
  public ResponseEntity<ProfileResponse> handleBalanceException(BalanceException exc) {
    final ProfileResponse error = new ProfileResponse(
        HttpStatus.BAD_REQUEST.value(), exc.getMessage(), System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
  }

  @ExceptionHandler
  public ResponseEntity<ProfileResponse> handleDocumentNotFoundException(DocumentNotFoundException exc) {
    final String message = "Document not found";
    final ProfileResponse error = new ProfileResponse(
        HttpStatus.BAD_REQUEST.value(), message, System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
  }

  /*
  @ExceptionHandler
  public ResponseEntity<ProfileResponse> handleAll(Exception exc) {
    final String message = "¯\\_(ツ)_/¯";
    final ProfileResponse error = new ProfileResponse(
        HttpStatus.BAD_REQUEST.value(), message, System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
  }
   */

}
