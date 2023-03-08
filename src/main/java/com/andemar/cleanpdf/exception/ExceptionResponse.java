package com.andemar.cleanpdf.exception;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
  private Date timestamp;
  private String mensaje;
  private String detalles;

  public ExceptionResponse(Date timestamp, String mensaje, String detalles) {
    this.timestamp = timestamp;
    this.mensaje = mensaje;
    this.detalles = detalles;
  }

}
