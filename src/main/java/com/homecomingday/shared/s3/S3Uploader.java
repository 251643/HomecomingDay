package com.homecomingday.shared.s3;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.imgscalr.Scalr;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.homecomingday.dto.S3Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {


    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름


    //게시글
    public S3Dto upload(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();


        MultipartFile resizingFile = resizeMainImage(multipartFile, fileName, fileFormatName, 1);

        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentLength(resizingFile.getSize());
        objectMetadata.setContentType(resizingFile.getContentType());

//        ObjectMetadata objectMetadata=new ObjectMetadata();
//        objectMetadata.setContentLength(multipartFile.getSize());
//        objectMetadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resizingFile.getInputStream(),objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
//        removeNewFile(new File(Objects.requireNonNull(resizingFile.getOriginalFilename())));


        return new S3Dto(fileName, uploadImageUrl);
    }


    //프로필 이미지 업로드
    public String upload1(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();

        MultipartFile resizingFile = resizeMainImage(multipartFile, fileName, fileFormatName, 2);

        ObjectMetadata objectMetadata=new ObjectMetadata();

        objectMetadata.setContentLength(resizingFile.getSize());
        objectMetadata.setContentType(resizingFile.getContentType());

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resizingFile.getInputStream(),objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));

        return uploadImageUrl;
    }


    //게시글/프로필 이미지 리사이즈
    private MultipartFile resizeMainImage(MultipartFile originalImage, String fileName, String fileFormatName, Integer checkNum) throws IOException {

        //요청 파일로 BufferedImage 객체를 생성 => MultipartFile에서 Buffered로 바꾸어 가공가능하도록
        BufferedImage srcImg = ImageIO.read(originalImage.getInputStream());

        int demandWidth;
        int demandHeight;
        // 줄이려고 하는 이미지 크기
        if (checkNum == 1) {//checkNum이 1이면 메인게시물
            demandWidth = 340;
            demandHeight = 250;

            BufferedImage destImg = Scalr.resize(srcImg, demandWidth, demandHeight);

            // 이미지 저장
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(destImg, fileFormatName.toLowerCase(), baos);
            baos.flush();
            destImg.flush();

            return new MockMultipartFile(fileName, baos.toByteArray());
        } else { //checkNum이 2면 프로필이미지로 사이즈 조정
            demandWidth = 160;
            demandHeight = 160;

        int originWidth = srcImg.getWidth();
        int originHeight = srcImg.getHeight();

        // 원본 너비를 기준으로 하여 이미지의 비율로 높이를 계산
        int newWidth;
        int newHeight;

        // 원본 넓이가 더 작을경우 리사이징 안함.
        if (demandWidth > originWidth && demandHeight > originHeight) {
            newWidth = originWidth;
            newHeight = originHeight;
        } else {
            newWidth = demandWidth;
            newHeight = (demandWidth * originHeight) / originWidth;
            // newHeigh = demandHeight; 차후 속도체크후 결정
        }

        // 이미지 기본 너비 높이 설정값으로 변경
        BufferedImage destImg = Scalr.resize(srcImg, newWidth, newHeight);

            // 이미지 저장
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(destImg, fileFormatName.toLowerCase(), baos);
            baos.flush();
            destImg.flush();

            return new MockMultipartFile(fileName, baos.toByteArray());
        }

    }

}
