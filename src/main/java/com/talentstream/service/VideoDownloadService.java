package com.talentstream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import java.util.UUID;
import org.apache.commons.io.FileUtils;

import java.io.*;

@Service
public class VideoDownloadService {

	@Autowired
	private StorageService service;

	@Value("${yt-dlp.path}")
	private String ytDlpPath;

	public String downloadAndUploadVideo(String videoUrl, String baseDirectory) {
		String uniqueDirName = "video_" + UUID.randomUUID();
		File tempDir = new File(baseDirectory, uniqueDirName);

		try {
			if (!tempDir.exists())
				tempDir.mkdirs();

			ProcessBuilder pb = new ProcessBuilder(
					ytDlpPath, 
					"--no-cache-dir", 
					"--force-overwrites", 
					"--no-part", 
					"-f",
					"bv*+ba/best[ext=mp4]",
					"-o", 
					tempDir.getAbsolutePath() + "/%(title)s.%(ext)s", videoUrl);
			pb.redirectErrorStream(true);
			Process process = pb.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			int exitCode = process.waitFor();
			if (exitCode != 0) {
				return "Download failed with exit code: " + exitCode;
			}

			File downloadedFile = getLatestMp4File(tempDir);
			if (downloadedFile == null)
				return "No video file found.";

			// Sanitize filename
			String originalName = downloadedFile.getName();
			String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
			String extension = originalName.substring(originalName.lastIndexOf('.'));
			String safeFileName = baseName.replaceAll("[^a-zA-Z0-9]", "_") + extension;

			File sanitizedFile = new File(downloadedFile.getParent(), safeFileName);
			if (!downloadedFile.renameTo(sanitizedFile)) {
				return "Failed to sanitize filename.";
			}

			MultipartFile multipartFile = fileToMultipartFile(sanitizedFile);
			String uploadedFileName = service.uploadFile(multipartFile);

			return "Uploaded to S3: " + uploadedFileName;

		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();

		} finally {
			// Clean up the whole temp directory
			try {
				FileUtils.deleteDirectory(tempDir);
			} catch (IOException e) {
				System.err.println("Failed to delete temp directory: " + tempDir.getAbsolutePath());
			}
		}
	}

	private File getLatestMp4File(File dir) {
		File[] files = dir.listFiles((d, name) -> name.endsWith(".mp4"));
		if (files == null || files.length == 0)
			return null;

		File latest = files[0];
		for (File file : files) {
			if (file.lastModified() > latest.lastModified()) {
				latest = file;
			}
		}
		return latest;
	}

	private MultipartFile fileToMultipartFile(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return new MockMultipartFile(file.getName(), file.getName(), "video/mp4", fis);
		}
	}
}
