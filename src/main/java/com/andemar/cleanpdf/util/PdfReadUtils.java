package com.andemar.cleanpdf.util;

import static com.andemar.cleanpdf.util.CommonVariables.HEIGHT;
import static com.andemar.cleanpdf.util.CommonVariables.WIDTH;

import com.andemar.cleanpdf.exception.CleanPdfException;
import com.andemar.cleanpdf.model.Dimensions;
import com.andemar.cleanpdf.model.ImagePosition;
import com.andemar.cleanpdf.model.PdfContent;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
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
  private static final Integer CHARACTER_PHRASE_POSITION = 80;
  private static final String SPLIT_DELETE_CHARACTERS = "[?\n\r\f]";

  public PdfContent multipartFileToStringBuilder(MultipartFile file, Dimensions rectangle) {
    PDDocument document = loadPdf(file);
    return PdfContent.builder()
            .content(extractContent(document, rectangle))
            .images(extractImagePosition(document))
            .build();
  }

  public static PDDocument loadPdf(MultipartFile file) {
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

  private static StringBuilder extractContent(PDDocument document, Dimensions rectangle) {
    StringBuilder content = new StringBuilder();
    try {
      PDFTextStripperByArea textStripper = new PDFTextStripperByArea();

      for(PDPage page: document.getPages()) {
        textStripper.addRegion("region", getRect(page, rectangle));
        textStripper.extractRegions(page);
        content.append(textStripper.getTextForRegion("region"));
      }

      return content;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Rectangle2D getRect(PDPage page, Dimensions rectangle) {
    if (rectangle != null && rectangle.exist()) {
      return new Rectangle2D.Float(rectangle.getX(),
                                   rectangle.getY(),
                                   rectangle.getWidth(),
                                   rectangle.getHeight());
    } else {
      Map<String, Float> dimensions = getDimensions(page);
      float width = dimensions.get(WIDTH) * 0.9f;
      float height = dimensions.get(WIDTH) * 0.9f;
      float x = width * 0.04f;
      float y = height * 0.04f;
      return new Rectangle2D.Float(x, y, width, height);
    }
  }

  public static Map<String, Float> getDimensions(PDPage page) {
    Map<String, Float> dimensions = new HashMap<>();
    dimensions.put(WIDTH, page.getMediaBox().getWidth());
    dimensions.put(HEIGHT, page.getMediaBox().getHeight());
    return dimensions;
  }

  public static void checkPermissionPdf(PDDocument pdf) {
    AccessPermission ap = pdf.getCurrentAccessPermission();
    if(!ap.canExtractContent())
      throw new CleanPdfException("You do not have permission to extract text");
  }

  public String cleanText(String text) {
    int iterations = 50;
    text.trim();

    if(text.isEmpty())
      return "";

    for (int i = 0; i < iterations; i++) {
      text = text.replaceAll("\r\n \r\n \r\n",JUMP_LINE);
      text = text.replaceAll("\r\n \r\n ",JUMP_LINE);
      text = text.replaceAll("\r\n\r\n",JUMP_LINE);
      text = text.replaceAll("\r\r", RETURN);
      text = text.replaceAll("\n\n", NEW_LINE);
//      text = text.replaceAll("1", NEW_PAGE);
      text = text.replaceAll("  ", SPACE);

    }
    text = text.replaceAll("\r\n \r\n", "\n\n");
    text = text.replaceAll("(\\r\\n)([áéííóúÁÉÍÓÚa-zA-Z ])", "$2");
    return text;
  }


  public List<ImagePosition> extractImagePosition(PDDocument document) {

    AtomicInteger pageHeight = new AtomicInteger();
    AtomicInteger pageWidth = new AtomicInteger();
    List<ImagePosition> imagePositions = new ArrayList<>();
    AtomicInteger lastPosition = new AtomicInteger(-1);


    for (int i = 0; i < document.getNumberOfPages(); i++) {
      PDPage page = document.getPage(i);

       // Dive 2 is the umbral to accept the image
      pageHeight.set((int) (page.getMediaBox().getHeight() / 1.5 ));
      pageWidth.set((int)  (page.getMediaBox().getWidth() / 1.5));

      try {
        // The images are extract and filter by size
        List<RenderedImage> extract = getImagesFromResources(page.getResources());
        extract = extract.stream()
                          .filter( image -> image.getHeight() > pageHeight.get() &&
                                            image.getWidth()  > pageWidth.get())
                          .toList();

        if(extract.isEmpty())
          continue;

        // This section allow to add the image after last phrase of last page (Before image)
        PDFTextStripper stripper = new PDFTextStripper();
        int pageNumber = (i == 0) ? 0 : i;
        stripper.setStartPage(pageNumber);
        stripper.setEndPage(pageNumber);
        String textPage = stripper.getText(document);
        String positionPhrase = getPositionPhrase(textPage);

        imagePositions.addAll(
            extract.stream()
                   .map(image -> { lastPosition.set(lastPosition.get()+1);
                                   return ImagePosition.builder()
                                                       .image(ImagesUtils.getByteArray(image))
                                                       .phrasePosition(positionPhrase)
                                                       .lastPosition(imagePositions.size() + lastPosition.get())
                                                       .build();
                                  }
                   )
                   .toList()
        );

      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      lastPosition.set(-1);
    }

    return imagePositions;
  }

  private List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
    List<RenderedImage> images = new ArrayList<>();

    for (COSName xObjectName : resources.getXObjectNames()) {
      PDXObject xObject = resources.getXObject(xObjectName);

      if (xObject instanceof PDFormXObject) {
        images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
      } else if (xObject instanceof PDImageXObject) {
        images.add(((PDImageXObject) xObject).getImage());
      }
    }

    return images;
  }

  private String getPositionPhrase(String textPage) {
    textPage = cleanText(textPage);

    if(textPage.isEmpty())
      return "";
    if(textPage.length() > CHARACTER_PHRASE_POSITION){
      String[] subString = textPage.substring(textPage.length()-CHARACTER_PHRASE_POSITION).split(SPLIT_DELETE_CHARACTERS);
      return (subString.length == 1) ? subString[0] : getBigger(subString);
    }

    return textPage;
  }

  private String getBigger(String[] subString) {
    int bigger = 0;

    for (int i = 0; i < subString.length; i++) {
      if (subString[i].length() >= subString[bigger].length())
        bigger = i;
    }

    return subString[bigger];
  }
}
