package com.andemar.cleanpdf.controller;

import com.andemar.cleanpdf.service.CleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private CleanService cleanService;

    public CleanController(@Autowired CleanService cleanService) {
        this.cleanService = cleanService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<String> cleanUpload(@RequestParam("file") MultipartFile file) {

        cleanService.cleanFile(file);

        return new ResponseEntity<>("upload", HttpStatus.ACCEPTED);
    }

}
