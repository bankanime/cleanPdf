package com.andemar.cleanpdf.util;

import com.andemar.cleanpdf.exception.CleanPdfException;
import com.andemar.cleanpdf.model.ImagePosition;
import com.andemar.cleanpdf.model.PageContent;
import com.andemar.cleanpdf.model.PdfContent;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PdfWriteUtils {

  private static final String CLEAN_PHRASE_POSITION_WINDOWS = "[\r\n]";
  private static final String CLEAN_PHRASE_POSITION_LINUX = "[\n\n]";

  public byte[] createPdf(PdfContent pdfContent) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    // generate PDF
    PdfDocument newPdf = null;
    newPdf = new PdfDocument(new PdfWriter(byteArrayOutputStream));
    newPdf.setDefaultPageSize(PageSize.LETTER);

    Document newDocument = new Document(newPdf);
    newDocument.setLeftMargin(68.04F);
    newDocument.setRightMargin(68.04F);

    if(pdfContent.hasImages()) {
      for(PageContent page : getPages(pdfContent)) {
        if(page.isImage())
          newDocument.add(page.getImage());
        else
          newDocument.add(page.getText());
      }
    } else {
      newDocument.add(getParagraph(pdfContent));
    }

    newDocument.close();
    return byteArrayOutputStream.toByteArray();
  }

  private static Paragraph getParagraph(PdfContent pdfContent) {
    return new Paragraph(pdfContent.getFlatContent());
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
         contentTextSplit = contentText.split(cleanPhrasePosition(image.getPhrasePosition()).trim());

         if(contentTextSplit.length <= 1)
           throwSplitException(contentTextSplit[0], image.getPhrasePosition());

         contentText = contentTextSplit[1];

         pageText.add(getText(contentTextSplit[0] + image.getPhrasePosition()));

         pages.add( PageContent.builder().text(pageText).build() );
         pages.add( PageContent.builder().image( new Image(ImageDataFactory.create(image.getImage()))).build() );
       }

       // The last text after the last image
       if(image.getLastPosition() == content.getImages().size()-1) {
         pageText = new Paragraph();
         pageText.add(getText(contentText));

         pages.add( PageContent.builder().text(pageText).build() );
       }
    }

    return pages;
  }

  private void throwSplitException(String contentTextSplit, String phrasePosition) {
    String textSplit = contentTextSplit.substring(contentTextSplit.length()-50);
    throw new CleanPdfException("TextSplit: |" + textSplit + "|   ^^^^^^^^^^   " + "PhrasePosition: |" + phrasePosition + "|");
  }


  private String cleanPhrasePosition(String phrasePosition) {
    String cleanPhrase = (System.getProperty("os.name").equalsIgnoreCase("Linux")) ? CLEAN_PHRASE_POSITION_LINUX : CLEAN_PHRASE_POSITION_WINDOWS;
    phrasePosition = phrasePosition.replaceAll(cleanPhrase, "");
    return phrasePosition;
  }

  public Text getText(String content) {
    Text text = new Text(content);
    text.setFontSize(9);
    text.setFont(getFont());
    text.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
    return text;
  }

  public PdfFont getFont() {
    try {
      return PdfFontFactory.createFont(StandardFonts.HELVETICA);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
