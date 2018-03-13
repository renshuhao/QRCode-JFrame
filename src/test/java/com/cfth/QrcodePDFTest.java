package com.cfth;

import com.cfth.qrgenerator.ZxingHandler;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
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

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created By Rsh
 *
 * @Description
 * @Date: 2018/3/13
 * @Time: 11:25
 */
public class QrcodePDFTest {

    public static void main(String[] args) throws IOException, WriterException {

        // 模板文件路径
        String src = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\form\\1-4.pdf";
        // 生成的文件路径
        String dest = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\form\\dest.pdf";

        // 生成的文件路径
        String data = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\form\\网址商品码.txt";

        PdfDocument srcDoc = new PdfDocument(new PdfReader(src));
        PdfPage origPage = srcDoc.getPage(1);
        PdfAcroForm srcForm = PdfAcroForm.getAcroForm(srcDoc, true);
        Map<String, PdfFormField> srcFields = srcForm.getFormFields();

        PdfDocument descDoc = new PdfDocument(new PdfWriter(dest));
        descDoc.addEventHandler(PdfDocumentEvent.INSERT_PAGE, new MyEventHandler(origPage));

        Document doc = new Document(descDoc, new PageSize(origPage.getPageSize()));
        PdfAcroForm form = PdfAcroForm.getAcroForm(descDoc, true);
        //form.setGenerateAppearance(true);

        List<String> list = new ArrayList<String>();
        File byFile = new File(data);
        InputStreamReader read;
        try {
            read = new InputStreamReader(new FileInputStream(byFile));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if (lineTxt.trim() != null || "".equals(lineTxt.trim())) {
                    list.add(lineTxt.trim());
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("data size : "+list.size());

        createPage(doc, origPage, false, srcFields, list, 0);

        System.out.println("ok");
        descDoc.close();
        srcDoc.close();
    }

    public static void createPage(Document doc, PdfPage origPage, boolean addAreaBreak, Map<String, PdfFormField> srcFields, List<String> list, int count) throws WriterException, IOException {
        if (addAreaBreak) {
            Rectangle rg = origPage.getPageSize();
            doc = doc.add(new AreaBreak(new PageSize(rg)));
        }
        int index =  count;
        int size = 0;
        String wcode = null;
        boolean createPage = true;

        for(Map.Entry<String, PdfFormField> e : srcFields.entrySet()) {
            if (list.size() == index) {
                createPage = false;
                break;
            }
            String key = e.getKey();
            PdfFormField field = e.getValue();
            PdfWidgetAnnotation widgetAnnotation = field.getWidgets().get(0);
            PdfArray annotationRect = widgetAnnotation.getRectangle();
            Rectangle rectangle = annotationRect.toRectangle();

            if (size % (4 + 1) == 0) {
                //System.out.println("size % (4 + 1) == 0");
                wcode = list.get(index);
                String content = wcode.split(",")[0];
                //System.out.println(content);
                addImage(doc, rectangle, content);
                index ++;
            } else {
                if ((size - 1) % (4 + 1) == 0) {
                    //System.out.println("(size - 1) % (4 + 1) == 0");
                    String content = wcode.split(",")[1];
                    //System.out.println(content);
                    addImage(doc, rectangle, content);
                } else {
                    //System.out.println("else");
                    wcode = list.get(index);
                    String content = wcode.split(",")[1];
                    //System.out.println(content);
                    addImage(doc, rectangle, content);
                    index ++;
                }
            }
            size ++;
        }
        if (createPage) {
            createPage(doc, origPage, true, srcFields, list, index);
        }
    }

    public static void addImage(Document doc, Rectangle rectangle, String content) throws WriterException, IOException {
        float x = rectangle.getLeft();
        float y = rectangle.getBottom();
        float width = rectangle.getWidth();
        float height = rectangle.getHeight();

        BitMatrix bitMatrix = ZxingHandler.GetBitMatrix(content, 1, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M, 0);
        BufferedImage bimage = ZxingHandler.toBufferedImage(bitMatrix);
        Image image3 = new Image(ImageDataFactory.create(bimage, java.awt.Color.BLACK));
        image3.scaleAbsolute(width, height);
        image3.setFixedPosition(x, y);
        doc.add(image3);
    }

    public static class MyEventHandler implements IEventHandler {
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
                pdfCanvas = pdfCanvas.addXObject(pageCopy,  0, 0);
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
