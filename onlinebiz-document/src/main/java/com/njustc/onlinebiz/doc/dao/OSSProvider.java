package com.njustc.onlinebiz.doc.dao;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class OSSProvider {
  MinioClient minioClient;

  public OSSProvider()
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
          NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
          XmlParserException, InternalException {
    minioClient =
        MinioClient.builder()
            .endpoint("https://oss.syh1en.asia")
            .credentials("spring", "123456Spr1ng")
            .build();
    boolean docFound = minioClient.bucketExists(BucketExistsArgs.builder().bucket("doc").build());
    if (!docFound) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket("doc").build());
    }
  }

  public boolean upload(String bucketName, String objectName, byte[] data, String contentType) {
    try {
      // 判断桶是否存在，不存在则先创建桶
      if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }
      // 产生输入流
      InputStream inputStream = new ByteArrayInputStream(data);

      // 上传文件
      minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(inputStream, data.length, -1).contentType(contentType).build());
      return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
  }

}
