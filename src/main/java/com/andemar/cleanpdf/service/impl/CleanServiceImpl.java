package com.andemar.cleanpdf.service.impl;

import com.andemar.cleanpdf.service.CleanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class CleanServiceImpl implements CleanService {

    PDDocument pdDocument = new PDDocument();

    @Override
    public void cleanFile(MultipartFile file) {

        try {
            byte[] bytes = file.getBytes();
            pdDocument = PDDocument.load(bytes);

            log.info("Pdf was load");
        } catch (IOException e) {
            log.error("pdf error");
            throw new RuntimeException(e);
        }
    }
}
