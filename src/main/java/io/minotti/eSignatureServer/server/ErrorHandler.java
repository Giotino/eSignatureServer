package io.minotti.eSignatureServer.server;

import io.minotti.eSignatureServer.signature.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(value = {MissingServletRequestPartException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String constraintViolationException(MissingServletRequestPartException ex) {
    return "Missing field \"" + ex.getRequestPartName() + "\"";
  }

  @ExceptionHandler(value = {MissingServletRequestParameterException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String constraintViolationException(MissingServletRequestParameterException ex) {
    return "Missing field \"" + ex.getParameterName() + "\"";
  }

  @ExceptionHandler(value = {SignatureException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String internalServerError(SignatureException ex) {
    return ex.toString();
  }

  @ExceptionHandler(value = {Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String internalServerError(Exception ex) {
    ex.printStackTrace();
    return "Internal server error";
  }
}
