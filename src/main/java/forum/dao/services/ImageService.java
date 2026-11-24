package forum.dao.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import jakarta.annotation.PostConstruct;

@Service 
public class ImageService {

    @Value("${storage.local-path:./uploads}")
    private Path storagePath;

    @PostConstruct
    public void init() {

        if (storagePath == null) {
            storagePath = Path.of("./uploads");
        }

        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }
    
    public String save(MultipartFile file, String path) {
        try {
            String fileName = file.getOriginalFilename();
            Path destination = storagePath.resolve(path + "/" + fileName).normalize();
            Files.createDirectories(destination.getParent());
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return path + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public String getImage(String path) {

        return "/uploads/defaultPfp.png";

        // if (storagePath == null) {
        //     return "/uploads/defaultPfp.png";
        // }

        // Path file = storagePath.resolve(path).normalize();

        // if (!file.startsWith(storagePath)) {
        //     throw new IllegalArgumentException("Invalid image path");
        // }

        // if (!Files.exists(file) || !Files.isReadable(file)) {
        //     return "/uploads/defaultPfp.png"; // or null — handle in template
        // }

        // // Return the public URL served by WebConfig's /uploads/** handler
        // return "/uploads/" + path;
    }

    public byte[] download(String path) {
        try {
            return Files.readAllBytes(storagePath.resolve(path).normalize());
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public Resource loadAsResource(String path) {
        try {
            Path file = storagePath.resolve(path).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file", e);
        }
        throw new RuntimeException("Failed to load file");
    }

    public void delete(String path) {
        try {
            Files.delete(storagePath.resolve(path).normalize());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public void upload(MultipartFile file, String path) {
        save(file, path);
    } // returns the stored filename/path
}
