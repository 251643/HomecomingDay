package com.homecomingday.service.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import org.imgscalr.Scalr;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.homecomingday.controller.S3Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {


    private final AmazonS3Client amazonS3Client;



    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public S3Dto upload(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();


        File resizingFile = resizeMainImage(multipartFile,fileName,fileFormatName,1).orElseThrow(() -> new io.jsonwebtoken.io.IOException("변환실패"));

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resizingFile));
        removeNewFile(resizingFile);
        return new S3Dto(fileName, uploadImageUrl);
    }




    //프로필 이미지 업로드
    public String upload1(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();

        File resizingFile = resizeMainImage(multipartFile,fileName,fileFormatName,2).orElseThrow(() -> new io.jsonwebtoken.io.IOException("변환실패"));


        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resizingFile));
        removeNewFile(resizingFile);
        return  uploadImageUrl;
    }


    //게시글/프로필 이미지 리사이즈
    private Optional<File> resizeMainImage(MultipartFile originalImage,String fileName,String fileFormatName,Integer checkNum) throws IOException {

        //요청 파일로 BufferedImage 객체를 생성 => MultipartFile에서 Buffered로 바꾸어 가공가능하도록
        BufferedImage srcImg = ImageIO.read(originalImage.getInputStream());

        int demandWidth;
        // 줄이려고 하는 이미지 크기
        if(checkNum==1){
            demandWidth = 330;
        }else {
            demandWidth = 160;
        }
        int originWidth = srcImg.getWidth();
        int originHeight = srcImg.getHeight();

        // 원본 너비를 기준으로 하여 이미지의 비율로 높이를 계산
        int newWidth;
        int newHeight;

        // 원본 넓이가 더 작을경우 리사이징 안함.
        if (demandWidth > originWidth) {
            newWidth = originWidth;
            newHeight = originHeight;
        }else {
            newWidth = demandWidth;
            newHeight = (demandWidth * originHeight) / originWidth;
        }

        // 이미지 기본 너비 높이 설정값으로 변경
        BufferedImage destImg = Scalr.resize(srcImg, newWidth, newHeight);

        // 이미지 저장
        File resizedImage = new File(fileName);

        if(resizedImage.createNewFile()) {
            ImageIO.write(destImg, fileFormatName.toUpperCase(), resizedImage);
            return Optional.of(resizedImage);
        }
        return Optional.empty();
    }




    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

}
