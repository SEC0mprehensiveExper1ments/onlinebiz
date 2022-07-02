package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS013;
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
public class DocServiceJS013 {

    private final OSSProvider ossProvider;

    public DocServiceJS013(OSSProvider ossProvider) {
        this.ossProvider = ossProvider;
    }

    /**
     * 以下是文档生成部分
     * */
    private static final float marginLeft;
    private static final float marginRight;
    private static final float marginTop;
    private static final float marginBottom;

    @Value("${document-dir}")
    private String DOCUMENT_DIR;
    static {
        // 在 iText 中每一个单位大小默认近似于点（pt）
        // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
        marginLeft = 85f;
        marginRight = 85f;
        marginTop = 60;
        marginBottom = 72;
    }

    private static JS013 JS013Json;
    /**
     * 填充JS013文档
     * */
    public String fill(String schemeReviewId, JS013 newJson) {
        JS013Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS013_" + schemeReviewId + ".pdf";
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            System.out.println(PageSize.A4);
            System.out.println("document.LeftMargin: " + document.leftMargin());
            System.out.println("document.Left: " + document.left());
            System.out.println("document.rightMargin: " + document.rightMargin());
            System.out.println("document.right: " + document.right());
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS013－2011";
            String[] footer = new String[]{"第 ", " 页 共 ", " 页"};
            int headerToPage = 100;
            int footerFromPage = -1;
            boolean isHaderLine = true;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
            float[] footerLoc = new float[]{(document.left() + document.right()) / 2.0f - 35, document.bottom() - 30};
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
                    "doc", "JS013_" + schemeReviewId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS013_" + schemeReviewId + ".pdf";
            } else { deleteOutFile(pdfPath); return "upload failed"; }
        } catch (Exception e) { e.printStackTrace(); deleteOutFile(pdfPath); return "minio error"; }
    }

    /**
     * 删除中间的out文件
     * */
    private void deleteOutFile(String pdfPath) {
        System.out.println(pdfPath);
        try {
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else { System.out.println("Delete" + file.getName() + "is failed."); }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private static Font titlefont;
    private static Font keyfont;
    private static Font textfont;

    /**
     * 生成JS013文档第一页
     * */
    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfSimSun = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfSimSun, 17f, Font.BOLD);
            keyfont = new Font(bfSimSun, 10.5f, Font.BOLD);
            textfont = new Font(bfSimSun, 10.5f, Font.NORMAL);
        } catch (Exception e) { e.printStackTrace(); }

        // 标题
        Paragraph title = new Paragraph("测试方案评审表", titlefont);
        title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
//        paragraph.setIndentationLeft(12); //设置左缩进
//        paragraph.setIndentationRight(12); //设置右缩进
//        paragraph.setFirstLineIndent(24); //设置首行缩进
//        paragraph.setLeading(0f); //行间距
        title.setSpacingBefore(-12f); //设置段落上空白
        title.setSpacingAfter(35f); //设置段落下空白

        float tableWidth = 430;
        float[] widths = new float[30];
        Arrays.fill(widths, tableWidth/30);
        // 行列每个基础单元格为 5mm x 5mm
        PdfPTable table = ItextUtils.createTable(widths, tableWidth);

        float[] paddings = new float[]{6f, 6f, 5f, 5f};
        float[] paddings2 = new float[]{12.5f, 12.5f, 5f, 5f};
        float[] paddings3 = new float[]{8f, 8f, 5f, 5f};
        float borderWidth = 0.3f;

        table.addCell(ItextUtils.createCell("软件名称", textfont, Element.ALIGN_CENTER, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputRuanJianMingChen(), textfont, Element.ALIGN_CENTER, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("版本号", textfont, Element.ALIGN_CENTER, 4, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBanBenHao(), textfont, Element.ALIGN_CENTER, 6, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("项目编号", textfont, Element.ALIGN_CENTER, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputXiangMuBianHao(), textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("测试类别", textfont, Element.ALIGN_CENTER, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputCeShiLeiBie(), textfont, Element.ALIGN_CENTER, 6, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("评审内容", keyfont, Element.ALIGN_CENTER, 10, 4, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("评    审    结    论", keyfont, Element.ALIGN_CENTER, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("通过", textfont, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("不通过及原因", textfont, Element.ALIGN_CENTER, 15, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("《测试方案》书写规范性", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo01(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo01(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("测试环境是否具有典型意义以及是否符合用户要求", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo02(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo02(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("测试内容的完整性，是否覆盖了整个系统", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo03(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo03(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("测试方法的选取是否合理", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo04(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo04(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("测试用例能否覆盖问题", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo05(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo05(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("输入、输出数据设计合理性", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo06(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo06(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("测试时间安排是否合理", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo07(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo07(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("测试人力资源安排是否合理", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputTongGuo08(), textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell(JS013Json.getInputBuTongGuo08(), textfont, Element.ALIGN_CENTER, 15, 2, paddings2, borderWidth));


        table.addCell(ItextUtils.createCell("职  责", textfont, Element.ALIGN_CENTER, 8, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("评审意见", textfont, Element.ALIGN_CENTER, 12, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("签  字", textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("日  期", textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("测试工程师", textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 12, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("测试室负责人", textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 12, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("质量负责人", textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 12, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("技术负责人", textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 12, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("监督人", textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 12, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("      ", textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(table);
    }
}
