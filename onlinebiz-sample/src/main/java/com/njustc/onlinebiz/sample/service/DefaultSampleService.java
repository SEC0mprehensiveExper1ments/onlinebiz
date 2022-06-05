package com.njustc.onlinebiz.sample.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import com.njustc.onlinebiz.common.model.sample.SampleStage;
import com.njustc.onlinebiz.sample.dao.SampleDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultSampleService implements SampleService {
  private final SampleDAO sampleDAO;

  public DefaultSampleService(SampleDAO sampleDAO) {
    this.sampleDAO = sampleDAO;
  }

  @Override
  public String createSampleCollection(String entrustId, Long userId, Role userRole) {
    // TODO:权限确认
    SampleCollection sampleCollection = new SampleCollection();
    sampleCollection.setEntrustId(entrustId);
    sampleCollection.setStage(SampleStage.WAIT_CONFIRM);
    return sampleDAO.insertSampleCollection(sampleCollection).getId();
  }

  @Override
  public SampleCollection findSampleCollection(
      String sampleCollectionId, Long userId, Role userRole) {
    SampleCollection sampleCollection = sampleDAO.findSampleCollectionById(sampleCollectionId);
    if (sampleCollection == null) {
      // TODO:异常处理
    }
    // TODO:权限确认
    return sampleCollection;
  }

  @Override
  public void updateSampleCollection(
      String sampleCollectionId, SampleCollection sampleCollection, Long userId, Role userRole) {
    SampleCollection oldSampleCollection = sampleDAO.findSampleCollectionById(sampleCollectionId);
    // 确认状态
    if (oldSampleCollection.getStage() == SampleStage.CONFIRMED) {
      // TODO:异常处理
    }
    // TODO:权限确认
    if (!sampleDAO.updateSampleCollection(sampleCollectionId, sampleCollection)) {
      // TODO:异常处理
    }
  }

  @Override
  public void confirmSampleCollection(String sampleCollectionId, Long userId, Role userRole) {
    SampleCollection sampleCollection = sampleDAO.findSampleCollectionById(sampleCollectionId);
    // 确认状态
    if (sampleCollection.getStage() != SampleStage.WAIT_CONFIRM) {
      // TODO:异常处理
    }
    // TODO:权限确认
    sampleCollection.setStage(SampleStage.CONFIRMED);
    if (!sampleDAO.updateSampleCollection(sampleCollectionId, sampleCollection)) {
      // TODO:异常处理
    }
  }

  @Override
  public void notConfirmSampleCollection(String sampleCollectionId, Long userId, Role userRole) {
    SampleCollection sampleCollection = sampleDAO.findSampleCollectionById(sampleCollectionId);
    // TODO:权限确认
    if (!sampleDAO.updateSampleCollection(sampleCollectionId, sampleCollection)) {
      // TODO:异常处理
    }
  }
}
