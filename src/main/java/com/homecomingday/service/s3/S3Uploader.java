package com.homecomingday.service.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.homecomingday.controller.S3Dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class S3Uploader {


    private final AmazonS3 amazonS3;

    public S3Uploader(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public S3Dto upload(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> 파일 변환 실패"));

        return uploadToS3(uploadFile);
    }

    // S3로 파일 업로드하기
    @Transactional
    public S3Dto uploadToS3(File uploadFile) throws IOException {

        String fileName = UUID.randomUUID() + uploadFile.getName();   // S3에 저장될 파일 이름
        //이미지 리사이징
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
//        String resizeFileName=resizeImageFile(uploadImageUrl,fileName);

        removeNewFile(uploadFile);
        S3Dto s3Dto = new S3Dto(fileName, uploadImageUrl);


        return s3Dto;
    }

    public String upload1(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> 파일 변환 실패"));

        return uploadToS31(uploadFile);
    }

    // S3로 파일 업로드하기
    @Transactional
    public String uploadToS31(File uploadFile) throws IOException {

        String fileName = UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);


        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());     // 현재 프로젝트 절대경로
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
                return Optional.of(convertFile);
            }

        }
        return null;
    }


    //이미지 리사이징하기
    private String resizeImageFile(String path, String resizedName) throws Exception {
        Image originalImage = ImageIO.read(new File(path, resizedName));

        int originWidth = originalImage.getWidth(null);
        int originHeight = originalImage.getHeight(null);

        int newWidth = 1600;

        if (originWidth > newWidth) {
            int newHeight = (originHeight * newWidth) / originWidth;

            Image resizeImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizeImage, 0, 0, null);
            graphics.dispose();

            String resizeImgName = "resize_" +resizedName;
            File newFile = new File(path + File.separator + resizeImgName);
            String formatName = resizedName.substring(resizedName.lastIndexOf(".") + 1);
            ImageIO.write(newImage, formatName.toLowerCase(), newFile);

            return resizeImgName;
        } else {
            return resizedName;
        }
    }

}
