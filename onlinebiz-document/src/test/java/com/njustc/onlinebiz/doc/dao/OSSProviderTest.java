package com.njustc.onlinebiz.doc.dao;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OSSProviderTest {

  @Test
  void uploadFileSuccess() {
    // prepare some random byte[] data
    byte[] data = new byte[1024];
    // random file name
    String fileName = "test-" + System.currentTimeMillis() + ".txt";
    Assertions.assertDoesNotThrow(
        () -> {
          OSSProvider ossProvider = new OSSProvider();
          ossProvider.upload("unit-test", fileName, data, "plain/text");
        });
    Assertions.assertDoesNotThrow(
        () -> {
          MinioClient minioClient =
              MinioClient.builder()
                  .endpoint("https://oss.syh1en.asia")
                  .credentials("spring", "123456Spr1ng")
                  .build();
          // make sure the file is uploaded
          minioClient.getObject(
              GetObjectArgs.builder().bucket("unit-test").object(fileName).build());
        });
  }

  @Test
  void uploadFileToNonexistentBucket() {
    // random bucket name
    String bucketName = "unit-test-bucket-" + System.currentTimeMillis();
    // prepare some random byte[] data
    byte[] data = new byte[1024];
    Assertions.assertDoesNotThrow(
        () -> {
          OSSProvider ossProvider = new OSSProvider();
          ossProvider.upload(bucketName, "unit_test", data, "plain/text");
        });
    // delete the bucket
    Assertions.assertDoesNotThrow(
        () -> {
          MinioClient minioClient =
              MinioClient.builder()
                  .endpoint("https://oss.syh1en.asia")
                  .credentials("spring", "123456Spr1ng")
                  .build();
          minioClient.removeObject(
              RemoveObjectArgs.builder().bucket(bucketName).object("unit_test").build());
          minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        });
  }
}
