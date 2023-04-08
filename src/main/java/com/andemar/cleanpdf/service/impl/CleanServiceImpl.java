package com.andemar.cleanpdf.service.impl;

import com.andemar.cleanpdf.model.Dimensions;
import com.andemar.cleanpdf.model.PdfContent;
import com.andemar.cleanpdf.service.CleanService;
import com.andemar.cleanpdf.util.PdfReadUtils;
import com.andemar.cleanpdf.util.PdfWriteUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class CleanServiceImpl implements CleanService {

    private final PdfReadUtils pdfReadUtils;

    private final PdfWriteUtils pdfWriteUtils;

    @Autowired
    public CleanServiceImpl(PdfReadUtils pdfReadUtils, PdfWriteUtils pdfWriteUtils) {
        this.pdfReadUtils = pdfReadUtils;
        this.pdfWriteUtils = pdfWriteUtils;
    }

    @Override
    public byte[] cleanFile(MultipartFile file, Dimensions rectangle) {
        log.info("upload file");
        PdfContent pdfContent = pdfReadUtils.multipartFileToStringBuilder(file, rectangle);
        pdfContent.setFlatContent(pdfReadUtils.cleanText(pdfContent.getContent().toString()));
        return pdfWriteUtils.createPdf(pdfContent);
    }


}
