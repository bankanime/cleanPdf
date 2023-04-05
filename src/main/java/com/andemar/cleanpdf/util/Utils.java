package com.andemar.cleanpdf.util;

import java.util.Arrays;

public class Utils {

  public static boolean allArgsExist(Float... args) {
    return !Arrays.stream(args).allMatch(arg -> arg == null || arg.isNaN());
  }

}
