package com.andemar.cleanpdf.controller;

import com.andemar.cleanpdf.service.CleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<byte[]> cleanUpload(@RequestParam("file") MultipartFile file) {
        String fileName = "clean_"+file.getName()+".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        byte[] newFile = cleanService.cleanFile(file);

        return new ResponseEntity<>(newFile, headers, HttpStatus.OK);
    }
}
