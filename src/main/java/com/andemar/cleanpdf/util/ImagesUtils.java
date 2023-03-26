package com.andemar.cleanpdf.util;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagesUtils {

  private ImagesUtils() {}

  private static final String FORMAT_JPEG = "jpeg";
  private static final String FORMAT_PNG = "png";

  public static byte[] getByteArray(RenderedImage image) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(image, getType(image), byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getType(RenderedImage image) {
    int typeNumber = -1;
    String type = "";
    if (image instanceof BufferedImage) {
      typeNumber = ((BufferedImage) image).getType();
    }

    switch (typeNumber){
      case 2:
        type = FORMAT_PNG;
        break;
      default:
        type = FORMAT_JPEG;
    }

    return type;
  }

}
