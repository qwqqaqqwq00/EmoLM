package hku.hk.EmoLM.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private static final String UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads/").toString();
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public FileService() {
        // Initialize upload directory
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new RuntimeException("Cannot create upload directory: " + UPLOAD_DIR);
            }
        }
    }

    public String saveFile(MultipartFile file) {
        validateFile(file);

        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath =Paths.get(UPLOAD_DIR, uniqueFileName).toString();

        try {
            System.out.println("Saving file to: " + filePath);
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("File save failed", e);
        }

        return filePath;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size cannot exceed 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
            throw new IllegalArgumentException("Only image and video files are supported");
        }
    }

    public void deleteExpiredFiles() {
        File uploadDir = new File(UPLOAD_DIR);
        long currentTime = System.currentTimeMillis();

        for (File file : Objects.requireNonNull(uploadDir.listFiles())) {
            if (currentTime - file.lastModified() > 24 * 60 * 60 * 1000) { // Over 24 hours
                file.deleteOnExit();
            }
        }
    }
}