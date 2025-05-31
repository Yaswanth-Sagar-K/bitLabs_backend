package com.talentstream.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

@Component
public class AssemblyAIClient {

    @Value("${assembly.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String UPLOAD_URL = "https://api.assemblyai.com/v2/upload";
    private final String TRANSCRIBE_URL = "https://api.assemblyai.com/v2/transcript";

    // Step 1: Upload audio
    public String uploadAudio(MultipartFile audioFile) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        InputStream inputStream = audioFile.getInputStream();
        byte[] fileBytes = StreamUtils.copyToByteArray(inputStream);

        HttpEntity<byte[]> request = new HttpEntity<>(fileBytes, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(UPLOAD_URL, request, Map.class);
        return response.getBody().get("upload_url").toString();
    }

    // Step 2: Start transcription job
    public String startTranscription(String uploadUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"audio_url\": \"" + uploadUrl + "\"}";

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(TRANSCRIBE_URL, request, Map.class);

        return response.getBody().get("id").toString();
    }

    // Step 3: Poll until complete, and then delete uploaded audio
    public String pollTranscription(String transcriptId, String uploadUrl) throws InterruptedException {
        String url = TRANSCRIBE_URL + "/" + transcriptId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        while (true) {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            Map<String, Object> body = response.getBody();
            String status = body.get("status").toString();

            if (status.equals("completed")) {
                // Automatically delete the uploaded file after successful transcription
                deleteAudio(uploadUrl);
                return body.get("text").toString();
            } else if (status.equals("error")) {
                throw new RuntimeException("Transcription failed: " + body.get("error").toString());
            }

            Thread.sleep(2000); // Poll every 2 seconds
        }
    }

    // Step 4: Delete the uploaded audio file
    public void deleteAudio(String uploadUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(uploadUrl, HttpMethod.DELETE, request, Void.class);
    }
}
