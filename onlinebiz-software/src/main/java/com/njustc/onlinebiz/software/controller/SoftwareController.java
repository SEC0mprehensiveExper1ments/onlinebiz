package com.njustc.onlinebiz.software.controller;

import com.njustc.onlinebiz.common.model.SoftwareFunctionList;
import com.njustc.onlinebiz.software.service.SoftwareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class SoftwareController {

    private final SoftwareService softwareService;

    public SoftwareController(SoftwareService softwareService){this.softwareService=softwareService;}

    //创建一份委托测试软件功能列表
    @PostMapping("/software")
    public ResponseEntity<SoftwareFunctionList> createSoftwareFunctionList(
            @RequestParam("principalId") Long principalId,
            @RequestBody SoftwareFunctionList softwareFunctionList
    ) {
        SoftwareFunctionList result = softwareService.createSoftwareFunctionList(principalId,softwareFunctionList);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(softwareFunctionList);
    }

    //更新委托测试软件功能列表信息
    @PostMapping("/software/{softwareId}")
    public ResponseEntity<Void> updateSoftwareFunctionListById(
            @PathVariable("softwareId") String softwareId,
            @RequestBody SoftwareFunctionList softwareFunctionList
    ) {
        return softwareId.equals(softwareFunctionList.getId()) ?
                softwareService.updateSoftwareFunctionList(softwareFunctionList) ?
                        ResponseEntity.ok().build() :
                        ResponseEntity.badRequest().build() :
                ResponseEntity.badRequest().build();
    }

    //删除委托测试软件功能列表信息
    @DeleteMapping("/software/{softwareId}")
    public ResponseEntity<Void> deleteSoftwareFunctionListById(@PathVariable("softwareId") String softwareId) {
        return softwareService.removeSoftwareFunctionList(softwareId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }

    // 查看任意委托测试软件功能列表的详细信息
    @GetMapping("/software/{softwareId}")
    public ResponseEntity<SoftwareFunctionList> getSoftwareFunctionListById(@PathVariable("softwareId") String softwareId) {
        SoftwareFunctionList softwareFunctionList = softwareService.findSoftwareFunctionListById(softwareId);
        return softwareFunctionList == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(softwareFunctionList);
    }

    // 查看用户自己的委托测试软件功能列表详情
    @GetMapping("/software/individual/{softwareId}")
    public ResponseEntity<SoftwareFunctionList> getIndividualSoftwareFunctionListById(
            @RequestParam("userId") Long userId,
            @PathVariable("softwareId") String softwareId
    ) {
        SoftwareFunctionList softwareFunctionList = softwareService.findSoftwareFunctionListByIdAndPrincipal(softwareId, userId);
        return softwareFunctionList == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(softwareFunctionList);
    }
}
