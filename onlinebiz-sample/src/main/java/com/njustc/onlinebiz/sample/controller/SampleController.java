package com.njustc.onlinebiz.sample.controller;

import com.njustc.onlinebiz.common.model.PageResult;
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

  // 获取样品集概要列表
  @GetMapping("/sample")
  public PageResult<SampleCollection> getSampleCollections(
          @RequestParam("userId") Long userId,
          @RequestParam("userRole") Role userRole,
          @RequestParam("page") Integer page,
          @RequestParam("pageSize") Integer pageSize
  ) {
    return sampleService.findAllCollections(page, pageSize, userId, userRole);
  }

  // 获取样品集详情
  @GetMapping("/sample/{sampleCollectionId}")
  public SampleCollection getSampleCollection(
      @RequestParam("userId") Long userId,
      @RequestParam("userRole") Role userRole,
      @PathVariable("sampleCollectionId") String sampleCollectionId) {
    return sampleService.findSpecificCollection(sampleCollectionId, userId, userRole);
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

}
