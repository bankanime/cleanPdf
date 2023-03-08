package com.andemar.cleanpdf.util;

import com.andemar.cleanpdf.exception.CleanPdfException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class PdfReadUtils {

  private static final String JUMP_LINE = "\r\n";
  private static final String RETURN = "\r";
  private static final String NEW_LINE = "\n";
  private static final String NEW_PAGE = "\f";
  private static final String SPACE = " ";


  public StringBuilder multipartFileToStringBuilder(MultipartFile file) {
    PDDocument document = loadPdf(file);

    StringBuilder content = new StringBuilder();
    try {
      PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
      Rectangle2D rect = new java.awt.geom.Rectangle2D.Float(40, 80, 500, 623);
      textStripper.addRegion("region", rect);

      for (int i = 0; i < document.getNumberOfPages(); i++) {
        textStripper.extractRegions(document.getPage(i));
        content.append(textStripper.getTextForRegion("region"));
      }

      return content;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public PDDocument loadPdf(MultipartFile file) {
    PDDocument pdDocument = null;

    try {
      byte[] bytes = file.getBytes();
      pdDocument = PDDocument.load(bytes);
      log.info("Pdf was load");
    } catch (IOException e) {
      String message = "Load Pdf error: " + e.getMessage();
      log.error(message);
      throw new CleanPdfException(message);
    }
    checkPermissionPdf(pdDocument);
    return pdDocument;
  }


  public void checkPermissionPdf(PDDocument pdf) {
    AccessPermission ap = pdf.getCurrentAccessPermission();
    if(!ap.canExtractContent())
      throw new CleanPdfException("You do not have permission to extract text");
  }


  public String cleanText(String text) {
    int iterations = 50;
    text.trim();



    for (int i = 0; i < iterations; i++) {
      text = text.replaceAll("\r\n \r\n ",JUMP_LINE);
      text = text.replaceAll("\r\n\r\n",JUMP_LINE);
      text = text.replaceAll("\r\r", RETURN);
      text = text.replaceAll("\n\n", NEW_LINE);
      text = text.replaceAll("1", NEW_PAGE);
      text = text.replaceAll("  ", SPACE);

    }
    text = text.replaceAll("\r\n \r\n", "\n\n");
    text = text.replaceAll("(\\r\\n)([áéíóúÁÉÍÓÚa-zA-Z ])", "$2");
    return text;
  }
}
