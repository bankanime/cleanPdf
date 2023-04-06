package com.andemar.cleanpdf.controller;

import static com.andemar.cleanpdf.util.Headers.contentPdfHeaders;
import static com.andemar.cleanpdf.util.Utils.allArgsExist;

import com.andemar.cleanpdf.model.Dimensions;
import com.andemar.cleanpdf.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/information")
public class InformationController {

  private final InformationService informationService;

  @Autowired
  public InformationController(InformationService informationService) {
    this.informationService = informationService;
  }

  @PostMapping("/pdf/dimensions/{pageNumber}")
  public ResponseEntity<Dimensions> getDimensions(@RequestParam("file") MultipartFile file, @PathVariable("pageNumber") int pageNumber) {
    return new ResponseEntity<>(informationService.getDimensions(file, pageNumber), HttpStatus.OK);
  }

  @PostMapping("/pdf/check")
  public ResponseEntity<byte[]> cleanUpload(@RequestParam(value = "file") MultipartFile file,
                                            @RequestParam("pageNumber") int pageNumber,
                                            @RequestParam(value = "x", required = false) Float x,
                                            @RequestParam(value = "y", required = false) Float y,
                                            @RequestParam(value = "height", required = false) Float height,
                                            @RequestParam(value = "width",  required = false) Float width) {
    String fileName = "clean_"+file.getName()+".pdf";
    Dimensions rectangle = null;
    if (allArgsExist(x, y, height, width)) {
      rectangle = Dimensions.builder().x(x).y(y).height(height).width(width).build();
    }

    return new ResponseEntity<>(informationService.check(file, pageNumber-1, rectangle), contentPdfHeaders(fileName), HttpStatus.OK);
  }
}
