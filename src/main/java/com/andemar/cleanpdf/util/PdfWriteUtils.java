package com.andemar.cleanpdf.util;

import com.andemar.cleanpdf.model.PdfContent;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.FileNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class PdfWriteUtils {

  Paragraph contenido;

  public void createPdf(PdfContent pdfContent) {
    String content = pdfContent.getFlatContent();
    contenido = new Paragraph();

    // generate PDF
    PdfDocument newPdf = null;
    try {
      newPdf = new PdfDocument(new PdfWriter("target/test.pdf"));
      newPdf.setDefaultPageSize(PageSize.LETTER);

      Document newDocument = new Document(newPdf);
      newDocument.setLeftMargin(68.04F);
      newDocument.setRightMargin(68.04F);

      //PdfFont font = PdfFontFactory.createFont();


      Text text = new Text(content);
      //text.setFont(font);
      text.setFontSize(9);
      text.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
      contenido.add(text);

      newDocument.add(contenido);

      pdfContent.getImages()
                  .forEach(imagePosition ->
                      newDocument.add(
                          new Image(ImageDataFactory.create(imagePosition.getImage()))
                      )
                  );

      newDocument.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
