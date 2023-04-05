package com.andemar.cleanpdf.controller;

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
}
