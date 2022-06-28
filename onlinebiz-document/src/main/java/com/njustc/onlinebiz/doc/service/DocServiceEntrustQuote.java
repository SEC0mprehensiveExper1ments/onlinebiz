package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.EntrustQuoteDoc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DocServiceEntrustQuote {

    private final OSSProvider ossProvider;

    public DocServiceEntrustQuote(OSSProvider ossProvider) {
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
        marginLeft = 60f;
        marginRight = 60f;
        marginTop = 72f;
        marginBottom = 72f;
    }

    public String fill(String entrustId, EntrustQuoteDoc newJson) {
        String pdfPath = DOCUMENT_DIR + "EntrustQuote_" + entrustId + ".pdf";
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
//            System.out.println("file: " + file);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//            System.out.println("Success");
            // 3.打开文档
            document.open();
            // 4.向文档中添加内容
            //generatePage(document);
            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to generate a pdf";
        }
        // 上传pdf
        try {
            if (ossProvider.upload(
                    "doc", "EntrustQuote_" + entrustId + ".pdf", Files.readAllBytes(Path.of(pdfPath)), "application/pdf")) {
                //System.out.println(pdfPath);
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/EntrustQuote_" + entrustId + ".pdf";
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
}
