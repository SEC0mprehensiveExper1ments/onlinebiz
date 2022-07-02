package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.model.JS004;
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
public class DocServiceJS004 {
  private final OSSProvider ossProvider;

  public DocServiceJS004(OSSProvider ossProvider) {
    this.ossProvider = ossProvider;
  }

  /**
   * 以下是文档生成部分
   * */
  private static final int marginLeft;
  private static final int marginRight;
  private static final int marginTop;
  private static final int maxWidth = 430; // 最大宽度
  private static final int marginBottom;

  @Value("${document-dir}")
  private String DOCUMENT_DIR;
  private static JS004 JS004Json;
  private static Font titlefont1;
  private static Font titlefont2;
  private static Font keyfont;
  private static Font textfont;
  private static BaseFont bfHeiTi;

  static {
    // 在 iText 中每一个单位大小默认近似于点（pt）
    // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
    marginLeft = 90; // 页边距：左
    marginRight = 90; // 页边距：右
    marginTop = 60; // 页边距：上
    marginBottom = 72; // 页边距：下
  }


  /**
   * 填充
   *
   * @param contractId 委托id
   * @param newJson 新json
   * @return {@link String} OSS下载链接
   */
  public String fill(String contractId, JS004 newJson) {
    JS004Json = newJson;
    String pdfPath = DOCUMENT_DIR + "JS004_" + contractId + ".pdf";
    try {
      // 1.新建document对象
      Document document = new Document(PageSize.A4); // 建立一个Document对象
      document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
      // 2.建立一个书写器(Writer)与document对象关联
      File file = new File(pdfPath.replaceAll("\\\\", "/"));
      System.out.println(pdfPath);
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
      System.out.println(1);
      // 2.5 添加页眉/页脚
      String header = "NST－04－JS004－2011";
      String[] footer = new String[] {"第 ", " 页 共 ", " 页"};
      int headerToPage = 1;
      int footerFromPage = 2;
      boolean isHaderLine = true;
      boolean isFooterLine = false;
      float[] headerLoc = new float[] {document.right() - 5, document.top() + 15};
      float[] footerLoc =
              new float[] {(document.left() + document.right()) / 2.0f - 35, document.bottom() - 20};
      float headLineOff = -5f;
      float footLineOff = 12f;
      writer.setPageEvent(
              new HeaderFooter(
                      header,
                      footer,
                      headerToPage,
                      footerFromPage,
                      isHaderLine,
                      isFooterLine,
                      headerLoc,
                      footerLoc,
                      headLineOff,
                      footLineOff));
      // 3.打开文档
      document.open();
      // 4.向文档中添加内容
      generatePageOne(document);
      document.newPage();
      generatePageTwo(document);
      // document.newPage();
      generatePageThree(document);
      // document.newPage();
      generatePageFour(document);
      document.newPage();
      generatePageFive(document);
      // 5.关闭文档
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
      return "unable to generate pdf";
    }
    // 上传pdf
    try {
      if (ossProvider.upload(
              "doc", "JS004_" + contractId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
        deleteOutFile(pdfPath.replaceAll("\\\\", "/"));
        return "https://oss.syh1en.asia/doc/JS004_" + contractId + ".pdf";
      } else {
        deleteOutFile(pdfPath.replaceAll("\\\\", "/"));
        return "upload failed";
      }
    } catch (Exception e) {
      e.printStackTrace();
      deleteOutFile(pdfPath.replaceAll("\\\\", "/"));
      return "minio error";
    }
  }

  /**
   * 删除中间的out文件
   * */
  private void deleteOutFile(String pdfPath) {
    try {
      File file = new File(pdfPath.replaceAll("\\\\", "/"));
      if (file.delete()) {
        System.out.println(file.getName() + " is deleted!");
      } else {
        System.out.println("Delete" + file.getName() + "is failed.");
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }



  /** 生成JS004文档第一页 */
  public void generatePageOne(Document document) throws Exception {
    // 加载字体
    try{
      BaseFont bfChinese = BaseFont.createFont(
              DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
      bfHeiTi =
              BaseFont.createFont(
                      DOCUMENT_DIR + "font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
      titlefont1 = new Font(bfHeiTi, 29, Font.BOLD);
      titlefont2 = new Font(bfHeiTi, 16, Font.BOLD);
      keyfont = new Font(bfChinese, 12.5f, Font.BOLD);
      textfont = new Font(bfChinese, 12f, Font.NORMAL);
    } catch (Exception e) {
      e.printStackTrace();
    }


    // 标题
    Paragraph title = new Paragraph("软件委托测试合同", titlefont1);
    title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
    title.setSpacingBefore(20f); // 设置段落上空白
    title.setSpacingAfter(90f); // 设置段落下空白

    float tableWidth = document.right() - document.left() - 150;
    PdfPTable table =
        ItextUtils.createTable(new float[] {tableWidth / 2 - 45, tableWidth / 2 + 45}, maxWidth);
    table.addCell(
        ItextUtils.createCell(
            "项目名称 ：",
            titlefont2,
            Element.ALIGN_RIGHT,
            ItextUtils.NO_BORDER,
            new float[] {40f + 5f, 40f, 0f, 0f}));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputXiangMuMingChen(),
            new Font(bfHeiTi, 16, Font.NORMAL),
            Element.ALIGN_LEFT,
            Element.ALIGN_TOP,
            ItextUtils.NO_BORDER,
            new float[] {40f, 0f, 0f, 0f},
            0.3f,
            20f));
    table.addCell(
        ItextUtils.createCell(
            "委托方（甲方）：",
            titlefont2,
            Element.ALIGN_RIGHT,
            ItextUtils.NO_BORDER,
            new float[] {40f + 5f, 40f, 0f, 0f}));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang(),
            new Font(bfHeiTi, 16, Font.NORMAL),
            Element.ALIGN_LEFT,
            Element.ALIGN_TOP,
            ItextUtils.NO_BORDER,
            new float[] {40f, 0f, 0f, 0f},
            0.3f,
            20f));
    table.addCell(
        ItextUtils.createCell(
            "委托方（乙方）：",
            titlefont2,
            Element.ALIGN_RIGHT,
            ItextUtils.NO_BORDER,
            new float[] {40f + 5f, 40f, 0f, 0f}));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang(),
            new Font(bfHeiTi, 16, Font.NORMAL),
            Element.ALIGN_LEFT,
            Element.ALIGN_TOP,
            ItextUtils.NO_BORDER,
            new float[] {40f, 0f, 0f, 0f},
            0.3f,
            20f));

