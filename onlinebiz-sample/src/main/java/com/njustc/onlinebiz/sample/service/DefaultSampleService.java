package com.njustc.onlinebiz.sample.service;

import com.njustc.onlinebiz.common.model.PageResult;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import com.njustc.onlinebiz.common.model.sample.SampleCollectionStage;
import com.njustc.onlinebiz.sample.dao.SampleDAO;
import com.njustc.onlinebiz.sample.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class DefaultSampleService implements SampleService {
  private final SampleDAO sampleDAO;
  private final RestTemplate restTemplate;
  private final String ENTRUST_SERVICE = "http://onlinebiz-entrust";

  public DefaultSampleService(SampleDAO sampleDAO, RestTemplate restTemplate) {
    this.sampleDAO = sampleDAO;
    this.restTemplate = restTemplate;
  }

  @Override
  public String createSampleCollection(String entrustId, Long userId, Role userRole) {
    // 权限确认
    if (userRole != Role.ADMIN && userRole != Role.MARKETER) {
      throw new SamplePermissionDeniedException("只有负责此委托的市场部人员和管理员可以添加样品集信息");
    }
    // 检查人员一致性
    String params = "?userId=" + userId + "&userRole=" + userRole;
    String url = ENTRUST_SERVICE + "/api/entrust/" + entrustId;
    Entrust entrust;
    try{
      entrust = restTemplate.getForObject(url + params, Entrust.class);
    } catch (HttpClientErrorException e) {
      throw new SampleDAOFailureException(e.getResponseBodyAsString());
    }
    // 判空
    if (entrust == null) {
      throw new SampleDAOFailureException("找不到对应委托申请");
    }
    if (!userId.equals(entrust.getMarketerId())) {
      throw new SampleDAOFailureException("委托还未分配市场部人员或操作人员ID与委托对应人员ID不匹配");
    }
    SampleCollection sampleCollection = new SampleCollection();
    sampleCollection.setEntrustId(entrustId);
    sampleCollection.setMarketerId(entrust.getMarketerId());
    sampleCollection.setStage(SampleCollectionStage.RECEIVED);
    return sampleDAO.insertSampleCollection(sampleCollection).getId();
  }

  @Override
  public SampleCollection findSpecificCollection(String sampleCollectionId, Long userId, Role userRole) {
    // 权限确认
    if (userRole == Role.CUSTOMER) {
      throw new SamplePermissionDeniedException("只有测试中心内部人员能够查看样品信息");
    }
    SampleCollection sampleCollection = sampleDAO.findSampleCollectionById(sampleCollectionId);
    if (sampleCollection == null) {
      throw new SampleNotFoundException("未找到该样品信息");
    }
    return sampleCollection;
  }

  @Override
  public PageResult<SampleCollection> findAllCollections(Integer page, Integer pageSize, Long userId, Role userRole) {
    if (page <= 0 || pageSize <= 0) {
      throw new SampleInvalidArgumentException("页号或每页大小必须为正整数");
    }
    long total;
    List<SampleCollection> list;
    if  (userRole == Role.CUSTOMER) {
      throw new SamplePermissionDeniedException("只有测试中心内部人员可以查看样品信息");
    }
    // 测试中心内部人员查看样品集信息，具有一致性视图
    total = sampleDAO.countAll();
    list = sampleDAO.findAllCollections(page, pageSize);

    return new PageResult<>(page, pageSize, total, list);
  }

  @Override
  public void updateSampleCollection(String sampleCollectionId, SampleCollection sampleCollection, Long userId, Role userRole) {
    // if (!sampleCollectionId.equals(sampleCollection.getId())) {
    //    throw new SampleDAOFailureException("待更新的样品集ID与路径参数不一致");
    // }
    SampleCollection oldSampleCollection = sampleDAO.findSampleCollectionById(sampleCollectionId);
    // 判空
    if (oldSampleCollection == null) {
      throw new SampleNotFoundException("找不到该样品集信息");
    }
    // 权限检查
    if (userRole != Role.ADMIN && userRole != Role.MARKETING_SUPERVISOR ) {
      if (userRole != Role.MARKETER || !userId.equals(oldSampleCollection.getMarketerId())) {
        throw new SamplePermissionDeniedException("无权更改样品信息");
      }
      // 确认状态
      if (oldSampleCollection.getStage() != SampleCollectionStage.RECEIVED) {
        throw new SampleInvalidStageException("当前阶段不能修改样品信息");
      }
    }
    // DAO只更新name, stage, samples
    if (!sampleDAO.updateSampleCollection(sampleCollectionId, sampleCollection)) {
      throw new SampleDAOFailureException("更新内容失败");
    }
  }

}
