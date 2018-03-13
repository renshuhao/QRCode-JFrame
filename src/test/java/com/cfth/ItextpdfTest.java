package com.cfth;


import com.cfth.qrgenerator.ZxingHandler;
import com.cfth.utils.SvgPngConverter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.signatures.PdfSigner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By Rsh
 *
 * @Description
 * @Date: 2018/3/8
 * @Time: 16:07
 */
public class ItextpdfTest {

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

        PdfDocument document = new PdfDocument(new PdfReader(templatePath), new PdfWriter(targetPath));
        Document doc = new Document(document, PageSize.A4);

        PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);
        form.setGenerateAppearance(true);
        Map<String, PdfFormField> fields = form.getFormFields();

        PdfFormField field = form.getField("Image1");
        PdfWidgetAnnotation widgetAnnotation = field.getWidgets().get(0);
        PdfArray annotationRect = widgetAnnotation.getRectangle();
        Rectangle rectangle = annotationRect.toRectangle();
        float x = rectangle.getLeft();
        float y = rectangle.getBottom();
        System.out.println("x=="+x);
        System.out.println("y=="+y);

        Image image = new Image(ImageDataFactory.create(imagePath1));
        image.scaleToFit(rectangle.getWidth(), rectangle.getHeight());
        image.setFixedPosition(x, y);
        doc.add(image);

        PdfFormField field3 = form.getField("Image3");
        PdfWidgetAnnotation widgetAnnotation3 = field3.getWidgets().get(0);
        PdfArray annotationRect3 = widgetAnnotation3.getRectangle();
        Rectangle rectangle3 = annotationRect3.toRectangle();
        float x3 = rectangle3.getLeft();
        float y3 = rectangle3.getBottom();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // BarcodeQRCode无法去掉二维码白边，解决方案：采用zxing生产二维码
        //BarcodeQRCode barcodeQRCode = new BarcodeQRCode("http://memorynotfound.com", hints);
        //PdfFormXObject pdfFormXObject = barcodeQRCode.createFormXObject(Color.BLACK, document);
        /*Image image3 = new Image(pdfFormXObject);
        image3.scaleAbsolute(rectangle3.getWidth(), rectangle3.getHeight());
        //image3.setWidth((int)rectangle3.getWidth());
        //image3.setHeight(rectangle3.getHeight());
        System.out.println("x3=="+x3);
        System.out.println("y3=="+y3);
        image3.setFixedPosition(x3, y3);
        doc.add(image3);*/

        BitMatrix bitMatrix = ZxingHandler.GetBitMatrix("http://memorynotfound.com", 1, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M, 0);
        BufferedImage bimage = ZxingHandler.toBufferedImage(bitMatrix);
        Image image3 = new Image(ImageDataFactory.create(bimage, java.awt.Color.BLACK));
        image3.scaleAbsolute(rectangle3.getWidth(), rectangle3.getHeight());
        image3.setFixedPosition(x3, y3);
        doc.add(image3);

        PdfPage page  = document.getPage(1);

        //Shrink original page content using transformation matrix
        //PdfCanvas canvas = new PdfCanvas(page);
        /*AffineTransform transformationMatrix = AffineTransform.getScaleInstance(page.getPageSize().getWidth() / orig.getWidth(), page.getPageSize().getHeight() / orig.getHeight());
        //canvas.concatMatrix(transformationMatrix);
        canvas.addXObject(pdfFormXObject, x3, y3);*/

        /*float w = pdfFormXObject.getWidth();
        float h = pdfFormXObject.getHeight();

        canvas.saveState();
        //canvas.setFillColor(Color.RED);
        canvas.rectangle(x3, y3, x, y);
        canvas.fill();
        canvas.restoreState();
        canvas.addXObject(pdfFormXObject, x3, y3);*/

        PdfPage pdfPage = document.addNewPage(PageSize.A4);
        PdfCanvas canvas = new PdfCanvas(pdfPage);

        document.close();
    }
}
