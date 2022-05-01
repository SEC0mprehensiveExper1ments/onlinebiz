package com.njustc.onlinebiz.contract;

import com.njustc.onlinebiz.contract.model.Contract;
import com.njustc.onlinebiz.contract.model.Outline;
import com.njustc.onlinebiz.contract.model.Party;
import com.njustc.onlinebiz.contract.service.ContractService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

// 服务层集成测试
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractServiceIT {

    @Autowired
    ContractService contractService;

    private static final Contract[] contracts = new Contract[3];

    @Test
    @Order(1)
    void testCreateContractSuccess() {
        // 保存创建的 Contract 对象用于后续测试
        contracts[0] = contractService.createContract(1L, 2L, "fake-id-1");
        contracts[1] = contractService.createContract(2L, 2L, "fake-id-2");
        contracts[2] = contractService.createContract(3L, 2L, "fake-id-3");
        // 检查测试结果
        for (int i = 1; i < 3; ++i) {
            Assertions.assertNotNull(contracts[i]);
            Assertions.assertEquals((i + 1L), contracts[i].getPrincipalId());
            Assertions.assertEquals(2L, contracts[i].getCreatorId());
            Assertions.assertEquals("fake-id-" + (i + 1), contracts[i].getEntrustId());
        }
    }

    @Test
    @Order(2)
    void testUpdateContractFail() {
        Contract contract = new Contract().setId("bcdedit");
        Assertions.assertFalse(contractService.updateContract(contract));
    }

    // 乙方都是一样的
    private final Party trustee = new Party()
            .setCompany("南京大学计算机软件新技术国家重点实验室")
            .setAuthorizedRepresentative("南大授权代表")
            .setSigDate("2022-05-01")
            .setContact("南大联系人")
            .setAddress("仙林大道163号")
            .setPhoneNumber("xxxxxxxxxxx")
            .setZipCode("025000")
            .setFax("南大传真号")
            .setBankName("中国工商银行股份有限公司南京汉口路分理处")
            .setAccountName("南京大学")
            .setAccount("4301011309001041656");

    @Test
    @Order(3)
    void testUpdateContractSuccess() {
        for (int i = 0; i < 3; ++i) {
            contracts[i].setSerialNumber("fake-serial-" + (i + 1));
            contracts[i].setProjectName("项目-" + (i + 1));
            // 甲方信息
            Party principal = new Party()
                    .setCompany("公司-" + (i + 1))
                    .setAuthorizedRepresentative("授权代表-" + (i + 1))
                    .setSigDate("签订日期-" + (i + 1))
                    .setContact("联系人-" + (i + 1))
                    .setAddress("地址-" + (i + 1))
                    .setPhoneNumber("电话-" + (i + 1))
                    .setZipCode("邮编-" + (i + 1))
                    .setFax("传真-" + (i + 1))
                    .setBankName("银行-" + (i + 1))
                    .setAccountName("户名-" + (i + 1))
                    .setAccount("账号-" + (i + 1));
            contracts[i].setPrincipal(principal);
            contracts[i].setTrustee(trustee);
            contracts[i].setSignedAt("签订地点-" + (i + 1));
            contracts[i].setSignedDate("签订日期-" + (i + 1));
            contracts[i].setTargetSoftware("受测软件-" + (i + 1));
            contracts[i].setPrice(100.0);
            contracts[i].setTotalWorkingDays(14);
            contracts[i].setRectificationLimit(2);
            contracts[i].setRectificationDaysEachTime(3);
            // 应当修改成功
            Assertions.assertTrue(contractService.updateContract(contracts[i]));
        }
    }

    @Test
    @Order(4)
    void testFindAllContracts() {
        List<Outline> result = contractService.findAllContracts();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    @Order(5)
    void testFindContractByNonExistingId() {
        Assertions.assertNull(contractService.findContractById("non-existing-id"));
    }

    @Test
    @Order(6)
    void testFindContractByIdSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(contracts[i], contractService.findContractById(contracts[i].getId()));
        }
    }

    @Test
    @Order(7)
    void testFindContractByNonExistingIdAndPrincipal() {
        Assertions.assertNull(contractService.findContractByIdAndPrincipal("non-existing-id", 1L));
    }

    @Test
    @Order(8)
    void testFindContractByIdAndNonExistingPrincipal() {
        Assertions.assertNull(contractService.findContractByIdAndPrincipal("fake-id-1", 10L));
    }

    @Test
    @Order(9)
    void testFindContractByIdAndPrincipalSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(contracts[i],
                    contractService.findContractByIdAndPrincipal(
                            contracts[i].getId(),
                            contracts[i].getPrincipalId()
                    ));
        }
    }

    @Test
    @Order(10)
    void testSearchByPrincipalIdExists() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(1, contractService.search()
                    .byPrincipalId(contracts[i].getPrincipalId()).getResult().size());
        }
    }

    @Test
    @Order(11)
    void testSearchByPrincipalIdNonExists() {
        Assertions.assertEquals(0, contractService.search()
                .byPrincipalId(100L).getResult().size());
    }

    @Test
    @Order(12)
    void testSearchByContactSingleExists() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(1, contractService.search()
                    .byContact(contracts[i].getPrincipal().getContact().substring(1)).getResult().size());
        }
    }

    @Test
    @Order(13)
    void testSearchByContactSingleNonExists() {
        Assertions.assertEquals(0, contractService.search()
                .byContact("不存在的联系人").getResult().size());
    }

    @Test
    @Order(14)
    void testSearchByContactMulti() {
        Assertions.assertEquals(3, contractService.search().byContact("联系").getResult().size());
    }

    @Test
    @Order(15)
    void testSearchByCompanySingleExists() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(1, contractService.search()
                    .byCompany(contracts[i].getPrincipal().getCompany()).getResult().size());
        }
    }

    @Test
    @Order(16)
    void testSearchByCompanySingleNonExists() {
        Assertions.assertEquals(0, contractService.search()
                .byCompany("不存在的公司").getResult().size());
    }

    @Test
    @Order(17)
    void testSearchByCompanyMulti() {
        Assertions.assertEquals(3, contractService.search().byCompany("公司").getResult().size());
    }

    @Test
    @Order(18)
    void testSearchByAuthorizedRepresentativeSingleExists() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(1, contractService.search()
                    .byAuthorizedRepresentative(
                            contracts[i].getPrincipal().getAuthorizedRepresentative()
                    ).getResult().size());
        }
    }

    @Test
    @Order(19)
    void testSearchByAuthorizedRepresentativeSingleNonExists() {
        Assertions.assertEquals(0, contractService.search()
                .byAuthorizedRepresentative("不存在的授权代表").getResult().size());
    }

    @Test
    @Order(20)
    void testSearchByAuthorizedRepresentativeMulti() {
        Assertions.assertEquals(3, contractService.search()
                .byAuthorizedRepresentative("授权").getResult().size());
    }

    @Test
    @Order(21)
    void testSearchByProjectNameSingleExists() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(1, contractService.search()
                    .byProjectName(contracts[i].getProjectName().substring(1)).getResult().size());
        }
    }

    @Test
    @Order(22)
    void testSearchByProjectNameSingleNonExists() {
        Assertions.assertEquals(0, contractService.search()
                .byProjectName("不存在的项目名称").getResult().size());
    }

    @Test
    @Order(23)
    void testSearchByProjectNameMulti() {
        Assertions.assertEquals(3, contractService.search()
                .byProjectName("项目-").getResult().size());
    }

    @Test
    @Order(24)
    void testSearchByTargetSoftwareSingleExists() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(1, contractService.search()
                    .byTargetSoftware(contracts[i].getTargetSoftware().substring(1)).getResult().size());
        }
    }

    @Test
    @Order(25)
    void testSearchByTargetSoftwareSingleNonExists() {
        Assertions.assertEquals(0, contractService.search()
                .byProjectName("不存在的受测软件").getResult().size());
    }

    @Test
    @Order(26)
    void testSearchByTargetSoftwareMulti() {
        Assertions.assertEquals(3, contractService.search()
                .byTargetSoftware("受测软").getResult().size());
    }

    @Test
    @Order(27)
    void testSearchByAllConditionsExists() {
        Assertions.assertEquals(3, contractService.search()
                .byContact("联系")
                .byCompany("公司")
                .byProjectName("项目")
                .byAuthorizedRepresentative("授权")
                .byTargetSoftware("受测")
                .getResult()
                .size());
    }

    @Test
    @Order(28)
    void testSearchByAllConditionsNonExists() {
        Assertions.assertEquals(0, contractService.search()
                .byContact("联系")
                .byCompany("公司")
                .byProjectName("不存在的项目名称")
                .byAuthorizedRepresentative("授权")
                .byTargetSoftware("受测")
                .getResult()
                .size());
    }

    @Test
    @Order(29)
    void testRemoveContractFail() {
        Assertions.assertFalse(contractService.removeContract("non-existing-id"));
    }

    @Test
    @Order(30)
    void testRemoveContractSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertTrue(contractService.removeContract(contracts[i].getId()));
        }
    }

    @Test
    @Order(31)
    void testFindAllContractsAfterRemove() {
        Assertions.assertEquals(0, contractService.findAllContracts().size());
    }
}
