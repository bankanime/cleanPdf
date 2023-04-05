package com.andemar.cleanpdf.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class Headers {

  public static HttpHeaders contentPdfHeaders(String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData(fileName, fileName);
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return headers;
  }

}
