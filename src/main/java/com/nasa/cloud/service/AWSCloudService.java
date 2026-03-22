package com.nasa.cloud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
public class AWSCloudService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;

    AWSCloudService(S3Client s3Client){
        this.s3Client=s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return fileName;
    }

    public byte[] downloadFile(String fileName){
        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> resposne = s3Client.getObjectAsBytes(getObjectRequest);

        return resposne.asByteArray();
    }

    public List<String> listFiles(){
        ListObjectsRequest request = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();
        ListObjectsResponse response= s3Client.listObjects(request);

        return response
                .contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }
    public String deleteFile(String filename){
        DeleteObjectRequest request = DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        s3Client.deleteObject(request);
        return "File Deleted Successfully";

    }
}