    PdfPTable table2 =
        ItextUtils.createTable(new float[] {tableWidth / 2 - 40, tableWidth / 2 + 40}, maxWidth);
    table2.addCell(
        ItextUtils.createCell(
            "签订地点 ：",
            titlefont2,
            Element.ALIGN_RIGHT,
            ItextUtils.NO_BORDER,
            new float[] {20f + 5f, 20f, 0f, 0f}));
    table2.addCell(
        ItextUtils.createCell(
            JS004Json.getInputQianDingDiDian(),
            new Font(bfHeiTi, 16, Font.NORMAL),
            Element.ALIGN_LEFT,
            Element.ALIGN_TOP,
            ItextUtils.NO_BORDER,
            new float[] {20f, 0f, 0f, 0f},
            0.3f,
            20f));
    table2.addCell(
        ItextUtils.createCell(
            "签订日期 ：",
            titlefont2,
            Element.ALIGN_RIGHT,
            ItextUtils.NO_BORDER,
            new float[] {20f, 20f, 0f, 0f}));
    String nian = "  " + JS004Json.getInputQianDingRiQi0Nian() + "  \u0232";
    String yue = "  " + JS004Json.getInputQianDingRiQi0Yue() + "  \u0232";
    String ri = "  " + JS004Json.getInputQianDingRiQi0Ri() + "  \u0232";
    table2.addCell(
        ItextUtils.createCell(
            new String[] {"", "  \u0232", nian, "年", yue, "月", ri, "日"},
            new Font(bfHeiTi, 16, Font.NORMAL),
            new Font(bfHeiTi, 16, Font.BOLD),
            Element.ALIGN_LEFT,
            Element.ALIGN_MIDDLE,
            ItextUtils.NO_BORDER,
            1,
            1,
            new float[] {20f, 20f, 0f, 0f},
            0.3f));

