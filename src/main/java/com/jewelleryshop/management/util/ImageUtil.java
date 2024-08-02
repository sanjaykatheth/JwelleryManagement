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

	public String saveFile(MultipartFile file, String imageId) {
		Path uploadPath = Paths.get(imagePathPrefix);
		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (InputStream inputStream = file.getInputStream()) {
			BufferedImage jfifImage = ImageIO.read(inputStream);
			if (jfifImage != null) {
				Path filePath = uploadPath.resolve(imageId +"-"+ file.getName() + ".gif");
				ImageIO.write(jfifImage, "gif", filePath.toFile());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return imageId;
	}
}