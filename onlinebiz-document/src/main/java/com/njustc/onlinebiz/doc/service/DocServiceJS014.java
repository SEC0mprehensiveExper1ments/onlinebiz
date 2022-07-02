package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS014;
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
public class DocServiceJS014 {

    private final OSSProvider ossProvider;

    public DocServiceJS014(OSSProvider ossProvider) {
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
        marginLeft = 65f;
        marginRight = 65f;
        marginTop = 70f;
        marginBottom = 72f;
    }

    private static JS014 JS014Json;

    /**
     * 填充JS014文档
     * */
    public String fill(String entrustId, JS014 newJson) {
        JS014Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS014_" + entrustId + ".pdf";
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
            String header = "NST－04－JS014－2011";
            String[] footer = new String[]{"第 ", " 页 共 ", " 页"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHaderLine = true;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
            float[] footerLoc = new float[]{(document.left() + document.right()) / 2.0f - 35, document.bottom() - 10};
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
                    "doc", "JS014_" + entrustId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS014_" + entrustId + ".pdf";
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
        System.out.println(pdfPath);
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

    private static Font titlefont;
    private static Font titlefont2;
    private static Font textfont;

    /**
     * 生成JS014文档第一页
     * */
    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            // 定义全局的字体静态变量
            BaseFont bfSimSun = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfSimSun, 17f, Font.NORMAL);
            titlefont2 = new Font(bfSimSun, 12f, Font.NORMAL);
            textfont = new Font(bfSimSun, 10.5f, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 标题
        Paragraph title = new Paragraph("软件文档评审表", titlefont);
        title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
//        paragraph.setIndentationLeft(12); //设置左缩进
//        paragraph.setIndentationRight(12); //设置右缩进
//        paragraph.setFirstLineIndent(24); //设置首行缩进
//        paragraph.setLeading(0f); //行间距
        title.setSpacingBefore(-12f); //设置段落上空白
        title.setSpacingAfter(15f); //设置段落下空白

        Paragraph text = new Paragraph();
        text.add(new Phrase("软件名称：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS014Json.getInputRuanJianMingChen(), titlefont2, 19, 0.3f));
        text.add(new Phrase(" 版本号：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS014Json.getInputBanBenHao(), titlefont2, 6, 0.3f));
        text.add(new Phrase("\n委托单位：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS014Json.getInputWeiTuoDanWei(), titlefont2, 30, 0.3f));
        text.add(new Phrase("\n评审人：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS014Json.getInputPingShenRen(), titlefont2, 12, 0.3f));
        text.add(new Phrase(" 评审完成时间：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS014Json.getInputPingShenShiJian0Nian(), titlefont2, 2, 0.3f));
        text.add(new Phrase("年", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS014Json.getInputPingShenShiJian0Yue(), titlefont2, 2, 0.3f));
        text.add(new Phrase("月", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS014Json.getInputPingShenShiJian0Ri(), titlefont2, 2, 0.3f));
        text.add(new Phrase("日", titlefont2));
        text.setLeading(30f);
        text.setSpacingAfter(17f); //设置段落下空白

        float tableWidth = 465;
        float[] widths = new float[33];
        Arrays.fill(widths, tableWidth/33);
        PdfPTable table = ItextUtils.createTable(widths, tableWidth);

        // float[] paddings = new float[]{6f, 6f, 5f, 5f};
        float[] paddings2 = new float[]{8f, 8f, 5f, 5f};
        float[] paddings3 = new float[]{7f, 7f, 5f, 5f};
        float borderWidth = 0.3f;

        table.addCell(ItextUtils.createCell("评审类别与评审项", textfont, Element.ALIGN_LEFT, 5, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("评审内容", textfont, Element.ALIGN_CENTER, 10, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("评审结果", textfont, Element.ALIGN_CENTER, 6, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("评审结果说明", textfont, Element.ALIGN_CENTER, 13, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("一、软件说明部分评审", textfont, Element.ALIGN_LEFT, 33, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("1", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("可用性", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("产品说明对于用户和潜在需方是可用的", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo011(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing011(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("2", textfont, Element.ALIGN_LEFT, 2, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("内容", textfont, Element.ALIGN_LEFT, 3, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("足够用于评价适用性", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0121(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0121(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("排除内在的不一致", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0122(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0122(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("可测试或可验证的", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0123(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0123(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("3", textfont, Element.ALIGN_LEFT, 2, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("标识和标示", textfont, Element.ALIGN_LEFT, 3, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("显示唯一标识", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0131(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0131(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("通过名称版本和日期指称", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0132(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0132(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("包含供方和至少一家经销商的名称和地址", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0133(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0133(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("4", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("功能性陈述", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("根据GB/T 25000.51-2010规范对软件的功能进行陈述", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo014(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing014(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("5", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("可靠性陈述", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("根据GB/T 25000.51-2010规范对软件的可靠性进行陈述", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo015(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing015(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("6", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("易用性陈述", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("根据GB/T 25000.51-2010规范对软件的易用性进行陈述", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo016(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing016(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("7", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("效率性陈述", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("根据GB/T 25000.51-2010规范对软件的效率进行陈述", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo017(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing017(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("8", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("维护性陈述", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("根据GB/T 25000.51-2010规范对软件的维护性进行陈述", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo018(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing018(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("9", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("功能性陈述", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("根据GB/T 25000.51-2010规范对软件的可移植性进行陈述", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo019(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing019(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("10", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("使用质量陈述", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("根据GB/T 25000.51-2010规范对软件的使用质量进行陈述", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0110(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0110(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("二、软件文档集评审", textfont, Element.ALIGN_LEFT, 33, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("1", textfont, Element.ALIGN_LEFT, 2, 22, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("完备性", textfont, Element.ALIGN_LEFT, 3, 22, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("包含所有必需信息", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0211(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0211(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("包含产品说明中所有功能以及可调用功能的说明", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0212(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0212(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("包含可靠性特征及其操作", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0213(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0213(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("包含已处理的和可造成系统失效终止的差错和失效", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0214(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0214(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("必要的数据备份与恢复指南", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0215(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0215(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("所有关键功能的完备的细则信息和参考信息", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0216(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0216(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("陈述产品说明中所有限制", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0217(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0217(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("陈述最大最小磁盘空间", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0218(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0218(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("关于应用管理职能的所有必要信息", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0219(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0219(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("让用户验证是否完成应用管理职能的信息", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo02110(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing02110(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("文档集分若干部分，需给出完整标识", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo02111(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing02111(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("2", textfont, Element.ALIGN_LEFT, 2, 4, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("正确性", textfont, Element.ALIGN_LEFT, 3, 4, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("文档中所有的信息都是正确的。", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0221(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0221(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("没有歧义的信息。", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0222(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0222(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("3", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("一致性", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("文档集中的各文档不相互矛盾, 与产品说明也不矛盾.", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0231(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0231(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("4", textfont, Element.ALIGN_LEFT, 2, 4, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("易理解性", textfont, Element.ALIGN_LEFT, 3, 4, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("使用用户可理解的术语和文体。", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0241(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0241(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("文档集为用户使用该软件提供必要的信息", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0242(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0242(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("5", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("易学性", textfont, Element.ALIGN_LEFT, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("为如何使用该软件提供了足够的信息", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0251(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0251(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("6", textfont, Element.ALIGN_LEFT, 2, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("可操作性", textfont, Element.ALIGN_LEFT, 3, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("电子文档可打印", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0261(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0261(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("有目次(主题词列表)和索引", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0262(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0262(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("不常用术语缩略语有定义", textfont, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenJieGuo0263(), textfont, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS014Json.getInputPingShenShuoMing0263(), textfont, Element.ALIGN_LEFT, 13, 2, paddings3, borderWidth));

        // float[] paddings4 = new float[]{20f, 0, 0, 0};
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_CENTER, 15, 2, paddings2, 0f));
        table.addCell(ItextUtils.createCell("检查人：", textfont, Element.ALIGN_LEFT, 4, 2, paddings2, 0f));
        table.addCell(ItextUtils.createCell(" ", textfont, Element.ALIGN_RIGHT, 14, 2, paddings2, 0f));

        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(text);
        document.add(table);
    }
}
