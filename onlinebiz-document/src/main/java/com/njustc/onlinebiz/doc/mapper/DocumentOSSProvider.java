package com.njustc.onlinebiz.doc.mapper;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DocumentOSSProvider {
  MinioClient minioClient;

  public DocumentOSSProvider()
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
          NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
          XmlParserException, InternalException {
    minioClient =
        MinioClient.builder()
            .endpoint("https://oss.syh1en.asia")
            .credentials("spring", "123456M1ni0")
            .build();
    boolean docFound = minioClient.bucketExists(BucketExistsArgs.builder().bucket("doc").build());
    if (!docFound) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket("doc").build());
    }
  }

  public String uploadDocument(String fileName, byte[] fileByte)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
          NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
          XmlParserException, InternalException {
    InputStream fileStream = new ByteArrayInputStream(fileByte);
    minioClient.putObject(
        PutObjectArgs.builder().bucket("doc").object(fileName).stream(
                fileStream, fileByte.length, -1)
            .contentType("application/pdf")
            .build());
    return "https://oss.syh1en.asia/doc/" + fileName;
  }
}
