package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS002;
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
public class DocServiceJS002 {
    private final OSSProvider ossProvider;

    public DocServiceJS002(OSSProvider ossProvider) {
        this.ossProvider = ossProvider;
    }

    /**
     * 以下是文档生成部分
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
        marginLeft = 65f;
        marginRight = 65f;
        marginTop = 68f;
        marginBottom = 65f;
    }

    private JS002 JS002Json;
    /**
     * 填充JS002文档
     * @param newJson JS002对象
     * @return 成功返回OSS链接，失败返回原因
     * */
    public String fill(String entrustId, JS002 newJson){
        JS002Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS002_" + entrustId + ".pdf";
//        System.out.println(DOCUMENT_DIR);
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS002－2011";
            String[] footer = new String[]{"南京大学计算机软件新技术国家重点实验室       密级范围：内部                   第", "页 共", "页"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHeaderLine = true;
            boolean isFooterLine = true;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
            float[] footerLoc = new float[]{document.left(), document.bottom() - 20};
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
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to generate a pdf";
        }
        // 上传pdf
        try {
            if(ossProvider.upload(
                    "doc", "JS002_" + entrustId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS002_" + entrustId + ".pdf";
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

    private Font titlefont;
    private Font titlefont2;
    private  Font symbolfont;
    private Font textfont;
    private Font textfontBOLD;
    private Font smallFourBoldFont;
    private Font smallFourFont;

    private static final String singleChoiceBlank = "\u0049";
    private static final String multiChoiceBlank = "\u004E";
    private static final String singleChoiceFilled = "\u004C";
    private static final String multiChoiceFilled = "\u0051";

    private static final String[] singleChoice = {"\u0049", "\u004C"};      /* 0--Blank, 1--Ticked */
    private static final String[] multiChoice = {"\u004E", "\u0051"};      /* 0--Blank, 1--Ticked */

    private static final String[] singleChoiceNoCircle = {" ", "\u0050"};        /* 0--Blank, 1--Ticked */
    private Font sixfont;

    /**
     * 生成JS002文档第一页
     * */
    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            // 定义全局的字体静态变量
            BaseFont bfSimSun = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont bfUnicode = BaseFont.createFont(DOCUMENT_DIR + "font/check.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            titlefont = new Font(bfSimSun, 17f, Font.BOLD);
            titlefont2 = new Font(bfSimSun, 12f, Font.NORMAL);
            symbolfont = new Font(bfUnicode, 14, Font.NORMAL);
            textfont = new Font(bfSimSun, 11f, Font.NORMAL);
            textfontBOLD = new Font(bfSimSun, 11f, Font.BOLD);
            smallFourBoldFont = new Font(bfSimSun, 12f, Font.BOLD);
            smallFourFont = new Font(bfSimSun, 12f, Font.NORMAL);
            sixfont = new Font(bfSimSun, 8f, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 标题
        Paragraph title = new Paragraph("软件项目委托测试申请书", titlefont);
        title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(-30f); //设置段落上空白
        title.setSpacingAfter(15f); //设置段落下空白

        Paragraph text = new Paragraph();
        text.add(new Phrase("请用", textfont));
        text.add(new Phrase("\u0050", symbolfont));
        text.add(new Phrase("选择：",  textfont));
        text.add(new Phrase(singleChoiceBlank,  symbolfont));
        text.add(new Phrase("——单选； ",  textfont));
        text.add(new Phrase(multiChoiceBlank, symbolfont));
        text.add(new Phrase("——多选。", textfont));
        text.setSpacingAfter(5f); //设置段落下空白

        float tableWidth = 465;
        float[] widths = new float[33];
        Arrays.fill(widths, tableWidth/33);
        PdfPTable table = ItextUtils.createTable(widths, tableWidth);

        float[] paddings = new float[]{5f, 6.5f, 5.5f, 5.5f};
        float[] paddings2 = new float[]{10f, 10f, 5f, 5f};
        float[] paddings3 = new float[]{3.5f, 4.5f, 5.5f, 5.5f};
        float borderWidth = 0.5f;
        float fixedLeading = 14f;
        PdfPCell cell;
        Phrase phrase;

        table.addCell(ItextUtils.createCell("测试类型", titlefont2, Element.ALIGN_CENTER, 5, 4, paddings2, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 28, 2, fixedLeading, paddings, borderWidth);
        phrase = ItextUtils.crossSetFont(new String[]{"",
                multiChoice[isTicked(JS002Json.getMultiCeShiLeiXing01())], "软件确认测试      ",
                multiChoice[isTicked(JS002Json.getMultiCeShiLeiXing02())], "其他："}, textfont, symbolfont);
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputCeShiLeiXing02(), textfont, 10, 0.7f, true));
        cell.setPhrase(phrase);
        table.addCell(cell);
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 28, 2, fixedLeading, paddings3, borderWidth);
        phrase = ItextUtils.crossSetFont(new String[]{"",
                multiChoice[isTicked(JS002Json.getMultiCeShiLeiXing03())], "成果/技术鉴定测试      ",
                multiChoice[isTicked(JS002Json.getMultiCeShiLeiXing04())], "专项资金验收测试"}, textfont, symbolfont);
        cell.setPhrase(phrase);
        table.addCell(cell);

        table.addCell(ItextUtils.createCell("软件名称", titlefont2, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputRuanJianMingChen(), titlefont2, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("版本号", titlefont2, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputBanBenHao(), titlefont2, Element.ALIGN_LEFT, 6, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("委托单位（中文）", titlefont2, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputWeiTuoDanWeiZhongWen(), titlefont2, Element.ALIGN_LEFT, 28, 2, fixedLeading, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("委托单位（英文）", titlefont2, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputWeiTuoDanWeiYingWen(), titlefont2, Element.ALIGN_LEFT, 28, 2, fixedLeading, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("开发单位", titlefont2, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputKaiFaDanWei(), titlefont2, Element.ALIGN_LEFT, 28, 2, fixedLeading, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("单位性质", titlefont2, Element.ALIGN_CENTER, 5, 2, paddings, borderWidth));

        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 28, 2, fixedLeading, paddings3, borderWidth);
        phrase = ItextUtils.crossSetFont(new String[]{"",
                singleChoice[isTicked(JS002Json.getSingleDanWeiXingZhi01())], "内资企业     ", singleChoice[isTicked(JS002Json.getSingleDanWeiXingZhi02())], "外(合)资企业     ", singleChoice[isTicked(JS002Json.getSingleDanWeiXingZhi03())], "港澳台(合)资企业\n",
                singleChoice[isTicked(JS002Json.getSingleDanWeiXingZhi04())], "科研院校     ", singleChoice[isTicked(JS002Json.getSingleDanWeiXingZhi05())], "政府事业团体     ", singleChoice[isTicked(JS002Json.getSingleDanWeiXingZhi06())], "其他"}, textfont, symbolfont);
        cell.setPhrase(phrase);
        table.addCell(cell);

        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 33, 2,fixedLeading,  paddings3, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("软件用户对象描述： ", titlefont2));
        phrase.add(new Chunk(JS002Json.getInputRuanJianMiaoShu() + "\n\n", titlefont2));
        cell.setFixedHeight(90f);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPhrase(phrase);
        table.addCell(cell);

        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 33, 2,fixedLeading,  paddings3, borderWidth);
        cell.setFixedHeight(170f);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        phrase = new Phrase();
        phrase.add(new Chunk("主要功能及用途简介（限200字）： ", titlefont2));
        phrase.add(new Chunk(JS002Json.getInputZhuYaoGongNeng(), titlefont2));
        cell.setPhrase(phrase);
        table.addCell(cell);

        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 33, 2, 18f,  paddings, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("测试依据： \n", titlefont2));
        phrase.add(ItextUtils.crossSetFont(new String[]{"",
                multiChoice[isTicked(JS002Json.getMultiCeShiYiJu01())], "GB/T 25000.51-2010     ", multiChoice[isTicked(JS002Json.getMultiCeShiYiJu02())], "GB/T 16260.1-2006\n",
                multiChoice[isTicked(JS002Json.getMultiCeShiYiJu03())], "NST-03-WI12-2011       ", multiChoice[isTicked(JS002Json.getMultiCeShiYiJu04())], "NST-03-WI13-2011\n",
                multiChoice[isTicked(JS002Json.getMultiCeShiYiJu05())], "其他"}, titlefont2, symbolfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputCeShiYiJu05(), textfont, 15, 0.7f, true));
        cell.setPhrase(phrase);
        table.addCell(cell);

        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 33, 2, 18f,  paddings, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("需要测试的技术指标：\n", titlefont2));
        phrase.add(ItextUtils.crossSetFont(new String[]{"",
                multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao01())], "功能性   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao02())], "可靠性   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao03())], "易用性性   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao04())], "效率   ",
                multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao05())], "可维护性   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao06())], "可移植性\n",
                multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao07())], "代码覆盖度   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao08())], "缺陷检测率   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao09())], "代码风格符合度   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao010())], "代码不符合项检测率\n",
                multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao011())], "产品说明要求   ", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao012())], "用户文档集要求\n", multiChoice[isTicked(JS002Json.getMultiJiShuZhiBiao013())], "其他"}, titlefont2, symbolfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputJiShuZhiBiao013(), textfont, 15, 0.7f, true));
        System.out.println("JiShuZhiBiao013 : : " + JS002Json.getMultiJiShuZhiBiao013() + "\n");
        System.out.println("JiShuZhiBiao013 : : " + JS002Json.getInputJiShuZhiBiao013() + "\n");
        cell.setPhrase(phrase);
        table.addCell(cell);

        cell = ItextUtils.createCell(Element.ALIGN_CENTER, 5, 6, 18f,  paddings3, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("软件规模", titlefont2));
        phrase.add(new Chunk("(至少一种)", textfont));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("功能数（到最后一级菜单）", titlefont2, Element.ALIGN_LEFT, 19, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputRuanJianGuiMo01(), titlefont2, Element.ALIGN_LEFT, 9, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("功能点数", titlefont2, Element.ALIGN_LEFT, 19, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputRuanJianGuiMo02(), titlefont2, Element.ALIGN_LEFT, 9, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("代码行数（不包括注释行、空行）", titlefont2, Element.ALIGN_LEFT, 19, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputRuanJianGuiMo03(), titlefont2, Element.ALIGN_LEFT, 9, 2, fixedLeading, paddings3, borderWidth));

        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 33, 2, 18f,  paddings3, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("软件类型", titlefont2));
        phrase.add(new Chunk("（单元）:\n", textfont));
        phrase.add(ItextUtils.crossSetFont(new String[]{
                "系统软件：",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0XiTong01())], "操作系统       ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0XiTong02())], "中文处理系统   ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0XiTong03())], "网络系统  ",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0XiTong04())], "嵌入式操作系统 ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0XiTong05())], "其他\n" +
                "支持软件：",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0ZhiChi01())], "程序设计语言   ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0ZhiChi02())], "数据库系统设计 ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0ZhiChi03())], "工具软件  ",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0ZhiChi04())], "网络通信软件\n          ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0ZhiChi05())], "中间件         ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0ZhiChi06())], "其他\n" +
                "应用软件：",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong01())], "行业管理软件   ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong02())], "办公软件  ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong03())], "模式识别软件 ",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong04())], "图形图像软件 ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong05())], "控制软件\n          ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong06())], "网络应用软件   ",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong07())], "信息管理软件 ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong08())], "数据库管理应用软件  ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong09())], "安全与保密软件\n          ",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong010())], "嵌入式应用软件 ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong011())], "教育软件     ", singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong012())], "游戏软件            ",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0YingYong013())], "其他\n" +
                "其他：    ",
                singleChoice[isTicked(JS002Json.getSingleRuanJianLeiXing0QiTa01())], "其他"}, textfont, symbolfont));
        cell.setPhrase(phrase);
        table.addCell(cell);

        table.addCell(ItextUtils.createCell("运行环境", titlefont2, Element.ALIGN_CENTER, 2, 14, 18f,  paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("客户端", textfont, Element.ALIGN_CENTER, 2, 2, 18f,  paddings3, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 29, 2, 18f,  paddings3, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"操作系统：", multiChoice[isTicked(JS002Json.getMultiKeHuDuan0Windows())], "Windows "}, textfont, symbolfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputKeHuDuan0Windows(), textfont, 3, 0.7f, false));
        phrase.add(ItextUtils.crossSetFont(new String[]{"（版本) ", multiChoice[isTicked(JS002Json.getInputKeHuDuan0Linux())], "Linux "}, textfont, symbolfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputKeHuDuan0Linux(), textfont, 3, 0.7f, false));
        phrase.add(ItextUtils.crossSetFont(new String[]{"(版本) ", multiChoice[isTicked(JS002Json.getMultiKeHuDuan0QiTa())], "其他 "}, textfont, symbolfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputKeHuDuan0QiTa(), textfont, 5, 0.7f, false));
        phrase.add(new Chunk("\n内存要求：", textfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputKeHuDuan0NeiCunYaoQiu(), textfont, 6, 0.7f, true));
        phrase.add(new Chunk("MB\n其他要求：", textfont));
        phrase.add(new Chunk(JS002Json.getInputKeHuDuan0QiTaYaoQiu(), textfont));
        cell.setPhrase(phrase);
        table.addCell(cell);

        table.addCell(ItextUtils.createCell("服务器端", textfont, Element.ALIGN_CENTER, 2, 10, 18f,  paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("硬件", textfont, Element.ALIGN_CENTER, 2, 2, 18f,  paddings3, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 27, 2, 18f,  paddings3, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"架构：", multiChoice[isTicked(JS002Json.getMultiFuWuQiYingJian0PC())], "PC服务器   ", multiChoice[isTicked(JS002Json.getMultiFuWuQiYingJian0Linux())], "UNIX/Linux服务器   ", multiChoice[isTicked(JS002Json.getMultiFuWuQiYingJian0QiTa())], "其他"}, textfont, symbolfont));
        System.out.println("Test :: " + JS002Json.getInputFuWuQiYingJian0QiTa());
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputFuWuQiYingJian0QiTa(), textfont, 10, 0.7f, true));
        phrase.add(new Chunk("\n内存要求：", textfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputFuWuQiYingJian0NeiCunYaoQiu(), textfont, 6, 0.7f, true));
        phrase.add(new Chunk("MB\n硬盘要求：", textfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputFuWuQiYingJian0YingPanYaoQiu(), textfont, 6, 0.7f, true));
        phrase.add(new Chunk("MB\n其他要求：", textfont));
        phrase.add(new Chunk(JS002Json.getInputFuWuQiYingJian0QiTaYaoQiu(), textfont));
        cell.setPhrase(phrase);
        table.addCell(cell);

        table.addCell(ItextUtils.createCell("软件", textfont, Element.ALIGN_CENTER, 2, 8, 18f,  paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("操作系统", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputFuWuQiRuanJian0CaoZuoXiTong(), textfont, Element.ALIGN_CENTER, 11, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("版本", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputFuWuQiRuanJian0BanBen(), textfont, Element.ALIGN_CENTER, 8, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("编程语言", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputFuWuQiRuanJian0BianChengYuYan(), textfont, Element.ALIGN_CENTER, 11, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("构架", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings3, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 11, 2, 12f,  new float[]{4f, 7f, 4f, 4f}, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"", multiChoice[isTicked(JS002Json.getMultiFuWuQiRuanJian0GouJia0CS())], "C/S   ", multiChoice[isTicked(JS002Json.getMultiFuWuQiRuanJian0GouJia0BS())], "B/S   ", multiChoice[isTicked(JS002Json.getMultiFuWuQiRuanJian0GouJia0QiTa())], "其他"}, textfont, symbolfont));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("数据库", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputFuWuQiRuanJian0ShuJuKu(), textfont, Element.ALIGN_CENTER, 11, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("中间件", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputFuWuQiRuanJian0ZhongJianJian(), textfont, Element.ALIGN_CENTER, 8, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("其他支撑软件", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputFuWuQiRuanJian0QiTaZhiCheng(), textfont, Element.ALIGN_CENTER, 23, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("网络环境", textfont, Element.ALIGN_CENTER, 4, 2, fixedLeading, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputWangLuoHuanJing(), textfont, Element.ALIGN_CENTER, 27, 2, fixedLeading, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("样品和数量", titlefont2, Element.ALIGN_CENTER, 2, 8, 18f,  paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("软件介质", textfont, Element.ALIGN_CENTER, 4, 2, 18f,  paddings3, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 27, 2, 18f,  paddings, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{
                "光盘（", singleChoiceNoCircle[isTicked(JS002Json.getSingleRuanJianJieZhi0GuangPan())],
                "）      U盘（", singleChoiceNoCircle[isTicked(JS002Json.getSingleRuanJianJieZhi0UPan())],
                "）       其它（", singleChoiceNoCircle[isTicked(JS002Json.getSingleRuanJianJieZhi0QiTa())],
                "）", singleChoiceNoCircle[isTicked(JS002Json.getSingleRuanJianLeiXing0QiTa01())], ""}, textfont, symbolfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputRuanJianJieZhi0QiTa(), textfont, 10, 0.7f, true));
        cell.setPhrase(phrase);
        table.addCell(cell);

        table.addCell(ItextUtils.createCell("文档资料", textfont, Element.ALIGN_CENTER, 4, 4, 18f,  paddings3, borderWidth));
        PdfPCell tmpCell = ItextUtils.createCell(JS002Json.getInputWenDangZiLiao(), textfont, Element.ALIGN_LEFT, 27, 2, 18f, new float[]{3f, 3f, 5.5f, 5.5f}, borderWidth);
        tmpCell.setFixedHeight(90f);
        tmpCell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(tmpCell);
        table.addCell(ItextUtils.createCell(
                "注：1、需求文档（例如：项目计划任务书、需求分析报告、合同等）（验收、鉴定测试必须）\n" +
                        "    2、用户文档（例如：用户手册、用户指南等）（必须）\n" +
                        "    3、操作文档（例如：操作员手册、安装手册、诊断手册、支持手册等）（验收项目必须）\n", sixfont, Element.ALIGN_LEFT, 27, 2, 12f,  paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("提交的样品（硬拷贝资料、硬件）\n" +
                "                五年保存期满：", textfont, Element.ALIGN_CENTER, 13, 2, 15f,  paddings3, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 18, 2, 12f,  new float[]{5f, 7f, 10f, 4f}, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"", singleChoice[isTicked(JS002Json.getSingleYangPingChuLi01())], "由本实验室销毁       ", singleChoice[isTicked(JS002Json.getSingleYangPingChuLi02())], "退还给我们"}, textfont, symbolfont));
        cell.setPhrase(phrase);
        table.addCell(cell);

        table.addCell(ItextUtils.createCell("委托单位信息", smallFourBoldFont, Element.ALIGN_CENTER, 2, 2, 18f,  paddings3, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 13, 2, 20f,  new float[]{3f, 7f, 2f, 2f}, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("电  话: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0DianHua(), smallFourFont, 10, 0.7f, true));
        phrase.add(new Chunk("\n传  真: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0ChuanZhen(), smallFourFont, 10, 0.7f, true));
        phrase.add(new Chunk("\n地  址: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0DiZhi(), smallFourFont, 10, 0.7f, true));
        phrase.add(new Chunk("\n邮  编: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0YouBian(), smallFourFont, 10, 0.7f, true));
        phrase.add(new Chunk("\n联系人: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0LianXiRen(), smallFourFont, 10, 0.7f, true));
        phrase.add(new Chunk("\n手  机: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0ShouJi(), smallFourFont, 10, 0.7f, true));
        phrase.add(new Chunk("\nE-mail: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0Email(), smallFourFont, 10, 0.7f, true));
        phrase.add(new Chunk("\n网  址: ", smallFourFont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputWeiTuoDanWei0WangZhi(), smallFourFont, 10, 0.5f, true));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("国家重点实验室联系方式", smallFourBoldFont, Element.ALIGN_CENTER, 2, 2, 14f,  paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(
                "单位地址: 南京市栖霞区仙林大道163号\n" +
                        "邮政编码: 210023\n" +
                        "电话: 86-25-89683467\n" +
                        "传真: 86-25-89686596\n" +
                        "网址: http://keysoftlab.nju.edu.cn \n" +
                        "E-mail: keysoftlab@nju.edu.cn", smallFourFont, Element.ALIGN_LEFT, 16, 2, 18f,  paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("", smallFourBoldFont, Element.ALIGN_CENTER, 2, 14, 18f,  paddings3, borderWidth));
        float[] paddings4 = new float[]{2f, 6f, 6f, 6f};
        table.addCell(ItextUtils.createCell("密级", textfontBOLD, Element.ALIGN_LEFT, 6, 2, 14f,  paddings4, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 25, 2, 14f,  new float[]{3f, 5f, 2f, 2f}, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"",
                singleChoice[isTicked(JS002Json.getSingleMiJi01())], "无密级  ", singleChoice[isTicked(JS002Json.getSingleMiJi02())], "秘密  ", singleChoice[isTicked(JS002Json.getSingleMiJi03())], "机密", }, textfont, symbolfont));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("查杀病毒", textfontBOLD, Element.ALIGN_LEFT, 6, 2, 14f,  new float[]{3f, 7f, 6f, 6f}, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 25, 2, 14f,  new float[]{3f, 7f, 2f, 2f}, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"",
                singleChoice[isTicked(JS002Json.getSingleChaShaBingDu01())], "已完成  ", singleChoice[isTicked(JS002Json.getSingleChaShaBingDu02())], "无法完成（所用查杀工具"}, textfont, symbolfont));
        phrase.add(ItextUtils.leastUnderlineChunk(JS002Json.getInputChaShaBingDu02(), textfont, 8, 0.7f, false));
        phrase.add(new Chunk("）", textfont));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("材料检查", textfontBOLD, Element.ALIGN_LEFT, 6, 2, 14f,  paddings4, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 25, 2, 14f,  new float[]{3f, 5f, 2f, 2f}, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{
                "测试样品：",
                multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0CeshiYangPing01())], "源代码  ", multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0CeshiYangPing02())], "可执行文件\n" +
                "需求文档：",
                multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0XuQiuWenDang01())], "项目计划任务书 ", multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0XuQiuWenDang02())], "需求分析报告 ", multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0XuQiuWenDang03())], "合同\n" +
                "用户文档：",
                multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0YongHuWenDang01())], "用户手册  ", multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0YongHuWenDang02())], "用户指南\n" +
                "操作文档：",
                multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0CaoZuoWenDang01())], "操作员手册  ", multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0CaoZuoWenDang02())], "安装手册  ", multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0CaoZuoWenDang03())], "诊断手册  ", multiChoice[isTicked(JS002Json.getMultiCaiLiaoJianCha0CaoZuoWenDang04())], "支持手册\n" +
                "其他："}, textfont, symbolfont));
        phrase.add(new Chunk(JS002Json.getInputCaiLiaoJianCha0QiTa(), textfont));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("确认意见", textfontBOLD, Element.ALIGN_LEFT, 6, 2, 14f,  paddings4, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 25, 2, 14f,  new float[]{3f, 5f, 2f, 2f}, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"",
                singleChoice[isTicked(JS002Json.getSingleQueRenYiJian01())], "测试所需材料不全，未达到受理条件。\n",
                singleChoice[isTicked(JS002Json.getSingleQueRenYiJian02())], "属依据国家标准或自编非标规范进行的常规检测，有资质、能力和资源满足委托方要求。 \n",
                singleChoice[isTicked(JS002Json.getSingleQueRenYiJian03())], "无国家标准和规范依据，或实验室缺乏检测设备和工具，无法完成检测。\n",
                singleChoice[isTicked(JS002Json.getSingleQueRenYiJian04())], "超出实验室能力和资质范围，无法完成检测。"}, textfont, symbolfont));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("受理意见", textfontBOLD, Element.ALIGN_LEFT, 6, 2, 14f,  paddings4, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 25, 2, 14f,  new float[]{3f, 5f, 2f, 2f}, borderWidth);
        phrase = new Phrase();
        phrase.add(ItextUtils.crossSetFont(new String[]{"",
                singleChoice[isTicked(JS002Json.getSingleShouLiYiJian01())], "受理-进入项目立项和合同评审流程  ",
                singleChoice[isTicked(JS002Json.getSingleShouLiYiJian02())], "不受理  ",
                singleChoice[isTicked(JS002Json.getSingleShouLiYiJian03())], "进一步联系"}, textfont, symbolfont));
        cell.setPhrase(phrase);
        table.addCell(cell);
        table.addCell(ItextUtils.createCell("测试项目编号", textfontBOLD, Element.ALIGN_LEFT, 6, 2, 14f,  paddings4, borderWidth));
        table.addCell(ItextUtils.createCell(JS002Json.getInputCeShiXiangMuBianHao(), textfont, Element.ALIGN_LEFT, 25, 2, 18f,  paddings4, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 31, 2, 14f,  paddings4, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("备注: ", textfontBOLD));
        phrase.add(new Chunk(JS002Json.getInputBeiZhu() +
                "\n\n\n\n\n\n", textfont));
        phrase.add(new Chunk("受理人（签字）____________________      日期 ____________________", textfontBOLD));
        cell.setPhrase(phrase);
        table.addCell(cell);

        table.addCell(ItextUtils.createCell("委托人填写", textfontBOLD, Element.ALIGN_LEFT, 2, 2, 14f,  paddings4, borderWidth));
        cell = ItextUtils.createCell(Element.ALIGN_LEFT, 31, 2, 14f,  paddings4, borderWidth);
        phrase = new Phrase();
        phrase.add(new Chunk("\n\n\n\n", textfont));
        phrase.add(new Chunk("受理人（签字）____________________      日期 ____________________", textfontBOLD));
        cell.setPhrase(phrase);
        table.addCell(cell);

        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(text);
        document.add(table);
    }

    static int isTicked(String option) {
        int i;
        try {
            i = Integer.parseInt(option);
        } catch (Exception e) { i = 0; }
        if (i > 1 || i < 0) i = 0;
        return i;
    }

}
