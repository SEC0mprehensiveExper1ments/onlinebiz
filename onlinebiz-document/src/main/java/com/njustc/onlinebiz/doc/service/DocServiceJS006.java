package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.exception.DownloadDAOFailureException;
import com.njustc.onlinebiz.doc.exception.DownloadNotFoundException;
import com.njustc.onlinebiz.doc.exception.DownloadPermissionDeniedException;
import com.njustc.onlinebiz.doc.model.JS006;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DocServiceJS006 {
    private static final String TEST_SERVICE = "http://onlinebiz-test";
    private final RestTemplate restTemplate;
    private final OSSProvider ossProvider;
    private String schemeId;

    public DocServiceJS006(RestTemplate restTemplate, OSSProvider ossProvider) {
        this.restTemplate = restTemplate;
        this.ossProvider = ossProvider;
    }

    public Scheme getScheme(String schemeId, Long userId, Role userRole) {
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/test/scheme/" + schemeId;
        ResponseEntity<Scheme> responseEntity = restTemplate.getForEntity(url + params, Scheme.class);
        // 检查测试方案 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试方案ID");
        } else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Scheme scheme = responseEntity.getBody();
        this.schemeId = schemeId;
        return scheme;
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

    private JS006 JS006Json;

    public String fill(JS006 newJson) {
        JS006Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS006_" + schemeId + ".pdf";
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS006－2011";
            String[] footer = new String[]{"第 ", " 页，共 ", " 页", "计算机软件新技术国家重点实验室（南京大学）"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHaderLine = true;
            boolean isFooterLine = true;
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
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to generate a pdf";
        }
        // 上传pdf
        try {
            if (ossProvider.upload(
                    "doc", "JS006_" + schemeId + ".pdf", Files.readAllBytes(Path.of(pdfPath)), "application/pdf")) {
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

    private static Font titlefont1;
    private static Font titlefont2;
    private static Font keyfont;
    private static Font textfont;
    private static BaseFont bfChinese;
    private static BaseFont bfHeiTi;

    public void generatePageOne(Document document) throws Exception {
        try {
            bfChinese =
                    BaseFont.createFont(
                            DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            bfHeiTi =
                    BaseFont.createFont(
                            DOCUMENT_DIR + "font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont1 = new Font(bfChinese, 40, Font.NORMAL);
            titlefont2 = new Font(bfHeiTi, 22, Font.NORMAL);
            keyfont = new Font(bfChinese, 12.5f, Font.BOLD);
            textfont = new Font(bfChinese, 12f, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 标题
        Paragraph title = new Paragraph("软件测试方案", titlefont1);
        title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(30f); // 设置段落上空白
        title.setSpacingAfter(90f); // 设置段落下空白
        Paragraph version = new Paragraph(JS006Json.getInputBanBenHao(), titlefont2);
        version.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        version.setSpacingBefore(30f); // 设置段落上空白
        version.setSpacingAfter(90f); // 设置段落下空白
        document.add(new Paragraph("\n\n\n"));
        document.add(title);
        document.add(version);
    }
}
