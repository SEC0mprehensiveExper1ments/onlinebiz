package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS007;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DocServiceJS007 {
    private final OSSProvider ossProvider;

    public DocServiceJS007(OSSProvider ossProvider) {
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

    private static JS007 JS007Json;

    public String fill(String reportId, JS007 newJson) {
        JS007Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS007_" + reportId + ".pdf";
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
            String header = "报告编号："+JS007Json.getInputBaoGaoBianHao();
            String[] footer = new String[]{"南京大学计算机软件新技术国家重点实验室      "+header+"         第 ", " 页，共 ", " 页"};
            int headerToPage = 0;
            int footerFromPage = 3;
            boolean isHaderLine = false;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
            float[] footerLoc = new float[]{document.left(), document.bottom() - 20};
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
        try {
            if (ossProvider.upload(
                    "doc", "JS007_" + reportId + ".pdf", Files.readAllBytes(Path.of(pdfPath)), "application/pdf")) {
                //System.out.println(pdfPath);
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS007_" + reportId + ".pdf";
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
    private static Font boldchusong;
    private static Font boldxiao3song;
    private static Font bold3song;
    private static Font boldxiao1song;
    private static Font normalxiao4song;

    private static Font normal5song;
    private static Font bold5song;
    private static Font bold4songblue;
    private static Font bold2song;

    public void generatePage(Document document) throws Exception {
        try {
            bfChinese =
                    BaseFont.createFont(
                            DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            bfHeiTi =
                    BaseFont.createFont(
                            DOCUMENT_DIR + "font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            boldchusong = new Font(bfChinese, 36f, Font.BOLD);
            boldxiao3song = new Font(bfChinese, 15f, Font.BOLD);
            bold3song = new Font(bfChinese, 16f, Font.BOLD);
            boldxiao1song = new Font(bfChinese, 24f, Font.BOLD);
            normalxiao4song = new Font(bfChinese, 12f, Font.NORMAL);

            normal5song = new Font(bfChinese, 10.5f, Font.NORMAL);
            bold5song = new Font(bfChinese, 10.5f, Font.BOLD);
            bold4songblue = new Font(bfChinese, 14f, Font.BOLD);
            bold4songblue.setColor(new BaseColor(61, 89, 171));
            bold2song = new Font(bfChinese, 22f, Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paragraph BaoGaoBianHao = new Paragraph("报告编号：" + JS007Json.getInputBaoGaoBianHao(), boldxiao3song);
        BaoGaoBianHao.setAlignment(2);
        BaoGaoBianHao.setSpacingBefore(20f);
        BaoGaoBianHao.setSpacingAfter(10f);
        document.add(BaoGaoBianHao);

        // 标题
        Paragraph title = new Paragraph("测 试 报 告", boldchusong);
        title.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(60f); // 设置段落上空白
        title.setSpacingAfter(10f); // 设置段落下空白
        document.add(title);
        document.add(new Paragraph("\n\n\n"));

        Paragraph firstPageContent = new Paragraph(
                "     软件名称： "+JS007Json.getInputRuanJianMingCheng()+"\n\n" +
                "     版 本 号： "+JS007Json.getInputBanBenHao()+"\n\n" +
                "     委托单位： "+JS007Json.getInputWeiTuoDanWei()+"\n\n" +
                "     测试类别： "+JS007Json.getInputCeShiLeiBie()+"\n\n" +
                "     报告日期： "+JS007Json.getInputBaoGaoRiQiNian()+" 年 "+JS007Json.getInputBaoGaoRiQiYue()+" 月 "+JS007Json.getInputBaoGaoRiQiRi()+" 日 \n\n"
                , bold3song);
        firstPageContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(firstPageContent);
        document.add(new Paragraph("\n\n\n\n\n"));

        Paragraph DanWei=new Paragraph(
                "南京大学计算机软件新技术\n"+
                "国家重点实验室"
                ,boldxiao1song);
        DanWei.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(DanWei);
        document.newPage();

        Paragraph ShengMing=new Paragraph("声  明\n\n",bold3song);
        ShengMing.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(ShengMing);

        Paragraph secondPageContent=new Paragraph(
                "    1、本测试报告仅适用于本报告明确指出的委托单位的被测样品及版本。\n" +
                        "    2、本测试报告是本实验室对所测样品进行科学、客观测试的结果，为被测样品提供第三方独立、客观、公正的重要判定依据，也为最终用户选择产品提供参考和帮助。\n" +
                        "    3、未经本实验室书面批准，不得复制本报告中的内容（全文复制除外），以免误导他人（尤其是用户）对被测样品做出不准确的评价。\n" +
                        "    4、在任何情况下，若需引用本测试报告中的结果或数据都应保持其本来的意义，在使用时务必要保持其完整，不得擅自进行增加、修改、伪造，并应征得本实验室同意。\n" +
                        "    5、本测试报告不得拷贝或复制作为广告材料使用。\n" +
                        "    6、当被测样品出现版本更新或其它任何改变时，本测试结果不再适用，涉及到的任何技术、模块（或子系统）甚至整个软件都必须按要求进行必要的备案或重新测试，更不能出现将本测试结果应用于低于被测样品版本的情况。\n" +
                        "    7、本报告无主测人员、审核人员、批准人员（授权签字人）签字无效。\n" +
                        "    8、本报告无本实验室章、涂改均无效。"
                ,normalxiao4song);
        secondPageContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        secondPageContent.setLeading(25f);
        document.add(secondPageContent);

        document.newPage();

        Paragraph CeShiBaoGaoTitle=new Paragraph("测 试 报 告",bold2song);
        CeShiBaoGaoTitle.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        CeShiBaoGaoTitle.setSpacingBefore(20f); // 设置段前间距
        CeShiBaoGaoTitle.setSpacingAfter(20f); // 设置段后间距
        document.add(CeShiBaoGaoTitle);

        //表格
        float[] paddings3 = new float[]{4f, 4f, 3f, 3f};        // 上下左右的间距
        float borderWidth = 0.3f;
        //float fixedLeading = 14f;
        PdfPTable reportTable=new PdfPTable(48);
        reportTable.setWidthPercentage(100); // 宽度100%居中

        reportTable.addCell(ItextUtils.createCell_Height("委托单位", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputWeiTuoDanWei(), normalxiao4song, Element.ALIGN_CENTER, 22, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("项目编号", normalxiao4song, Element.ALIGN_CENTER, 7, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputXiangMuBianHao(), normalxiao4song, Element.ALIGN_CENTER, 11, 2, 25f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("样品名称", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYangPinMingCheng(), normalxiao4song, Element.ALIGN_CENTER, 22, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("版本/型号", normalxiao4song, Element.ALIGN_CENTER, 7, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputBanBenXingHao(), normalxiao4song, Element.ALIGN_CENTER, 11, 2, 25f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("来样日期", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(
                JS007Json.getInputLaiYangRiQiNian()+" 年 "+JS007Json.getInputLaiYangRiQiYue()+" 月 "+JS007Json.getInputLaiYangRiQiRi()+" 日"
                , normalxiao4song, Element.ALIGN_CENTER, 22, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("测试类型", normalxiao4song, Element.ALIGN_CENTER, 7, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCeShiLeiBie(), normalxiao4song, Element.ALIGN_CENTER, 11, 2, 25f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("测试时间", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(
                JS007Json.getInputCeShiKaiShiRiQiNian()+" 年 "+JS007Json.getInputCeShiKaiShiRiQiYue()+" 月 "+JS007Json.getInputCeShiKaiShiRiQiRi()+" 日"
                +" 至 "+JS007Json.getInputCeShiJieShuRiQiNian()+" 年 "+JS007Json.getInputCeShiJieShuRiQiYue()+" 月 "+JS007Json.getInputCeShiJieShuRiQiRi()+" 日"
                , normalxiao4song, Element.ALIGN_CENTER, 40, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("样品状态", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYangPinZhuangTai(), normalxiao4song, Element.ALIGN_CENTER, 40, 2, 25f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("测试依据", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 45f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCeShiYiJu(), normalxiao4song, Element.ALIGN_CENTER, 40, 2, 40f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("样品清单", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 100f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYangPinQingDan(), normalxiao4song, Element.ALIGN_CENTER, 40, 2, 100f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("测试结论", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 60f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCeShiJieLun(), normalxiao4song, Element.ALIGN_CENTER, 40, 2, 60f, paddings3, borderWidth));

        document.add(reportTable);
        document.newPage();
    }

}
