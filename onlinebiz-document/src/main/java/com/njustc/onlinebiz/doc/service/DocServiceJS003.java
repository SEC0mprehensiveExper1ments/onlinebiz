package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.model.JS003.JS003;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class DocServiceJS003 {

  private final OSSProvider ossProvider;

  public DocServiceJS003(OSSProvider ossProvider) {
    this.ossProvider = ossProvider;
  }


  /**
   * 以下为文档生成部分
   * */
  //  基础页面设置
  private static final float marginLeft;
  private static final float marginRight;
  private static final float marginTop;
  private static final float marginBottom;

  @Value("${document-dir}")
  private String DOCUMENT_DIR;
  static {
    // 在 iText 中每一个单位大小默认近似于点（pt）
    // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
    marginLeft = 90f;
    marginRight = 90f;
    marginTop = 72f;
    marginBottom = 72f;
  }

  private JS003 JS003Json;
  /**
   * 填充JS003文档
   * @param newJson JS003对象
   * @return 成功返回OSS链接，失败返回原因
   * */
  public String fill(String entrustId, JS003 newJson) {
    JS003Json = newJson;
    String pdfPath = DOCUMENT_DIR + "JS003_" + entrustId + ".pdf";
    try {
      // 1.新建document对象
      Document document = new Document(PageSize.A4);// 建立一个Document对象
      document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
      // 2.建立一个书写器(Writer)与document对象关联
      File file = new File(pdfPath.replaceAll("\\\\", "/"));
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
    } catch (Exception e) { e.printStackTrace(); return "unable to generate a pdf"; }
    // 上传pdf
    try {
      if(ossProvider.upload(
              "doc", "JS003_" + entrustId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
        deleteOutFile(pdfPath);
        return "https://oss.syh1en.asia/doc/JS003_" + entrustId + ".pdf";
      } else { deleteOutFile(pdfPath); return "upload failed"; }
    } catch (Exception e) { e.printStackTrace(); deleteOutFile(pdfPath); return "minio error"; }
  }

  /**
   * 删除中间的out文件
   * */
  private void deleteOutFile(String pdfPath) {
    try {
      File file = new File(pdfPath.replaceAll("\\\\", "/"));
      if (file.delete()) {
        System.out.println(file.getName() + " is deleted!");
      } else { System.out.println("Delete" + file.getName() + "is failed."); }
    } catch(Exception e) { e.printStackTrace(); }
  }

  private Font titlefont1;
  private Font titlefont2;
  private Font titlefont3;
  private Font keyfont;
  private Font textfont;


  public void generatePageOne(Document document) throws Exception {
    // 加载字体
    try {
      BaseFont bfChinese = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
      titlefont1 = new Font(bfChinese, 16, Font.BOLD);
      titlefont2 = new Font(bfChinese, 13f, Font.BOLD);
      titlefont3 = new Font(bfChinese, 13.5f, Font.NORMAL);
      keyfont = new Font(bfChinese, 11.5f, Font.BOLD);
      textfont = new Font(bfChinese, 11.5f, Font.NORMAL);
    } catch (Exception e) { e.printStackTrace(); }

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

//    float[] paddings = new float[]{6f, 6f, 5f, 5f};
//    float[] paddings2 = new float[]{12.5f, 12.5f, 5f, 5f};
    float[] paddings3 = new float[]{8.5f, 8.5f, 5f, 5f};        // 上下左右的间距
    float borderWidth = 0.3f;

    table.addCell(ItextUtils.createCell("软件名称", titlefont2, Element.ALIGN_CENTER, 6, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell(JS003Json.getInputRuanJianMingCheng(), titlefont3, Element.ALIGN_CENTER, 14, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell("版本号", titlefont2, Element.ALIGN_CENTER, 4, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell(JS003Json.getInputBanBenHao(), titlefont3, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));

    table.addCell(ItextUtils.createCell("软件功能项目", titlefont2, Element.ALIGN_CENTER, 17, 2, paddings3, borderWidth));
    table.addCell(ItextUtils.createCell("功能说明", titlefont2, Element.ALIGN_CENTER, 15, 2, paddings3, borderWidth));

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
