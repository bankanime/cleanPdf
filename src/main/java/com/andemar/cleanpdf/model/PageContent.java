package com.andemar.cleanpdf.model;

import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageContent {

  private Paragraph text;
  private Image image;

  public boolean isImage() {
    return image != null;
  }
}