    document.add(new Paragraph("\n\n"));
    document.add(title);
    document.add(table);
    document.add(new Paragraph(("\n\n\n\n")));
    document.add(table2);
  }

  /** 生成JS004文档第二页 */
  public void generatePageTwo(Document document) throws Exception {
    Paragraph text1 = new Paragraph();
    text1.setLeading(24f);
    Chunk chunk1 = new Chunk(JS004Json.getInputJiaFang(), textfont);
    chunk1.setUnderline(0.3f, -3f);
    Chunk chunk2 = new Chunk(JS004Json.getInputYiFang(), textfont);
    chunk2.setUnderline(0.3f, -3f);

    text1.add(new Chunk("本合同由作为委托方的", textfont));
    text1.add(chunk1);
    text1.add(new Chunk("（以下简称“甲方”）与作为受托方的", textfont));
    text1.add(chunk2);
    text1.add(new Chunk("本合同由作为委托方的", textfont));
    text1.add(new Chunk("（以下简称“乙方”）在平等自愿的基础上，依据《中华人民共和国合同法》有关规定就项目的执行，经友好协商后订立。", textfont));
    text1.setSpacingAfter(0f); // 设置段落下空白

    Paragraph text2 = new Paragraph();
    text2.setLeading(24f);
    Chunk chunk3 = new Chunk(JS004Json.getInputShouCeRuanJian(), textfont);
    chunk3.setUnderline(0.3f, -3f);

    text2.add(new Chunk("一、任务表述\n", keyfont));
    text2.add(new Chunk("    乙方按照国家软件质量测试标准和测试规范，完成甲方委托的软件", textfont));
    text2.add(chunk3);
    text2.add(new Chunk("(下称受测软件)的质量特性", textfont));
    text2.add(new Chunk("，进行测试，并出具相应的测试报告。", textfont));
    text2.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text3 = new Paragraph();
    text3.setLeading(24f);
    text3.add(new Phrase("二、双方的主要义务\n", keyfont));
    text3.add(
        new Phrase(
            ""
                + "    1. 甲方的主要义务：\n"
                + "      （1）按照合同约定支付所有费用。\n"
                + "      （2）按照乙方要求以书面形式出具测试需求，包括测试子特性、测试软\n"
                + "           硬件环境等。\n"
                + "      （3）提供符合交付要求的受测软件产品及相关文档，包括软件功能列\n"
                + "           表、需求分析、设计文档、用户文档至乙方。\n"
                + "      （4）指派专人配合乙方测试工作，并提供必要的技术培训和技术协助。\n"
                + "    2. 乙方的主要义务：\n"
                + "      （1）设计测试用例，制定和实施产品测试方案。\n"
                + "      （2）在测试过程中，定期知会甲方受测软件在测试过程中出现的问题。\n"
                + "      （3）按期完成甲方委托的软件测试工作。\n"
                + "      （4）出具正式的测试报告。",
            textfont));
    text3.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text4 = new Paragraph();
    text4.setLeading(24f);
    text4.add(new Phrase("三、履约地点\n", keyfont));
    text4.add(
        new Phrase(
            "    由甲方将受测软件产品送到乙方实施测试。如果由于被测软件本身特点或其"
                + "它乙方认可的原因，需要在甲方所在地进行测试时，甲方应负担乙方现场测试人"
                + "员的差旅和食宿费用。",
            textfont));
    text4.setSpacingAfter(30f); // 设置段落下空白

    document.add(text1);
    document.add(text2);
    document.add(text3);
    document.add(text4);
  }

  /** 生成JS004文档第三页 */
  public void generatePageThree(Document document) throws Exception {
    Paragraph text1 = new Paragraph();
    text1.setLeading(24f);
    Chunk chunk1 = new Chunk(JS004Json.getInputCeShiFeiYong(), textfont);
    chunk1.setUnderline(0.3f, -3f);
    //Chunk chunk2 = new Chunk(JS004Json.getInputCeShiFeiYong0Yuan(), textfont);
    //        chunk2.setUnderline(0.3f, -3f);

    text1.add(new Chunk("四、合同价款\n", keyfont));
    text1.add(new Chunk("    本合同软件测试费用为人民币", textfont));
    text1.add(chunk1);
    text1.add(new Chunk("（¥ ", textfont));
    //text1.add(chunk2);
    text1.add(new Chunk(" 元）。", textfont));
    text1.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text2 = new Paragraph();
    text2.setLeading(24f);
    text2.add(new Chunk("五、测试费用支付方式\n", keyfont));
    text2.add(new Chunk("    本合同签定后，十个工作日内甲方合同价款至乙方帐户。", textfont));
    text2.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text3 = new Paragraph();
    text3.setLeading(24f);
    Chunk chunk3 = new Chunk(JS004Json.getInputLvXingQiXian(), textfont);
    //        chunk3.setUnderline(0.3f, -3f);
    Chunk chunk4 = new Chunk(JS004Json.getInputZhengGaiCishu(), textfont);
    //        chunk4.setUnderline(0.3f, -3f);
    Chunk chunk5 = new Chunk(JS004Json.getInputZhengGaiTianShu(), textfont);
    //        chunk5.setUnderline(0.3f, -3f);

    text3.add(new Phrase("六、履行的期限\n", keyfont));
    text3.add(new Chunk("    1. 本次测试的履行期限为合同生效之日起 ", textfont));
    text3.add(chunk3);
    text3.add(new Chunk(" 个自然日内完成。\n", textfont));
    text3.add(new Chunk("    2. 经甲乙双方同意，可对测试进度作适当修改，并以修改后的测试进度作\n", textfont));
    text3.add(new Chunk("       为本合同执行的期限。\n", textfont));
    text3.add(new Chunk("    3. 如受测软件在测试过程中出现的问题，导致继续进行测试会影响整体测\n", textfont));
    text3.add(new Chunk("       试进度，则乙方暂停测试并以书面形式通知甲方进行整改。在整个测试\n", textfont));
    text3.add(new Chunk("       过程中，整改次数限于 ", textfont));
    text3.add(chunk4);
    text3.add(new Chunk(" 次，每次不超过 ", textfont));
    text3.add(chunk5);
    text3.add(new Chunk(" 天。\n", textfont));
    text3.add(new Chunk("    4. 如因甲方原因，导致测试进度延迟、应由甲方负责,乙方不承担责任。 \n", textfont));
    text3.add(new Chunk("    5. 如因乙方原因，导致测试进度延迟，则甲方可酌情提出赔偿要求，赔偿\n", textfont));
    text3.add(new Chunk("       作为本合同的补充。\n", textfont));
    text3.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text4 = new Paragraph();
    text4.setLeading(24f);
    text4.add(new Phrase("七、资料的保密\n", keyfont));
    text4.add(
        new Phrase(
            "    对于一方向另一方提供使用的秘密信息，另一方负有保密的责任，不得向"
                + "任何第三方透露。为明确双方的保密义务，双方应签署《软件项目委托测试保密"
                + "协议》，并保证切实遵守其中条款。",
            textfont));
    text4.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text5 = new Paragraph();
    text5.setLeading(24f);
    text5.add(new Phrase("八、风险责任的承担\n", keyfont));
    text5.add(
        new Phrase(
            "    乙方人员在本协议有效期间（包括可能的到甲方出差）发生人身意外或"
                + "罹患疾病时由乙方负责处理。甲方人员在本协议有效期间（包括可能的到乙方出差）"
                + "发生人身意外或罹患疾病时由甲方负责处理。",
            textfont));
    text5.setSpacingAfter(40f); // 设置段落下空白

    document.add(text1);
    document.add(text2);
    document.add(text3);
    document.add(text4);
    document.add(text5);
  }

  /** 生成JS004文档第四页 */
  public void generatePageFour(Document document) throws Exception {
    Paragraph text1 = new Paragraph();
    text1.setLeading(24f);
    text1.add(new Chunk("九、验收方法\n", keyfont));
    text1.add(new Chunk("    由乙方向甲方提交软件产品鉴定测试报告正本一份，甲方签收鉴定测试报告后，完成验收。\n", textfont));
    text1.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text2 = new Paragraph();
    text2.setLeading(24f);
    text2.add(new Phrase("十、争议解决\n", keyfont));
    text2.add(
        new Phrase(
            "    双方因履行本合同所发生的一切争议，应通过友好协商解决；如协商解决不成，" + "就提交市级仲裁委员会进行仲裁。裁决对双方当事人具有同等约束力。\n",
            textfont));
    text2.setSpacingAfter(30f); // 设置段落下空白

    Paragraph text3 = new Paragraph();
    text3.setLeading(24f);
    text3.add(new Phrase("十一、其他\n", keyfont));
    text3.add(
        new Phrase(
            "    本合同自双方授权代表签字盖章之日起生效，自受托方的主要义务履行完毕之日起终止。\n"
                + "    本合同未尽事宜由双方协商解决。\n"
                + "    本合同的正本一式肆份，双方各执两份，具有同等法律效力。\n",
            textfont));
    text3.setSpacingAfter(30f); // 设置段落下空白

    document.add(text1);
    document.add(text2);
    document.add(text3);
  }

  /** 生成JS004文档第五页 */
  public void generatePageFive(Document document) throws Exception {
    Paragraph text1 = new Paragraph();
    text1.setLeading(24f);
    text1.add(new Chunk("十二、签章\n", keyfont));
    text1.setSpacingAfter(30f); // 设置段落下空白

    float tableWidth = 430;
    float[] widths = new float[30];
    Arrays.fill(widths, tableWidth / 30);
    // 行列每个基础单元格为 5mm x 5mm
    PdfPTable table = ItextUtils.createTable(widths, tableWidth);

    float[] paddings = new float[] {10f, 10f, 5f, 5f};
    float borderWidth = 1f;

    table.addCell(
        ItextUtils.createCell(
            "委\n\n\n\n托\n\n\n\n方", textfont, Element.ALIGN_CENTER, 2, 18, paddings, borderWidth));

    table.addCell(
        ItextUtils.createCell(
            "单位全称\n\n\n\n", textfont, Element.ALIGN_CENTER, 5, 5, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang() + "\n\n\n（签章）\n",
            textfont,
            Element.ALIGN_CENTER,
            23,
            5,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("授权代表", textfont, Element.ALIGN_CENTER, 5, 3, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0ShouQuanDaiBiao(),
            textfont,
            Element.ALIGN_CENTER,
            9,
            3,
            paddings,
            borderWidth));
    table.addCell(
        ItextUtils.createCell("签章日期", textfont, Element.ALIGN_CENTER, 3, 3, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0QianZhangRiQi(),
            textfont,
            Element.ALIGN_CENTER,
            11,
            3,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("联系人", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0LianXiRen(),
            textfont,
            Element.ALIGN_CENTER,
            23,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("通讯地址", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0TongXunDiZhi(),
            textfont,
            Element.ALIGN_CENTER,
            23,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("电话", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0DianHua(),
            textfont,
            Element.ALIGN_CENTER,
            9,
            2,
            paddings,
            borderWidth));
    table.addCell(
        ItextUtils.createCell("传真", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0ChuanZhen(),
            textfont,
            Element.ALIGN_CENTER,
            11,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("开户银行", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0KaiHuYinHang(),
            textfont,
            Element.ALIGN_CENTER,
            23,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("账号", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0ZhangHao(),
            textfont,
            Element.ALIGN_CENTER,
            9,
            2,
            paddings,
            borderWidth));
    table.addCell(
        ItextUtils.createCell("邮编", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputJiaFang0YouBian(),
            textfont,
            Element.ALIGN_CENTER,
            11,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell(
            "受\n\n\n\n托\n\n\n\n方", textfont, Element.ALIGN_CENTER, 2, 20, paddings, borderWidth));

    table.addCell(
        ItextUtils.createCell(
            "单位全称\n\n\n\n", textfont, Element.ALIGN_CENTER, 5, 5, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang() + "\n\n\n（签章）\n",
            textfont,
            Element.ALIGN_CENTER,
            23,
            5,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("授权代表", textfont, Element.ALIGN_CENTER, 5, 3, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0ShouQuanDaiBiao(),
            textfont,
            Element.ALIGN_CENTER,
            9,
            3,
            paddings,
            borderWidth));
    table.addCell(
        ItextUtils.createCell("签章日期", textfont, Element.ALIGN_CENTER, 3, 3, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0QianZhangRiQi(),
            textfont,
            Element.ALIGN_CENTER,
            11,
            3,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("联系人", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0LianXiRen(),
            textfont,
            Element.ALIGN_CENTER,
            23,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("通讯地址", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0TongXunDiZhi(),
            textfont,
            Element.ALIGN_CENTER,
            23,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("电话", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0DianHua(),
            textfont,
            Element.ALIGN_CENTER,
            9,
            2,
            paddings,
            borderWidth));
    table.addCell(
        ItextUtils.createCell("传真", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0ChuanZhen(),
            textfont,
            Element.ALIGN_CENTER,
            11,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("户名", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0HuMing(),
            textfont,
            Element.ALIGN_CENTER,
            23,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("开户银行", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0KaiHuYinHang(),
            textfont,
            Element.ALIGN_CENTER,
            23,
            2,
            paddings,
            borderWidth));

    table.addCell(
        ItextUtils.createCell("账号", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0ZhangHao(),
            textfont,
            Element.ALIGN_CENTER,
            9,
            2,
            paddings,
            borderWidth));
    table.addCell(
        ItextUtils.createCell("邮编", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
    table.addCell(
        ItextUtils.createCell(
            JS004Json.getInputYiFang0YouBian(),
            textfont,
            Element.ALIGN_CENTER,
            11,
            2,
            paddings,
            borderWidth));

    document.add(text1);
    document.add(table);
  }

}
