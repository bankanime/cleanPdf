package com.andemar.cleanpdf.service.impl;

import static com.andemar.cleanpdf.util.CommonVariables.HEIGHT;
import static com.andemar.cleanpdf.util.CommonVariables.WIDTH;
import static com.andemar.cleanpdf.util.PdfReadUtils.loadPdf;

import com.andemar.cleanpdf.model.Dimensions;
import com.andemar.cleanpdf.model.PdfContent;
import com.andemar.cleanpdf.service.InformationService;
import com.andemar.cleanpdf.util.PdfReadUtils;
import com.andemar.cleanpdf.util.PdfWriteUtils;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InformationServiceImpl implements InformationService {

  private final PdfReadUtils pdfReadUtils;
  private final PdfWriteUtils pdfWriteUtils;


  public InformationServiceImpl(PdfReadUtils pdfReadUtils, PdfWriteUtils pdfWriteUtils) {
    this.pdfReadUtils = pdfReadUtils;
    this.pdfWriteUtils = pdfWriteUtils;
  }

  @Override
  public Dimensions getDimensions(MultipartFile file, int pageNumber) {
    PDDocument document = loadPdf(file);
    PDPage page = document.getPage(pageNumber);
    Map<String, Float> dimensions = PdfReadUtils.getDimensions(page);
    return Dimensions.builder()
        .width(dimensions.get(WIDTH))
        .height(dimensions.get(HEIGHT))
        .build();
  }

  @Override
  public byte[] check(MultipartFile file, int pageNumber, Dimensions rectangle) {
    PdfContent pdfContent = pdfReadUtils.multipartFileToStringBuilder(file, pageNumber, rectangle);
    pdfContent.setFlatContent(pdfReadUtils.cleanText(pdfContent.getContent().toString()));
    return pdfWriteUtils.createPdf(pdfContent);
  }
}
