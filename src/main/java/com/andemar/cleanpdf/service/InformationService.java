package com.andemar.cleanpdf.service;

import com.andemar.cleanpdf.model.Dimensions;
import org.springframework.web.multipart.MultipartFile;

public interface InformationService {

  Dimensions getDimensions(MultipartFile file, int pageNumber);

  byte[] check(MultipartFile file, int pageNumber, Dimensions rectangle);
}
