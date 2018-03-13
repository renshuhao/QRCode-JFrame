package com.cfth;

import com.cfth.qrgenerator.ZxingHandler;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * Created By Rsh
 *
 * @Description
 * @Date: 2018/3/12
 * @Time: 16:37
 */
public class NewFormTest {

    static PdfFont helvetica = null;
    static PdfFont helveticaBold = null;

    public static void main(String[] args) throws IOException, WriterException {
        helvetica = PdfFontFactory.createFont(FontConstants.HELVETICA);
        helveticaBold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        // 模板文件路径
        String src = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\form\\1-4.pdf";
        // 生成的文件路径
        String dest = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\form\\dest.pdf";

        PdfDocument srcDoc = new PdfDocument(new PdfReader(src));
        PdfPage origPage = srcDoc.getPage(1);
        PdfAcroForm srcForm = PdfAcroForm.getAcroForm(srcDoc, true);
        Map<String, PdfFormField> srcFields = srcForm.getFormFields();

        PdfDocument descDoc = new PdfDocument(new PdfWriter(dest));
        descDoc.addEventHandler(PdfDocumentEvent.INSERT_PAGE, new MyEventHandler(origPage));

        Document doc = new Document(descDoc, PageSize.A4);

        PdfAcroForm form = PdfAcroForm.getAcroForm(descDoc, true);
        //form.setGenerateAppearance(true);

        PdfPage page = descDoc.addPage(origPage.copyTo(descDoc));
        int num = 1;
        for(Map.Entry<String, PdfFormField> e : srcFields.entrySet()) {
            String key = e.getKey();
            PdfFormField field = e.getValue();
            PdfWidgetAnnotation widgetAnnotation = field.getWidgets().get(0);
            PdfArray annotationRect = widgetAnnotation.getRectangle();
            Rectangle rectangle = annotationRect.toRectangle();
            float x = rectangle.getLeft();
            float y = rectangle.getBottom();
            float width = rectangle.getWidth();
            float height = rectangle.getHeight();
            System.out.println("x=="+x);
            System.out.println("y=="+y);
            System.out.println("width=="+width);
            System.out.println("height=="+height);

            key = e.getKey() + "" + num;

            PdfButtonFormField button = PdfFormField.createPushButton(descDoc,
                    new Rectangle(x, y, width, height), key, "RESET");
            button.setAction(PdfAction.createResetForm(new String[] {"name", "language", "experience1", "experience2", "experience3", "shift", "info"}, 0));
            //button.setBackgroundColor(null);
            //button.setBorderColor(null);
            //button.setBorderWidth(0);
            //button.setColor(null);
            //form.addField(button, page);

            BitMatrix bitMatrix = ZxingHandler.GetBitMatrix("http://memorynotfound.com", 1, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M, 0);
            BufferedImage bimage = ZxingHandler.toBufferedImage(bitMatrix);
            Image image3 = new Image(ImageDataFactory.create(bimage, java.awt.Color.BLACK));
            image3.scaleAbsolute(width, height);
            image3.setFixedPosition(x, y);
            doc.add(image3);
        }
        num ++;

        //
        Rectangle rg = page.getPageSize();
        float rgx = rg.getLeft();
        float rgy = rg.getBottom();
        float rgwidth = rg.getWidth();
        float rgheight = rg.getHeight();

        doc = doc.add(new AreaBreak(new PageSize(rg)));
        //page = descDoc.addPage(origPage.copyTo(descDoc));

        for(Map.Entry<String, PdfFormField> e : srcFields.entrySet()) {
            String key = e.getKey();
            PdfFormField field = e.getValue();
            PdfWidgetAnnotation widgetAnnotation = field.getWidgets().get(0);
            PdfArray annotationRect = widgetAnnotation.getRectangle();
            Rectangle rectangle = annotationRect.toRectangle();
            float x = rectangle.getLeft();
            float y = rectangle.getBottom();
            float width = rectangle.getWidth();
            float height = rectangle.getHeight();
            System.out.println("x=="+x);
            System.out.println("y=="+y);
            System.out.println("width=="+width);
            System.out.println("height=="+height);

            key = e.getKey() + "" + num;

            PdfButtonFormField button = PdfFormField.createPushButton(doc.getPdfDocument(),
                    new Rectangle(x, y, width, height), key, "RESET");
            button.setAction(PdfAction.createResetForm(new String[] {"name", "language", "experience1", "experience2", "experience3", "shift", "info"}, 0));
            //form.addField(button, page);

            BitMatrix bitMatrix = ZxingHandler.GetBitMatrix("http://memorynotfound.com", 1, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M, 0);
            BufferedImage bimage = ZxingHandler.toBufferedImage(bitMatrix);
            Image image3 = new Image(ImageDataFactory.create(bimage, java.awt.Color.BLACK));
            image3.scaleAbsolute(width, height);
            image3.setFixedPosition(x, y);
            doc.add(image3);
        }

        descDoc.close();
        srcDoc.close();
    }

    protected static class MyEventHandler implements IEventHandler {
        PdfPage origPage;
        public MyEventHandler(PdfPage origPage){
            this.origPage = origPage;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);

            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            //Set background
            Color limeColor = new DeviceCmyk(0.208f, 0, 0.584f, 0);
            Color blueColor = new DeviceCmyk(0.445f, 0.0546f, 0, 0.0667f);
            /*pdfCanvas.saveState()
                    .setFillColor(pageNumber % 2 == 1 ? limeColor : blueColor)
                    .rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight())
                    .fill().restoreState();*/

            //Add header and footer
            /*pdfCanvas.beginText()
                    .setFontAndSize(helvetica, 9)
                    .moveText(pageSize.getWidth() / 2 - 60, pageSize.getTop() - 20)
                    .showText("THE TRUTH IS OUT THERE")
                    .moveText(60, -pageSize.getTop() + 30)
                    .showText(String.valueOf(pageNumber))
                    .endText();*/


            try {
                com.itextpdf.kernel.pdf.xobject.PdfXObject pageCopy = origPage.copyAsFormXObject(pdfDoc);
                Rectangle orig = origPage.getPageSize();
                PdfCanvas pdfCanvas1 = pdfCanvas.addXObject(pageCopy,  0, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Add watermark
            Canvas canvas = new Canvas(pdfCanvas, pdfDoc, page.getPageSize());
            /*canvas.setFontColor(Color.WHITE);
            canvas.setProperty(Property.FONT_SIZE, 60);
            canvas.setProperty(Property.FONT, helveticaBold);
            canvas.showTextAligned(new Paragraph("CONFIDENTIAL"), 298, 421, pdfDoc.getPageNumber(page),
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);*/

            pdfCanvas.release();
        }
    }

}
