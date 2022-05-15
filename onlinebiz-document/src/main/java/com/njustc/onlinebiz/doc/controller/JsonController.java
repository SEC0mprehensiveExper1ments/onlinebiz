package com.njustc.onlinebiz.doc.controller;

import com.itextpdf.text.DocumentException;
import com.njustc.onlinebiz.doc.domain.JS003;
import com.njustc.onlinebiz.doc.service.DocServiceJS003;
import com.njustc.onlinebiz.doc.domain.JS004;
import com.njustc.onlinebiz.doc.service.DocServiceJS004;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class JsonController {
  @PostMapping("/doc/JS004")
  @ResponseBody
  public String JsonControllerJS004(@RequestBody JS004 newJson) {
    DocServiceJS004 fileJS004 = new DocServiceJS004();
    // System.out.println(newJson.toString());
    return fileJS004.fill(newJson);
  }

  @PostMapping("/doc/JS003")
  @ResponseBody
  public String JsonControllerJS003(@RequestBody JS003 newJson)
      throws DocumentException, IOException {
    // System.out.println(newJson.toString());
    DocServiceJS003 fileJS003 = new DocServiceJS003();
    return fileJS003.fillJS003(newJson);
  }
}
