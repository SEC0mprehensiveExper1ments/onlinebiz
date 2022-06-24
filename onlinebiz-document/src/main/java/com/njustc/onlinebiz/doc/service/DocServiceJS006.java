package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.scheme.Modification;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.exception.DownloadDAOFailureException;
import com.njustc.onlinebiz.doc.exception.DownloadNotFoundException;
import com.njustc.onlinebiz.doc.exception.DownloadPermissionDeniedException;
import com.njustc.onlinebiz.doc.model.JS006;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays;

@Service
public class DocServiceJS006 {
    private final OSSProvider ossProvider;

    public DocServiceJS006(OSSProvider ossProvider) {
        this.ossProvider = ossProvider;
    }

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

    private static JS006 JS006Json;

    public String fill(String schemeId, JS006 newJson) {
        JS006Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS006_" + schemeId + ".pdf";
//        System.out.println("pdfPath: " + pdfPath);
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath);
//            System.out.println("file: " + file);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//            System.out.println("Success");
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS006－2011";
            String[] footer = new String[]{"第 ", " 页，共 ", " 页"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHaderLine = true;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
            float[] footerLoc = new float[]{document.right() - 10, document.bottom() - 20};
            float headLineOff = -5f;
            float footLineOff = 12f;
            writer.setPageEvent(new HeaderFooter(header, footer, headerToPage, footerFromPage, isHaderLine, isFooterLine,
                    headerLoc, footerLoc, headLineOff, footLineOff));
            // 3.打开文档
            document.open();
            // 4.向文档中添加内容
            generatePage(document);
            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to generate a pdf";
        }
        // 上传pdf
        //System.out.println(pdfPath);
        try {
            if (ossProvider.upload(
                    "doc", "JS006_" + schemeId + ".pdf", Files.readAllBytes(Path.of(pdfPath)), "application/pdf")) {
                //System.out.println(pdfPath);
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS006_" + schemeId + ".pdf";
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
     */
    private void deleteOutFile(String pdfPath) {
        try {
            File file = new File(pdfPath);
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete" + file.getName() + "is failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BaseFont bfChinese;
    private static BaseFont bfHeiTi;
    private static Font titlefont1;
    private static Font titlefont2;
    private static Font normal5song;
    private static Font bold5song;
    private static Font bold4song;

    public void generatePage(Document document) throws Exception {
        try {
            bfChinese =
                    BaseFont.createFont(
                            DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            bfHeiTi =
                    BaseFont.createFont(
                            DOCUMENT_DIR + "font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont1 = new Font(bfChinese, 40, Font.NORMAL);
            titlefont2 = new Font(bfHeiTi, 22, Font.NORMAL);
            normal5song = new Font(bfChinese, 10.5f, Font.NORMAL);
            bold5song = new Font(bfChinese, 10.5f, Font.BOLD);
            bold4song = new Font(bfChinese, 14f, Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 标题
        Paragraph title = new Paragraph("软件测试方案", titlefont1);
        title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(80f); // 设置段落上空白

        PdfPTable table = new PdfPTable(1);             //蓝线
        table.setWidthPercentage(100);                  //表格宽度100%
        PdfPCell cell = table.getDefaultCell();
        cell.setBorderWidth(1.0f);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.BLUE);
        table.addCell(cell);

        Paragraph version = new Paragraph(JS006Json.getInputBanBenHao(), titlefont2);
        version.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        version.setSpacingBefore(10f); // 设置段落上空白

        document.add(new Paragraph("\n\n\n"));
        document.add(title);
        document.add(new Paragraph("\n"));
        document.add(table);
        document.add(version);

        document.newPage();
        document.add(new Paragraph("\n"));

        Paragraph modificationTitle = new Paragraph("文档修改记录", bold5song);
        //modificationTitle.setSpacingBefore(60f); // 设置段落上空白
        modificationTitle.setSpacingAfter(40f); // 设置段落下空白
        modificationTitle.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(modificationTitle);

        // 表格
        float[] paddings3 = new float[]{4f, 4f, 3f, 3f};        // 上下左右的间距
        float borderWidth = 0.3f;
        PdfPTable modificationTable = new PdfPTable(43);
        modificationTable.setWidthPercentage(100);

        // 第一行
        modificationTable.addCell(ItextUtils.createGreyCell("版本", normal5song, Element.ALIGN_LEFT, 4, 2, paddings3, borderWidth));
        modificationTable.addCell(ItextUtils.createGreyCell("日期", normal5song, Element.ALIGN_LEFT, 15, 2, paddings3, borderWidth));
        modificationTable.addCell(ItextUtils.createGreyCell("AMD", normal5song, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
        modificationTable.addCell(ItextUtils.createGreyCell("修订者", normal5song, Element.ALIGN_LEFT, 8, 2, paddings3, borderWidth));
        modificationTable.addCell(ItextUtils.createGreyCell("说明", normal5song, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));

        List<Modification> modifications = JS006Json.getWenDangXiuGaiJiLu();
        for (Modification modification : modifications) {
            modificationTable.addCell(ItextUtils.createCell(modification.getVersion(), normal5song, Element.ALIGN_LEFT, 4, 2, paddings3, borderWidth));
            modificationTable.addCell(ItextUtils.createCell(modification.getDate().toString(), normal5song, Element.ALIGN_LEFT, 15, 2, paddings3, borderWidth));
            modificationTable.addCell(ItextUtils.createCell(modification.getMethod().toString(), normal5song, Element.ALIGN_LEFT, 6, 2, paddings3, borderWidth));
            modificationTable.addCell(ItextUtils.createCell(modification.getModifier(), normal5song, Element.ALIGN_LEFT, 8, 2, paddings3, borderWidth));
            modificationTable.addCell(ItextUtils.createCell(modification.getIllustration(), normal5song, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));
        }
        document.add(modificationTable);
        Paragraph AMDComment = new Paragraph("   （A-添加，M-修改，D-删除）", normal5song);
        AMDComment.setSpacingBefore(10f); // 设置段落上空白
        AMDComment.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(AMDComment);

        document.newPage();
        //document.add(new Paragraph("\n"));

        Paragraph directory = new Paragraph("目录", bold4song);
        directory.setSpacingBefore(60f); // 设置段落上空白
        directory.setSpacingAfter(10f); // 设置段落下空白

    }
}
