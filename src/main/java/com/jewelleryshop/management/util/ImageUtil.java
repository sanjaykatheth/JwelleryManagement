package com.jewelleryshop.management.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUtil {

	@Value("${image.path}")
	private String imagePathPrefix;

	public String saveImagePath(MultipartFile file, String imageId) {
		Path uploadPath = Paths.get(imagePathPrefix);
		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String originalFileName = file.getOriginalFilename();
		if (originalFileName == null) {
			throw new IllegalArgumentException("File name cannot be null");
		}
		String fileExtension = getFileExtension(originalFileName);
		String fileName = imageId + "-" + originalFileName;
		Path filePath = uploadPath.resolve(fileName);

		try (InputStream inputStream = file.getInputStream()) {
			BufferedImage image = ImageIO.read(inputStream);
			if (image != null) {
				if (!ImageIO.write(image, fileExtension, filePath.toFile())) {
					throw new IllegalArgumentException("Unsupported image format: " + fileExtension);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return fileName;
	}

	private String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex >= 0) {
			return fileName.substring(lastDotIndex + 1).toLowerCase();
		} else {
			throw new IllegalArgumentException("File name does not have an extension");
		}
	}
}
