package com.njustc.onlinebiz.sample.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import org.springframework.stereotype.Service;

@Service
public class DefaultSampleService implements SampleService {
  // TODO: 实现接口方法
  @Override
  public String createSampleCollection(String entrustId, Long userId, Role userRole) {
    return null;
  }

  @Override
  public SampleCollection findSampleCollection(
      String sampleCollectionId, Long userId, Role userRole) {
    return null;
  }

  @Override
  public void updateSampleCollection(
      String sampleCollectionId, SampleCollection sampleCollection, Long userId, Role userRole) {}

  @Override
  public void confirmSampleCollection(String sampleCollectionId, Long userId, Role userRole) {}

  @Override
  public void notConfirmSampleCollection(String sampleCollectionId, Long userId, Role userRole) {}
}
