package com.andemar.cleanpdf.service.impl;

import com.andemar.cleanpdf.exception.CleanPdfException;
import com.andemar.cleanpdf.service.CleanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.andemar.cleanpdf.utils.PdfUtils.*;

@Slf4j
@Service
public class CleanServiceImpl implements CleanService {

    PDDocument pdDocument;

    @Override
    public void cleanFile(MultipartFile file) {
        PDDocument document = multiPartFileToPdf(file);
        checkPermissionPdf(document);
        extractText(document);
    }

    private void extractText(PDDocument document) {

        try {
            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setSortByPosition(true);

//            for (int p = 1; p <= 10; ++p) {
                stripper.setStartPage(1);
                stripper.setEndPage(10);

                String text = stripper.getText(document);
                text = cleanText(text);

                savePdf(text);
//            }


        } catch (IOException e) {
            String message = "Error to try extract text: " + e.getMessage();
            log.error(message);
            throw new CleanPdfException(message);
        }
    }

    private void savePdf(String text) {
        try {
            PDDocument documentSave = new PDDocument();
            PDPage page = new PDPage(PDRectangle.LETTER);
            documentSave.addPage(page);
            PDPageContentStream content = new PDPageContentStream(documentSave, page);

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.newLineAtOffset(20, page.getMediaBox().getHeight()-52);
            content.showText(text);
            content.endText();

            content.close();

            documentSave.save("D:\\text.pdf");
        } catch (IOException|IllegalArgumentException e) {
            String message = "Error try to save document: " + e.getMessage();
            log.error(message);
            throw new CleanPdfException(message);
        }
    }

//    List<String> lineas = Arrays.stream(text.split("\r\n"))
//            .filter(line -> !line.equals("") && !line.equals(" "))
//            .collect(Collectors.toList());
//
//    List<List<String>> pages = new ArrayList<>();
//
//    int i = 0;
//    int lastPage = 0;
//while(i != 1) {
//        int finalpage = lineas.indexOf("\f ");
//        List<String> page = lineas.subList(0, finalpage);
////    Collection<String> page = lineas.subList(0, finalpage);
//        pages.add(page);
////    lineas.subList(0, finalpage).clear();
//        for (int j = 0; j <= finalpage; j++) {
//            lineas.remove(j);
//        }
//        i++;
//    }
//pages.add(lineas);
//
//    pages
}
