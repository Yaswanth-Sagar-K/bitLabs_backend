package com.talentstream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.util.AssemblyAIClient;

@Service
public class TranscriptionService {

    @Autowired
    private AssemblyAIClient assemblyAIClient;

    public String transcribe(MultipartFile audioFile) {
        try {
            // Step 1: Upload audio file to AssemblyAI
            String uploadUrl = assemblyAIClient.uploadAudio(audioFile);

            // Step 2: Start transcription job with uploaded URL
            String transcriptId = assemblyAIClient.startTranscription(uploadUrl);

            // Step 3: Poll transcription status and auto-delete audio after completion
            return assemblyAIClient.pollTranscription(transcriptId, uploadUrl);
        } catch (Exception e) {
            throw new RuntimeException("Transcription failed: " + e.getMessage());
        }
    }
}
