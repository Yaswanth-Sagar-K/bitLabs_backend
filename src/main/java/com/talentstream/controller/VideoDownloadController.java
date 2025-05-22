package com.talentstream.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.service.VideoDownloadService;

@RestController
@RequestMapping("/api/video")
public class VideoDownloadController {

    @Autowired
    private VideoDownloadService videoDownloadService;
    

    @PostMapping("/download")
    public String downloadAndUpload(@RequestParam String url) {
        String outputDir = "downloads"; 
        return videoDownloadService.downloadAndUploadVideo(url, outputDir);
    }
  
}

