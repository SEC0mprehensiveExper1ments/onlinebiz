package com.njustc.onlinebiz.doc.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase.TestcaseList;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS008;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class DocServiceJS008 {

    private final OSSProvider ossProvider;

    public DocServiceJS008(OSSProvider ossProvider) {
        this.ossProvider = ossProvider;
    }

    /**
     * 以下是文档生成部分
     * */
    private static final int marginLeft;
    private static final int marginRight;
    private static final int marginTop;
    private static final int marginBottom;

    @Value("${document-dir}")
    private String DOCUMENT_DIR;

    private static Font titlefont1;
    private static Font titlefont2;
    private static Font textfont;

    static {
        // absolutePath = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("font")).getPath().substring(1) + "/../";
        // ---> 下面有com, font, out, static
        // 在 iText 中每一个单位大小默认近似于点（pt）
        // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
        marginLeft = 50; // 页边距：左
        marginRight = 50; // 页边距：右
        marginTop = 20; // 页边距：上
        marginBottom = 60; // 页边距：下
    }

    private static JS008 JS008Json;

    /** 填充JS008文档 */
    public String fill(String testcaseId, JS008 newJson) {
        JS008Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS008_" + testcaseId + ".pdf";
        try {
            // 1.新建document对象
            Rectangle pageSizeJS008 = new RectangleReadOnly(841.8F, 595.2F);
            Document document = new Document(pageSizeJS008);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS008－2011";
            String[] footer = new String[]{"第 ", " 页，共 ", " 页"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHaderLine = false;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() - 20};
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
                    "doc", "JS008_" + testcaseId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS008_" + testcaseId + ".pdf";
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

    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            BaseFont bfChinese = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont1 = new Font(bfChinese, 20, Font.NORMAL);
            titlefont2 = new Font(bfChinese, 12, Font.BOLD);
            // keyfont = new Font(bfChinese, 12.5f, Font.BOLD);
            textfont = new Font(bfChinese, 12f, Font.NORMAL);
        } catch (Exception e) { e.printStackTrace(); }


        // 标题
        Paragraph title = new Paragraph("测试用例(项目编号)", titlefont1);
        title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(0f); // 设置段落上空白
        title.setSpacingAfter(15f); // 设置段落下空白

        // 表格
        float tableWidth = document.right()-document.left();
        float[] widths = new float[58];
        // 每一行的各单元的span col数值要达到30
        Arrays.fill(widths, tableWidth/58);
        // 行列每个基础单元格为 5mm x 5mm
        PdfPTable table = ItextUtils.createTable(widths, tableWidth);

        float[] paddings3 = new float[]{4f, 4f, 3f, 3f};        // 上下左右的间距
        float borderWidth = 0.3f;

        table.addCell(ItextUtils.createCell("测试分类", titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("ID", titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("测试用例设计说明", titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("与本测试用例有关的规约说明", titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("预期的结果", titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("测试用例设计者", titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("测试时间", titlefont2, Element.ALIGN_CENTER, 12, 2, paddings3, borderWidth));

        List<TestcaseList> testcases = JS008Json.getTestcases();
        for (TestcaseList testcaseList: testcases) {
            table.addCell(ItextUtils.createCell(testcaseList.getCategory(), textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testcaseList.getTestcaseId(), textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testcaseList.getDesignInstruction(), textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testcaseList.getStatute(), textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testcaseList.getExpectedResult(), textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testcaseList.getDesigner(), textfont, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testcaseList.getTime(), textfont, Element.ALIGN_CENTER, 12, 2, paddings3, borderWidth));
        }

        document.add(title);
        document.add(table);
    }


}
