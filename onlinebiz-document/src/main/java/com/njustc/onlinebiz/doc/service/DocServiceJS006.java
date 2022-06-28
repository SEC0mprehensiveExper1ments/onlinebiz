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
            File file = new File(pdfPath.replaceAll("\\\\", "/"));
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

    private static BaseFont bfChinese;
    private static BaseFont bfHeiTi;
    private static Font titlefont1;
    private static Font titlefont2;
    private static Font normal5song;
    private static Font bold5song;
    private static Font bold4songblue;
    private static Font bold2song;
    private static Font bold3song;

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
            bold4songblue = new Font(bfChinese, 14f, Font.BOLD);
            bold4songblue.setColor(new BaseColor(61, 89, 171));
            bold2song = new Font(bfChinese, 22f, Font.BOLD);
            bold3song = new Font(bfChinese, 16f, Font.BOLD);
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
        cell.setBorderColor(new BaseColor(61, 89, 171));
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
        modificationTable.addCell(ItextUtils.createGreyCell("日期", normal5song, Element.ALIGN_LEFT, 17, 2, paddings3, borderWidth));
        modificationTable.addCell(ItextUtils.createGreyCell("AMD", normal5song, Element.ALIGN_LEFT, 4, 2, paddings3, borderWidth));
        modificationTable.addCell(ItextUtils.createGreyCell("修订者", normal5song, Element.ALIGN_LEFT, 8, 2, paddings3, borderWidth));
        modificationTable.addCell(ItextUtils.createGreyCell("说明", normal5song, Element.ALIGN_LEFT, 10, 2, paddings3, borderWidth));

        List<Modification> modifications = JS006Json.getWenDangXiuGaiJiLu();
        for (Modification modification : modifications) {
            modificationTable.addCell(ItextUtils.createCell(modification.getVersion(), normal5song, Element.ALIGN_LEFT, 4, 2, paddings3, borderWidth));
            modificationTable.addCell(ItextUtils.createCell(modification.getDate().toString(), normal5song, Element.ALIGN_LEFT, 17, 2, paddings3, borderWidth));
            modificationTable.addCell(ItextUtils.createCell(modification.getMethod().toString(), normal5song, Element.ALIGN_LEFT, 4, 2, paddings3, borderWidth));
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

        Paragraph directoryTitle = new Paragraph("目录", bold4songblue);
        directoryTitle.setSpacingBefore(60f); // 设置段落上空白
        directoryTitle.setSpacingAfter(10f); // 设置段落下空白
        directoryTitle.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        Paragraph directory = new Paragraph();
        directory.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        directory.add(
                new Phrase(
                        ""
                                + "1 引言………………………………………………………………………………………………1\n"
                                + "     1.1标识………………………………………………………………………………………1\n"
                                + "     1.2系统概述…………………………………………………………………………………1\n"
                                + "     1.3文档概述…………………………………………………………………………………1\n"
                                + "     1.4基线………………………………………………………………………………………1\n"
                                + "2 引用文件…………………………………………………………………………………………1\n"
                                + "3 软件测试环境……………………………………………………………………………………1\n"
                                + "     3.1硬件………………………………………………………………………………………1\n"
                                + "     3.2软件………………………………………………………………………………………1\n"
                                + "     3.3其他………………………………………………………………………………………1\n"
                                + "     3.4参与组织…………………………………………………………………………………1\n"
                                + "     3.5人员………………………………………………………………………………………2\n"
                                + "4 计划………………………………………………………………………………………………2\n"
                                + "     4.1总体设计…………………………………………………………………………………2\n"
                                + "           4.1.1测试级别………………………………………………………………………2\n"
                                + "           4.1.2测试类别………………………………………………………………………2\n"
                                + "           4.1.3一般测试条件…………………………………………………………………2\n"
                                + "     4.2计划执行的测试…………………………………………………………………………2\n"
                                + "     4.3测试用例…………………………………………………………………………………2\n"
                                + "5 测试进度表………………………………………………………………………………………2\n"
                                + "6 需求的可追踪性…………………………………………………………………………………3\n"
                        , normal5song));
        document.add(directoryTitle);
        document.add(directory);

        document.newPage();

        Paragraph YinYan = new Paragraph("1 引言", bold2song);
        YinYan.setSpacingBefore(80f); // 设置段落上空白
        //YinYan.setSpacingAfter(30f); // 设置段落下空白
        YinYan.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YinYan);

        Paragraph BiaoShi = new Paragraph("1.1 标识", bold3song);
        BiaoShi.setSpacingBefore(20f); // 设置段落上空白
        //BiaoShi.setSpacingAfter(30f); // 设置段落下空白
        BiaoShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(BiaoShi);

        Paragraph BiaoShiContent = new Paragraph("    " + JS006Json.getInputBiaoShi(), normal5song);
        BiaoShiContent.setSpacingBefore(20f); // 设置段落上空白
        BiaoShiContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(BiaoShiContent);

        Paragraph XiTongGaiShu = new Paragraph("1.2 系统概述", bold3song);
        XiTongGaiShu.setSpacingBefore(20f); // 设置段落上空白
        //BiaoShi.setSpacingAfter(30f); // 设置段落下空白
        XiTongGaiShu.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(XiTongGaiShu);

        Paragraph XiTongGaiShuContent = new Paragraph("    " + JS006Json.getInputXiTongGaiShu(), normal5song);
        XiTongGaiShuContent.setSpacingBefore(20f); // 设置段落上空白
        XiTongGaiShuContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(XiTongGaiShuContent);

        Paragraph WenDangGaiShu = new Paragraph("1.3 文档概述", bold3song);
        WenDangGaiShu.setSpacingBefore(20f); // 设置段落上空白
        //BiaoShi.setSpacingAfter(30f); // 设置段落下空白
        WenDangGaiShu.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(WenDangGaiShu);

        Paragraph WenDangGaiShuContent = new Paragraph("    " + JS006Json.getInputWenDangGaiShu(), normal5song);
        WenDangGaiShuContent.setSpacingBefore(20f); // 设置段落上空白
        WenDangGaiShuContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(WenDangGaiShuContent);

        Paragraph JiXian = new Paragraph("1.4 基线", bold3song);
        JiXian.setSpacingBefore(20f); // 设置段落上空白
        //BiaoShi.setSpacingAfter(30f); // 设置段落下空白
        JiXian.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(JiXian);

        Paragraph JiXianContent = new Paragraph("    " + JS006Json.getInputJiXian(), normal5song);
        JiXianContent.setSpacingBefore(20f); // 设置段落上空白
        JiXianContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(JiXianContent);

        Paragraph YinYongWenJian = new Paragraph("2 引用文件", bold2song);
        YinYongWenJian.setSpacingBefore(20f); // 设置段落上空白
        YinYongWenJian.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YinYongWenJian);

        Paragraph YinYongWenJianContent = new Paragraph("    " + "《计算机软件文档编制规范》GB/T 8567－2006。", normal5song);
        YinYongWenJianContent.setSpacingBefore(20f); // 设置段落上空白
        YinYongWenJianContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YinYongWenJianContent);

        Paragraph HuanJing = new Paragraph("3 软件测试环境", bold2song);
        HuanJing.setSpacingBefore(20f); // 设置段落上空白
        HuanJing.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(HuanJing);

        Paragraph YingJian = new Paragraph("3.1 硬件", bold3song);
        YingJian.setSpacingBefore(20f); // 设置段落上空白
        YingJian.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YingJian);

        Paragraph YingJianContent = new Paragraph("    " + JS006Json.getInputYingJian(), normal5song);
        YingJianContent.setSpacingBefore(20f); // 设置段落上空白
        YingJianContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YingJianContent);

        Paragraph RuanJian = new Paragraph("3.2 软件", bold3song);
        RuanJian.setSpacingBefore(20f); // 设置段落上空白
        RuanJian.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(RuanJian);

        Paragraph RuanJianContent = new Paragraph("    " + JS006Json.getInputRuanJian(), normal5song);
        RuanJianContent.setSpacingBefore(20f); // 设置段落上空白
        RuanJianContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(RuanJianContent);

        Paragraph QiTa = new Paragraph("3.3 其他", bold3song);
        QiTa.setSpacingBefore(20f); // 设置段落上空白
        QiTa.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(QiTa);

        Paragraph QiTaContent = new Paragraph("    " + JS006Json.getInputQiTa(), normal5song);
        QiTaContent.setSpacingBefore(20f); // 设置段落上空白
        QiTaContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(QiTaContent);

        Paragraph CanYuZuZhi = new Paragraph("3.4 参与组织", bold3song);
        CanYuZuZhi.setSpacingBefore(20f); // 设置段落上空白
        CanYuZuZhi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CanYuZuZhi);

        Paragraph CanYuZuZhiContent = new Paragraph("    " + JS006Json.getInputCanYuZuZhi(), normal5song);
        CanYuZuZhiContent.setSpacingBefore(20f); // 设置段落上空白
        CanYuZuZhiContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CanYuZuZhiContent);

        Paragraph RenYuan = new Paragraph("3.5 人员", bold3song);
        RenYuan.setSpacingBefore(20f); // 设置段落上空白
        RenYuan.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(RenYuan);

        Paragraph RenYuanContent = new Paragraph("    " + JS006Json.getInputRenYuan(), normal5song);
        RenYuanContent.setSpacingBefore(20f); // 设置段落上空白
        RenYuanContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(RenYuanContent);

        Paragraph JiHua = new Paragraph("4 计划", bold2song);
        JiHua.setSpacingBefore(20f); // 设置段落上空白
        JiHua.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(JiHua);

        Paragraph JiHuaContent = new Paragraph("    " + "本章描述了计划测试的总范围并且描述了本测试计划适用的每个测试，包括对相关文档的审查。", normal5song);
        JiHuaContent.setSpacingBefore(20f); // 设置段落上空白
        JiHuaContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(JiHuaContent);

        Paragraph ZongTiSheJi = new Paragraph("4.1 总体设计", bold3song);
        ZongTiSheJi.setSpacingBefore(20f); // 设置段落上空白
        ZongTiSheJi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(ZongTiSheJi);

        Paragraph CeShiJiBie = new Paragraph("4.1.1 测试级别", bold3song);
        CeShiJiBie.setSpacingBefore(20f); // 设置段落上空白
        CeShiJiBie.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiJiBie);

        Paragraph CeShiJiBieContent = new Paragraph("    " + JS006Json.getInputCeShiJiBie(), normal5song);
        CeShiJiBieContent.setSpacingBefore(20f); // 设置段落上空白
        CeShiJiBieContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiJiBieContent);

        Paragraph CeShiLeiBie = new Paragraph("4.1.2 测试类别", bold3song);
        CeShiLeiBie.setSpacingBefore(20f); // 设置段落上空白
        CeShiLeiBie.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiLeiBie);

        Paragraph CeShiLeiBieContent = new Paragraph("    " + JS006Json.getInputCeShiLeiBie(), normal5song);
        CeShiLeiBieContent.setSpacingBefore(20f); // 设置段落上空白
        CeShiLeiBieContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiLeiBieContent);

        Paragraph YiBanCeShiTiaoJian = new Paragraph("4.1.3 一般测试条件", bold3song);
        YiBanCeShiTiaoJian.setSpacingBefore(20f); // 设置段落上空白
        YiBanCeShiTiaoJian.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YiBanCeShiTiaoJian);

        Paragraph YiBanCeShiTiaoJianContent = new Paragraph("    " + JS006Json.getInputYiBanCeShiTiaoJian(), normal5song);
        YiBanCeShiTiaoJianContent.setSpacingBefore(20f); // 设置段落上空白
        YiBanCeShiTiaoJianContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(YiBanCeShiTiaoJianContent);

        Paragraph JiHuaZhiXingDeCeShi = new Paragraph("4.2 计划执行的测试", bold3song);
        JiHuaZhiXingDeCeShi.setSpacingBefore(20f); // 设置段落上空白
        JiHuaZhiXingDeCeShi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(JiHuaZhiXingDeCeShi);

        Paragraph JiHuaZhiXingDeCeShiContent = new Paragraph("    " + JS006Json.getInputJiHuaZhiXingDeCeShi(), normal5song);
        JiHuaZhiXingDeCeShiContent.setSpacingBefore(20f); // 设置段落上空白
        JiHuaZhiXingDeCeShiContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(JiHuaZhiXingDeCeShiContent);

        Paragraph CeShiYongLi = new Paragraph("4.3 测试用例", bold3song);
        CeShiYongLi.setSpacingBefore(20f); // 设置段落上空白
        CeShiYongLi.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiYongLi);

        Paragraph CeShiYongLiContent = new Paragraph("    " + JS006Json.getInputCeShiYongLi(), normal5song);
        CeShiYongLiContent.setSpacingBefore(20f); // 设置段落上空白
        CeShiYongLiContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiYongLiContent);

        Paragraph CeShiJinDuBiao = new Paragraph("5 测试进度表", bold2song);
        CeShiJinDuBiao.setSpacingBefore(20f); // 设置段落上空白
        CeShiJinDuBiao.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiJinDuBiao);

        Paragraph CeShiJinDuBiaoContent = new Paragraph("    " + "此项目主要分为：业务测试和文档审查两部分的工作。两部分的工作可以并行完成。测试方为完成本方案所述的测试所需时间大约为"+JS006Json.getInputYuJiGongZuoRi()+"个工作日，如测试需求产生变更会导致测试时间的变化。\n"+"    "+"下表大致估计了本次测试各个阶段所需工作量及起止时间。下表大致估计了本次测试各个阶段所需工作量及起止时间。", normal5song);
        CeShiJinDuBiaoContent.setSpacingBefore(20f); // 设置段落上空白
        CeShiJinDuBiaoContent.setSpacingAfter(20f); // 设置段落下空白
        CeShiJinDuBiaoContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(CeShiJinDuBiaoContent);

        PdfPTable CeShiJinDuTable = new PdfPTable(4);
        CeShiJinDuTable.setWidthPercentage(100);

        // 第一行
        CeShiJinDuTable.addCell(ItextUtils.createCell("里程碑任务", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell("工作量", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell("开始时间", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell("结束时间", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        // 第二行
        CeShiJinDuTable.addCell(ItextUtils.createCell("制定测试计划", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getZhiDingJiHua().getWorkload(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getZhiDingJiHua().getStartDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getZhiDingJiHua().getEndDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        // 第三行
        CeShiJinDuTable.addCell(ItextUtils.createCell("设计测试", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getSheJiCeShi().getWorkload(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getSheJiCeShi().getStartDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getSheJiCeShi().getEndDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        // 第四行
        CeShiJinDuTable.addCell(ItextUtils.createCell("执行测试", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getZhiXingCeShi().getWorkload(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getZhiXingCeShi().getStartDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getZhiXingCeShi().getEndDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        // 第五行
        CeShiJinDuTable.addCell(ItextUtils.createCell("评估测试", normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getPingGuCeShi().getWorkload(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getPingGuCeShi().getStartDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        CeShiJinDuTable.addCell(ItextUtils.createCell(JS006Json.getPingGuCeShi().getEndDate().toString(), normal5song, Element.ALIGN_LEFT, 1, 2, paddings3, borderWidth));
        document.add(CeShiJinDuTable);

        Paragraph XuQiuKeZhuiZongXing = new Paragraph("6 需求的可追踪性", bold2song);
        XuQiuKeZhuiZongXing.setSpacingBefore(20f); // 设置段落上空白
        XuQiuKeZhuiZongXing.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(XuQiuKeZhuiZongXing);

        Paragraph XuQiuKeZhuiZongXingContent = new Paragraph("    " + JS006Json.getInputXuQiuKeZhuiZongXing(), normal5song);
        XuQiuKeZhuiZongXingContent.setSpacingBefore(20f); // 设置段落上空白
        XuQiuKeZhuiZongXingContent.setAlignment(0); // 设置文字居中 0靠左   1，居中     2，靠右
        document.add(XuQiuKeZhuiZongXingContent);
    }
}
