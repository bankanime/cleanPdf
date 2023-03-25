package com.andemar.cleanpdf.util;

import javax.imageio.ImageIO;

import java.awt.image.RenderedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImagesUtils {

  private static final String FORMAT_NAME = "jpeg";

  public static byte[] getByteArray(RenderedImage image) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(image, FORMAT_NAME, byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
