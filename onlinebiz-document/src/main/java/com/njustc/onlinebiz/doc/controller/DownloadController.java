package com.njustc.onlinebiz.doc.controller;

import com.itextpdf.text.DocumentException;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.doc.model.JS002;
import com.njustc.onlinebiz.doc.model.JS003;
import com.njustc.onlinebiz.doc.service.DocServiceJS001;
import com.njustc.onlinebiz.doc.service.DocServiceJS002;
import com.njustc.onlinebiz.doc.service.DocServiceJS003;
import com.njustc.onlinebiz.doc.model.JS004;
import com.njustc.onlinebiz.doc.service.DocServiceJS004;
import com.njustc.onlinebiz.entrust.model.Entrust;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
/**
 * 收到请求后，文档服务向对应服务发出字段获取请求，通过DTO对象转换成文档内定义个的类对象
 * */
public class DownloadController {

  private final DocServiceJS001 docServiceJS001;
  private final DocServiceJS002 docServiceJS002;
  private final DocServiceJS003 docServiceJS003;
  private final DocServiceJS003 docServiceJS004;

  public DownloadController(DocServiceJS001 docServiceJS001,
                            DocServiceJS002 docServiceJS002,
                            DocServiceJS003 docServiceJS003,
                            DocServiceJS003 docServiceJS004) {
    this.docServiceJS001 = docServiceJS001;
    this.docServiceJS002 = docServiceJS002;
    this.docServiceJS003 = docServiceJS003;
    this.docServiceJS004 = docServiceJS004;
  }

  @PostMapping("/doc/JS001")
  public String downloadJS001(
          @RequestParam("userId") Long userId,
          @RequestParam("userRole") Role userRole
  ) {
    // TODO: 添加JS001的链接
    return null;
  }

  @PostMapping("/doc/JS002/{entrustId}")
  public String downloadJS002(
          @PathVariable("entrustId") String entrustId,
          @RequestParam("userId") Long userId,
          @RequestParam("userRole") Role userRole
  ) {
    Entrust entrust = docServiceJS002.getEntrustById(entrustId, userId, userRole);
    JS002 newJson = new JS002(entrust);
    return docServiceJS002.fill(newJson);
  }

  @PostMapping("/doc/JS003")
  public String downloadJS003(@RequestBody JS003 newJson)
          throws DocumentException, IOException {
    DocServiceJS003 fileJS003 = new DocServiceJS003();
    return fileJS003.fillJS003(newJson);
  }

  @PostMapping("/doc/JS004")
  public String downloadJS005(@RequestBody JS004 newJson) {
    DocServiceJS004 fileJS004 = new DocServiceJS004();
    // System.out.println(newJson.toString());
    return fileJS004.fill(newJson);
  }


}
