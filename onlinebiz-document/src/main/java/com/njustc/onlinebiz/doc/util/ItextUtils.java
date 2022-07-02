package com.njustc.onlinebiz.doc.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItextUtils {

    public static final int NO_BORDER = 0;
    public static final int BOX = Rectangle.BOX;

/**------------------------创建表格单元格的方法start----------------------------*/
    /**
     * 创建单元格(指定字体)
     * @param value
     * @param font
     * @return
     */
    public static PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int align, int haveBorder, float[] paddings) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setBorder(haveBorder);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param alignH
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int alignH, int alignV, int haveBorder, float[] paddings, float thickness, float leading) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(alignV);
        cell.setHorizontalAlignment(alignH);
        Chunk chunk = ItextUtils.fixedUnderlineChunk(value, font, 16, thickness);
//        Chunk chunk = new Chunk(value, font);
//        chunk.setUnderline(thickness, -3f);
        cell.setPhrase(new Phrase(chunk));
        cell.setBorder(haveBorder);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setLeading(leading, 0f);
        return cell;
    }
    /**
     * 创建单元格（指定字体、左对齐零padding..、单元格跨x列合并、设置单元格内边距）
     * @param values
     * @param font
     * @param otherFont
     * @param alignH
     * @param haveBorder
     * @param paddings
     * @param thickness
     * @return
     */
    public static PdfPCell createCell(String[] values, Font font, Font otherFont, int alignH,  int alignV, int haveBorder, int colSpan, int rowSpan, float[] paddings, float thickness) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(alignV);
        cell.setHorizontalAlignment(alignH);
        Phrase container = new Phrase();
        // combine.setLeading();    // 设置行距
        for (int i = 0; i < values.length; i++) {
            if (i % 2 == 0) {
                Chunk chunk = new Chunk(values[i], font);
                chunk.setUnderline(thickness, -3f);
                container.add(chunk);
            }
            else {
                Chunk chunk = new Chunk(values[i], otherFont);
                container.add(chunk);
            }
        }
        cell.setPhrase(container);
        cell.setRowspan(rowSpan);
        cell.setColspan(colSpan);
        cell.setBorder(haveBorder);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x行合并）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param rowspan
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int align, int colspan, int rowspan, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, font));
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
//        cell.setBorder(haveBorder);
//        cell.setLeading(12f, 0f);
        return cell;
    }

    public static PdfPCell createCell_Height(String value, Font font, int align, int colspan, int rowspan, float height, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, font));
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setFixedHeight(height);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
//        cell.setBorder(haveBorder);
//        cell.setLeading(12f, 0f);
        return cell;
    }

    public static PdfPCell createCell_Leading(String value, Font font, int align, int colspan, int rowspan, float leading, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        Phrase phrase=new Phrase(value, font);
        cell.setPhrase(phrase);
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
//        cell.setBorder(haveBorder);
        cell.setLeading(leading, 0f);
        return cell;
    }

    public static PdfPCell createGreyCell(String value, Font font, int align, int colspan, int rowspan, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(220, 220, 220));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, font));
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
//        cell.setBorder(haveBorder);
//        cell.setLeading(12f, 0f);
        return cell;
    }

    public static PdfPCell createGreyCell_Height(String value, Font font, int align, int colspan, int rowspan, float height, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(220, 220, 220));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, font));
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setFixedHeight(height);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
//        cell.setBorder(haveBorder);
//        cell.setLeading(12f, 0f);
        return cell;
    }

    public static PdfPCell createGreenCell_Height(String value, Font font, int align, int colspan, int rowspan, float height, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(240, 255, 240));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, font));
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setFixedHeight(height);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
//        cell.setBorder(haveBorder);
//        cell.setLeading(12f, 0f);
        return cell;
    }

    public static PdfPCell createCell(int align, int colspan, int rowspan, float fixedLeading, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
        cell.setLeading(fixedLeading, 0f);
//        cell.setBorder(haveBorder);
        return cell;
    }

    public static PdfPCell createCell(String value, Font font, int align, int colspan, int rowspan, float fixedLeading, float[] paddings, float borderWidth) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, font));
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorderWidth(borderWidth);
//        cell.setBorder(haveBorder);
        cell.setLeading(fixedLeading, 0f);
        return cell;
    }

    public static PdfPCell createCell(String value, Font font, int alignH, int alignV, int colspan, int rowspan, float[] paddings, int haveBorder, int fixLength, float thickness) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(alignV);
        cell.setHorizontalAlignment(alignH);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setPhrase(new Phrase(value, font));
//        Chunk chunk = ItextUtils.fixedUnderlineChunk(value, font, fixLength, thickness);
//        cell.setPhrase(new Phrase(chunk));
//        cell.setPaddingTop(10.0f);
//        cell.setPaddingBottom(10.0f);
//        cell.setPaddingLeft(5.0f);
//        cell.setPaddingRight(5.0f);
//        cell.setBorderWidth(1f);
        cell.setPaddingTop(paddings[0]);
        cell.setPaddingBottom(paddings[1]);
        cell.setPaddingLeft(paddings[2]);
        cell.setPaddingRight(paddings[3]);
        cell.setBorder(haveBorder);
