package com.njustc.onlinebiz.apply;


import com.njustc.onlinebiz.apply.model.Apply;
import com.njustc.onlinebiz.apply.model.Outline;
import com.njustc.onlinebiz.apply.model.Party;
import com.njustc.onlinebiz.apply.model.Software;
import com.njustc.onlinebiz.apply.service.ApplyService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplyServiceIT {

    @Autowired
    ApplyService applyService;

    private static final Apply[] applys = new Apply[3];

    @Test
    @Order(1)
    void testCreateApplySuccess() {
        // 保存创建的 Apply 对象用于后续测试
        applys[0] = applyService.createApply(1L, 2L);
        applys[1] = applyService.createApply(2L, 3L);
        applys[2] = applyService.createApply(3L, 4L);
        // 检查测试结果
        for (int i = 1; i < 3; ++i) {
            Assertions.assertNotNull(applys[i]);
            Assertions.assertEquals((i + 1L), applys[i].getPrincipalId());
            Assertions.assertEquals(2L, applys[i].getPrincipalId());
        }
    }

    @Test
    @Order(2)
    void testUpdateApplyFail() {
        Apply apply = new Apply().setId("bcdedit");
        Assertions.assertFalse(applyService.updateApply(apply));
    }


    @Test
    @Order(3)
    void testUpdateApplySuccess() {
        for (int i = 0; i < 3; ++i) {
            // 测试软件
            Software testedSoftware = new Software()
                    .setServerHardArch(new String[]{"test;test", "ok;ok"})
                    .setClientOS(new String[]{"test;test", "ok;ok"})
                    .setServSoftArch(new String[]{"test;test", "ok;ok"})
                    .setSoftwareName("" + (i + 1))
                    .setVersion("" + (i + 1))
                    .setDeveloper("" + (i + 1))
                    .setDeveloperType("" + (i + 1))
                    .setUserDescription("" + (i + 1))
                    .setFunctionIntro("" + (i + 1))
                    .setFunctionNums("" + (i + 1))
                    .setFunctionPoint("" + (i + 1))
                    .setCodeLine("" + (i + 1))
                    .setSoftwareType("" + (i + 1))
                    .setClientMemoryRequirement("" + (i + 1))
                    .setClientOtherRequirement("" + (i + 1))
                    .setServHardMemoryRequirement("" + (i + 1))
                    .setServHardDiskRequirement("" + (i + 1))
                    .setServHardOtherRequirement("" + (i + 1))
                    .setServSoftOS("" + (i + 1))
                    .setServSoftVersion("" + (i + 1))
                    .setServSoftProgramLang("" + (i + 1))
                    .setServSoftMiddleware("" + (i + 1))
                    .setServSoftOherSupport("" + (i + 1))
                    .setNetworkEnv("" + (i + 1));
            // 委托方信息
            Party principal = new Party()
                    .setCompanyEN("测试" + (i + 1))
                    .setCompanyCH("测试" + (i + 1))
                    .setPhoneNumber("测试" + (i + 1))
                    .setFax("测试" + (i + 1))
                    .setAddress("测试" + (i + 1))
                    .setZipCode("测试" + (i + 1))
                    .setContact("测试" + (i + 1))
                    .setCellphoneNumber("测试" + (i + 1))
                    .setEmail("测试" + (i + 1))
                    .setWebsite("测试" + (i + 1));
            applys[i].setPrincipal(principal);
            applys[i].setTestedSoftware(testedSoftware);
            applys[i].setTestType(new String[] {"" + (i + 2), "" + (i + 3)});
            applys[i].setTestStandard(new String[] {"" + (i + 2), "" + (i + 3)});
            applys[i].setTechIndex(new String[] {"" + (i + 2), "" + (i + 3)});
            applys[i].setCheckMaterious(new String[] {"" + (i + 2), "" + (i + 3)});
            applys[i].setSoftwareMedium("" + (i + 1));
            applys[i].setDocument("" + (i + 1));
            applys[i].setSampleHandling("" + (i + 1));
            applys[i].setExpectedTime("" + (i + 1));
            applys[i].setSecurityLevel("" + (i + 1));
            applys[i].setCheckVirus("" + (i + 1));
            applys[i].setConfirmation("" + (i + 1));
            applys[i].setAcceptance("" + (i + 1));
            applys[i].setTestSerialNumber("" + (i + 1));
            applys[i].setAuditorStage("" + (i + 1));
            // 应当修改成功
            Assertions.assertTrue(applyService.updateApply(applys[i]));
        }
    }

    @Test
    @Order(4)
    void testFindAllContracts() {
        List<Outline> result = applyService.findAllApplys();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    @Order(5)
    void testFindContractByNonExistingId() {
        Assertions.assertNull(applyService.findApplyById("non-existing-id"));
    }

    @Test
    @Order(6)
    void testFindContractByIdSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(applys[i], applyService.findApplyById(applys[i].getId()));
        }
    }

    @Test
    @Order(7)
    void testFindContractByNonExistingIdAndPrincipal() {
        Assertions.assertNull(applyService.findApplyByIdAndPrincipal("non-existing-id", 1L));
    }

    @Test
    @Order(8)
    void testFindContractByIdAndNonExistingPrincipal() {
        Assertions.assertNull(applyService.findApplyByIdAndPrincipal("fake-id-1", 10L));
    }

    @Test
    @Order(9)
    void testFindContractByIdAndPrincipalSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(applys[i],
                    applyService.findApplyByIdAndPrincipal(
                            applys[i].getId(),
                            applys[i].getPrincipalId()
                    ));
        }
    }

    @Test
    @Order(10)
    void testRemoveContractFail() {
        Assertions.assertFalse(applyService.removeApply("non-existing-id"));
    }

    @Test
    @Order(11)
    void testRemoveContractSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertTrue(applyService.removeApply(applys[i].getId()));
        }
    }

    @Test
    @Order(12)
    void testFindAllContractsAfterRemove() {
        Assertions.assertEquals(0, applyService.findAllApplys().size());
    }
}
