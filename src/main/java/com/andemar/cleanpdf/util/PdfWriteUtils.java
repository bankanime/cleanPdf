package com.andemar.cleanpdf.util;

import com.andemar.cleanpdf.model.ImagePosition;
import com.andemar.cleanpdf.model.PageContent;
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
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PdfWriteUtils {

//  private final String CLEAN_PHRASE_POSITION = "[”\"\r\n]";
  private final String CLEAN_PHRASE_POSITION = "[\r\n]";

  public void createPdf(PdfContent pdfContent) {
    // generate PDF
    PdfDocument newPdf = null;
    try {
      newPdf = new PdfDocument(new PdfWriter("target/test.pdf"));
      newPdf.setDefaultPageSize(PageSize.LETTER);

      Document newDocument = new Document(newPdf);
      newDocument.setLeftMargin(68.04F);
      newDocument.setRightMargin(68.04F);

      for(PageContent page : getPages(pdfContent)) {
        if(page.isImage())
          newDocument.add(page.getImage());
        else
          newDocument.add(page.getText());
      }

      newDocument.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }


  private List<PageContent> getPages(PdfContent content) {
    List<PageContent> pages = new ArrayList<>();
    String contentText = content.getFlatContent();
    String[] contentTextSplit;
    Paragraph pageText;

    for(ImagePosition image : content.getImages()){
       contentTextSplit = image.hasPosition() ? (contentText.split(image.getPhrasePosition())) : null;

       if(contentTextSplit == null) {
         pages.add( PageContent.builder().image( new Image(ImageDataFactory.create(image.getImage()))).build() );
       } else {
         pageText = new Paragraph();
         contentTextSplit = contentText.split(cleanPhrasePosition(image.getPhrasePosition()));
         contentText = contentTextSplit[1];

         Text text = new Text(contentTextSplit[0] + image.getPhrasePosition());
         text.setFontSize(9);
         text.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
         pageText.add(text);

         pages.add( PageContent.builder().text(pageText).build() );
         pages.add( PageContent.builder().image( new Image(ImageDataFactory.create(image.getImage()))).build() );
       }

       // The last text after the last image
       if(image.getLastPosition() == content.getImages().size()-1) {
         pageText = new Paragraph();

         Text text = new Text(contentText);
         text.setFontSize(9);
         text.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
         pageText.add(text);

         pages.add( PageContent.builder().text(pageText).build() );
       }
    }

    return pages;
  }


  private String cleanPhrasePosition(String phrasePosition) {
    phrasePosition = phrasePosition.replaceAll(CLEAN_PHRASE_POSITION, "");
    return phrasePosition;
  }
}
