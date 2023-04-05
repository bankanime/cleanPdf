package com.andemar.cleanpdf.service;

import com.andemar.cleanpdf.model.Dimensions;
import org.springframework.web.multipart.MultipartFile;

public interface InformationService {

  public Dimensions getDimensions(MultipartFile file, int pageNumber);
}
