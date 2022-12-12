package pro.sky.teamproject.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.teamproject.entity.UserReport;
import pro.sky.teamproject.entity.UserReportPhoto;
import pro.sky.teamproject.repository.UsersReportPhotoRepository;
import pro.sky.teamproject.repository.UsersReportRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class UserReportPhotoService {

    private final UsersReportRepository usersReportRepository;
    private final UsersReportPhotoRepository usersReportPhotoRepository;

    @Value("${reports.photo.dir.path}")
    private String photoDir;

    public UserReportPhotoService(UsersReportRepository usersReportRepository, UsersReportPhotoRepository usersReportPhotoRepository) {
        this.usersReportRepository = usersReportRepository;
        this.usersReportPhotoRepository = usersReportPhotoRepository;
    }

    public void uploadPhoto(Long reportId, MultipartFile file) throws IOException {
        Optional<UserReport> userReport = usersReportRepository.findUserReportById(reportId);

        Path filePath = Path.of(photoDir, reportId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        UserReportPhoto userReportPhoto = findUserReportPhoto(reportId);
        userReportPhoto.setUserReportId(reportId);
        userReportPhoto.setReportPhotoFilepath(filePath.toString());
        userReportPhoto.setReportPhotoFilesize(file.getSize());
        userReportPhoto.setReportPhotoMediaType(file.getContentType());

        usersReportPhotoRepository.save(userReportPhoto);
    }

    public UserReportPhoto findUserReportPhoto(Long reportId) {
        Optional<UserReportPhoto> userReportPhotosByUserReportId = Optional.ofNullable(usersReportPhotoRepository.findUserReportPhotosByUserReportId(reportId));
        return userReportPhotosByUserReportId.orElse(new UserReportPhoto());
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
}
