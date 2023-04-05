package com.andemar.cleanpdf.controller;

import static com.andemar.cleanpdf.util.Headers.contentPdfHeaders;
import static com.andemar.cleanpdf.util.Utils.*;

import com.andemar.cleanpdf.model.Dimensions;
import com.andemar.cleanpdf.service.CleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/clean")
public class CleanController {

    private final CleanService cleanService;

    @Autowired
    public CleanController(CleanService cleanService) {
        this.cleanService = cleanService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> cleanUpload(  @RequestParam(value = "file") MultipartFile file,
                                                @RequestHeader(value = "x", required = false) Float x,
                                                @RequestHeader(value = "y", required = false) Float y,
                                                @RequestHeader(value = "height", required = false) Float height,
                                                @RequestHeader(value = "width",  required = false) Float width) {
        String fileName = "clean_"+file.getName()+".pdf";
        Dimensions rectangle = null;
        if (allArgsExist(x, y, height, width)) {
             rectangle = Dimensions.builder().x(x).y(y).height(height).width(width).build();
        }

        return new ResponseEntity<>(cleanService.cleanFile(file, rectangle), contentPdfHeaders(fileName), HttpStatus.OK);
    }
}
