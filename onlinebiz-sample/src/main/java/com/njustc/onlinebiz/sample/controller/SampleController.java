package com.njustc.onlinebiz.sample.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import com.njustc.onlinebiz.sample.service.SampleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SampleController {
  private final SampleService sampleService;

  public SampleController(SampleService sampleService) {
    this.sampleService = sampleService;
  }

  // 创建样品集，提供委托Id，返回样品集Id
  @PostMapping("/sample")
  public String createSampleCollection(
      @RequestParam("userId") Long userId,
      @RequestParam("userRole") Role userRole,
      @RequestParam("entrustId") String entrustId) {
    return sampleService.createSampleCollection(entrustId, userId, userRole);
  }

  // 获取样品集详情
  @GetMapping("/sample/{sampleCollectionId}")
  public SampleCollection getSampleCollection(
      @RequestParam("userId") Long userId,
      @RequestParam("userRole") Role userRole,
      @PathVariable("sampleCollectionId") String sampleCollectionId) {
    return sampleService.findSampleCollection(sampleCollectionId, userId, userRole);
  }

  // 更新样品集
  @PostMapping("/sample/{sampleCollectionId}")
  public void updateSampleCollection(
      @RequestParam("userId") Long userId,
      @RequestParam("userRole") Role userRole,
      @PathVariable("sampleCollectionId") String sampleCollectionId,
      @RequestBody SampleCollection sampleCollection) {
    sampleService.updateSampleCollection(sampleCollectionId, sampleCollection, userId, userRole);
  }

  // 将样品集标记为已确认
  @PostMapping("/sample/{sampleCollectionId}/confirm")
  public void confirmSampleCollection(
      @RequestParam("userId") Long userId,
      @RequestParam("userRole") Role userRole,
      @PathVariable("sampleCollectionId") String sampleCollectionId) {
    sampleService.confirmSampleCollection(sampleCollectionId, userId, userRole);
  }

  // 将样品集标记为未确认
  @PostMapping("/sample/{sampleCollectionId}/notConfirm")
  public void notConfirmSampleCollection(
      @RequestParam("userId") Long userId,
      @RequestParam("userRole") Role userRole,
      @PathVariable("sampleCollectionId") String sampleCollectionId) {
    sampleService.notConfirmSampleCollection(sampleCollectionId, userId, userRole);
  }
}
