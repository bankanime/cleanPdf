package com.andemar.cleanpdf.service.impl;

import com.andemar.cleanpdf.service.CleanService;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class CleanServiceImpl implements CleanService {

    Document document;
    FileOutputStream archivo;
    Paragraph contenido = new Paragraph();

    @Override
    public void cleanFile(MultipartFile file) {
        log.info("upload file");
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(file.getInputStream()));
            StringBuilder content = new StringBuilder();

            for(int i = 1; i < pdfDocument.getNumberOfPages(); i++) {
                content.append(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i)));
            }
//            log.info(content.toString());

            // generate PDF
            PdfDocument newPdf = new PdfDocument(new PdfWriter("target/test.pdf"));
            newPdf.setDefaultPageSize(PageSize.LETTER);

            Document newDocument = new Document(newPdf);
            newDocument.setLeftMargin(68.04F);
            newDocument.setRightMargin(68.04F);

//            PdfFont font = PdfFontFactory.createFont();


            Text text = new Text(content.toString());
//            text.setFont(font);
            text.setFontSize(9);
            text.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
            contenido.add(text);

            newDocument.add(contenido);
            newDocument.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
