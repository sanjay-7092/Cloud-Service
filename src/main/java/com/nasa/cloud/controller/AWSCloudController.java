package com.nasa.cloud.controller;

import com.nasa.cloud.service.AWSCloudService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/aws/s3")
public class AWSCloudController {

    private final AWSCloudService awsCloudService;

    AWSCloudController(AWSCloudService awsCloudService){
        this.awsCloudService=awsCloudService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(awsCloudService.uploadFile(file));
    }

    @GetMapping("/get-file/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName){
        return ResponseEntity.ok(awsCloudService.downloadFile(fileName));
    }

    @GetMapping("/list-files")
    public ResponseEntity<List<String>>  getListFiles(){
        return ResponseEntity.ok(awsCloudService.listFiles());
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return ResponseEntity.ok(awsCloudService.deleteFile(fileName));
    }
}
