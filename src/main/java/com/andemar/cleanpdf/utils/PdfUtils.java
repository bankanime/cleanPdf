package com.andemar.cleanpdf.utils;

import com.andemar.cleanpdf.exception.CleanPdfException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class PdfUtils {

    private PdfUtils() {}

    public static PDDocument multiPartFileToPdf(MultipartFile file) {
        PDDocument pdDocument;

        try {
            byte[] bytes = file.getBytes();
            pdDocument = PDDocument.load(bytes);
            log.info("Pdf was load");
        } catch (IOException e) {
            String message = "Load Pdf error: " + e.getMessage();
            log.error(message);
            throw new CleanPdfException(message);
        }

        return pdDocument;
    }

    public static void checkPermissionPdf(PDDocument pdf) {
        AccessPermission ap = pdf.getCurrentAccessPermission();
        if(!ap.canExtractContent())
            throw new CleanPdfException("You do not have permission to extract text");
    }

    public static String cleanText(String text) {
        int iterations = 50;
        text.trim();

        for (int i = 0; i < iterations; i++) {
            text = text.replaceAll("\r\n \r\n ","\r\n");
            text = text.replaceAll("\r\n\r\n","\r\n");
            text = text.replaceAll("\r\r", "\r");
            text = text.replaceAll("\n\n", "\n");
            text = text.replaceAll("  ", " ");
            text = text.replaceAll("1", "\f");
        }
        return text;
    }
}
