package com.njustc.onlinebiz.sample.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;

public interface SampleService {
  String createSampleCollection(String entrustId, Long userId, Role userRole);

  SampleCollection findSampleCollection(String sampleCollectionId, Long userId, Role userRole);

  void updateSampleCollection(
      String sampleCollectionId, SampleCollection sampleCollection, Long userId, Role userRole);

  void confirmSampleCollection(String sampleCollectionId, Long userId, Role userRole);

  void notConfirmSampleCollection(String sampleCollectionId, Long userId, Role userRole);
}
