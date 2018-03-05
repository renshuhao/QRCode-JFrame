package com.cfth.qrgenerator;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JProgressBar;

import com.cfth.utils.Utils;
import com.google.zxing.WriterException;

/**
 * 从文件创建码图进程类，新建一个线程，在该线程中生成图片，并保存到本地。
 */
public class CreatePicProgress extends Thread {
    Settings setting = Settings.getInstance();
    JProgressBar progressBar;
    JButton button;
    ArrayList<String> list;
    String folder;
    public volatile boolean exit = false;

    CreatePicProgress(JProgressBar progressBar, JButton button, ArrayList<String> list, String folder) {
        this.progressBar = progressBar;
        this.button = button;
        this.list = list;
        this.folder = folder;
    }

    @Override
    public void run() {
        //这里有时间可以优化下。
        if (list.size() > 0) {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            for (int i = 0; i < list.size(); i++) {
                String content = list.get(i);
                String filename = "QR" + (i + 1);
                String path = Utils.CreateFilePathAndFile(folder, setting.getQrcodeFiletype(), filename);
                File file = new File(path);
                try {
                    if (setting.getQrcodeColor() == 2) {
                        try {
                            new CreateAuthQRCode(setting.getQrcodeSize(), file).create(content, setting.getQrcodeSize(), setting.getQrcodeErrorRate());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                    if (setting.getQrcodeFiletype().equals("eps") || setting.getQrcodeFiletype().equals("pdf")
                            || setting.getQrcodeFiletype().equals("svg")) {
                        if (setting.getQrcodeFiletype().equals("eps")) {
                            ZxingHandler.createEPSQRCode(setting, file, content);
                        }
                        if (setting.getQrcodeFiletype().equals("pdf")) {
                            ZxingHandler.createPDFQRCode(setting, file, content);
                        }
                        if (setting.getQrcodeFiletype().equals("svg")) {
                            ZxingHandler.createSVGQRCode(setting, file, content);
                        }
                    } else {
                        if (setting.getQrcodeColor() == 0) {
                            ZxingHandler.writeToFileWithColor(ZxingHandler.GetBitMatrix(content, setting.getQrcodeSize(), setting.getQrcodeErrorRate()),
                                    setting.getQrcodeFiletype(), file);
                        }
                        if (setting.getQrcodeColor() == 1) {
                            ZxingHandler.writeToFile(ZxingHandler.GetBitMatrix(content, setting.getQrcodeSize(), setting.getQrcodeErrorRate()),
                                    setting.getQrcodeFiletype(), file);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                String result = numberFormat.format((float) (i + 1) / (float) list.size());
                if (!"0".equals(result) && !"1".equals(result)) {
                    String tmp = result.split("\\.")[1];
                    int num;
                    if (tmp.length() < 2) {
                        num = Integer.parseInt(tmp) * 10;
                    } else {
                        num = Integer.parseInt(tmp);
                    }
                    progressBar.setValue(num);  //设置progressBar的进度值1-100的int值
                } else {
                    if ("1".equals(result)) {
                        progressBar.setValue(100);
                    }
                }
            }
        }
        progressBar.setIndeterminate(false);
        progressBar.setString("生成完成！");
        button.setEnabled(true);
    }
}  
