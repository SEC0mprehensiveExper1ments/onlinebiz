package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.njustc.onlinebiz.common.model.test.report.SoftwareEnvironment;
import com.njustc.onlinebiz.common.model.test.report.TestContent;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS007;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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

    public void mergePdfFiles(String[] files, String savepath) {
        try
        {
            //获取当前pdf的高度及宽度
            Document document = new Document(new PdfReader(files[0]).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));
            //打开当前操作的document，方便写入
            document.open();
            for (String file : files) {
                //读取当前文件的内容
                PdfReader reader = new PdfReader(file);
                //获取当前文件的长度
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    //一页一页的复制
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    //这个方法是获取当前页面内容 注意：只是文本内容
                    //PdfTextExtractor.getTextFromPage(reader, j);
                    copy.addPage(page);
                }
                reader.close();
            }
            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static JS007 JS007Json;
    /**
     * 填充
     *
     * @param reportId 委托id
     * @param newJson 新json
     * @return {@link String} OSS下载链接
     */
    public String fill(String reportId, JS007 newJson) {
        JS007Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS007_" + reportId + ".pdf";
        String pdfCover = DOCUMENT_DIR + "JS007_cover_" + reportId + ".pdf";
        String pdfMainText = DOCUMENT_DIR + "JS007_mainbody_" + reportId + ".pdf";
        try {
            // 生辰pdf封面
            // 1.新建document对象
            Document coverDocument = new Document(PageSize.A4);// 建立一个Document对象
            coverDocument.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File coverFile = new File(pdfCover.replaceAll("\\\\", "/"));
            PdfWriter coverWriter = PdfWriter.getInstance(coverDocument, new FileOutputStream(coverFile)); // 不要删
            // 3.打开文档
            coverDocument.open();
            // 4.向文档中添加内容
            generateCover(coverDocument);
            // 5.关闭文档
            coverDocument.close();

            // 生成pdf正文
            // 1.新建document对象
            Document bodyDocument = new Document(PageSize.A4);// 建立一个Document对象
            bodyDocument.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File bodyFile = new File(pdfMainText.replaceAll("\\\\", "/"));
            PdfWriter bodyWriter = PdfWriter.getInstance(bodyDocument, new FileOutputStream(bodyFile));
            // 2.5 添加页眉/页脚
            String header = "报告编号：" + JS007Json.getInputBaoGaoBianHao();
            String[] footer = new String[]{"南京大学计算机软件新技术国家重点实验室      " + header + "         第 ", " 页，共 ", " 页"};
            int headerToPage = 0;
            int footerFromPage = 1;
            boolean isHaderLine = false;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{bodyDocument.right() - 5, bodyDocument.top() + 15};
            float[] footerLoc = new float[]{bodyDocument.left() + 15, bodyDocument.bottom() - 20};
            float headLineOff = -5f;
            float footLineOff = 12f;
            bodyWriter.setPageEvent(new HeaderFooter(header, footer, headerToPage, footerFromPage, isHaderLine, isFooterLine,
                    headerLoc, footerLoc, headLineOff, footLineOff));
            // 3.打开文档
            bodyDocument.open();
            // 4.向文档中添加内容
            generatePage(bodyDocument);
            // 5.关闭文档
            bodyDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to generate a pdf";
        }
        // 合并pdf
        mergePdfFiles(new String[]{pdfCover, pdfMainText}, pdfPath);
        // 删除封面与正文pdf
        System.gc();    // 及时清除缓存
        deleteOutFile(pdfCover);
        deleteOutFile(pdfMainText);
        // 上传pdf
        try {
            if (ossProvider.upload(
                    "doc", "JS007_" + reportId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
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
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete" + file.getName() + "is failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Font boldchusong;
    private static Font boldxiao3song;
    private static Font bold3song;
    private static Font boldxiao1song;
    private static Font normalxiao4song;
    private static Font boldxiao4song;
    private static Font normal4song;

    private static Font normal5song;
    private static Font bold5song;
    private static Font bold4song;
    private static Font bold2song;

    public void generateCover(Document document) throws Exception {
        try {
            BaseFont bfChinese = BaseFont.createFont(
                    DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            boldchusong = new Font(bfChinese, 36f, Font.BOLD);
            boldxiao3song = new Font(bfChinese, 15f, Font.BOLD);
            bold3song = new Font(bfChinese, 16f, Font.BOLD);
            boldxiao1song = new Font(bfChinese, 24f, Font.BOLD);
            normalxiao4song = new Font(bfChinese, 12f, Font.NORMAL);
            boldxiao4song = new Font(bfChinese, 12f, Font.BOLD);
            normal4song = new Font(bfChinese, 14f, Font.NORMAL);

            normal5song = new Font(bfChinese, 10.5f, Font.NORMAL);
            bold5song = new Font(bfChinese, 10.5f, Font.BOLD);
            bold4song = new Font(bfChinese, 14f, Font.BOLD);
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
                "     软件名称： " + JS007Json.getInputRuanJianMingCheng() + "\n\n" +
                        "     版 本 号： " + JS007Json.getInputBanBenHao() + "\n\n" +
                        "     委托单位： " + JS007Json.getInputWeiTuoDanWei() + "\n\n" +
                        "     测试类别： " + JS007Json.getInputCeShiLeiBie() + "\n\n" +
                        "     报告日期： " + JS007Json.getInputBaoGaoRiQiNian() + " 年 " + JS007Json.getInputBaoGaoRiQiYue() + " 月 " + JS007Json.getInputBaoGaoRiQiRi() + " 日 \n\n"
                , bold3song);
        firstPageContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(firstPageContent);
        document.add(new Paragraph("\n\n\n\n\n"));

        Paragraph DanWei = new Paragraph(
                "南京大学计算机软件新技术\n" +
                        "国家重点实验室"
                , boldxiao1song);
        DanWei.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(DanWei);
        document.newPage();

        Paragraph ShengMing = new Paragraph("声  明\n\n", bold3song);
        ShengMing.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(ShengMing);

        Paragraph secondPageContent = new Paragraph(
                "    1、本测试报告仅适用于本报告明确指出的委托单位的被测样品及版本。\n" +
                        "    2、本测试报告是本实验室对所测样品进行科学、客观测试的结果，为被测样品提供第三方独立、客观、公正的重要判定依据，也为最终用户选择产品提供参考和帮助。\n" +
                        "    3、未经本实验室书面批准，不得复制本报告中的内容（全文复制除外），以免误导他人（尤其是用户）对被测样品做出不准确的评价。\n" +
                        "    4、在任何情况下，若需引用本测试报告中的结果或数据都应保持其本来的意义，在使用时务必要保持其完整，不得擅自进行增加、修改、伪造，并应征得本实验室同意。\n" +
                        "    5、本测试报告不得拷贝或复制作为广告材料使用。\n" +
                        "    6、当被测样品出现版本更新或其它任何改变时，本测试结果不再适用，涉及到的任何技术、模块（或子系统）甚至整个软件都必须按要求进行必要的备案或重新测试，更不能出现将本测试结果应用于低于被测样品版本的情况。\n" +
                        "    7、本报告无主测人员、审核人员、批准人员（授权签字人）签字无效。\n" +
                        "    8、本报告无本实验室章、涂改均无效。"
                , normalxiao4song);
        secondPageContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        secondPageContent.setLeading(25f);
        document.add(secondPageContent);
    }

    public void generatePage(Document document) throws Exception {
//        document.newPage();

        Paragraph CeShiBaoGaoTitle = new Paragraph("测 试 报 告", bold2song);
        CeShiBaoGaoTitle.setAlignment(1); // 设置文字居中 0靠左   1，居中     2，靠右
        CeShiBaoGaoTitle.setSpacingBefore(20f); // 设置段前间距
        CeShiBaoGaoTitle.setSpacingAfter(20f); // 设置段后间距
        document.add(CeShiBaoGaoTitle);

        //表格
        float[] paddings3 = new float[]{4f, 4f, 3f, 3f};        // 上下左右的间距
        float borderWidth = 0.3f;
        //float fixedLeading = 14f;
        PdfPTable reportTable = new PdfPTable(48);
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
                JS007Json.getInputLaiYangRiQiNian() + " 年 " + JS007Json.getInputLaiYangRiQiYue() + " 月 " + JS007Json.getInputLaiYangRiQiRi() + " 日"
                , normalxiao4song, Element.ALIGN_CENTER, 22, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("测试类型", normalxiao4song, Element.ALIGN_CENTER, 7, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCeShiLeiBie(), normalxiao4song, Element.ALIGN_CENTER, 11, 2, 25f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("测试时间", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(
                JS007Json.getInputCeShiKaiShiRiQiNian() + " 年 " + JS007Json.getInputCeShiKaiShiRiQiYue() + " 月 " + JS007Json.getInputCeShiKaiShiRiQiRi() + " 日"
                        + " 至 " + JS007Json.getInputCeShiJieShuRiQiNian() + " 年 " + JS007Json.getInputCeShiJieShuRiQiYue() + " 月 " + JS007Json.getInputCeShiJieShuRiQiRi() + " 日"
                , normalxiao4song, Element.ALIGN_CENTER, 40, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("样品状态", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYangPinZhuangTai(), normalxiao4song, Element.ALIGN_CENTER, 40, 2, 25f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("测试依据", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 45f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCeShiYiJu(), normalxiao4song, Element.ALIGN_LEFT, 40, 2, 40f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("样品清单", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 120f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYangPinQingDan(), normalxiao4song, Element.ALIGN_LEFT, 40, 2, 100f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("测试结论", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 60f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCeShiJieLun(), normalxiao4song, Element.ALIGN_LEFT, 40, 2, 60f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("主测人", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputZhuCeRen(), normalxiao4song, Element.ALIGN_LEFT, 16, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("日期", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(
                JS007Json.getInputZhuCeRiQiNian() + " 年 " + JS007Json.getInputZhuCeRiQiYue() + " 月 " + JS007Json.getInputZhuCeRiQiRi() + " 日"
                , normalxiao4song, Element.ALIGN_CENTER, 16, 2, 30f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("审核人", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputZhuCeRen(), normalxiao4song, Element.ALIGN_LEFT, 16, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("日期", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(
                JS007Json.getInputShenHeRiQiNian() + " 年 " + JS007Json.getInputShenHeRiQiYue() + " 月 " + JS007Json.getInputShenHeRiQiRi() + " 日"
                , normalxiao4song, Element.ALIGN_CENTER, 16, 2, 30f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("批准人", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputPiZhunRen(), normalxiao4song, Element.ALIGN_LEFT, 16, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("日期", normalxiao4song, Element.ALIGN_CENTER, 8, 2, 30f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height(
                JS007Json.getInputPiZhunRiQiNian() + " 年 " + JS007Json.getInputPiZhunRiQiYue() + " 月 " + JS007Json.getInputPiZhunRiQiRi() + " 日"
                , normalxiao4song, Element.ALIGN_CENTER, 16, 2, 30f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Height("委托单位联系方式", boldxiao4song, Element.ALIGN_LEFT, 24, 2, 25f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Height("测试单位联系方式", boldxiao4song, Element.ALIGN_LEFT, 24, 2, 25f, paddings3, borderWidth));

        reportTable.addCell(ItextUtils.createCell_Leading(
                "电话：" + JS007Json.getInputDianHua() + "\n" +
                        "传真：" + JS007Json.getInputChuanZhen() + "\n" +
                        "地址： " + JS007Json.getInputDiZhi() + "\n" +
                        "邮编： " + JS007Json.getInputYouBian() + "\n" +
                        "联系人： " + JS007Json.getInputLianXiRen() + "\n" +
                        "E-mail: " + JS007Json.getInputEmail() + "\n"
                , normalxiao4song, Element.ALIGN_LEFT, 24, 2, 20f, paddings3, borderWidth));
        reportTable.addCell(ItextUtils.createCell_Leading(
                "单位地址：南京市栖霞区仙林大道163号\n" +
                        "邮政编码：210023\n" +
                        "电话： 86-25-89683467\n" +
                        "传真： 86-25-89686596\n" +
                        "网址： http://keysoftlab.nju.edu.cn \n" +
                        "E-mail: keysoftlab@nju.edu.cn\n"
                , normalxiao4song, Element.ALIGN_LEFT, 24, 2, 20f, paddings3, borderWidth));
        document.add(reportTable);
        document.newPage();

        Paragraph CeShiHuanJing = new Paragraph("一、测试环境", bold4song);
        CeShiHuanJing.setSpacingBefore(40f); // 设置段落上空白
        CeShiHuanJing.setSpacingAfter(20f); // 设置段落下空白
        CeShiHuanJing.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiHuanJing);

        Paragraph YingJianHuanJing = new Paragraph("硬件环境", boldxiao4song);
        YingJianHuanJing.setSpacingAfter(20f); // 设置段落下空白
        YingJianHuanJing.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YingJianHuanJing);

        Paragraph YingJianHuanJingContent = new Paragraph("    本次测试中使用到的硬件环境如下：", normalxiao4song);
        YingJianHuanJingContent.setSpacingAfter(10f); // 设置段落下空白
        YingJianHuanJingContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YingJianHuanJingContent);

        PdfPTable YingJianTable = new PdfPTable(48);
        YingJianTable.setWidthPercentage(100);

        // 第一行
        YingJianTable.addCell(ItextUtils.createGreyCell_Height("硬件类别", bold5song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));
        YingJianTable.addCell(ItextUtils.createGreyCell_Height("硬件名称", bold5song, Element.ALIGN_CENTER, 10, 2, 25f, paddings3, borderWidth));
        YingJianTable.addCell(ItextUtils.createGreyCell_Height("配置", bold5song, Element.ALIGN_CENTER, 23, 2, 25f, paddings3, borderWidth));
        YingJianTable.addCell(ItextUtils.createGreyCell_Height("数量", bold5song, Element.ALIGN_CENTER, 7, 2, 25f, paddings3, borderWidth));

        YingJianTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYingJianLeiBie(), normal5song, Element.ALIGN_CENTER, 8, 2, 60f, paddings3, borderWidth));
        YingJianTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYingJianMingCheng(), normal5song, Element.ALIGN_CENTER, 10, 2, 60f, paddings3, borderWidth));
        YingJianTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYingJianPeiZhi(), normal5song, Element.ALIGN_CENTER, 23, 2, 60f, paddings3, borderWidth));
        YingJianTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputYingJianShuLiang(), normal5song, Element.ALIGN_CENTER, 7, 2, 60f, paddings3, borderWidth));

        document.add(YingJianTable);

        Paragraph RuanJianHuanJing = new Paragraph("软件环境", boldxiao4song);
        RuanJianHuanJing.setSpacingBefore(10f); // 设置段落上空白
        RuanJianHuanJing.setSpacingAfter(20f); // 设置段落下空白
        RuanJianHuanJing.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(RuanJianHuanJing);

        Paragraph RuanJianHuanJingContent = new Paragraph("    本次测试中使用到的软件环境如下：", normalxiao4song);
        RuanJianHuanJingContent.setSpacingAfter(10f); // 设置段落下空白
        RuanJianHuanJingContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(RuanJianHuanJingContent);

        PdfPTable RuanJianTable = new PdfPTable(48);
        RuanJianTable.setWidthPercentage(100);

        // 第一行
        RuanJianTable.addCell(ItextUtils.createGreyCell_Height("软件类别", bold5song, Element.ALIGN_CENTER, 10, 2, 25f, paddings3, borderWidth));
        RuanJianTable.addCell(ItextUtils.createGreyCell_Height("软件名称", bold5song, Element.ALIGN_CENTER, 26, 2, 25f, paddings3, borderWidth));
        RuanJianTable.addCell(ItextUtils.createGreyCell_Height("版本", bold5song, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));

        RuanJianTable.addCell(ItextUtils.createCell_Height("操作系统", normal5song, Element.ALIGN_CENTER, 10, 2, 26f, paddings3, borderWidth));
        RuanJianTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCaoZuoXiTongMingCheng(), normal5song, Element.ALIGN_CENTER, 26, 2, 26f, paddings3, borderWidth));
        RuanJianTable.addCell(ItextUtils.createCell_Height(JS007Json.getInputCaoZuoXiTongBanBen(), normal5song, Element.ALIGN_CENTER, 12, 2, 26f, paddings3, borderWidth));

        List<SoftwareEnvironment> RuanJianHuanJingList = JS007Json.getInputRuanJianHuanJing();
        for (SoftwareEnvironment softwareEnvironment : RuanJianHuanJingList) {
            RuanJianTable.addCell(ItextUtils.createCell_Height(softwareEnvironment.getSoftwareType(), normal5song, Element.ALIGN_CENTER, 10, 2, 26f, paddings3, borderWidth));
            RuanJianTable.addCell(ItextUtils.createCell_Height(softwareEnvironment.getSoftwareName(), normal5song, Element.ALIGN_CENTER, 26, 2, 26f, paddings3, borderWidth));
            RuanJianTable.addCell(ItextUtils.createCell_Height(softwareEnvironment.getSoftwareVersion(), normal5song, Element.ALIGN_CENTER, 12, 2, 26f, paddings3, borderWidth));
        }
        document.add(RuanJianTable);

        Paragraph WangLuoHuanJing = new Paragraph("网络环境", boldxiao4song);
        WangLuoHuanJing.setSpacingBefore(10f); // 设置段落上空白
        WangLuoHuanJing.setSpacingAfter(20f); // 设置段落下空白
        WangLuoHuanJing.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(WangLuoHuanJing);

        Paragraph WangLuoHuanJingContent = new Paragraph("    " + JS007Json.getInputWangLuoHuanJing(), normalxiao4song);
        WangLuoHuanJingContent.setSpacingAfter(10f); // 设置段落下空白
        WangLuoHuanJingContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(WangLuoHuanJingContent);

        Paragraph CeShiYiJuCanKaoZiLiao = new Paragraph("二、测试依据和参考资料", bold4song);
        CeShiYiJuCanKaoZiLiao.setSpacingBefore(10f); // 设置段落上空白
        CeShiYiJuCanKaoZiLiao.setSpacingAfter(20f); // 设置段落下空白
        CeShiYiJuCanKaoZiLiao.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiYiJuCanKaoZiLiao);

        Paragraph CeShiYiJu = new Paragraph("测试依据", boldxiao4song);
        CeShiYiJu.setSpacingAfter(20f); // 设置段落下空白
        CeShiYiJu.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiYiJu);

        List<String> CeShiYiJuList = JS007Json.getInputTestBases();
        for (String testBase : CeShiYiJuList) {
            Paragraph testBaseContent = new Paragraph("■ " + testBase, normalxiao4song);
            testBaseContent.setSpacingAfter(10f); // 设置段落下空白
            testBaseContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
            document.add(testBaseContent);
        }

        Paragraph CanKaoZiLiao = new Paragraph("参考资料", boldxiao4song);
        CanKaoZiLiao.setSpacingAfter(20f); // 设置段落下空白
        CanKaoZiLiao.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CanKaoZiLiao);

        List<String> CanKaoZiLiaoList = JS007Json.getInputCanKaoZiLiao();
        for (String canKaoZiLiao : CanKaoZiLiaoList) {
            Paragraph canKaoZiLiaoContent = new Paragraph("■ " + canKaoZiLiao, normalxiao4song);
            canKaoZiLiaoContent.setSpacingAfter(10f); // 设置段落下空白
            canKaoZiLiaoContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
            document.add(canKaoZiLiaoContent);
        }

        Paragraph CeShiNeiRong = new Paragraph("三、测试内容", bold4song);
        CeShiNeiRong.setSpacingBefore(10f); // 设置段落上空白
        CeShiNeiRong.setSpacingAfter(20f); // 设置段落下空白
        CeShiNeiRong.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiNeiRong);

        Paragraph GongNengXingCeShi = new Paragraph("功能性测试", boldxiao4song);
        GongNengXingCeShi.setSpacingAfter(10f); // 设置段落下空白
        GongNengXingCeShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(GongNengXingCeShi);

        PdfPTable GongNengXingCeShiTable = new PdfPTable(48);
        GongNengXingCeShiTable.setWidthPercentage(100);
        // 第一行
        GongNengXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("功能模块", bold5song, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));
        GongNengXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("功能要求", bold5song, Element.ALIGN_CENTER, 28, 2, 25f, paddings3, borderWidth));
        GongNengXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试结果", bold5song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));

        List<TestContent> GongNengXingCeShiList = JS007Json.getInputGongNengXingCeShi();
        for (TestContent testContent : GongNengXingCeShiList) {
            GongNengXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getContent(), normal5song, Element.ALIGN_LEFT, 12, 2, 25f, paddings3, borderWidth));
            GongNengXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getDescription(), normal5song, Element.ALIGN_LEFT, 28, 2, 25f, paddings3, borderWidth));
            GongNengXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getResult(), normal5song, Element.ALIGN_LEFT, 8, 2, 25f, paddings3, borderWidth));
        }
        document.add(GongNengXingCeShiTable);

        Paragraph XiaoLvCeShi = new Paragraph("效率测试", boldxiao4song);
        XiaoLvCeShi.setSpacingAfter(10f); // 设置段落下空白
        XiaoLvCeShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(XiaoLvCeShi);

        PdfPTable XiaoLvCeShiTable = new PdfPTable(48);
        XiaoLvCeShiTable.setWidthPercentage(100);
        // 第一行
        XiaoLvCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试特性", bold5song, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));
        XiaoLvCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试说明", bold5song, Element.ALIGN_CENTER, 28, 2, 25f, paddings3, borderWidth));
        XiaoLvCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试结果", bold5song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));

        List<TestContent> XiaoLvCeShiList = JS007Json.getInputXiaoLvCeShi();
        for (TestContent testContent : XiaoLvCeShiList) {
            XiaoLvCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getContent(), normal5song, Element.ALIGN_LEFT, 12, 2, 25f, paddings3, borderWidth));
            XiaoLvCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getDescription(), normal5song, Element.ALIGN_LEFT, 28, 2, 25f, paddings3, borderWidth));
            XiaoLvCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getResult(), normal5song, Element.ALIGN_LEFT, 8, 2, 25f, paddings3, borderWidth));
        }
        document.add(XiaoLvCeShiTable);

        Paragraph KeYiZhiXingCeShi = new Paragraph("可移植性测试", boldxiao4song);
        KeYiZhiXingCeShi.setSpacingAfter(10f); // 设置段落下空白
        KeYiZhiXingCeShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(KeYiZhiXingCeShi);

        PdfPTable KeYiZhiXingCeShiTable = new PdfPTable(48);
        KeYiZhiXingCeShiTable.setWidthPercentage(100);
        // 第一行
        KeYiZhiXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试特性", bold5song, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));
        KeYiZhiXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试说明", bold5song, Element.ALIGN_CENTER, 28, 2, 25f, paddings3, borderWidth));
        KeYiZhiXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试结果", bold5song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));

        List<TestContent> KeYiZhiXingCeShiList = JS007Json.getInputKeYiZhiXingCeShi();
        for (TestContent testContent : KeYiZhiXingCeShiList) {
            KeYiZhiXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getContent(), normal5song, Element.ALIGN_LEFT, 12, 2, 25f, paddings3, borderWidth));
            KeYiZhiXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getDescription(), normal5song, Element.ALIGN_LEFT, 28, 2, 25f, paddings3, borderWidth));
            KeYiZhiXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getResult(), normal5song, Element.ALIGN_LEFT, 8, 2, 25f, paddings3, borderWidth));
        }
        document.add(KeYiZhiXingCeShiTable);

        Paragraph YiYongXingCeShi = new Paragraph("易用性测试", boldxiao4song);
        YiYongXingCeShi.setSpacingAfter(10f); // 设置段落下空白
        YiYongXingCeShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YiYongXingCeShi);

        PdfPTable YiYongXingCeShiTable = new PdfPTable(48);
        YiYongXingCeShiTable.setWidthPercentage(100);
        // 第一行
        YiYongXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试特性", bold5song, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));
        YiYongXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试说明", bold5song, Element.ALIGN_CENTER, 28, 2, 25f, paddings3, borderWidth));
        YiYongXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试结果", bold5song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));

        List<TestContent> YiYongXingCeShiList = JS007Json.getInputYiYongXingCeShi();
        for (TestContent testContent : YiYongXingCeShiList) {
            YiYongXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getContent(), normal5song, Element.ALIGN_LEFT, 12, 2, 25f, paddings3, borderWidth));
            YiYongXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getDescription(), normal5song, Element.ALIGN_LEFT, 28, 2, 25f, paddings3, borderWidth));
            YiYongXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getResult(), normal5song, Element.ALIGN_LEFT, 8, 2, 25f, paddings3, borderWidth));
        }
        document.add(YiYongXingCeShiTable);

        Paragraph KeKaoXingCeShi = new Paragraph("可靠性测试", boldxiao4song);
        KeKaoXingCeShi.setSpacingAfter(10f); // 设置段落下空白
        KeKaoXingCeShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(KeKaoXingCeShi);

        PdfPTable KeKaoXingCeShiTable = new PdfPTable(48);
        KeKaoXingCeShiTable.setWidthPercentage(100);
        // 第一行
        KeKaoXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试特性", bold5song, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));
        KeKaoXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试说明", bold5song, Element.ALIGN_CENTER, 28, 2, 25f, paddings3, borderWidth));
        KeKaoXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试结果", bold5song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));

        List<TestContent> KeKaoXingCeShiList = JS007Json.getInputKeKaoXingCeShi();
        for (TestContent testContent : KeKaoXingCeShiList) {
            KeKaoXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getContent(), normal5song, Element.ALIGN_LEFT, 12, 2, 25f, paddings3, borderWidth));
            KeKaoXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getDescription(), normal5song, Element.ALIGN_LEFT, 28, 2, 25f, paddings3, borderWidth));
            KeKaoXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getResult(), normal5song, Element.ALIGN_LEFT, 8, 2, 25f, paddings3, borderWidth));
        }
        document.add(KeKaoXingCeShiTable);

        Paragraph KeWeiHuXingCeShi = new Paragraph("可维护性测试", boldxiao4song);
        KeWeiHuXingCeShi.setSpacingAfter(10f); // 设置段落下空白
        KeWeiHuXingCeShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(KeWeiHuXingCeShi);

        PdfPTable KeWeiHuXingCeShiTable = new PdfPTable(48);
        KeWeiHuXingCeShiTable.setWidthPercentage(100);
        // 第一行
        KeWeiHuXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试特性", bold5song, Element.ALIGN_CENTER, 12, 2, 25f, paddings3, borderWidth));
        KeWeiHuXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试说明", bold5song, Element.ALIGN_CENTER, 28, 2, 25f, paddings3, borderWidth));
        KeWeiHuXingCeShiTable.addCell(ItextUtils.createGreyCell_Height("测试结果", bold5song, Element.ALIGN_CENTER, 8, 2, 25f, paddings3, borderWidth));

        List<TestContent> KeWeiHuXingCeShiList = JS007Json.getInputKeWeiHuXingCeShi();
        for (TestContent testContent : KeWeiHuXingCeShiList) {
            KeWeiHuXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getContent(), normal5song, Element.ALIGN_LEFT, 12, 2, 25f, paddings3, borderWidth));
            KeWeiHuXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getDescription(), normal5song, Element.ALIGN_LEFT, 28, 2, 25f, paddings3, borderWidth));
            KeWeiHuXingCeShiTable.addCell(ItextUtils.createCell_Height(testContent.getResult(), normal5song, Element.ALIGN_LEFT, 8, 2, 25f, paddings3, borderWidth));
        }
        document.add(KeWeiHuXingCeShiTable);

        Paragraph CeShiZhiXingJiLu = new Paragraph("四、测试执行记录", bold4song);
        CeShiZhiXingJiLu.setSpacingBefore(10f); // 设置段落上空白
        CeShiZhiXingJiLu.setSpacingAfter(20f); // 设置段落下空白
        CeShiZhiXingJiLu.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiZhiXingJiLu);

        Paragraph KongBai = new Paragraph("以 下 空 白", normal4song);
        KongBai.setSpacingBefore(10f); // 设置段落上空白
        KongBai.setSpacingAfter(20f); // 设置段落下空白
        KongBai.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(KongBai);
    }

}
