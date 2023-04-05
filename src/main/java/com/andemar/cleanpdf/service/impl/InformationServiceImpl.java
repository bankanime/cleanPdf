package com.andemar.cleanpdf.service.impl;

import static com.andemar.cleanpdf.util.PdfReadUtils.loadPdf;
import static com.andemar.cleanpdf.util.CommonVariables.*;

import com.andemar.cleanpdf.model.Dimensions;
import com.andemar.cleanpdf.service.InformationService;
import com.andemar.cleanpdf.util.PdfReadUtils;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InformationServiceImpl implements InformationService {

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
}
