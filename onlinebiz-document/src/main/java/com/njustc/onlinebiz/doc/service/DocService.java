package com.njustc.onlinebiz.doc.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.njustc.onlinebiz.doc.domain.JS003;
import com.njustc.onlinebiz.doc.mapper.DocumentOSSProvider;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class DocService {

    public String fillJS003(JS003 newJson) throws IOException, DocumentException {
        Document doc=new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, byteArrayOutputStream);
        doc.open();

        //doc.addTitle("委托测试软件功能列表");

        BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);
        Font font=new Font(baseFont);
        font.setSize(16);
        font.setStyle(Font.BOLD);

        Chunk chunk=new Chunk("委托测试软件功能列表");
        chunk.setFont(font);
        Paragraph paragraph=new Paragraph(chunk);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(paragraph);

        BaseFont baseContentFont = BaseFont.createFont("C:/Windows/Fonts/simhei.ttf", BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);
        Font nameFont=new Font(baseContentFont);
        nameFont.setSize(14);
        nameFont.setStyle(Font.NORMAL);

        Font contentFont=new Font(baseFont);
        contentFont.setSize(12);
        contentFont.setStyle(Font.NORMAL);

        PdfPTable table=new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setTotalWidth(400);
        table.setLockedWidth(true);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        Chunk chunk1=new Chunk("软件名称");
        chunk1.setFont(nameFont);
        Paragraph paragraph1=new Paragraph(chunk1);
        PdfPCell cell1=new PdfPCell(paragraph1);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setFixedHeight(30);
        table.addCell(cell1);

        Chunk chunk2=new Chunk(newJson.getInputRuanJianMingCheng());
        chunk2.setFont(contentFont);
        Paragraph paragraph2=new Paragraph(chunk2);
        PdfPCell cell2=new PdfPCell(paragraph2);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setFixedHeight(30);
        cell2.setColspan(3);
        table.addCell(cell2);

        Chunk chunk3=new Chunk("版本号");
        chunk3.setFont(nameFont);
        Paragraph paragraph3=new Paragraph(chunk3);
        PdfPCell cell3=new PdfPCell(paragraph3);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3.setFixedHeight(30);
        table.addCell(cell3);

        Chunk chunk4=new Chunk(newJson.getInputBanBenHao());
        chunk4.setFont(contentFont);
        Paragraph paragraph4=new Paragraph(chunk4);
        PdfPCell cell4=new PdfPCell(paragraph4);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell4.setFixedHeight(30);
        table.addCell(cell4);

        Chunk chunk5=new Chunk("软件功能项目");
        chunk5.setFont(nameFont);
        Paragraph paragraph5=new Paragraph(chunk5);
        PdfPCell cell5=new PdfPCell(paragraph5);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell5.setFixedHeight(30);
        cell5.setColspan(3);
        table.addCell(cell5);

        Chunk chunk6=new Chunk("功能说明");
        chunk6.setFont(nameFont);
        Paragraph paragraph6=new Paragraph(chunk6);
        PdfPCell cell6=new PdfPCell(paragraph6);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell6.setFixedHeight(30);
        cell6.setColspan(3);
        table.addCell(cell6);

        //System.out.println(newJson.getGongNengSum());
        for (int i=0;i<newJson.getGongNengSum();i++){
            System.out.println(newJson.getZiGongNengSum(i));
            Chunk gongNengNameChunk=new Chunk(newJson.getGongNengName(i));
            gongNengNameChunk.setFont(contentFont);
            Paragraph gongNengNameParagraph=new Paragraph(gongNengNameChunk);
            PdfPCell gongNengNameCell=new PdfPCell(gongNengNameParagraph);
            gongNengNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            gongNengNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            gongNengNameCell.setFixedHeight(30);
            gongNengNameCell.setRowspan(newJson.getZiGongNengSum(i));
            table.addCell(gongNengNameCell);
            for (int j=0;j<newJson.getZiGongNengSum(i);j++){
                Chunk ziGongNengNameChunk=new Chunk(newJson.getZiGongNengName(i,j));
                ziGongNengNameChunk.setFont(contentFont);
                Paragraph ziGongNengNameParagraph=new Paragraph(ziGongNengNameChunk);
                PdfPCell ziGongNengNameCell=new PdfPCell(ziGongNengNameParagraph);
                ziGongNengNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                ziGongNengNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                ziGongNengNameCell.setFixedHeight(30);
                ziGongNengNameCell.setColspan(2);
                table.addCell(ziGongNengNameCell);

                Chunk ziGongNengShuoMingChunk=new Chunk(newJson.getZiGongNengShuoMing(i,j));
                ziGongNengShuoMingChunk.setFont(contentFont);
                Paragraph ziGongNengShuoMingParagraph=new Paragraph(ziGongNengShuoMingChunk);
                PdfPCell ziGongNengShuoMingCell=new PdfPCell(ziGongNengShuoMingParagraph);
                ziGongNengShuoMingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                ziGongNengShuoMingCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                ziGongNengShuoMingCell.setFixedHeight(30);
                ziGongNengShuoMingCell.setColspan(3);
                table.addCell(ziGongNengShuoMingCell);
            }
        }

        doc.add(table);

        Font zhuFont=new Font(baseContentFont);
        zhuFont.setSize(12);
        zhuFont.setStyle(Font.BOLD);
        Chunk zhuChunk=new Chunk("注：\n");
        zhuChunk.setFont(zhuFont);
        Paragraph zhuParagraph=new Paragraph(zhuChunk);
        zhuParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        doc.add(zhuParagraph);

        Chunk lastChunk=new Chunk("1.软件功能说明按树型结构方式描述。软件功能项目栏中应列出软件产品的所有功能（包括各级子功能）。具体可见样例。\n" +
                "      2.功能说明栏目应填写功能项目概述等信息。\n");
        lastChunk.setFont(contentFont);
        Paragraph lastParagraph=new Paragraph(lastChunk);
        lastParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        doc.add(lastParagraph);

        doc.close();
        try {
            DocumentOSSProvider documentOSSProvider=new DocumentOSSProvider();
            return documentOSSProvider.uploadDocument("JS003.pdf", byteArrayOutputStream.toByteArray());
        }
        catch (Exception e){
            e.printStackTrace();
            return "minio error";
        }
    }
}
