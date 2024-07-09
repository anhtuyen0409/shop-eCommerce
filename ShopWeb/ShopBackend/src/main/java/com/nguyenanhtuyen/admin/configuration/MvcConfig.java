package com.nguyenanhtuyen.admin.configuration;

import java.nio.file.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// user photos
		String dirName = "user-photos";
		Path userPhotosDir = Paths.get(dirName);
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + userPhotosPath + "/");
		
		// category images
		String categoryImagesDirName = "../category-images";
		Path categoryImagesDir = Paths.get(categoryImagesDirName);
		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/category-images/**").addResourceLocations("file:/" + categoryImagesPath + "/");
		
		// brand logos
		String brandLogosDirName = "../brand-logos";
		Path brandLogosDir = Paths.get(brandLogosDirName);
		String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/" + brandLogosPath + "/");
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

}
