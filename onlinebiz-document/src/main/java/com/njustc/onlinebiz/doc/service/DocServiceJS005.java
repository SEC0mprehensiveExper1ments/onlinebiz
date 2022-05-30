package com.njustc.onlinebiz.doc.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.doc.dao.OSSProvider;
import com.njustc.onlinebiz.doc.exception.DownloadDAOFailureException;
import com.njustc.onlinebiz.doc.exception.DownloadNotFoundException;
import com.njustc.onlinebiz.doc.exception.DownloadPermissionDeniedException;
import com.njustc.onlinebiz.doc.model.JS005;
import com.njustc.onlinebiz.doc.util.HeaderFooter;
import com.njustc.onlinebiz.doc.util.ItextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class DocServiceJS005 {

    private static final String CONTRACT_SERVICE = "http://onlinebiz-contract";
    private final RestTemplate restTemplate;
    private final OSSProvider ossProvider;
    private String contracId;


    public DocServiceJS005(RestTemplate restTemplate, OSSProvider ossProvider) {
        this.restTemplate = restTemplate;
        this.ossProvider = ossProvider;
    }

    /**
     * 通过 contracId 向contract服务获取对象，以供后续生成文档并下载
     * @param contracId 待下载的合同 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从contract服务中获得对象，则返回；否则，返回异常信息
     * */
    public Contract getContractById(String contracId, Long userId, Role userRole) {
        // 调用contract服务的getContract的接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = CONTRACT_SERVICE + "/api/contract/" + contracId;
        ResponseEntity<Contract> responseEntity = restTemplate.getForEntity(url + params, Contract.class);
        // 检查委托 id 及权限的有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该委托ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Contract contract = responseEntity.getBody();
        this.contracId = contracId;

        return contract;
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
        // absolutePath = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("font")).getPath().substring(1);
        // 在 iText 中每一个单位大小默认近似于点（pt）
        // 1mm = 72 ÷ 25.4 ≈ 2.834645...（pt）
        marginLeft = 85f;
        marginRight = 85f;
        marginTop = 60;
        marginBottom = 72;
    }

    private static JS005 JS005Json;



    /**
     * 填充JS005文档
     * */
    public String fill(JS005 newJson) {
        JS005Json = newJson;
        String pdfPath = DOCUMENT_DIR + "JS005_" + contracId +  "out.pdf";
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(pdfPath);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 2.5 添加页眉/页脚
            String header = "NST－04－JS005－2011";
            String[] footer = new String[]{"第 ", " 页 共 ", " 页"};
            int headerToPage = 100;
            int footerFromPage = 1;
            boolean isHaderLine = true;
            boolean isFooterLine = false;
            float[] headerLoc = new float[]{document.right() - 5, document.top() + 15};
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
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to generate a pdf";
        }
        // 上传pdf
        try {
            if(ossProvider.upload(
                    "doc", "JS005_" + contracId + ".pdf", Files.readAllBytes(Path.of(pdfPath)), "application/pdf")) {
                deleteOutFile(pdfPath);
                return "https://oss.syh1en.asia/doc/JS005_" + contracId + ".pdf";
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
        try {
            File file = new File(pdfPath);
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete" + file.getName() + "is failed.");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private BaseFont bfSimSun;
    private Font titlefont;
    private Font textfont;

    /**
     * 生成JS005文档第一页
     * */
    public void generatePageOne(Document document) throws Exception {
        // 加载字体
        try {
            bfSimSun = BaseFont.createFont(DOCUMENT_DIR + "font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfSimSun, 15.5f, Font.BOLD);
            textfont = new Font(bfSimSun, 12f, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 标题
        Paragraph title = new Paragraph("软件项目委托测试保密协议", titlefont);
        title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
        title.setSpacingBefore(-15f); //设置段落上空白
        title.setSpacingAfter(25f); //设置段落下空白

        Paragraph text1 = new Paragraph();
        text1.setAlignment(Element.ALIGN_JUSTIFIED);
        text1.setLeading(24f);
        Chunk chunk1 = new Chunk(JS005Json.getInputJiaFang(), textfont);
        chunk1.setUnderline(0.3f, -3f);
        Chunk chunk2 = new Chunk(JS005Json.getInputWeiTuoXiangMu(), textfont);
        chunk2.setUnderline(0.3f, -3f);

        text1.add(new Chunk("    委托方", textfont));
        text1.add(chunk1);
        text1.add(new Chunk("（以下简称“甲方”）与南京大学计算机软件新技术国家重点实验室（简称“乙方”）在签订《", textfont));
        text1.add(chunk2);
        text1.add(new Chunk("软件项目委托测试》委托合同的前提下，为保证双方的合法权利，经协双方达成如下保密协议：\n", textfont));
        text1.add(new Chunk("    1、甲方不得向第三方透露在合作期间获得和知晓的乙方(包括其分支机构) 的商业秘密和其他有关的保密信息。商业秘密包括技术秘密和经营秘密，" +
                "其中技术秘密包括计算机软件、数据库、技术报告、测试报告、检测报告、实验数据、测试结果、操作手册、技术文档、相关的函电等。经营秘密包括但不限于双方洽谈的情况、签署的任何文件，包括合同、协议、备忘录等文件中所包含的一切信息、" +
                "定价政策、设备资源、人力资源信息等。\n", textfont));
        text1.add(new Chunk("    2、乙方负有对甲方委托测试的软件保密的责任，保密内容包括：软件产品代码、软件可执行程序、测试报告、测试结果、操作手册、技术文档、用户手册等。\n", textfont));
        text1.add(new Chunk("    3、未经对方书面同意，任何一方不得在双方合作目的之外使用或向第三方透露对方的任何商业秘密，不管这些商业秘密是口头的或是书面的，还是以磁 盘、胶片或电子邮件等形式存在的。\n", textfont));
        text1.add(new Chunk("    4、在对方公司内活动时，应尊重对方有关保密的管理规定，听从接待人员的安排和引导。未经允许不得进入对方实验室、办公室内受控的工作环境，与对方技术人员进行的交流，仅限于合作项目有关的内容。\n", textfont));
        text1.add(new Chunk("    5、如果一方违反上述条款，另一方有权根据违反的程度以及造成的损害采取以下措施：\n", textfont));
        text1.add(new Chunk("    （1）终止双方的合作；\n", textfont));
        text1.add(new Chunk("    （2）要求赔偿因失密造成的损失。\n", textfont));
        text1.add(new Chunk("    在采取上述措施之前，一方将给予违约的另一方合理的在先通知。\n", textfont));
        text1.add(new Chunk("    6、负有保密义务的双方，如果涉密人因本方无法控制的原因(如擅自离职) 造成由涉密人有意泄密，其相应的民事和法律责任由当事人承担。\n", textfont));
        text1.add(new Chunk("    7、与本协议有关的任何争议，双方应通过友好协商解决。如协商不成，任何一方可将此争议提交南京市仲裁委员会进行仲裁。仲裁裁决是终局的，对双方均有约束力。\n", textfont));
        text1.add(new Chunk("    8、本协议作为委托测试合同的附件，一式两份，双方各执一份，与合同具有同等法律效力。\n", textfont));
        text1.add(new Chunk("    本协议自双方授权代表签字盖章之日起生效，但有效期不限于合同有效期。\n", textfont));
        text1.setSpacingAfter(30f); //设置段落下空白

        float tableWidth = document.right() - document.left();
        PdfPTable table = ItextUtils.createTable(new float[] {tableWidth/4+30, tableWidth/4-30, tableWidth/4+30, tableWidth/4-30}, tableWidth);
        table.addCell(ItextUtils.createCell("甲 方：(公章)", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_RIGHT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("乙 方：(公章)", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_RIGHT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("法人代表：\n\n\n\n", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("法人代表：\n\n\n\n", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("     年   月    日", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("     年   月    日", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));
        table.addCell(ItextUtils.createCell("", textfont, Element.ALIGN_LEFT, ItextUtils.NO_BORDER, new float[]{40f+5f, 40f, 0f, 0f}));

        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(text1);
        document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
        document.add(table);
    }
}
