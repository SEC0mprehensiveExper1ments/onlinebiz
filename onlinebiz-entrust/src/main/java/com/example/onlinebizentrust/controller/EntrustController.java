package com.example.onlinebizentrust.controller;

import com.example.onlinebizentrust.model.Entrust;
import com.example.onlinebizentrust.model.Outline;
import com.example.onlinebizentrust.service.EntrustService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class EntrustController {

    private final EntrustService entrustService;

    public EntrustController(EntrustService entrustService) {
        this.entrustService = entrustService;
    }

    @PostMapping("/entrust")
    public ResponseEntity<Entrust> createEntrust(
            @RequestParam("principalId") Long principalId,
            @RequestParam("userId") Long creatorId,
            @RequestParam("entrustId") String entrustId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    // 查看系统中所有的委托
    @GetMapping("/entrust")
    public ResponseEntity<List<Outline>> getAllEntrusts() {
        return ResponseEntity.ok().body(entrustService.findAllEntrusts());
    }

    // 查看任意委托的详细信息
    @GetMapping("/entrust/{entrustId}")
    public ResponseEntity<Entrust> getEntrustById(@PathVariable("entrustId") String entrustId) {
        Entrust entrust = entrustService.findEntrustById(entrustId);
        return entrust == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(entrust);
    }

    // 查看用户自己的所有委托
    @GetMapping("/entrust/individual")
    public ResponseEntity<List<Outline>> getIndividualEntrusts(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(entrustService.search().byPrincipalId(userId).getResult());
    }

    // 查看用户自己的委托详情
    @GetMapping("/entrust/individual/{entrustId}")
    public ResponseEntity<Entrust> getIndividualEntrustById(
            @RequestParam("userId") Long userId,
            @PathVariable("entrustId") String entrustId
    ) {
        Entrust entrust = entrustService.findEntrustByIdAndPrincipal(entrustId, userId);
        return entrust == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(entrust);
    }

    // 根据组合条件查询委托
    @GetMapping("/entrust/search")
    public ResponseEntity<List<Outline>> searchEntrusts(
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "representativeName", required = false) String representativeName,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "targetSoftware", required = false) String targetSoftware
    ) {
        // 检查条件是否全空
        if (contactName == null && companyName == null && representativeName == null &&
                projectName == null && targetSoftware == null) {
            return ResponseEntity.badRequest().build();
        }
        // null 参数会被忽略
        return ResponseEntity.ok().body(
                entrustService.search()
                        .byContact(contactName)
                        .byCompany(companyName)
                        .byAuthorizedRepresentative(representativeName)
                        .byProjectName(projectName)
                        .byTargetSoftware(targetSoftware)
                        .getResult()
        );
    }

    @PostMapping("/entrust/{entrustId}")
    public ResponseEntity<Void> updateEntrustById(
            @PathVariable("entrustId") String entrustId,
            @RequestBody Entrust entrust
    ) {
        return entrustId.equals(entrust.getId()) ?
                entrustService.updateEntrust(entrust) ?
                        ResponseEntity.ok().build() :
                        ResponseEntity.badRequest().build() :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/entrust/{entrustId}")
    public ResponseEntity<Void> deleteEntrustById(@PathVariable("entrustId") String entrustId) {
        return entrustService.removeEntrust(entrustId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }
}
