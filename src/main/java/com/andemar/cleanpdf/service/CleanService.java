package com.andemar.cleanpdf.service;

import com.andemar.cleanpdf.model.Dimensions;
import org.springframework.web.multipart.MultipartFile;

public interface CleanService {
    byte[] cleanFile(MultipartFile file, Dimensions rectangle);
}
