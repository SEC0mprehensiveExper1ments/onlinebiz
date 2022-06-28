package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS010;
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
public class DocServiceJS010 {
    private final OSSProvider ossProvider;

    public DocServiceJS010(OSSProvider ossProvider) {
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
        // absolutePath = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("font")).getPath() + "/../";
        // 在 iText 中每一个单位大小默认近似于点（pt）
        // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
        marginLeft = 85f;
        marginRight = 85f;
        marginTop = 60;
        marginBottom = 72;
    }

    private static JS010 JS010Json;

    /**
     * 填充JS010文档
     * */
    public String fill(String reportReviewId, JS010 newJson) {
        JS010Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS010_" + reportReviewId + ".pdf";
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
            String header = "NST－04－JS010－2011";
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
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to generate a pdf";
        }
        // 上传pdf
        try {
            if (ossProvider.upload(
                    "doc", "JS010_" + reportReviewId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS010_" + reportReviewId + ".pdf";
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

    private static Font titlefont1;
    private static Font titlefont2;
    private static Font keyfont;
    private static Font textfont;

    /**
     * 生成JS010文档第一页
     * */
    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            BaseFont bfSimSun = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont1 = new Font(bfSimSun, 22f, Font.BOLD);
            titlefont2 = new Font(bfSimSun, 14f, Font.BOLD);
            keyfont = new Font(bfSimSun, 12f, Font.BOLD);
            textfont = new Font(bfSimSun, 12f, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 标题
        Paragraph title = new Paragraph("测 试 报 告 检 查 表", titlefont1);
        title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
//        paragraph.setIndentationLeft(12); //设置左缩进
//        paragraph.setIndentationRight(12); //设置右缩进
//        paragraph.setFirstLineIndent(24); //设置首行缩进
//        paragraph.setLeading(0f); //行间距
        title.setSpacingBefore(-17f); //设置段落上空白
        title.setSpacingAfter(35f); //设置段落下空白

        float tableWidth = 450;
        float[] widths = new float[32];
        Arrays.fill(widths, tableWidth/32);
        // 行列每个基础单元格为 5mm x 5mm
        PdfPTable table = ItextUtils.createTable(widths, tableWidth);

        float[] paddings = new float[]{4.5f, 4.5f, 5f, 5f};
        float borderWidth = 0.3f;

        table.addCell(ItextUtils.createCell("软件名称", titlefont2, Element.ALIGN_CENTER, 9, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputRuanJianMingChen(), textfont, Element.ALIGN_CENTER, 23, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("委托单位", titlefont2, Element.ALIGN_CENTER, 9, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputWeiTuoDanWei(), textfont, Element.ALIGN_CENTER, 23, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("序号", keyfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("检查内容", keyfont, Element.ALIGN_CENTER, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("内容描述", keyfont, Element.ALIGN_CENTER, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("检查结果", keyfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("1", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("报告编号", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("检查报告编号的正确性（是否符合编码规则）与前后的一致性（报告首页与每页页眉）。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo01(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("2", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("页码", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("检查页码与总页数是否正确（报告首页与每页页  眉）。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo02(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("3", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("软件名称", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("是否和确认单一致，是否前后一致（共三处，包括首页、报告页、附件三）。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo03(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("4", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("版本号", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("是否和确认单一致，是否前后一致（共二处，包括首页、报告页）。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo04(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("5", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("委托单位", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("是否和确认单一致，是否前后一致（共二处，包括首页、报告页）。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo05(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("6", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("完成日期", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("是否前后一致 (共二处，包括首页、报告页页末)。", textfont, Element.ALIGN_LEFT, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo06(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("7", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("委托单位地址", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("是否和确认单一致（共一处，报告页）。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo07(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("8", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("序号", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("附件二、附件三中的序号是否正确、连续。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo08(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("9", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("测试样品", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("样品名称是否正确，数量是否正确。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo09(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("10", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("软、硬件列表", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("列表是否完整(如打印机),用途描述是否合理正确。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo010(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("11", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("文字、内容、格式", textfont, Element.ALIGN_JUSTIFIED, 29, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("11.1", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("错别字", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("报告中是否还有错别字。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo0111(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("11.2", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("语句", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("报告的语句是否通顺合理；每个功能描述结束后是否都有句号。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo0112(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("11.3", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("格式", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("报告的格式是否美观，字体是否一致，表格大小是否一致。（如无特殊情况请尽量不要将报告页中的表格分为2页。）", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo0113(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        table.addCell(ItextUtils.createCell("12", textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("用户文档测试报告", textfont, Element.ALIGN_JUSTIFIED, 6, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell("语句是否通顺，是否准确描述用户的文档。", textfont, Element.ALIGN_JUSTIFIED, 20, 2, paddings, borderWidth));
        table.addCell(ItextUtils.createCell(JS010Json.getInputJianChaJieGuo012(), textfont, Element.ALIGN_CENTER, 3, 2, paddings, borderWidth));

        float[] paddings2 = new float[]{15f, 0, 0, 0};
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_CENTER, 15, 2, paddings2, 0f));
        table.addCell(ItextUtils.createCell("检查人：", textfont, Element.ALIGN_LEFT, 4, 2, paddings2, 0f));
        table.addCell(ItextUtils.createCell(" ", textfont, Element.ALIGN_RIGHT, 5, 2, paddings2, 0f));
        table.addCell(ItextUtils.createCell("日期:", textfont, Element.ALIGN_RIGHT, 3, 2, paddings2, 0f));
        table.addCell(ItextUtils.createCell(" ", textfont, Element.ALIGN_RIGHT, 6, 2, paddings2, 0f));

        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(table);
    }
}
