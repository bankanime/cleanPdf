package com.andemar.cleanpdf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dimensions {
  private Float x;
  private Float y;
  private Float width;
  private Float height;

  public boolean exist() {
    return x != null && y != null && width != null && height != null;
  }
}
