package com.Jakima_Estate.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;

    private static final long MAX_VIDEO_SIZE = 50 * 1024 * 1024;

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif",
            "image/webp", "image/bmp", "image/tiff", "image/svg+xml",
            "image/heic", "image/heif", "image/avif"
    );

    private static final List<String> ALLOWED_VIDEO_TYPES = List.of(
            "video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo",
            "video/x-ms-wmv", "video/webm", "video/3gpp"
    );

    public String uploadFile(MultipartFile file) {
        validateFile(file);
        try {
            String resourceType = determineResourceType(file.getContentType());
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "jakima_properties",
                    "resource_type", resourceType
            ));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public String uploadVideo(MultipartFile file) {
        if (file.getSize() > MAX_VIDEO_SIZE) {
            throw new RuntimeException("Video file exceeds the 50MB limit");
        }
        if (!ALLOWED_VIDEO_TYPES.contains(file.getContentType())) {
            throw new RuntimeException("Unsupported video type: " + file.getContentType());
        }
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "jakima_properties/videos",
                    "resource_type", "video"
            ));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload video: " + e.getMessage());
        }
    }

    public List<String> uploadMultipleFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFile(file));
        }
        return urls;
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new RuntimeException("Could not determine file type");
        }
        if (!ALLOWED_IMAGE_TYPES.contains(contentType) && !ALLOWED_VIDEO_TYPES.contains(contentType)) {
            throw new RuntimeException("Unsupported file type: " + contentType);
        }
        if (ALLOWED_VIDEO_TYPES.contains(contentType) && file.getSize() > MAX_VIDEO_SIZE) {
            throw new RuntimeException("Video file exceeds the 50MB limit");
        }
    }

    private String determineResourceType(String contentType) {
        if (contentType != null && contentType.startsWith("video/")) {
            return "video";
        }
        return "image";
    }
}