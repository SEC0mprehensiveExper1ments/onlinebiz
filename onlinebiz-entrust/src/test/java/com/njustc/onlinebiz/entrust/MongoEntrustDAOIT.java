package com.njustc.onlinebiz.entrust;

import com.njustc.onlinebiz.common.model.Software;
import com.njustc.onlinebiz.common.model.entrust.*;
import com.njustc.onlinebiz.entrust.dao.EntrustDAO;
import com.njustc.onlinebiz.entrust.dao.MongoEntrustDAO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 有的测试用例会运行久一点，因为有的测试用例会遍历100条记录
@Import(MongoEntrustDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class MongoEntrustDAOIT {

    @Autowired
    private EntrustDAO entrustDAO;

    // 创建的委托ID集合
    private static final List<String> entrustIds = new ArrayList<>();

    // 生成一个不存在的ID
    private String getNonExistId() {
        return new ObjectId(new Date(0)).toString();
    }

    @Test
    @Order(1)
    public void testInsertEntrust() {
        // 创建一百条合同记录，后面要测试分页
        for (int i = 0; i < 100; ++i) {
            EntrustContent content = new EntrustContent();
            Software software = new Software();
            software.setName("受测软件");
            content.setSoftware(software);
            Entrust entrust = new Entrust();
            entrust.setContent(content);
            Entrust result = entrustDAO.insertEntrust(entrust);
            Assertions.assertNotNull(result);
            Assertions.assertNotNull(result.getId());
            entrustIds.add(result.getId());
        }
    }

    @Test
    @Order(2)
    public void testFindEntrustByIdNotExist() {
        Assertions.assertNull(entrustDAO.findEntrustById(getNonExistId()));
    }

    @Test
    @Order(3)
    public void testFindEntrustByIdExist() {
        for (String entrustId : entrustIds) {
            Entrust entrust = entrustDAO.findEntrustById(entrustId);
            Assertions.assertNotNull(entrust);
            Assertions.assertEquals("受测软件", entrust.getContent().getSoftware().getName());
        }
    }

    @Test
    @Order(4)
    public void testFindAllEntrusts() {
        for (int i = 1; i < 5; ++i) {
            List<EntrustOutline> result = entrustDAO.findAllEntrusts(1, 20);
            Assertions.assertEquals(20, result.size());
            Assertions.assertEquals("受测软件", result.get(0).getSoftwareName());
        }
    }

    @Test
    @Order(5)
    public void testCountAll() {
        Assertions.assertEquals(100, entrustDAO.countAll());
    }

    @Test
    @Order(6)
    public void testUpdateCustomerIdNotExist() {
        Assertions.assertFalse(entrustDAO.updateCustomerId(getNonExistId(), 1L));
    }

    @Test
    @Order(7)
    public void testUpdateCustomerIdExist() {
        // 每个客户有10条委托
        for (int i = 0; i < 100; ++i) {
            String entrustId = entrustIds.get(i);
            Assertions.assertTrue(entrustDAO.updateCustomerId(entrustId, i / 10L));
        }
    }

    @Test
    @Order(8)
    public void testCountByCustomerIdNotExist() {
        Assertions.assertEquals(0, entrustDAO.countByCustomerId(-1L));
    }

    @Test
    @Order(9)
    public void testCountByCustomerIdExist() {
        for (long i = 0; i < 10; ++i) {
            Assertions.assertEquals(10, entrustDAO.countByCustomerId(i));
        }
    }

    @Test
    @Order(10)
    public void testFindEntrustsByCustomerIdNotExist() {
        Assertions.assertEquals(0, entrustDAO.findEntrustsByCustomerId(-1L, 1, 10).size());
    }

    @Test
    @Order(11)
    public void testFindEntrustsByCustomerIdExist() {
        for (long i = 0; i < 10; ++i) {
            List<EntrustOutline> result = entrustDAO.findEntrustsByCustomerId(i, 1, 10);
            Assertions.assertEquals(10, result.size());
            Assertions.assertEquals(i, result.get(0).getCustomerId());
            result = entrustDAO.findEntrustsByCustomerId(i, 2, 10);
            Assertions.assertEquals(0, result.size());
        }
    }

    @Test
    @Order(12)
    public void testUpdateMarketerIdNotExist() {
        Assertions.assertFalse(entrustDAO.updateMarketerId(getNonExistId(), 1L));
    }

    @Test
    @Order(13)
    public void testUpdateMarketerIdExist() {
        // 每个市场部人员负责20条委托
        for (int i = 0; i < 100; ++i) {
            String entrustId = entrustIds.get(i);
            Assertions.assertTrue(entrustDAO.updateMarketerId(entrustId, i / 20L));
        }
    }

    @Test
    @Order(14)
    public void testCountByMarketerIdNotExist() {
        Assertions.assertEquals(0, entrustDAO.countByMarketerId(-1L));
    }

    @Test
    @Order(15)
    public void testCountByMarketerIdExist() {
        for (long i = 0; i < 5; ++i) {
            Assertions.assertEquals(20, entrustDAO.countByMarketerId(i));
        }
    }

    @Test
    @Order(16)
    public void testFindEntrustsByMarketerIdNotExist() {
        Assertions.assertEquals(0, entrustDAO.findEntrustsByMarketerId(-1L, 1, 20).size());
    }

    @Test
    @Order(17)
    public void testFindEntrustsByMarketerIdExist() {
        for (long i = 0; i < 5; ++i) {
            List<EntrustOutline> result = entrustDAO.findEntrustsByMarketerId(i, 1, 20);
            Assertions.assertEquals(20, result.size());
            Assertions.assertEquals(i, result.get(0).getMarketerId());
            result = entrustDAO.findEntrustsByMarketerId(i, 2, 20);
            Assertions.assertEquals(0, result.size());
        }
    }

    @Test
    @Order(18)
    public void testUpdateTesterIdNotExist() {
        Assertions.assertFalse(entrustDAO.updateTesterId(getNonExistId(), 1L));
    }

    @Test
    @Order(19)
    public void testUpdateTesterIdExist() {
        // 每个测试部人员负责2条委托
        for (int i = 0; i < 100; ++i) {
            String entrustId = entrustIds.get(i);
            Assertions.assertTrue(entrustDAO.updateTesterId(entrustId, i / 2L));
        }
    }

    @Test
    @Order(20)
    public void testCountByTesterIdNotExist() {
        Assertions.assertEquals(0, entrustDAO.countByMarketerId(-1L));
    }

    @Test
    @Order(21)
    public void testCountByTesterIdExist() {
        for (long i = 0; i < 50; ++i) {
            Assertions.assertEquals(2, entrustDAO.countByTesterId(i));
        }
    }

    @Test
    @Order(22)
    public void testFindEntrustsByTesterIdNotExist() {
        Assertions.assertEquals(0, entrustDAO.findEntrustsByTesterId(-1L, 1, 20).size());
    }

    @Test
    @Order(23)
    public void testFindEntrustsByTesterIdExist() {
        for (long i = 0; i < 50; ++i) {
            List<EntrustOutline> result = entrustDAO.findEntrustsByTesterId(i, 1, 200);
            Assertions.assertEquals(2, result.size());
            Assertions.assertEquals(i, result.get(0).getTesterId());
            result = entrustDAO.findEntrustsByTesterId(i, 2, 10);
            Assertions.assertEquals(0, result.size());
        }
    }

    @Test
    @Order(24)
    public void testUpdateContractIdNotExist() {
        Assertions.assertFalse(entrustDAO.updateContractId(getNonExistId(), new ObjectId().toString()));
    }

    @Test
    @Order(25)
    public void testUpdateContractIdExist() {
        for (String entrustId : entrustIds) {
            String contractId = new ObjectId().toString();
            Assertions.assertTrue(entrustDAO.updateContractId(entrustId, contractId));
            Assertions.assertEquals(contractId, entrustDAO.findEntrustById(entrustId).getContractId());
        }
    }

    @Test
    @Order(26)
    public void testUpdateContentNotExist() {
        Assertions.assertFalse(entrustDAO.updateContent(getNonExistId(), new EntrustContent()));
    }

    @Test
    @Order(27)
    public void testUpdateContentExist() {
        EntrustContent content = new EntrustContent();
        content.setExpectedTime("2022-05-24");
        content.setSoftwareMedium("光盘");
        // EntrustOutline 需要用 software
        Software software = new Software();
        software.setName("受测软件");
        software.setVersion("0.0.1");
        content.setSoftware(software);
        for (String entrustId : entrustIds) {
            Assertions.assertTrue(entrustDAO.updateContent(entrustId, content));
            Assertions.assertEquals(content, entrustDAO.findEntrustById(entrustId).getContent());
        }
    }

    @Test
    @Order(28)
    public void testUpdateSoftwareDocReviewNotExist() {
        Assertions.assertFalse(entrustDAO.updateSoftwareDocReview(getNonExistId(), new SoftwareDocReview()));
    }

    @Test
    @Order(29)
    public void testUpdateSoftwareDocReviewExist() {
        SoftwareDocReview review = new SoftwareDocReview();
        review.setReviewTeam("南大测试中心");
        review.setSoftwareVersion("0.0.1");
        for (String entrustId : entrustIds) {
            Assertions.assertTrue(entrustDAO.updateSoftwareDocReview(entrustId, review));
            Assertions.assertEquals(review, entrustDAO.findEntrustById(entrustId).getSoftwareDocReview());
        }
    }

    @Test
    @Order(30)
    public void testUpdateReviewNotExist() {
        Assertions.assertFalse(entrustDAO.updateReview(getNonExistId(), new EntrustReview()));
    }

    @Test
    @Order(31)
    public void testUpdateReviewExist() {
        EntrustReview review = new EntrustReview();
        review.setAcceptance("不接受");
        review.setCheckVirus("有病毒");
        for (String entrustId : entrustIds) {
            Assertions.assertTrue(entrustDAO.updateReview(entrustId, review));
            Assertions.assertEquals(review, entrustDAO.findEntrustById(entrustId).getReview());
        }
    }

    @Test
    @Order(32)
    public void testUpdateQuoteNotExist() {
        Assertions.assertFalse(entrustDAO.updateQuote(getNonExistId(), new EntrustQuote()));
    }

    @Test
    @Order(33)
    public void testUpdateQuoteExist() {
        EntrustQuote quote = new EntrustQuote();
        quote.setBankName("南大汉口银行分行");
        quote.setTotal(100.0);
        for (String entrustId : entrustIds) {
            Assertions.assertTrue(entrustDAO.updateQuote(entrustId, quote));
            Assertions.assertEquals(quote, entrustDAO.findEntrustById(entrustId).getQuote());
        }
    }

    @Test
    @Order(34)
    public void testUpdateStatusNotExist() {
        Assertions.assertFalse(entrustDAO.updateStatus(getNonExistId(), new EntrustStatus()));
    }

    @Test
    @Order(35)
    public void testUpdateStatusExist() {
        EntrustStatus status = new EntrustStatus(EntrustStage.CUSTOMER_ACCEPT_QUOTE, "接收报价");
        for (String entrustId : entrustIds) {
            Assertions.assertTrue(entrustDAO.updateStatus(entrustId, status));
            Assertions.assertEquals(status, entrustDAO.findEntrustById(entrustId).getStatus());
        }
    }

    @Test
    @Order(36)
    public void testPaginationInComplete() {
        // 总共100条，每页30条，最后一页应该是第4页，有10条记录
        Assertions.assertEquals(10, entrustDAO.findAllEntrusts(4, 30).size());
    }

    @Test
    @Order(37)
    public void testUpdateProjectIdNotExist() {
        Assertions.assertFalse(entrustDAO.updateProjectId(getNonExistId(), "123"));
    }

    @Test
    @Order(38)
    public void testUpdateProjectIdExist() {
        for (String entrustId : entrustIds) {
            Assertions.assertTrue(entrustDAO.updateProjectId(entrustId, new ObjectId().toString()));
        }
    }

    @Test
    @Order(39)
    public void testDeleteEntrustNotExist() {
        Assertions.assertFalse(entrustDAO.deleteEntrust(getNonExistId()));
    }

    @Test
    @Order(40)
    public void testDeleteEntrustExist() {
        for (String entrustId : entrustIds) {
            Assertions.assertTrue(entrustDAO.deleteEntrust(entrustId));
        }
    }

}
