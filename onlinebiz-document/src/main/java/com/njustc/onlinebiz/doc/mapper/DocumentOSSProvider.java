package com.njustc.onlinebiz.doc.mapper;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;
import java.util.Calendar;

public class DocumentOSSProvider {
  AmazonS3 space;

  public DocumentOSSProvider() {
    // 实例化一个client
    AWSCredentialsProvider credentialsProvider =
        new AWSStaticCredentialsProvider(
            new BasicAWSCredentials(
                "ZTCFDGSJ4SF2T5DRZ6PU", "4nFXpWy8meu/tI+bz4zVxXWW5dbk2OoPeqLsU0Rfw+Q"));
    space =
        AmazonS3ClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                    "https://sgp1.digitaloceanspaces.com", "sgp1"))
            .build();
  }

  /*
  将一个任意大小的PDF文档上传到OSS，返回一个可以直接下载PDF的signed URL
  fileName: 文件名
  content: PDF文件的byte数组
   */
  public String uploadDocument(String fileName, byte[] content) {
    InputStream inputStream = new java.io.ByteArrayInputStream(content);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(content.length);
    metadata.setContentType("application/pdf");

    space.putObject("mcdse", fileName, inputStream, metadata);

    // 设置过期时间为一天
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new java.util.Date());
    calendar.add(Calendar.DATE, 1);

    return space.generatePresignedUrl("mcdse", fileName, calendar.getTime()).toString();
  }
}
