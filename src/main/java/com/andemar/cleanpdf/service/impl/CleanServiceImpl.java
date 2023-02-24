package com.andemar.cleanpdf.service.impl;

import com.andemar.cleanpdf.service.CleanService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class CleanServiceImpl implements CleanService {

    @Override
    public void cleanFile(MultipartFile file) {
        log.info("upload file");
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(file.getInputStream()));
            log.info(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
