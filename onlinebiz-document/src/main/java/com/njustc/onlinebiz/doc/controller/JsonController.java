package com.njustc.onlinebiz.doc.controller;

import com.itextpdf.text.DocumentException;
import com.njustc.onlinebiz.doc.domain.JS003;
import com.njustc.onlinebiz.doc.service.DocService;
import com.njustc.onlinebiz.doc.domain.JS004;
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
    DocService fileJS004 = new DocService();
    // System.out.println(newJson.toString());
    return fileJS004.fillJS004(newJson);
  }

  @PostMapping("/doc/JS003")
  @ResponseBody
  public String JsonControllerJS003(@RequestBody JS003 newJson)
      throws DocumentException, IOException {
    // System.out.println(newJson.toString());
    DocService fileJS003 = new DocService();
    return fileJS003.fillJS003(newJson);
  }
}
