package com.njustc.onlinebiz.doc.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class HeaderFooter extends PdfPageEventHelper {
    String header;
    String[] footer;
    int headerToPage;               // 添加页眉，从第一页直到某页为止， 可设为0或-1，表示不添加
    int footerFromPage;             // 页脚从哪一页开始， 如果是-1，则没有页脚
    boolean isHaderLine;            // 该文档是否需要页眉处画直线
    boolean isFooterLine;           // 该文档是否需要页脚处画直线
    float[] headerLoc;
    float[] footerLoc;
    float headLineOff, footLineOff;

    public HeaderFooter(String header, String[] footer, int headerToPage, int footerFromPage, boolean isHaderLine, boolean isFooterLine,
                 float[] headerLoc, float[] footerLoc, float headLineOff, float footLineOff) {
        this.header = header;
        this.footer = footer;
        this.headerToPage = headerToPage;
        this.footerFromPage = footerFromPage;
        this.isHaderLine = isHaderLine;
        this.isFooterLine = isFooterLine;
        this.headerLoc = headerLoc;
        this.footerLoc = footerLoc;
        this.headLineOff = headLineOff;
        this.footLineOff = footLineOff;
    }

    PdfTemplate totalPage;  // 总页数模板
    static BaseFont headerBf, footerBf;
    static Font headFont, feetFont;
    static float headFontSize = 10f;
    static float footFontSize = 9.5f;
    static {
        try {
            headerBf = BaseFont.createFont("font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            footerBf = BaseFont.createFont("font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            headFont = new Font(headerBf, headFontSize, Font.NORMAL);
            feetFont = new Font(footerBf, footFontSize, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开文档时，创建一个总页数的模版
     * */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfContentByte cb =writer.getDirectContent();
        totalPage = cb.createTemplate(50, 50);      // 共 页 的矩形的长宽高
    }

    /**
     * 一页加载完成触发，写入页眉和页脚
     * */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
//            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            if (writer.getPageNumber() <= headerToPage) {                         // 写入页眉
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(header, headFont), headerLoc[0], headerLoc[1], 0);
            }
            if ( footerFromPage > -1 && writer.getPageNumber() >= footerFromPage) {     // 写入页脚
                int pageS = writer.getPageNumber();
                String foot1 = footer[0] + pageS + footer[1];
                Phrase halfFooter = new Phrase(foot1, feetFont);
                float len = footerBf.getWidthPoint(foot1, footFontSize);
                PdfContentByte cb = writer.getDirectContent();
                ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, halfFooter, footerLoc[0], footerLoc[1], 0);
                cb.addTemplate(totalPage, footerLoc[0] + len, footerLoc[1]); // 调节模版显示的位置
            }
            if (isHaderLine) {                                                      // 写入页眉处直线
                PdfPTable table = new PdfPTable(1);             // 用来画页眉页脚处直线
                table.setTotalWidth(document.right() - document.left());
                PdfPCell cell = table.getDefaultCell();
                cell.setBorderWidth(1.0f);
                cell.setBorder(Rectangle.BOTTOM);
                table.addCell(cell);
                table.writeSelectedRows(0, 1, document.left(), headerLoc[1] + headLineOff, writer.getDirectContent());
            }
            if (isFooterLine) {                                                     // 写入页脚处直线
                PdfPTable table = new PdfPTable(1);             // 用来画页眉页脚处直线
                table.setTotalWidth(document.right() - document.left());
                PdfPCell cell = table.getDefaultCell();
                cell.setBorder(Rectangle.TOP);
                cell.setBorderWidth(0.3f);
                table.addCell(cell);
                table.writeSelectedRows(0, 1, document.left(), footerLoc[1] + footLineOff, writer.getDirectContent());
            }
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * 关闭文档时，将模板替换成实际的Y值。至此，page x of y 制作完毕，完美兼容各种文档size。
     * */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        totalPage.beginText();
        totalPage.setFontAndSize(footerBf, footFontSize);           // 生成的模版的字体、颜色
        totalPage.showText(writer.getPageNumber() + footer[2]);       // 模版显示的内容
        totalPage.endText();
        totalPage.closePath();
    }
}
