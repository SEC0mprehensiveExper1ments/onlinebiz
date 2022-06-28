package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS011;
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
public class DocServiceJS011 {

    private final OSSProvider ossProvider;

    public DocServiceJS011(OSSProvider ossProvider) {
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

    static {
        // absolutePath = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("font")).getPath().substring(1) + "/../";
        // ---> 下面有com, font, out, static
        // 在 iText 中每一个单位大小默认近似于点（pt）
        // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
        marginLeft = 35; // 页边距：左
        marginRight = 35; // 页边距：右
        marginTop = 20; // 页边距：上
        marginBottom = 60; // 页边距：下
    }

    private static JS011 JS011Json;
    /** 填充JS011文档 */
    public String fill(String testIssueId, JS011 newJson) {
        JS011Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS011_" + testIssueId + ".pdf";
        try {
            // 1.新建document对象
            Rectangle pageSizeJS008 = new RectangleReadOnly(841.8F, 595.2F);
            Document document = new Document(pageSizeJS008);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS011－2011";
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
                    "doc", "JS011_" + testIssueId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS011_" + testIssueId + ".pdf";
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


    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            BaseFont bfChinese = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont1 = new Font(bfChinese, 20, Font.NORMAL);
            titlefont2 = new Font(bfChinese, 11f, Font.BOLD);
        } catch (Exception e) { e.printStackTrace(); }

        // 标题
        Paragraph title = new Paragraph("软件测试问题清单(项目编号)", titlefont1);
        title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(0f); // 设置段落上空白
        title.setSpacingAfter(15f); // 设置段落下空白

        // 表格
        float tableWidth = document.right()-document.left();
        float[] widths = new float[59];
        // 每一行的各单元的span col数值要达到30
        Arrays.fill(widths, tableWidth/59);
        // 行列每个基础单元格为 5mm x 5mm
        PdfPTable table = ItextUtils.createTable(widths, tableWidth);

        float[] paddings3 = new float[]{4f, 4f, 3f, 3f};        // 上下左右的间距
        float borderWidth = 0.3f;

        table.addCell(ItextUtils.createCell("序号", titlefont2, Element.ALIGN_CENTER, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("问题（缺陷）简要描述", titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("对应需求条目", titlefont2, Element.ALIGN_CENTER, 7, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("发现缺陷的初始条件", titlefont2, Element.ALIGN_CENTER, 7, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("发现缺陷用例及具体操作路径（要具体）", titlefont2, Element.ALIGN_CENTER, 13, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("关联用例", titlefont2, Element.ALIGN_CENTER, 4, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("发现时间", titlefont2, Element.ALIGN_CENTER, 4, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("责任人", titlefont2, Element.ALIGN_CENTER, 3, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("修改建议", titlefont2, Element.ALIGN_CENTER, 10, 2, paddings3, borderWidth));

        List<TestIssueList.TestIssue> testIssues = JS011Json.getTestIssues();
        for (TestIssueList.TestIssue testIssue: testIssues) {
            table.addCell(ItextUtils.createCell(testIssue.getTestIssueId(), titlefont2, Element.ALIGN_CENTER, 3, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getDescription(), titlefont2, Element.ALIGN_CENTER, 8, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getCorrespondingRequirement(), titlefont2, Element.ALIGN_CENTER, 7, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getInitialConditions(), titlefont2, Element.ALIGN_CENTER, 7, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getSpecificOperation(), titlefont2, Element.ALIGN_CENTER, 13, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getAssociatedCase(), titlefont2, Element.ALIGN_CENTER, 4, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getFindTime(), titlefont2, Element.ALIGN_CENTER, 4, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getResponsiblePerson(), titlefont2, Element.ALIGN_CENTER, 3, 2, paddings3, borderWidth));
            table.addCell(ItextUtils.createCell(testIssue.getSuggestion(), titlefont2, Element.ALIGN_CENTER, 10, 2, paddings3, borderWidth));
        }

        document.add(title);
        document.add(table);
    }

}
