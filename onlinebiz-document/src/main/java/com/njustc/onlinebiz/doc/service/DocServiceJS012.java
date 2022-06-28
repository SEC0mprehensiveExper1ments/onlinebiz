package com.njustc.onlinebiz.doc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.model.JS012;
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
public class DocServiceJS012 {

    private final OSSProvider ossProvider;

    public DocServiceJS012(OSSProvider ossProvider) {
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
        marginLeft = 65f;
        marginRight = 65f;
        marginTop = 70f;
        marginBottom = 72f;
    }

    private static JS012 JS012Json;



    /**
     * 填充JS012文档
     * */
    public String fill(String entrustTestReviewId, JS012 newJson) {
        JS012Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS012_" + entrustTestReviewId + ".pdf";
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
            String header = "NST－04－JS012－2011";
            String[] footer = new String[]{"第 ", " 页 共 ", " 页"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHaderLine = true;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
            float[] footerLoc = new float[]{(document.left() + document.right()) / 2.0f - 35, document.bottom() - 20};
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
            if(ossProvider.upload(
                    "doc", "JS012_" + entrustTestReviewId + ".pdf", Files.readAllBytes(Path.of(pdfPath.replaceAll("\\\\", "/"))), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS012_" + entrustTestReviewId + ".pdf";
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

    private static Font titlefont;
    private static Font titlefont2;
    private static Font textfont;

    /**
     * 生成JS012文档第一页
     * */
    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            // 定义全局的字体静态变量
            BaseFont bfSimSun = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfSimSun, 17f, Font.BOLD);
            titlefont2 = new Font(bfSimSun, 12f, Font.NORMAL);
            textfont = new Font(bfSimSun, 10.5f, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 标题
        Paragraph title = new Paragraph("软件项目委托测试工作检查表", titlefont);
        title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(-25f); //设置段落上空白
        title.setSpacingAfter(1f); //设置段落下空白

        Paragraph text = new Paragraph();
        text.add(new Phrase("软件名称：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputRuanJianMingChen(), titlefont2, 19, 0.3f));
        text.add(new Phrase(" 版本号：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputBanBenHao(), titlefont2, 6, 0.3f));
        text.add(new Phrase("\n申报单位：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputShenBaoDanWei(), titlefont2, 30, 0.3f));
        text.add(new Phrase("\n起始时间：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputQiShiShiJian0Nian(), titlefont2, 2, 0.3f));
        text.add(new Phrase("年", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputQiShiShiJian0Yue(), titlefont2, 2, 0.3f));
        text.add(new Phrase("月", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputQiShiShiJian0Ri(), titlefont2, 2, 0.3f));
        text.add(new Phrase("日", titlefont2));
        text.add(new Phrase(" 预计完成时间：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputYuJiShiJian0Nian(), titlefont2, 2, 0.3f));
        text.add(new Phrase("年", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputYuJiShiJian0Yue(), titlefont2, 2, 0.3f));
        text.add(new Phrase("月", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputYuJiShiJian0Ri(), titlefont2, 2, 0.3f));
        text.add(new Phrase("日", titlefont2));
        text.add(new Phrase("\n主测人：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputZhuCeRen(), titlefont2, 12, 0.3f));
        text.add(new Phrase(" 实际完成时间：", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputShiJiShiJian0Nian(), titlefont2, 2, 0.3f));
        text.add(new Phrase("年", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputShiJiShiJian0Yue(), titlefont2, 2, 0.3f));
        text.add(new Phrase("月", titlefont2));
        text.add(ItextUtils.fixedUnderlineChunk(JS012Json.getInputShiJiShiJian0Ri(), titlefont2, 2, 0.3f));
        text.add(new Phrase("日", titlefont2));
        text.setLeading(30f);
        text.setSpacingAfter(17f); //设置段落下空白

        float tableWidth = 465;
        float[] widths = new float[33];
        Arrays.fill(widths, tableWidth/33);
        PdfPTable table = ItextUtils.createTable(widths, tableWidth);

        // float[] paddings = new float[]{6f, 6f, 5f, 5f};
        float[] paddings2 = new float[]{10f, 10f, 5f, 5f};
        float[] paddings3 = new float[]{5f, 5f, 5.5f, 5.5f};
        float borderWidth = 0.3f;
        float fixedLeading = 14f;

        table.addCell(ItextUtils.createCell("工作（项目）流程", textfont, Element.ALIGN_LEFT, 11, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("可预见问题及注意事项", textfont, Element.ALIGN_CENTER, 17, 2, paddings2, borderWidth));
        table.addCell(ItextUtils.createCell("确 认", textfont, Element.ALIGN_CENTER, 5, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("一、前期指导工作", textfont, Element.ALIGN_LEFT, 33, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("1", textfont, Element.ALIGN_LEFT, 2, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("接受委托单位委托测试申请", textfont, Element.ALIGN_LEFT, 9, 6, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、为委托单位提供详尽的有关软件项目委托测试\n" +
                        "   的相关法律法规、优惠政策、业务办理流程等\n" +
                        "   事项。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen011(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、建议委托单位阅读《软件项目委托测试流程图\n" +
                        "   和工作介绍》，了解申报流程；\n",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen012(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("3、根据《软件项目委托测试提交材料》，指导委\n" +
                        "   托单位提交申报资料。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen013(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("2", textfont, Element.ALIGN_LEFT, 2, 4, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("填写《软件项目委托测试申请表》、《委托测试软件功能列表》，按《软件项目委托测试提交材料》提交材料；", textfont, Element.ALIGN_LEFT, 9, 4, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、确保委托方应填内容正确、完备；纸质材料已\n" +
                        "   盖公章；",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen021(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、明确委托方按《软件项目委托测试提交材料》\n" +
                        "   提交材料。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen022(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("3", textfont, Element.ALIGN_LEFT, 2, 4, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("签订《软件项目委托测试合同》、《软件项目委托测试保密协议》", textfont, Element.ALIGN_LEFT, 9, 4, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、合同及保密协议内容、数量符合要求；",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen031(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、合同编号方式符合要求；",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen032(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("二、对委托测试软件的可测状态进行评估", textfont, Element.ALIGN_LEFT, 33, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("4", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("对委托测试软件的可测状态进行评估", textfont, Element.ALIGN_LEFT, 9, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("实验室在收到委托单位的有关资料后，即成立测试项目小组，该项目小组的任务是消化用户提供的有关资料，对委托软件的可测状态进行评估， 若委托软件未达到可测状态，" +
                        "则向委托方提出改进建议，直到委托软件达到可测状态为止。项目小组的任务包括负责编制测试方案，搭建测试环境，执行测试过程，记录测试结果，编制测试报告，提交测试报告，将有关资料归档等。",
                textfont, Element.ALIGN_JUSTIFIED, 17, 2, fixedLeading, new float[]{4f, 4f, 5.5f, 5.5f}, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen041(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("三、实施测试", textfont, Element.ALIGN_LEFT, 33, 2, paddings2, borderWidth));

        table.addCell(ItextUtils.createCell("5", textfont, Element.ALIGN_LEFT, 2, 10, new float[]{34f, 34f, 5.5f, 5.5f}, borderWidth));
        table.addCell(ItextUtils.createCell("编制测试方案", textfont, Element.ALIGN_LEFT, 9, 10, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、测试方案必须经实验室质量负责人审核，技术\n" +
                        "   负责人批准方能生效。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen051(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、委托测试软件介绍：简要介绍委托测试软件的\n" +
                        "   功能特点、应用行业及技术特性等。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen052(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("3、软件功能：以委托单位提供的功能列表为依\n" +
                        "   据，以表格形式列出所有功能项目，并对功能\n" +
                        "   列表的各功能项目按照层次关系进行编号，以\n" +
                        "   便于标识。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen053(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("4、资源需求：资源需求要列出人员需求和软硬件\n" +
                        "   设备需求。人员需求要列出人员名单、职称及\n" +
                        "   所承担的角色（项目组长或成员）；软硬件设\n" +
                        "   备需求要根据委托测试软件要求的运行环境及\n" +
                        "   实验室的设备情况，列出硬件设备的名称、型\n" +
                        "   号、配置、机身编号、用途，软件的名称、版\n" +
                        "   本号、用途等。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen054(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("5、参考文档：列出编制本方案所参考的标准、规\n" +
                        "   范及用户文档等的名称、作者、类型、版本/\n" +
                        "   标识号。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen055(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("6", textfont, Element.ALIGN_LEFT, 2, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("搭建测试环境", textfont, Element.ALIGN_LEFT, 9, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、实验室按照委托方提供的委托测试软件运行环\n" +
                        "   境搭建测试环境；",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen061(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("7", textfont, Element.ALIGN_LEFT, 2, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("实施测试", textfont, Element.ALIGN_LEFT, 9, 6, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、测试过程主要以测试方案为依据，按照用户手\n" +
                        "   册所述的操作方法运行软件，考察软件是否具\n" +
                        "   有用户手册所描述的操作界面，对功能列表的\n" +
                        "   主要功能逐项进行符合性测试并作记录，对未\n" +
                        "   测的次要功能或细节部分，应作出说明。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen071(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、对文档的测试：从完整性、正确性、一致性、\n" +
                        "   易理解性、易浏览性和外观质量六个方面，对\n" +
                        "   用户文档进行评审。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen072(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("3、对测试过程观察到的结果进行如实记录，对发\n" +
                        "   现的问题整理成问题清单；",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen073(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("8", textfont, Element.ALIGN_LEFT, 2, 8, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("编制测试报告", textfont, Element.ALIGN_LEFT, 9, 8, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、根据《软件项目委托测试报告编制作业指导书》\n" +
                        "   和测试结果编制测试报告。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, new float[]{5f, 5f, 3f, 0f}, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen081(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、检查测试报告，并填写《报告检查表》。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, new float[]{5f, 5f, 3f, 0f}, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen082(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("3、测试报告的编码请参阅《测试报告编码规则》。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, new float[]{5f, 5f, 3f, 0f}, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen083(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("4、报告审查：在分发报告前，应按实验室质量管\n" +
                        "   理程序对报告进行严格审查。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, new float[]{5f, 5f, 3f, 0f}, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen084(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("9", textfont, Element.ALIGN_LEFT, 2, 6, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("评测资料归档", textfont, Element.ALIGN_LEFT, 9, 6, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、委托测试的软件产品及测试相关文件、原始记\n" +
                        "   录等能够随时复现测试过程所需的材料，也同\n" +
                        "   测试报告一并交由实验室资料室的材料管理员\n" +
                        "   归档，以作为日后对测试结果产生异议时进行\n" +
                        "   复核或仲裁的依据。上述材料由实验室保存三\n" +
                        "   年后，委托方可凭样品接收单取回或由实验室\n" +
                        "   进行销毁。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen091(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、归档资料同时填写《软件项目委托测试资料清\n" +
                        "   单》，打印《软件委托测试资料标签》并编号\n" +
                        "   号码，贴于档案盒制定位置。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen092(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("3、该检查表与本次软件委托测试归档资料一同归\n" +
                        "   档，与《软件项目委托测试资料目录》、《软\n" +
                        "   件项目委托测试试资料清单》一起，作为软件\n" +
                        "   委托测试测试工作的检查、资料查询的主要依\n" +
                        "   据。",
                textfont, Element.ALIGN_LEFT, 17, 2, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen093(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        table.addCell(ItextUtils.createCell("10", textfont, Element.ALIGN_LEFT, 2, 16, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("附件目录", textfont, Element.ALIGN_LEFT, 9, 16, fixedLeading, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("1、《软件项目委托测试工作流程》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
//        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0101(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("2、《需提供的书面文档》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
//        table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0102(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("3、《软件项目委托测试报告编制作业指导书》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
        // table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0103(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("4、《报告检查表》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
        // table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0104(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("5、《测试报告编码规则》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
        // table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0105(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("6、《软件委托测试资料清单》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
        // table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0106(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("7、《软件委托测试资料标签》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
        // table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0107(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));
        table.addCell(ItextUtils.createCell("8、《软件委托测试资料目录》", textfont, Element.ALIGN_LEFT, 22, 2, fixedLeading, paddings3, borderWidth));
        // table.addCell(ItextUtils.createCell(JS012Json.getInputQueRen0108(), textfont, Element.ALIGN_LEFT, 5, 2, paddings3, borderWidth));

        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(text);
        document.add(table);
    }

}
