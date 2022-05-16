package com.njustc.onlinebiz.software;

import com.njustc.onlinebiz.common.model.SoftwareFunction;
import com.njustc.onlinebiz.common.model.SoftwareFunctionList;
import com.njustc.onlinebiz.common.model.SoftwareSubFunction;
import com.njustc.onlinebiz.software.service.SoftwareService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SoftwareServiceIT {
    @Autowired
    SoftwareService softwareService;

    private static final SoftwareFunctionList[] softwareFunctionLists=new SoftwareFunctionList[3];

    @Test
    @Order(1)
    void testCreateSoftwareFunctionListSuccess(){
        //保存创建的 softwareFunctionList 对象用于后续测试
        softwareFunctionLists[0]=softwareService.createSoftwareFunctionList(1L,new SoftwareFunctionList());
        softwareFunctionLists[1]=softwareService.createSoftwareFunctionList(2L,new SoftwareFunctionList());
        softwareFunctionLists[2]=softwareService.createSoftwareFunctionList(3L,new SoftwareFunctionList());
        //检查测试结果
        for (int i=0;i<3;++i){
            Assertions.assertNotNull(softwareFunctionLists[i]);
            Assertions.assertEquals((i+1L),softwareFunctionLists[i].getPrincipalId());
        }
    }

    @Test
    @Order(2)
    void testUpdateSoftwareFunctionListFail(){
        SoftwareFunctionList softwareFunctionList =new SoftwareFunctionList().setId("bcd");
        Assertions.assertFalse(softwareService.updateSoftwareFunctionList(softwareFunctionList));
    }

    @Test
    @Order(3)
    void testUpdateSoftwareFunctionListSuccess() {
        for (int i = 0; i < 3; ++i) {
            SoftwareSubFunction testedSoftwareSubFunction=new SoftwareSubFunction()
                    .setSubFunctionName(""+(i+1))
                    .setDescription(""+(i+1));
            List<SoftwareSubFunction> subFunctionList= null;
            subFunctionList.add(testedSoftwareSubFunction);

            SoftwareFunction testedSoftwareFunction=new SoftwareFunction()
                    .setFunctionName(""+(i+1))
                    .setSubFunctionList(subFunctionList);
            List<SoftwareFunction> functionList=null;
            functionList.add(testedSoftwareFunction);

            softwareFunctionLists[i].setSoftwareName(""+(i+1));
            softwareFunctionLists[i].setSoftwareFunctionProject(functionList);

            Assertions.assertTrue(softwareService.updateSoftwareFunctionList(softwareFunctionLists[i]));
        }
    }

    @Test
    @Order(4)
    void testFindSoftwareFunctionListByNonExistingId() {
        Assertions.assertNull(softwareService.findSoftwareFunctionListById("non-existing-id"));
    }


    @Test
    @Order(5)
    void testFindSoftwareFunctionListByIdSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(softwareFunctionLists[i], softwareService.findSoftwareFunctionListById(softwareFunctionLists[i].getId()));
        }
    }

    @Test
    @Order(6)
    void testFindSoftwareFunctionListByNonExistingIdAndPrincipal() {
        Assertions.assertNull(softwareService.findSoftwareFunctionListByIdAndPrincipal("non-existing-id", 1L));
    }

    @Test
    @Order(7)
    void testFindSoftwareFunctionListByIdAndNonExistingPrincipal() {
        Assertions.assertNull(softwareService.findSoftwareFunctionListByIdAndPrincipal("fake-id-1", 10L));
    }

    @Test
    @Order(8)
    void testFindSoftwareFunctionListByIdAndPrincipalSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertEquals(softwareFunctionLists[i],
                    softwareService.findSoftwareFunctionListByIdAndPrincipal(
                            softwareFunctionLists[i].getId(),
                            softwareFunctionLists[i].getPrincipalId()
                    ));
        }
    }

    @Test
    @Order(9)
    void testRemoveSoftwareFunctionListFail() {
        Assertions.assertFalse(softwareService.removeSoftwareFunctionList("non-existing-id"));
    }

    @Test
    @Order(10)
    void testRemoveSoftwareFunctionListSuccess() {
        for (int i = 0; i < 3; ++i) {
            Assertions.assertTrue(softwareService.removeSoftwareFunctionList(softwareFunctionLists[i].getId()));
        }
    }
}
