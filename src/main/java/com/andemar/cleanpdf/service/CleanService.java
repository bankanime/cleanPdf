package com.andemar.cleanpdf.service;

import org.springframework.web.multipart.MultipartFile;

public interface CleanService {
    byte[] cleanFile(MultipartFile file);
}
