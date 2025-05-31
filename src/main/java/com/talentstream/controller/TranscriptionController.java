package com.talentstream.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.service.TranscriptionService;

@RestController
@RequestMapping("/api/transcribe")
public class TranscriptionController {

    @Autowired
    private TranscriptionService transcriptionService;

    @PostMapping("/upload")
    public ResponseEntity<?> transcribeAudio(@RequestParam("audio") MultipartFile audioFile) {
        try {
            String transcribedText = transcriptionService.transcribe(audioFile);
            return ResponseEntity.ok().body(transcribedText);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

