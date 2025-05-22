package com.talentstream.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

@Service
public class StorageService {

	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Autowired
	private AmazonS3 s3Client;
	
	private File convertMultipartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try(FileOutputStream fos = new FileOutputStream(convertedFile)){
			fos.write(file.getBytes());
		}catch (IOException e) {
			System.out.println(e);
		}
		return convertedFile;
	}
	
	
	public String uploadFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		File fileObj = convertMultipartFileToFile(file);
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
		fileObj.delete();
		return fileName;
	}
	


	public byte[] getFile(String fileName) {
	    try {
	        S3Object s3Object = s3Client.getObject(bucketName, fileName);
	        try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
	            return IOUtils.toByteArray(inputStream);
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("Error reading file from S3", e);
	    }
	}

	
	
	private boolean isVideoFile(String key) {
	    String lowerKey = key.toLowerCase();
	    return lowerKey.endsWith(".mp4") || lowerKey.endsWith(".mov") ||
	           lowerKey.endsWith(".avi") || lowerKey.endsWith(".mkv") ||
	           lowerKey.endsWith(".flv") || lowerKey.endsWith(".wmv") ||
	           lowerKey.endsWith(".webm");
	}
	
//	get video file urls in sub folders of a given folder
	public List<String> getAllVideoFiles(String folderPrefix) {
        List<String> videoFiles = new ArrayList<>();
        String normalizedPrefix = folderPrefix.endsWith("/") ? folderPrefix : folderPrefix + "/";

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(normalizedPrefix);

        ListObjectsV2Result result;

        do {
            result = s3Client.listObjectsV2(request);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                String key = objectSummary.getKey();
                if (isVideoFile(key)) {
                    videoFiles.add(key);
                }
            }
            // continue if truncated
            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return videoFiles;
    }

//	get video urls from main folder
	 public List<String> listVideoUrls() {
	        ListObjectsV2Request request = new ListObjectsV2Request()
	                .withBucketName(bucketName);

	        ListObjectsV2Result result = s3Client.listObjectsV2(request);

	        return result.getObjectSummaries().stream()
	                .map(S3ObjectSummary::getKey) 
	                .filter(this::isVideoFile)
	                .map(this::generateFileUrl) 
	                .collect(Collectors.toList());
	    }

	    private boolean isImageFile(String key) {
	        String lowerKey = key.toLowerCase();
	        return lowerKey.endsWith(".jpg") || lowerKey.endsWith(".jpeg") ||
	               lowerKey.endsWith(".png") || lowerKey.endsWith(".gif") ||
	               lowerKey.endsWith(".bmp") || lowerKey.endsWith(".webp");
	    }
	 public List<String> listImageUrls() {
	        ListObjectsV2Request request = new ListObjectsV2Request()
	                .withBucketName(bucketName);

	        ListObjectsV2Result result = s3Client.listObjectsV2(request);

	        return result.getObjectSummaries().stream()
	                .map(S3ObjectSummary::getKey) 
	                .filter(this::isImageFile)
	                .map(this::generateFileUrl) 
	                .collect(Collectors.toList());
	    }

	    private String generateFileUrl(String key) {
	        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
	    }
	
	
}
