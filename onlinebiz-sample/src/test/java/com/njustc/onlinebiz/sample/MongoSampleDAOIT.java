package com.njustc.onlinebiz.sample;

import com.njustc.onlinebiz.common.model.sample.Sample;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import com.njustc.onlinebiz.common.model.sample.SampleCollectionStage;
import com.njustc.onlinebiz.sample.dao.MongoSampleDAO;
import com.njustc.onlinebiz.sample.dao.SampleDAO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoSampleDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class MongoSampleDAOIT {

    @Autowired
    private SampleDAO sampleDAO;

    // 创建的样品集ID集合
    private static final List<String> sampleCollectionIds = new ArrayList<>();

    // 生成一个不存在的ID
    private String getNotExistedId() { return new ObjectId(new Date(0)).toString(); }

    @Test
    @Order(1)
    public void testInsertSampleCollection() {
        // 创建一百条sampleCollection记录，后面要测试分页
        for (int i = 0; i < 100; ++i) {
            List<Sample> samples = new ArrayList<>();
            SampleCollection sampleCollection = new SampleCollection();
            sampleCollection.setEntrustId("100001");
            sampleCollection.setMarketerId(111L);
            sampleCollection.setName("测试样品集" + i);
            sampleCollection.setSamples(samples);
            sampleCollection.setStage(SampleCollectionStage.RECEIVED);
            SampleCollection result = sampleDAO.insertSampleCollection(sampleCollection);
            Assertions.assertNotNull(sampleCollection);
            Assertions.assertNotNull(result.getId());
            sampleCollectionIds.add(result.getId());
        }
    }

    @Test
    @Order(2)
    public void testFindEntrustByIdNotExist() {
        Assertions.assertNull(sampleDAO.findSampleCollectionById(getNotExistedId()));
    }

    @Test
    @Order(3)
    public void testFindEntrustByIdExist() {
        for (String sampleCollectionId : sampleCollectionIds) {
            SampleCollection sampleCollection = sampleDAO.findSampleCollectionById(sampleCollectionId);
            Assertions.assertNotNull(sampleCollection);
            Assertions.assertEquals("100001", sampleCollection.getEntrustId());
        }
    }

    @Test
    @Order(4)
    public void testFindAllEntrusts() {
        for (int i = 1; i < 5; ++i) {
            List<SampleCollection> result = sampleDAO.findAllCollections(1, 20);
            Assertions.assertEquals(20, result.size());
            Assertions.assertEquals("100001", result.get(0).getEntrustId());
        }
    }

    @Test
    @Order(5)
    public void testCountAll() {
        Assertions.assertEquals(100, sampleDAO.countAll());
    }

    @Test
    @Order(6)
    public void testUpdateByNotExistSampleCollectionId() {
        Assertions.assertFalse(sampleDAO.updateSampleCollection(getNotExistedId(), new SampleCollection()));
    }

    @Test
    @Order(7)
    public void testUpdateByExistSampleCollectionId() {
        for (int i = 0; i < 100; ++i) {
            String sampleCollectionId = sampleCollectionIds.get(i);
            List<Sample> samples = new ArrayList<>();
            Assertions.assertTrue(sampleDAO.updateSampleCollection(sampleCollectionId,
                    new SampleCollection()
                            .setName("新测试样品集" + i)
                            .setSamples(samples)
                            .setEntrustId("100001")
                            .setStage(SampleCollectionStage.RECEIVED)
                            .setMarketerId(111L)));
        }
    }

    @Test
    @Order(8)
    public void testDeleteSampleCollectionNotExist() {
        Assertions.assertFalse(sampleDAO.deleteSampleCollection(getNotExistedId()));
    }

    @Test
    @Order(9)
    public void testDeleteSampleCollectionExist() {
        for (String sampleCollectionId: sampleCollectionIds) {
            Assertions.assertTrue(sampleDAO.deleteSampleCollection(sampleCollectionId));
        }
    }

}
