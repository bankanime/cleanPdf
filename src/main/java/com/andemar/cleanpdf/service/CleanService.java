package com.andemar.cleanpdf.service;

import org.springframework.web.multipart.MultipartFile;

public interface CleanService {
    void cleanFile(MultipartFile file);
}
