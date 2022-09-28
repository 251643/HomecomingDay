package com.homecomingday.service.s3;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import org.imgscalr.Scalr;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.homecomingday.controller.S3Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

//    public S3Dto upload(MultipartFile multipartFile) throws IOException {
//        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
//        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
//        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
//
//
//        File resizingFile = resizeMainImage(multipartFile,fileName,fileFormatName,1).orElseThrow(() -> new io.jsonwebtoken.io.IOException("변환실패"));
//
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resizingFile));
//        removeNewFile(resizingFile);
//        return new S3Dto(fileName, uploadImageUrl);
//    }
//
//
//
//
//    //프로필 이미지 업로드
//    public String upload1(MultipartFile multipartFile) throws IOException {
//        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
//        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
//        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
//
//        File resizingFile = resizeMainImage(multipartFile,fileName,fileFormatName,2).orElseThrow(() -> new io.jsonwebtoken.io.IOException("변환실패"));
//
//
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resizingFile));
//        removeNewFile(resizingFile);
//        return  uploadImageUrl;
//    }
//
//
//    //게시글/프로필 이미지 리사이즈
//    private Optional<File> resizeMainImage(MultipartFile originalImage,String fileName,String fileFormatName,Integer checkNum) throws IOException {
//
//        //요청 파일로 BufferedImage 객체를 생성 => MultipartFile에서 Buffered로 바꾸어 가공가능하도록
//        BufferedImage srcImg = ImageIO.read(originalImage.getInputStream());
//
//        int demandWidth;
//        int demandHeight;
//        // 줄이려고 하는 이미지 크기
//        if(checkNum==1){//checkNum이 1이면 메인게시물
//            demandWidth = 330;
//            demandHeight = 250;
//        }else { //checkNum이 2면 프로필이미지로 사이즈 조정
//            demandWidth = 160;
//            demandHeight = 160;
//        }
//        int originWidth = srcImg.getWidth();
//        int originHeight = srcImg.getHeight();
//
//        // 원본 너비를 기준으로 하여 이미지의 비율로 높이를 계산
//        int newWidth;
//        int newHeight;
//
//        // 원본 넓이가 더 작을경우 리사이징 안함.
//        if (demandWidth > originWidth&&demandHeight > originHeight ) {
//            newWidth = originWidth;
//            newHeight = originHeight;
//        }else {
//            newWidth = demandWidth;
//            newHeight = (demandWidth * originHeight) / originWidth;
//           // newHeigh = demandHeight; 차후 속도체크후 결정
//        }
//
//        // 이미지 기본 너비 높이 설정값으로 변경
//        BufferedImage destImg = Scalr.resize(srcImg, newWidth, newHeight);
//
//        // 이미지 저장
//        File resizedImage = new File(fileName);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(destImg, fileFormatName.toUpperCase(), resizedImage);
//        baos.flush();
//        destImg.flush();
//
//        return new MultipartFile(fileName,baos.toByteArray());
//
////        if(resizedImage.createNewFile()) {
////            ImageIO.write(destImg, fileFormatName.toUpperCase(), resizedImage);
////            return Optional.of(resizedImage);
////        }
////        return Optional.empty();
//    }
//
//
//
//
//    private void removeNewFile(File targetFile) {
//        if (targetFile.delete()) {
//            log.info("파일이 삭제되었습니다.");
//        } else {
//            log.info("파일이 삭제되지 못했습니다.");
//        }
//    }
public String upload1(MultipartFile multipartFile) throws IOException {

    String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
    String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
    File newFile = resizeImage(multipartFile, fileName, fileFormatName,2);
    String uploadImageUrl = putS3(newFile, fileName);
    removeNewFile(newFile);     // File 생성시 로컬에 저장되는 파일 삭제

    return uploadImageUrl;
}

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }
    public S3Dto upload(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        File resizingFile = resizeImage(multipartFile,fileName,fileFormatName,1);

        amazonS3Client.putObject(new com.amazonaws.services.s3.model.PutObjectRequest(bucket, fileName, resizingFile));
        removeNewFile(resizingFile);
        return new S3Dto(fileName, uploadImageUrl);
    }

//    Scalr 라이브러리로 Cropping 및 Resizing

    private File resizeImage(MultipartFile originalImage, String fileName, String fileFormatName,Integer checkNum) throws IOException {

        // 요청 받은 파일로 부터 BufferedImage 객체를 생성합니다.
        BufferedImage srcImg = ImageIO.read(originalImage.getInputStream());

        // 썸네일의 너비와 높이 입니다.

        int demandWidth;
        int demandHeight;
        // 줄이려고 하는 이미지 크기
        if(checkNum==1){//checkNum이 1이면 메인게시물
            demandWidth = 330;
            demandHeight = 250;
        }else { //checkNum이 2면 프로필이미지로 사이즈 조정
            demandWidth = 160;
            demandHeight = 160;
        }
        int originWidth = srcImg.getWidth();
        int originHeight = srcImg.getHeight();

        // 원본 너비를 기준으로 하여 이미지의 비율로 높이를 계산
        int newWidth;
        int newHeight;

        // 원본 넓이가 더 작을경우 리사이징 안함.
        if (demandWidth > originWidth&&demandHeight > originHeight ) {
            newWidth = originWidth;
            newHeight = originHeight;
        }else {
            newWidth = demandWidth;
            newHeight = (demandWidth * originHeight) / originWidth;
            // newHeigh = demandHeight; 차후 속도체크후 결정
        }
        // crop 된 이미지로 썸네일을 생성합니다.
        BufferedImage destImg = Scalr.resize(srcImg, demandWidth, demandHeight);

        // 썸네일을 저장합니다.
        File resizedImage = new File(fileName);

        ImageIO.write(destImg, fileFormatName.toUpperCase(), resizedImage);
        return resizedImage;
    }


}
