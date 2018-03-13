package com.cfth;

import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created By Rsh
 *
 * @Description
 * @Date: 2018/3/9
 * @Time: 18:15
 */
public class NewPageTest {

    public static void main(String[] args) throws IOException {

        // 模板文件路径
        String SRC = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\1-4.pdf";
        // 生成的文件路径
        String dest = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\newPage.pdf";

        PdfDocument document = new PdfDocument(new PdfReader(SRC));
        PdfPage origPage = document.getPage(1);

        PdfDocument document1 = new PdfDocument(new PdfWriter(dest));

        //PdfPage page = document1.addNewPage(PageSize.A4.rotate());
        //PdfCanvas canvas = new PdfCanvas(page);
        //PdfFormXObject pageCopy = origPage.copyAsFormXObject(document1);
        //canvas.addXObject(pageCopy, 0, 0);

        document1.addPage(origPage.copyTo(document1));

        document1.addPage(origPage.copyTo(document1));

        //PdfPage page1 = document1.addNewPage(PageSize.A4.rotate());

        document1.close();
        document.close();
    }
}
