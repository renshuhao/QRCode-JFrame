package com.cfth.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import com.itextpdf.kernel.geom.AffineTransform;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.renderer.ConcreteImageRendererFactory;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.gvt.renderer.ImageRendererFactory;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class SvgPngConverter {

    /**
     *@Description: 将svg字符串转换为png
     *@Author:
     *@param svgCode svg代码
     *@param pngFilePath  保存的路径
     *@throws IOException io异常
     *@throws TranscoderException svg代码异常
     */
    public static void convertToPng(String svgCode,String pngFilePath) throws IOException,TranscoderException{

        File file = new File (pngFilePath);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile ();
            outputStream = new FileOutputStream (file);
            convertToPng (svgCode, outputStream);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    /**
     *@Description: 将svgCode转换成png文件，直接输出到流中
     *@param svgCode svg代码
     *@param outputStream 输出流
     *@throws TranscoderException 异常
     *@throws IOException io异常
     */
    public static void convertToPng(String svgCode,OutputStream outputStream) throws TranscoderException,IOException{
        try {
            byte[] bytes = svgCode.getBytes ("UTF-8");
            PNGTranscoder t = new PNGTranscoder ();
            TranscoderInput input = new TranscoderInput (new ByteArrayInputStream (bytes));
            TranscoderOutput output = new TranscoderOutput (outputStream);
            t.transcode (input, output);
            outputStream.flush ();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    /**
     *@Description: 将svg字符串转换为png
     *@Author:
     *@param doc Document
     *@param pngFilePath  保存的路径
     *@throws IOException io异常
     *@throws TranscoderException svg代码异常
     */
    public static void convertToPng(Document doc, String pngFilePath) throws IOException,TranscoderException{

        File file = new File (pngFilePath);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile ();
            outputStream = new FileOutputStream (file);
            convertToPng (doc, outputStream);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    /**
     *@Description: 将svgCode转换成png文件，直接输出到流中
     *@param doc Document
     *@param outputStream 输出流
     *@throws TranscoderException 异常
     *@throws IOException io异常
     */
    public static void convertToPng(Document doc, OutputStream outputStream) throws TranscoderException,IOException{
        try {
            PNGTranscoder t = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput (outputStream);
            t.transcode (input, output);
            outputStream.flush ();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    public static BufferedImage renderToImage(int width, int height, boolean stretch) throws IOException {
        String imagePath1 = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\test.svg";
        String imagePath2 = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\test.png";

        File file = new File(imagePath1);
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        SVGDocument document = f.createSVGDocument(file.toURI().toString());

        ImageRendererFactory rendererFactory;
        rendererFactory = new ConcreteImageRendererFactory();
        ImageRenderer renderer = rendererFactory.createStaticImageRenderer();

        GVTBuilder builder = new GVTBuilder();
        BridgeContext ctx = new BridgeContext(new UserAgentAdapter());
        ctx.setDynamicState(BridgeContext.STATIC);
        GraphicsNode rootNode = builder.build(ctx, document);

        renderer.setTree(rootNode);

        float docWidth  = (float) ctx.getDocumentSize().getWidth();
        float docHeight = (float) ctx.getDocumentSize().getHeight();

        float xscale = width/docWidth;
        float yscale = height/docHeight;
        if(!stretch){
            float scale = Math.min(xscale, yscale);
            xscale = scale;
            yscale = scale;
        }

        AffineTransform px  = AffineTransform.getScaleInstance(xscale, yscale);

        double tx = -0 + (width/xscale - docWidth)/2;
        double ty = -0 + (height/yscale - docHeight)/2;
        px.translate(tx, ty);
        //cgn.setViewingTransform(px);

        renderer.updateOffScreen(width, height);
        renderer.setTree(rootNode);
        //renderer.setTransform(px);
        //renderer.clearOffScreen();
        renderer.repaint(new Rectangle(0, 0, width, height));

        return renderer.getOffScreen();

    }

    public static void main(String[] args) throws IOException, TranscoderException {
        String imagePath1 = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\test.svg";
        String imagePath2 = "E:\\workspace\\intellij IDEA\\QRCode-JFrame\\test.png";

        File file = new File(imagePath1);
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        Document doc = f.createDocument(file.toURI().toString());

        convertToPng(doc, imagePath2);
    }

}