package com.njustc.onlinebiz.doc.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.doc.model.*;
import com.njustc.onlinebiz.doc.model.JS003.JS003;
import com.njustc.onlinebiz.doc.service.*;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
/**
 * 收到请求后，文档服务向对应服务发出字段获取请求，通过DTO对象转换成文档内定义个的类对象
 * */
public class DownloadController {

  private final DocServiceJS001 docServiceJS001;
  private final DocServiceJS002 docServiceJS002;
  private final DocServiceJS003 docServiceJS003;
  private final DocServiceJS004 docServiceJS004;
  private final DocServiceJS005 docServiceJS005;
  private final DocServiceJS009 docServiceJS009;
  private final DocServiceJS011 docServiceJS011;

  public DownloadController(DocServiceJS001 docServiceJS001,
                            DocServiceJS002 docServiceJS002,
                            DocServiceJS003 docServiceJS003,
                            DocServiceJS004 docServiceJS004,
                            DocServiceJS005 docServiceJS005, DocServiceJS009 docServiceJS009, DocServiceJS011 docServiceJS011) {
    this.docServiceJS001 = docServiceJS001;
    this.docServiceJS002 = docServiceJS002;
    this.docServiceJS003 = docServiceJS003;
    this.docServiceJS004 = docServiceJS004;
    this.docServiceJS005 = docServiceJS005;
    this.docServiceJS009 = docServiceJS009;
    this.docServiceJS011 = docServiceJS011;
  }

  @GetMapping("/doc/JS001")
  public String downloadJS001() {
    return docServiceJS001.fill();
  }

  @GetMapping("/doc/JS002/{entrustId}")
  public String downloadJS002(
          @PathVariable("entrustId") String entrustId,
          @RequestParam("userId") Long userId,
          @RequestParam("userRole") Role userRole
  ) {
    Entrust entrust = docServiceJS002.getEntrustById(entrustId, userId, userRole);
    JS002 newJson = new JS002(entrust);
    return docServiceJS002.fill(newJson);
  }

  @GetMapping("/doc/JS003/{entrustId}")
  public String downloadJS003(
          @PathVariable("entrustId") String entrustId,
          @RequestParam("userId") Long userId,
          @RequestParam("userRole") Role userRole
  ){
    Entrust entrust = docServiceJS003.getEntrustById(entrustId, userId, userRole);
    JS003 newJson = new JS003(entrust);

    return docServiceJS003.fill(newJson);
  }

  @GetMapping("/doc/JS004/{contractId}")
  public String downloadJS004(
          @PathVariable("contractId") String contractId,
          @RequestParam("userId") Long userId,
          @RequestParam("userRole") Role userRole
  ) {
    Contract contract = docServiceJS004.getContractById(contractId, userId, userRole);
    JS004 newJson = new JS004(contract);
    return docServiceJS004.fill(newJson);
  }

  @GetMapping("/doc/JS005/{contractId}")
  public String downloadJS005(
          @PathVariable("contractId") String contractId,
          @RequestParam("userId") Long userId,
          @RequestParam("userRole") Role userRole
  ) {
    Contract contract = docServiceJS005.getContractById(contractId, userId, userRole);
    JS005 newJson = new JS005(contract);
    return docServiceJS005.fill(newJson);
  }

  // 测试用，非生产接口
  @GetMapping("/doc/JS009")
  public String downloadJS009(
  ) {
    List<TestRecordList.TestRecord> testRecords = new ArrayList<>();
    testRecords.add(new TestRecordList.TestRecord("你好", "你好1", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好2", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好3", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好4", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好5", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好6", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好7", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好8", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好9", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好0", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好11", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testRecords.add(new TestRecordList.TestRecord("你好", "你好12", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));

    JS009 newJson = new JS009().setTestRecords(testRecords);
    String result = docServiceJS009.fill(newJson);
    System.out.println(result);
    return result;
  }

  @GetMapping("/doc/JS011")
  public String downloadJS011(
  ) {
    List<TestIssueList.TestIssue> testIssueList = new ArrayList<>();
    testIssueList.add(new TestIssueList.TestIssue("你好1", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testIssueList.add(new TestIssueList.TestIssue("你好2", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testIssueList.add(new TestIssueList.TestIssue("你好3", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testIssueList.add(new TestIssueList.TestIssue("你好4", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testIssueList.add(new TestIssueList.TestIssue("你好5", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testIssueList.add(new TestIssueList.TestIssue("你好6", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testIssueList.add(new TestIssueList.TestIssue("你好7", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    testIssueList.add(new TestIssueList.TestIssue("你好8", "你好", "你好", "你好", "你好", "你好", "你好", "你好", "你好"));
    JS011 newJson = new JS011().setTestIssues(testIssueList);
    String result = docServiceJS011.fill(newJson);
    System.out.println(result);
    return result;
  }


}
