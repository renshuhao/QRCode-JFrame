package com.cfth.qrgenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.cfth.utils.Utils;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 生成IDcode认证二维码类
 */
public class CreateAuthQRCode {
    public static int authSize;
    static File file;
    int x;
    int y;
    int size;

    CreateAuthQRCode(int authSize, File file) {
        CreateAuthQRCode.authSize = authSize;
        CreateAuthQRCode.file = file;
    }

    /**
     * 生成IDcode认证二维码
     *
     * @param content              生成内容
     * @param size                 码大小
     * @param errorCorrectionLevel 容错率
     */
    public void create(String content, int size, ErrorCorrectionLevel errorCorrectionLevel) throws IOException, WriterException {
        pub(size);
        InputStream in = ClassLoader.getSystemResourceAsStream(authSize + ".png");
        BufferedImage imageOne = ImageIO.read(in);
        BitMatrix matrix = ZxingHandler.GetBitMatrix(content, this.size, errorCorrectionLevel, 0);
        BufferedImage imageTwo = ZxingHandler.toBufferedImageWithAuthColor(matrix);
        Utils.modifyImageTogeter(imageOne, imageTwo, x, y, file);
    }

    /**
     * 生成IDcode认证二维码
     *
     * @param content              生成内容
     * @param size                 码大小
     * @param errorCorrectionLevel 容错率
     * @return BufferedImage 返回生成我IDcode认证码
     */
    public BufferedImage createBufferedImage(String content, int size, ErrorCorrectionLevel errorCorrectionLevel) {
        pub(size);
        System.out.println(authSize);
        InputStream in = ClassLoader.getSystemResourceAsStream(authSize + ".png");
        BitMatrix matrix = null;
        BufferedImage imageOne = null;
        ;
        try {
            imageOne = ImageIO.read(in);
            matrix = ZxingHandler.GetBitMatrix(content, this.size, errorCorrectionLevel, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BufferedImage imageTwo = ZxingHandler.toBufferedImageWithAuthColor(matrix);
        return Utils.modifyImageTogeterRBufferedImage(imageOne, imageTwo, x, y, file);
    }

    private void pub(int size) {
        switch (size) {
            case 200:
                this.size = 82;
                x = 62;
                y = 64;
                break;
            case 300:
                this.size = 120;
                x = 94;
                y = 97;
                break;
            case 500:
                this.size = 200;
                x = 158;
                y = 164;
                break;
            case 600:
                this.size = 240;
                x = 190;
                y = 196;
                break;
            case 800:
                this.size = 320;
                x = 254;
                y = 262;
                break;
        }
    }

    public static void main(String[] args) {
        URL url = ClassLoader.getSystemResource("300.png");
        System.out.println(url.getPath());
    }
}
