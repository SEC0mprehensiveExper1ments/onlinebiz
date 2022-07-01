package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.common.model.entrust.EntrustQuote;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.EntrustQuoteDoc;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
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
        marginLeft = 40f;
        marginRight = 40f;
        marginTop = 20f;
        marginBottom = 20f;
    }

    private static EntrustQuoteDoc EntrustQuoteJson;

    /**
     * 填充
     *
     * @param entrustId 委托id
     * @param newJson 新json
     * @return {@link String} OSS下载链接
     */
    public String fill(String entrustId, EntrustQuoteDoc newJson) {
        EntrustQuoteJson = newJson;
        String pdfPath = DOCUMENT_DIR + "EntrustQuote_" + entrustId + ".pdf";
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
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

    private static Font normal5YaHei;
    private static Font normalxiao4YaHei;
    private static Font normalxiao5song;
    private static Font bold5YaHei;
    private static Font Under5YaHei;

    public void generatePage(Document document) throws Exception {
        try {
            BaseFont bfYaHei = BaseFont.createFont(
                    DOCUMENT_DIR + "font/msyh.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont bfChinese = BaseFont.createFont(
                    DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            normal5YaHei = new Font(bfYaHei, 10.5f, Font.NORMAL);
            normalxiao4YaHei = new Font(bfYaHei, 14f, Font.NORMAL);
            bold5YaHei = new Font(bfYaHei, 10.5f, Font.BOLD);
            Under5YaHei = new Font(bfYaHei, 10.5f, Font.UNDERLINE);
            normalxiao5song = new Font(bfChinese, 9f, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String headPicture = "https://oss.syh1en.asia/doc/title.jpg";
        try {
            Image image = Image.getInstance(new URL(headPicture));
            float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            float documentHeight = documentWidth / 580 * 120;//重新设置宽高
            image.scaleAbsolute(documentWidth, documentHeight);//重新设置宽高
            document.add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paragraph BaoJiaRiQi = new Paragraph("报价日期：" + EntrustQuoteJson.getQuotationDate() + "\n" +
                "至             报价有效期：" + EntrustQuoteJson.getEffectiveDate(), normal5YaHei);
        BaoJiaRiQi.setAlignment(2);
        BaoJiaRiQi.setSpacingBefore(10f);
        BaoJiaRiQi.setSpacingAfter(5f);
        document.add(BaoJiaRiQi);

        Paragraph ZhangHu = new Paragraph("   户      名：  " + EntrustQuoteJson.getAccountName() + "\n" +
                "   开户银行：  " + EntrustQuoteJson.getBankName() + "\n" +
                "   账      号：  " + EntrustQuoteJson.getAccount() + "\n"
                , normal5YaHei);
        ZhangHu.setAlignment(0);
        ZhangHu.setSpacingAfter(20f);
        ZhangHu.setLeading(25f);
        document.add(ZhangHu);

        Paragraph RuanJianMingCheng=new Paragraph("软件名称：  " + EntrustQuoteJson.getSoftwareName(), normalxiao4YaHei);
        RuanJianMingCheng.setAlignment(0);
        RuanJianMingCheng.setSpacingAfter(20f);
        document.add(RuanJianMingCheng);

        Paragraph DanWei = new Paragraph("单位：元", normal5YaHei);
        DanWei.setAlignment(2);
        DanWei.setSpacingAfter(10f);
        document.add(DanWei);

        float[] paddings3 = new float[]{4f, 4f, 3f, 3f};        // 上下左右的间距
        float borderWidth = 0.3f;
        PdfPTable quoteTable = new PdfPTable(48);
        quoteTable.setWidthPercentage(100); // 宽度100%居中

        quoteTable.addCell(ItextUtils.createGreenCell_Height("项目", bold5YaHei, Element.ALIGN_CENTER, 13, 2, 25f, paddings3, borderWidth));
        quoteTable.addCell(ItextUtils.createGreenCell_Height("分项", bold5YaHei, Element.ALIGN_CENTER, 11, 2, 25f, paddings3, borderWidth));
        quoteTable.addCell(ItextUtils.createGreenCell_Height("单价", bold5YaHei, Element.ALIGN_CENTER, 5, 2, 25f, paddings3, borderWidth));
        quoteTable.addCell(ItextUtils.createGreenCell_Height("说明", bold5YaHei, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));
        quoteTable.addCell(ItextUtils.createGreenCell_Height("行合计", bold5YaHei, Element.ALIGN_CENTER, 7, 2, 25f, paddings3, borderWidth));

        List<EntrustQuote.EntrustQuoteRow> entrustQuoteRows = EntrustQuoteJson.getRowList();
        for (EntrustQuote.EntrustQuoteRow entrustQuoteRow : entrustQuoteRows) {
            quoteTable.addCell(ItextUtils.createCell_Height(entrustQuoteRow.getProjectName(), normal5YaHei, Element.ALIGN_LEFT, 13, 2, 30f, paddings3, borderWidth));
            quoteTable.addCell(ItextUtils.createCell_Height(entrustQuoteRow.getSubProject(), normal5YaHei, Element.ALIGN_LEFT, 11, 2, 30f, paddings3, borderWidth));
            quoteTable.addCell(ItextUtils.createCell_Height(entrustQuoteRow.getPrice().toString(), normal5YaHei, Element.ALIGN_RIGHT, 5, 2, 30f, paddings3, borderWidth));
            quoteTable.addCell(ItextUtils.createCell_Height(entrustQuoteRow.getDescription(), normal5YaHei, Element.ALIGN_LEFT, 12, 2, 30f, paddings3, borderWidth));
            quoteTable.addCell(ItextUtils.createCell_Height(entrustQuoteRow.getRowTotal().toString(), normal5YaHei, Element.ALIGN_RIGHT, 7, 2, 30f, paddings3, borderWidth));
        }

        quoteTable.addCell(ItextUtils.createCell_Height("小计", normal5YaHei, Element.ALIGN_RIGHT, 41, 2, 30f, paddings3, borderWidth));
        quoteTable.addCell(ItextUtils.createCell_Height(EntrustQuoteJson.getSubTotal().toString(), normal5YaHei, Element.ALIGN_RIGHT, 7, 2, 30f, paddings3, borderWidth));

        quoteTable.addCell(ItextUtils.createCell_Height("税率（8%）", normal5YaHei, Element.ALIGN_RIGHT, 41, 2, 30f, paddings3, borderWidth));
        quoteTable.addCell(ItextUtils.createCell_Height(EntrustQuoteJson.getTaxRate().toString(), normal5YaHei, Element.ALIGN_RIGHT, 7, 2, 30f, paddings3, borderWidth));

        quoteTable.addCell(ItextUtils.createCell_Height("总计", normal5YaHei, Element.ALIGN_RIGHT, 41, 2, 30f, paddings3, borderWidth));
        quoteTable.addCell(ItextUtils.createCell_Height(EntrustQuoteJson.getTotal().toString(), normal5YaHei, Element.ALIGN_RIGHT, 7, 2, 30f, paddings3, borderWidth));

        document.add(quoteTable);
        Paragraph end = new Paragraph();
        Chunk BaoJiaTiGongRen = new Chunk("    报价提供人： ", normal5YaHei);
        Chunk BaoJiaTiGongRenContent = ItextUtils.fixedUnderlineChunk(EntrustQuoteJson.getProvider(), Under5YaHei, 64, 0.5f);
        // Chunk BaoJiaTiGongRenContent = new Chunk(EntrustQuoteJson.getProvider()+"                                                                                                                                   \n\n", Under5YaHei);
        Chunk QianZi = new Chunk("\n    如果接受报价，请在此签字： ", normal5YaHei);
        Chunk QianZiContent = new Chunk("                                                                                                                    \n", Under5YaHei);

        end.add(BaoJiaTiGongRen);
        end.add(BaoJiaTiGongRenContent);
        end.add(QianZi);
        end.add(QianZiContent);

        end.setAlignment(0);
        end.setSpacingBefore(20f);
        end.setSpacingAfter(10f);
        end.setLeading(25f);
        document.add(end);

        Paragraph JieYu = new Paragraph("祝事业顺利！", normal5YaHei);
        JieYu.setAlignment(1);
        JieYu.setSpacingAfter(10f);
        document.add(JieYu);

        Paragraph Info = new Paragraph("南京大学 计算机软件新技术国家重点实验室 软件测试中心\n" +
                "江苏省 南京市 栖霞区 仙林大道163号  南京大学仙林校区计算机科学与技术楼\n" +
                "电话025-89683467  传真025-89686596   Email: keysoftlab@nju.edu.cn\n", normalxiao5song);
        Info.setAlignment(1);
        Info.setSpacingBefore(120f);
        document.add(Info);

    }
}