//        cell.setBorder(haveBorder);
//        cell.setLeading(12f, 0f);
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }
    /**
     * 创建单元格（指定字体、左对齐零padding..、单元格跨x列合并、设置单元格内边距）
     * @param values
     * @param font
     * @param otherFont
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public static PdfPCell createCell(String[] values, Font font, Font otherFont, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        Phrase combine = new Phrase();
        // combine.setLeading();    // 设置行距
        for (int i = 0; i < values.length; i++) {
            combine.add(new Chunk(values[i], (i%2 == 0) ? font : otherFont));
        }
        cell.setPhrase(combine);
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(3.0f);
            cell.setPaddingLeft(0.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setBorderWidthLeft(borderWidth[0]);
        cell.setBorderWidthRight(borderWidth[1]);
        cell.setBorderWidthTop(borderWidth[2]);
        cell.setBorderWidthBottom(borderWidth[3]);
        cell.setPaddingTop(paddingSize[0]);
        cell.setPaddingBottom(paddingSize[1]);
        if (flag) {
            cell.setColspan(2);
        }
        return cell;
    }

/**------------------------创建表格单元格的方法end----------------------------*/


/**--------------------------创建表格的方法start------------------- ---------*/
    /**
     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
     * @param colNumber
     * @param align
     * @return
     */
    public static PdfPTable createTable(int colNumber, int align, float maxWidth) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(align);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    /**
     * 创建指定列宽、列数的表格
     * @param widths
     * @return
     */
    public static PdfPTable createTable(float[] widths, float maxWidth) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.getDefaultCell().setBorder(1);
//            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            // 直接调用table.addCell()，不会设置边界
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
/**--------------------------创建表格的方法end------------------- ---------*/

    /**
     * 给图片添加图章（通过拷贝生成另一份pdf实现，新生成pdf文件名后缀为 _out.pdf）
     * @param pdfPath
     * @param sealPath
     * @param pageNums
     * @param locX
     * @param locY
     * @param percent
     */
    public static void addImageSeal(String pdfPath, String[] sealPath, int[] pageNums, float[] locX, float[] locY, float[] percent) throws Exception {
        String newPDFPath = pdfPath.replace("tmp", "out");            //生成的新文件路径
        PdfReader reader = null;                            // pdf读入流
        FileOutputStream output = null;                     // 文件输出流
        PdfStamper stamper = null;                          // pdf填充器
        try {
            output = new FileOutputStream(newPDFPath);      // 打开文件输出流
            reader = new PdfReader(pdfPath);
            stamper = new PdfStamper(reader, output);       // 根据表单生成一个新的pdf(通过打印器)
            // 添加图片印章
            for (int i = 0; i < pageNums.length; i++) {
                Image img = Image.getInstance(sealPath[i]);
                img.setAbsolutePosition(locX[i], locY[i]);
                img.scalePercent(percent[i]);
                PdfContentByte under = stamper.getUnderContent(pageNums[i]);     // 背景被覆盖，字体不被覆盖
                under.addImage(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stamper.close();
                reader.close();
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成每行固定长度的下划线Chunk，以实现下划线对齐居中效果，超过一行则换行
     * TODO: 英文、数字、和其他特殊字符
     * */
    public static Chunk fixedUnderlineChunk(String value, Font font, int fixLength, float thickness) {
        String res = "";
        Pattern pattern = Pattern.compile("[a-z0-9A-Z_-]");
        while (value.length() > 0) {
            Matcher matcher1 = pattern.matcher(value);
            int letters = 0;
            while (matcher1.find()) letters++;
            if (value.length() - letters / 2 > fixLength) {
                String tmp = value.substring(0, fixLength);
                value = value.substring(fixLength);
                Matcher matcher2 = pattern.matcher(tmp);
                int subletters = 0;
                while (matcher2.find())  subletters++;
                if (subletters > 0) {
                    tmp += value.substring(subletters / 2);
                    value = value.substring(subletters / 2);
                    if (subletters % 2 == 1) tmp += " ";
                }
                res += " " + tmp + " \u0232\n";
            }
            else {
                int blanks = fixLength - value.length() + letters / 2;
                String tmp = " ";
                for (int i = 0; i < blanks; i++) { tmp = " " + tmp; }
                tmp = tmp + value;
                for (int i = 0; i < blanks; i++) { tmp = tmp + " "; }
                if (letters % 2 == 1) tmp = tmp + " ";
                tmp = tmp + " \u0232";
                value = "";
                res += tmp;
            }
        }
        Chunk chunk = new Chunk(res, font);
        chunk.setUnderline(thickness, -3f);
        return chunk;
    }

    public static Chunk leastUnderlineChunk(String value, Font font, int leastLen, float thickness, boolean isCenter) {
        String res;
        System.out.println(value);
        Pattern pattern = Pattern.compile("[a-z0-9A-Z_-]|\\x20");
        Matcher matcher1 = pattern.matcher(value);
        int letters = 0;
        while (matcher1.find()) letters++;
        int blanks = leastLen - value.length() + letters / 2;
        if (blanks > 0) {
            if (isCenter == true) {
                res = "";
                for (int i = 0; i < blanks; i++) {
                    res = " " + res;
                }
                res = res + value;
                for (int i = 0; i < blanks; i++) {
                    res += " ";
                }
                if (letters % 2 == 1) res += " ";
                res += "\u0232";
            }
            else {
                res = value;
                for (int i = 0; i < blanks; i++) {
                    res += "  ";
                }
                if (letters % 2 == 1) res += " ";
                res += "\u0232";
            }
        }
        else
            res = value;
        System.out.println(blanks);
        Chunk chunk = new Chunk(res, font);
        chunk.setUnderline(thickness, -3f);
        return chunk;
    }

    public static Phrase crossSetFont(String[] value, Font font, Font otherFont) {
        Phrase phrase = new Phrase();
        for(int i = 0; i < value.length; i++) {
            phrase.add(i % 2 == 0 ?
                    new Chunk(value[i], font) :
                    new Chunk(value[i], otherFont));
        }
        return phrase;
    }
}
