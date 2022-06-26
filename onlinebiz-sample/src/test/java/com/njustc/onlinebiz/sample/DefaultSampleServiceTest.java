package com.njustc.onlinebiz.sample;


import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import com.njustc.onlinebiz.common.model.sample.SampleCollectionStage;
import com.njustc.onlinebiz.sample.dao.SampleDAO;
import com.njustc.onlinebiz.sample.exception.*;
import com.njustc.onlinebiz.sample.service.DefaultSampleService;
import com.njustc.onlinebiz.sample.service.SampleService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class DefaultSampleServiceTest {

    private final SampleDAO sampleDAO = mock(SampleDAO.class);
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final SampleService sampleService = new DefaultSampleService(sampleDAO, restTemplate);

    private String getNotExistId() { return new ObjectId(new Date(0)).toString(); }

    @Test
    public void testCreateSampleCollection() {
        Entrust entrust = new Entrust();
        SampleCollection sampleCollection = new SampleCollection();
        // 检测权限异常
        Assertions.assertThrows(SamplePermissionDeniedException.class,
                ()->sampleService.createSampleCollection("100001", 11L, Role.CUSTOMER));
        // 找不到对应的委托申请
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
        Assertions.assertThrows(SampleDAOFailureException.class,
                ()->sampleService.createSampleCollection("100001", 11L, Role.MARKETER));
        // 委托还未分配市场部人员
        entrust.setMarketerId(null);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(entrust);
        Assertions.assertThrows(SampleDAOFailureException.class,
                ()->sampleService.createSampleCollection("100001", 11L, Role.MARKETER));
        // 成功创建
        entrust.setMarketerId(11L);
        sampleCollection.setId("12345");
        when(restTemplate.getForObject(anyString(), any())).thenReturn(entrust);
        when(sampleDAO.insertSampleCollection(any())).thenReturn(sampleCollection);
        Assertions.assertEquals("12345",
                sampleService.createSampleCollection("100001", 11L, Role.MARKETER));
    }

    @Test
    public void testFindSpecificCollection() {
        SampleCollection sampleCollection = new SampleCollection();
        // 权限错误，客户访问
        Assertions.assertThrows(SamplePermissionDeniedException.class,
                ()->sampleService.findSpecificCollection("111", 11L, Role.CUSTOMER));
        // 无效ID
        when(sampleDAO.findSampleCollectionById(anyString())).thenReturn(null);
        Assertions.assertThrows(SampleNotFoundException.class,
                ()->sampleService.findSpecificCollection(getNotExistId(), 11L, Role.MARKETING_SUPERVISOR));
        // 成功找到
        when(sampleDAO.findSampleCollectionById(anyString())).thenReturn(sampleCollection);
        Assertions.assertNotNull(sampleService.findSpecificCollection("111", 11L, Role.MARKETER));
    }

    @Test
    public void testFindAllCollections() {
        // 测试非法参数是
        Assertions.assertThrows(SampleInvalidArgumentException.class,
                ()->sampleService.findAllCollections(-1, -2, 11L, Role.MARKETER));
        // 测试权限
        Assertions.assertThrows(SamplePermissionDeniedException.class,
                ()->sampleService.findAllCollections(1, 10, 11L, Role.CUSTOMER));
        // 成功返回
        List<SampleCollection> list = new ArrayList<>();
        when(sampleDAO.countAll()).thenReturn(10L);
        when(sampleDAO.findAllCollections(any(), any())).thenReturn(list);
        Assertions.assertNotNull(sampleService.findAllCollections(1, 10, 11L, Role.MARKETER));
    }

    @Test
    public void testUpdateSampleCollection() {
        SampleCollection sampleCollection = new SampleCollection();
        // 找不到样品信息
        when(sampleDAO.findSampleCollectionById(anyString())).thenReturn(null);
        Assertions.assertThrows(SampleNotFoundException.class,
                ()->sampleService.updateSampleCollection("123", new SampleCollection(), 11L, Role.MARKETER));
        // 权限判断
        when(sampleDAO.findSampleCollectionById(anyString())).thenReturn(sampleCollection);
        // 非市场部员工/主管，非管理员
        Assertions.assertThrows(SamplePermissionDeniedException.class,
                ()->sampleService.updateSampleCollection("123", new SampleCollection(), 11L, Role.CUSTOMER));
        // 市场部员工，但与样品集中市场部ID不一致
        sampleCollection.setMarketerId(22L);
        Assertions.assertThrows(SamplePermissionDeniedException.class,
                ()->sampleService.updateSampleCollection("123", new SampleCollection(), 11L, Role.MARKETER));
        // 状态判断，无法修改
        sampleCollection.setStage(SampleCollectionStage.RETURNED);
        Assertions.assertThrows(SampleInvalidStageException.class,
                ()->sampleService.updateSampleCollection("123", new SampleCollection(), 22L, Role.MARKETER));
        // DAO层更新失败
        sampleCollection.setStage(SampleCollectionStage.RECEIVED);
        Assertions.assertThrows(SampleDAOFailureException.class,
                ()->sampleService.updateSampleCollection("123", new SampleCollection(), 22L, Role.MARKETER));

    }
}
