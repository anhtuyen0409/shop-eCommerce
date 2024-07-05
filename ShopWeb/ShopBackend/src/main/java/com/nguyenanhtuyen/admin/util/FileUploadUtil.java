package com.nguyenanhtuyen.admin.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;


import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName, ex);
		}
	}
	
	// remove the old user's photo after update new photo
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		try {
			Files.list(dirPath).forEach(file -> {
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException ex) {
						System.out.println("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException ex) {
			System.out.println("Could not list directory: " + dirPath);
		}
	}
	
	// remove category's image folder
	public static void removeDir(String dir) {
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (Exception ex) {
			System.out.println("Could not remove directory: " + dir);
		}
	}
}
