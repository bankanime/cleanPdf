package com.andemar.cleanpdf.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImagePosition {
  private byte[] image;
  private String phrasePosition;
  private int lastPosition;

  public boolean hasPosition() {
    return !phrasePosition.isEmpty();
  }
}
