package com.andemar.cleanpdf.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PdfContent {

  private StringBuilder content;
  private String flatContent;
  private List<ImagePosition> images;
}
