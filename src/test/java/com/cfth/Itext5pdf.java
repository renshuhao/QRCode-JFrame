/*
package com.cfth.qrgenerator;


import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.Document;
import com.itextpdf.signatures.PdfSigner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created By Rsh
 *
 * @Description
 * @Date: 2018/3/8
 * @Time: 16:07
 *//*

public class Itext5pdf {

    public static void main(String[] args) throws Exception {
        // 模板文件路径
        String templatePath = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\12.pdf";
        // 生成的文件路径
        String targetPath = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\target.pdf";
        // 书签名
        String fieldName = "Image1";
        // 图片路径
        String imagePath1 = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\1520563834(1).jpg";
        String imagePath3 = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\1516258435.png";

        // 读取模板文件
        InputStream input = new FileInputStream(new File(templatePath));
        PdfReader reader = new PdfReader(input);

        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
        // 提取pdf中的表单
        AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));

        form.setField("Text1", "21312321");

        int pageNo = form.getFieldPositions(fieldName).get(0).page;
        System.out.println("pageNo=="+pageNo);
        Rectangle signRect = form.getFieldPositions(fieldName).get(0).position;
        float x = signRect.getLeft();
        float y = signRect.getBottom();
        System.out.println("x=="+x);
        System.out.println("y=="+y);

        Image image = Image.getInstance(imagePath1);
        PdfImage stream = new PdfImage(image, "", null);
        PdfIndirectObject ref = stamper.getWriter().addToBody(stream);

        image.setDirectReference(ref.getIndirectReference());
        System.out.println("width="+signRect.getWidth());
        image.scaleToFit(signRect.getWidth(), signRect.getHeight());
        image.setAbsolutePosition(x, y);
        PdfContentByte over = stamper.getOverContent(pageNo);
        over.addImage(image);

        int pageNo3 = form.getFieldPositions("Image3").get(0).page;
        System.out.println("pageNo3=="+pageNo3);
        Rectangle signRect3 = form.getFieldPositions("Image3").get(0).position;
        float x3 = signRect3.getLeft();
        float y3 = signRect3.getBottom();
        System.out.println("x3=="+x3);
        System.out.println("y3=="+y3);

        //Image image3 = Image.getInstance(imagePath3);
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("http://memorynotfound.com", 150, 150, hints);
        Image image3 = barcodeQRCode.getImage();
        Rectangle rectangle = barcodeQRCode.getBarcodeSize();
        //Image image3 = Image.getInstance(imagePath3);
        System.out.println("getWidth=="+rectangle.getWidth());

        PdfImage stream3 = new PdfImage(image3, "", null);
        PdfIndirectObject ref3 = stamper.getWriter().addToBody(stream3);

        image3.setDirectReference(ref3.getIndirectReference());
        image3.scaleToFit(signRect3.getWidth(), signRect3.getHeight());
        image3.setAbsolutePosition(x3, y3);

        over.addImage(image3);

        stamper.close();
        reader.close();
    }

}
*/
