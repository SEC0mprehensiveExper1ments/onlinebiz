package com.njustc.onlinebiz.doc.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@Service
@PropertySource("classpath:application.yml")
public class DocServiceJS001 {

    private final OSSProvider ossProvider;

    public DocServiceJS001(OSSProvider ossProvider) {
        this.ossProvider = ossProvider;
    }

    /**
     * 以下是文档生成部分
     * */
    //  基础页面设置
    // 在 iText 中每一个单位大小默认近似于点（pt）
    // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
    private static final float marginLeft = 65f;
    private static final float marginRight = 65f;
    private static final float marginTop = 68f;
    private static final float marginBottom = 65f;

    @Value("${document-dir}")
    private String DOCUMENT_DIR;

    private Font titlefont2;
    private Font keyfont;
    private Font textfont;


    /**
     * 填充JS001文档
     * @return 成功返回OSS链接，失败返回原因字符串
     * */
    public String fill(){
        String pdfPath = DOCUMENT_DIR + "JS001_out.pdf";
        System.out.println(DOCUMENT_DIR);
        // 创建文档
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS001－2011";
            String[] footer = new String[] {"第 ", " 页 共 ", " 页"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHeaderLine = true;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
            float[] footerLoc = new float[] {(document.left() + document.right()) / 2.0f - 35, document.bottom() - 20};
            float headLineOff = -5f;
            float footLineOff = 12f;
            writer.setPageEvent(new HeaderFooter(header, footer, headerToPage, footerFromPage, isHeaderLine, isFooterLine,
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
                    "doc", "JS001_out.pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS001_out.pdf";
            } else { deleteOutFile(pdfPath); return "upload failed"; }
        } catch (Exception e) { e.printStackTrace(); deleteOutFile(pdfPath); return "minio error"; }
    }

    // 删除中间的out文件
    private void deleteOutFile(String pdfPath) {
        try {
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else { System.out.println("Delete" + file.getName() + "is failed."); }
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            BaseFont bfChinese = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont2 = new Font(bfChinese, 21, Font.NORMAL);
            keyfont = new Font(bfChinese, 12.5f, Font.BOLD);
            textfont = new Font(bfChinese, 12f, Font.NORMAL);
        } catch (Exception e) { e.printStackTrace(); }
        // 标题
        Paragraph title = new Paragraph("软件项目委托测试提交材料", titlefont2);
        title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(10f); // 设置段落上空白
        title.setSpacingAfter(30f); // 设置段落下空白

        Paragraph text1 = new Paragraph();
        text1.setLeading(24f);
        text1.add(new Chunk("一、 软件项目委托测试需要提交的文档：\n", keyfont));
        text1.add(new Chunk("     1.《软件项目委托测试申请表》：\n" +
                                      "         ① 书面一份加盖公章；\n" +
                                      "         ② 电子文档。\n" +
                                      "     2.《委托测试软件功能列表》: ①书面一份加盖公章；②电子文档\n" +
                                      "     3.《软件项目委托测试保密协议》、《软件项目委托测试合同》：格式按照合\n" +
                                      "       同及协议规范签订。①书面各两分；②电子文档\n" +
                                      "     4.软件资料: 包括《用户手册》、《安装手册》、《操作手册》、《维护手册》。\n" +
                                      "       (手册要有详细的目录索引、页码标识、文档版本号、技术支持联系方法、公司\n" +
                                      "       名称、地址等) \n" +
                                      "     ① 电子文档。\n" +
                                      "     5. 《无法检测功能的声明》\n" +
                                      "     ① 书面一份加盖公章；②电子文档。\n", textfont));
        text1.setSpacingAfter(0f); // 设置段落下空白

        Paragraph text2 = new Paragraph();
        text2.setLeading(24f);
        text2.add(new Phrase("二、 提交软件样品\n", keyfont));
        text2.add(
                new Phrase(
                        "     1、提交软件样品一套（与《委托测试软件功能列表》一致）；附相\n" +
                                "        应的平台及支持性数据。\n",
                        textfont));
        text2.setSpacingAfter(0f); // 设置段落下空白

        Paragraph text3 = new Paragraph();
        text3.setLeading(24f);
        text3.add(new Phrase("三、 其它\n", keyfont));
        text3.add(
                new Phrase(
                        "     1、以上一、二项所要求提交的电子文档集中刻成一张（或按实际情\n" +
                                "        况分开来刻录）光盘里；\n" +
                                "     2、以上申报材料（书面文档、电子文档）请在签订合同 5 天内提交\n" +
                                "        我中心；\n" +
                                "     3、如有疑问请发电子邮件或电话联系。\n",
                        textfont));
        text3.setSpacingAfter(0f); // 设置段落下空白

        document.add(title);
        document.add(text1);
        document.add(text2);
        document.add(text3);
    }

}
