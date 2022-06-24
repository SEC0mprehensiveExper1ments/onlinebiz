package com.njustc.onlinebiz.contract;

import com.njustc.onlinebiz.contract.dao.ContractDAO;
import com.njustc.onlinebiz.contract.dao.MongoContractDAO;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.ContractStage;
import com.njustc.onlinebiz.common.model.contract.ContractStatus;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.Date;

@Import(MongoContractDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class   MongoContractDAOIT {

    @Autowired
    private ContractDAO contractDAO;

    private static String contractId;

    private String getNonExistId() {
        return new ObjectId(new Date(0)).toString();
    }

    @Test
    @Order(1)
    public void testInsertContract() {
        Contract contract = new Contract();
        Contract result = contractDAO.insertContract(contract);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        contractId = result.getId();
    }

    @Test
    @Order(2)
    public void testFindContractByIdNonExist() {
        Assertions.assertNull(contractDAO.findContractById(getNonExistId()));
    }

    @Test
    @Order(3)
    public void testFindContractByIdExist() {
        Assertions.assertNotNull(contractDAO.findContractById(contractId));
    }

    @Test
    @Order(4)
    public void testUpdateContractNonExist() {
        Assertions.assertFalse(contractDAO.updateContract(getNonExistId(), new Contract()));
    }

    @Test
    @Order(5)
    public void testUpdateContractExist() {
        Contract contract = new Contract();
        contract.setId(contractId);
        contract.setProjectName("软件测试项目");
        contract.setTargetSoftware("受测软件");
        contract.setTotalWorkingDays(14);
        contract.setRectificationLimit(2);
        contract.setRectificationDaysEachTime(2);
        Assertions.assertTrue(contractDAO.updateContract(contractId, contract));
        Assertions.assertEquals(contract, contractDAO.findContractById(contractId));
    }

    @Test
    @Order(6)
    public void testUpdateMarketerIdNonExist() {
        Assertions.assertFalse(contractDAO.updateMarketerId(getNonExistId(), 1L));
    }

    @Test
    @Order(7)
    public void testUpdateMarketerIdExist() {
        Assertions.assertTrue(contractDAO.updateMarketerId(contractId, 7L));
        Assertions.assertEquals(7, contractDAO.findContractById(contractId).getMarketerId());
    }

    @Test
    @Order(8)
    public void testUpdateCustomerIdNonExist() {
        Assertions.assertFalse(contractDAO.updateCustomerId(getNonExistId(), 1L));
    }

    @Test
    @Order(9)
    public void testUpdateCustomerIdExist() {
        Assertions.assertTrue(contractDAO.updateCustomerId(contractId, 11L));
        Assertions.assertEquals(11, contractDAO.findContractById(contractId).getCustomerId());
    }

    @Test
    @Order(10)
    public void testUpdateScannedCopyPathNonExist() {
        Assertions.assertFalse(contractDAO.updateScannedCopyPath(getNonExistId(), "/tmp/" + contractId + ".pdf"));
    }

    @Test
    @Order(11)
    public void testUpdateScannedCopyPathExist() {
        String path = "/tmp/" + contractId + ".pdf";
        Assertions.assertTrue(contractDAO.updateScannedCopyPath(contractId, path));
        Assertions.assertEquals(path, contractDAO.findContractById(contractId).getScannedCopyPath());
    }

    @Test
    @Order(12)
    public void testUpdateNonDisclosureNonExist() {
        Assertions.assertFalse(contractDAO.updateNonDisclosure(getNonExistId(), new NonDisclosureAgreement()));
    }

    @Test
    @Order(13)
    public void testUpdateNonDisclosureExist() {
        NonDisclosureAgreement nonDisclosureAgreement = new NonDisclosureAgreement("partyA", "partyB");
        Assertions.assertTrue(contractDAO.updateNonDisclosure(contractId, nonDisclosureAgreement));
        Assertions.assertEquals(nonDisclosureAgreement, contractDAO.findContractById(contractId).getNonDisclosureAgreement());
    }

    @Test
    @Order(14)
    public void testUpdateStatusNonExist() {
        Assertions.assertFalse(contractDAO.updateStatus(getNonExistId(), new ContractStatus()));
    }

    @Test
    @Order(15)
    public void testUpdateStatusExist() {
        ContractStatus status = new ContractStatus(ContractStage.COPY_SAVED, "已上传扫描件");
        Assertions.assertTrue(contractDAO.updateStatus(contractId, status));
        Assertions.assertEquals(status, contractDAO.findContractById(contractId).getStatus());
    }

    @Test
    @Order(16)
    public void deleteContractByIdNonExist() {
        Assertions.assertFalse(contractDAO.deleteContractById(getNonExistId()));
    }

    @Test
    @Order(17)
    public void deleteContractByIdExist() {
        Assertions.assertTrue(contractDAO.deleteContractById(contractId));
    }

}
