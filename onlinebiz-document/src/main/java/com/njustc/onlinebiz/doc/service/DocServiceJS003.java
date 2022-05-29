package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.doc.exception.DownloadDAOFailureException;
import com.njustc.onlinebiz.doc.exception.DownloadNotFoundException;
import com.njustc.onlinebiz.doc.exception.DownloadPermissionDeniedException;
import com.njustc.onlinebiz.doc.model.JS003.JS003;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

@Service
public class DocServiceJS003 {

  private static final String ENTRUST_SERVICE = "http://onlinebiz-entrust";
  private final RestTemplate restTemplate;
  private final OSSProvider ossProvider;

  private String entrustId;

  public DocServiceJS003(RestTemplate restTemplate, OSSProvider ossProvider) {
    this.restTemplate = restTemplate;
    this.ossProvider = ossProvider;
  }

  /**
   * 通过 entrustId 向entrust服务获取对象，以供后续生成文档并下载
   * @param entrustId 待下载的委托 id
   * @param userId 操作的用户 id
   * @param userRole 操作的用户角色
   * @return 若成功从entrust服务中获得对象，则返回；否则，返回异常信息
   * */
  public Entrust getEntrustById(String entrustId, Long userId, Role userRole) {
    // 调用entrust服务的getEntrust的接口
    String params = "?userId=" + userId + "&userRole=" + userRole;
    String url = ENTRUST_SERVICE + "/api/entrust/" + entrustId;
    ResponseEntity<Entrust> responseEntity = restTemplate.getForEntity(url + params, Entrust.class);
    // 检查委托 id 及权限的有效性
    if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
      throw new DownloadPermissionDeniedException("无权下载该文件");
    }
    else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
      throw new DownloadNotFoundException("未找到该委托ID");
    }
    else if (responseEntity.getStatusCode() != HttpStatus.ACCEPTED && responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DownloadDAOFailureException("其他问题");
    }
    Entrust entrust = responseEntity.getBody();
    this.entrustId = entrustId;

    return entrust;
  }


  /**
   * 以下为文档生成部分
   * */
  //  基础页面设置
  private static final float marginLeft;
  private static final float marginRight;
  private static final float marginTop;
  private static final float marginBottom;
  private static final int maxWidth = 430;      // 最大宽度
  private static final String absolutePath;
  static {
    // 修复一个Windows下的路径问题，Linux下的情况需部署后具体实践下
    absolutePath = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("font")).getPath().substring(1) + "/../";
    // 在 iText 中每一个单位大小默认近似于点（pt）
    // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
    marginLeft = 90f;
    marginRight = 90f;
    marginTop = 72f;
    marginBottom = 72f;
  }

  private static JS003 JS003Json;
  /**
   * 填充JS003文档
   * @param newJson JS003对象
   * @return 成功返回OSS链接，失败返回原因
   * */
  public String fill(JS003 newJson) {
    JS003Json = newJson;
    String pdfPath = absolutePath + "out/JS003_" + entrustId + ".pdf";
    System.out.println(absolutePath);
    try {
      // 1.新建document对象
      Document document = new Document(PageSize.A4);// 建立一个Document对象
      document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
      // 2.建立一个书写器(Writer)与document对象关联
      File file = new File(pdfPath);
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
      // 2.5 添加页眉/页脚
      String header = "NST－04－JS003－2011";
      String[] footer = new String[]{"", "", ""};
      int headerToPage = 100;
      int footerFromPage = 100;
      boolean isHaderLine = true;
      boolean isFooterLine = false;
      float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
      float[] footerLoc = new float[]{document.left(), document.bottom() - 20};
      float headLineOff = -5f;
      float footLineOff = 12f;
      writer.setPageEvent(new HeaderFooter(header, footer, headerToPage, footerFromPage, isHaderLine, isFooterLine,
              headerLoc, footerLoc, headLineOff, footLineOff));
      // 3.打开文档
      document.open();
      // 4.向文档中添加内容
      generatePageOne(document);
      // 5.关闭文档
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
      return "unable to generate a pdf";
    }
    // 上传pdf
    try {
      if(ossProvider.upload(
              "doc", "JS003_" + entrustId + ".pdf", Files.readAllBytes(Path.of(pdfPath)), "application/pdf")) {
        deleteOutFile(pdfPath);
        return "https://oss.syh1en.asia/doc/JS003_" + entrustId + ".pdf";
      } else {
        deleteOutFile(pdfPath);
        return "upload failed";
      }
    } catch (Exception e) {
      e.printStackTrace();
      deleteOutFile(pdfPath);
      return "minio error";
    }
  }

  /**
   * 删除中间的out文件
   * */
  private void deleteOutFile(String pdfPath) {
    try {
      File file = new File(pdfPath);
      if (file.delete()) {
        System.out.println(file.getName() + " is deleted!");
      } else {
        System.out.println("Delete" + file.getName() + "is failed.");
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private static Font titlefont1;
  private static Font titlefont2;
  private static Font titlefont3;
  private static Font keyfont;
  private static Font textfont;
  private static BaseFont bfChinese;
  private static BaseFont bfHeiTi;

  static {
    try {
      bfChinese =
              BaseFont.createFont(
                      absolutePath + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
      bfHeiTi =
              BaseFont.createFont(
                      absolutePath + "font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
      titlefont1 = new Font(bfChinese, 16, Font.BOLD);
      titlefont2 = new Font(bfChinese, 13f, Font.BOLD);
      titlefont3 = new Font(bfChinese, 13.5f, Font.NORMAL);
      keyfont = new Font(bfChinese, 11.5f, Font.BOLD);
      textfont = new Font(bfChinese, 11.5f, Font.NORMAL);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void generatePageOne(Document document) throws Exception {
    // 标题
    Paragraph title = new Paragraph("委托测试软件功能列表", titlefont1);
    title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
    title.setSpacingBefore(10f); // 设置段落上空白
    title.setSpacingAfter(15f); // 设置段落下空白

    // 表格
    float tableWidth = document.right()-document.left();
    float[] widths = new float[32];
    // 每一行的各单元的span col数值要达到30
    Arrays.fill(widths, tableWidth/30);
    // 行列每个基础单元格为 5mm x 5mm
    PdfPTable table = ItextUtils.createTable(widths, tableWidth);

    float[] paddings = new float[]{6f, 6f, 5f, 5f};
    float[] paddings2 = new float[]{12.5f, 12.5f, 5f, 5f};
    float[] paddings3 = new float[]{8.5f, 8.5f, 5f, 5f};        // 上下左右的间距
    float borderWidth = 0.3f;

    table.addCell(ItextUtils.createCell("软件名称", titlefont2, Element.ALIGN_CENTER, 6, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell(JS003Json.getInputRuanJianMingCheng(), titlefont3, Element.ALIGN_CENTER, 14, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell("版本号", titlefont2, Element.ALIGN_CENTER, 4, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell(JS003Json.getInputBanBenHao(), titlefont3, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));

    table.addCell(ItextUtils.createCell("软件功能项目", titlefont2, Element.ALIGN_CENTER, 17, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell("功能说明", titlefont2, Element.ALIGN_CENTER, 15, 2, paddings3, borderWidth));


    // System.out.println(newJson.getGongNengSum());
//    for (int i = 0; i < JS003Json.getGongNengSum(); i++) {
//      System.out.println(JS003Json.getZiGongNengSum(i));
//      Chunk gongNengNameChunk = new Chunk(JS003Json.getGongNengName(i));
//      gongNengNameChunk.setFont(contentFont);
//      Paragraph gongNengNameParagraph = new Paragraph(gongNengNameChunk);
//      PdfPCell gongNengNameCell = new PdfPCell(gongNengNameParagraph);
//      gongNengNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//      gongNengNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//      gongNengNameCell.setFixedHeight(30);
//      gongNengNameCell.setRowspan(JS003Json.getZiGongNengSum(i));
//      table.addCell(gongNengNameCell);
//      for (int j = 0; j < JS003Json.getZiGongNengSum(i); j++) {
//        Chunk ziGongNengNameChunk = new Chunk(JS003Json.getZiGongNengName(i, j));
//        ziGongNengNameChunk.setFont(contentFont);
//        Paragraph ziGongNengNameParagraph = new Paragraph(ziGongNengNameChunk);
//        PdfPCell ziGongNengNameCell = new PdfPCell(ziGongNengNameParagraph);
//        ziGongNengNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        ziGongNengNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        ziGongNengNameCell.setFixedHeight(30);
//        ziGongNengNameCell.setColspan(2);
//        table.addCell(ziGongNengNameCell);
//
//        Chunk ziGongNengShuoMingChunk = new Chunk(JS003Json.getZiGongNengShuoMing(i, j));
//        ziGongNengShuoMingChunk.setFont(contentFont);
//        Paragraph ziGongNengShuoMingParagraph = new Paragraph(ziGongNengShuoMingChunk);
//        PdfPCell ziGongNengShuoMingCell = new PdfPCell(ziGongNengShuoMingParagraph);
//        ziGongNengShuoMingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        ziGongNengShuoMingCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        ziGongNengShuoMingCell.setFixedHeight(30);
//        ziGongNengShuoMingCell.setColspan(3);
//        table.addCell(ziGongNengShuoMingCell);
//      }
//    }

    //
    for (int i = 0; i < JS003Json.getGongNengSum(); i++) {
      int rowSpan = JS003Json.getZiGongNengSum(i) * 2;
      table.addCell(ItextUtils.createCell(JS003Json.getGongNengName(i), textfont, Element.ALIGN_CENTER, 6, rowSpan, paddings3, borderWidth));
      for (int j = 0; j < JS003Json.getZiGongNengSum(i); j++) {
        table.addCell(ItextUtils.createCell(JS003Json.getZiGongNengName(i, j), textfont, Element.ALIGN_CENTER, 11, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS003Json.getZiGongNengShuoMing(i, j), textfont, Element.ALIGN_CENTER, 15, 2, paddings3, borderWidth));
      }
    }

    // 注解
    Paragraph text = new Paragraph();
    text.setLeading(24f);
    text.add(new Phrase("   注：\n", keyfont));
    text.add(
            new Phrase(
                    "   1.软件功能说明按树型结构方式描述。软件功能项目栏中应列出软件产品的所\n" +
                             "     有功能（包括各级子功能）。具体可见样例。\n" +
                             "   2.功能说明栏目应填写功能项目概述等信息。\n",
                    textfont));
    text.setSpacingAfter(0f); // 设置段落下空白

    // 添加到文档中
    document.add(title);
    document.add(table);
    document.add(text);
  }
}
