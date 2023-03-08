package com.andemar.cleanpdf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CleanPdfException extends RuntimeException{
  public CleanPdfException(String message) {
    super("Error cleanPdf process: " + message, null, false, false);
  }
}
