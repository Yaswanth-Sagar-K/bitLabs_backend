package com.talentstream.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.service.StorageService;

@RestController
@RequestMapping("/file")
public class StorageController {

	@Autowired
	private StorageService service;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file){
		
		return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
		
	}
	
	@GetMapping("/videos/{folderName}")
	public ResponseEntity<List<String>> getVideos(@PathVariable String folderName) {
        List<String> videos = service.getAllVideoFiles(folderName);
        return ResponseEntity.ok(videos);
    }
  
	
	@GetMapping("/{fileName}")
	public ResponseEntity<byte[]> streamFile(@PathVariable String fileName) {
		System.out.println("Fetching from S3: " + fileName);

	    byte[] content = service.getFile(fileName);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); 
	    headers.setContentLength(content.length);
	    headers.setContentDisposition(ContentDisposition.inline().filename(fileName).build());

	    return new ResponseEntity<>(content, headers, HttpStatus.OK);
	}

	
	
	 @GetMapping("/allImages")
    public ResponseEntity<List<String>> getAllImages() {
        List<String> images = service.listImageUrls();
        return ResponseEntity.ok(images);
    }
	 
	  @GetMapping("/allVideos")
	    public ResponseEntity<List<String>> getAllVideos() {
	        List<String> images = service.listVideoUrls();
	        return ResponseEntity.ok(images);
	    }

	
}
