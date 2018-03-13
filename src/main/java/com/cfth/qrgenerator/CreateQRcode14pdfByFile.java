package com.cfth.qrgenerator;

import com.cfth.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * Created By Rsh
 *
 * @Description
 * @Date: 2018/3/13
 * @Time: 15:10
 */
public class CreateQRcode14pdfByFile {

    public static void create(String byFilePath) {
        ArrayList<String> list = new ArrayList<String>();
        File byFile = new File(byFilePath);
        InputStreamReader read;
        String folder = new File(byFilePath).getName().split("\\.")[0];
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
        new ProgressDialog1(list, folder).jd.setVisible(true);
    }

}

/**
 * 生成二维码进度，主要逻辑在此处理。
 */
class ProgressDialog1 {
    JDialog jd = new JDialog();
    Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
    Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
    int screenWidth = screenSize.width; // 获取屏幕的宽
    int screenHeight = screenSize.height; // 获取屏幕的高

    ProgressDialog1(ArrayList<String> list, final String folder) {
        jd.setSize(300, 160);
        jd.setTitle("提示");
        jd.setModal(true);
        jd.setLocation(screenWidth / 2 - jd.getWidth() / 2, screenHeight / 2 - jd.getHeight() / 2);
        jd.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel("正在生成PDF");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(260, 30));
        JButton button = new JButton("打开");
        button.setEnabled(false);
        Container container = jd.getContentPane();
        container.setLayout(new GridLayout(3, 1));
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel1.add(label);
        panel2.add(progressBar);
        panel3.add(button);
        container.add(panel1);
        container.add(panel2);
        container.add(panel3);
        progressBar.setStringPainted(true);
        final CreateQEcode14pdfProgress cpp = new CreateQEcode14pdfProgress(progressBar, button, list, folder);
        cpp.start();
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Runtime.getRuntime().exec("explorer.exe " + Utils.getCurrentPath() + File.separator + folder);
                    Runtime.getRuntime().exec("explorer.exe E:\\workspace\\intellij IDEA\\QRCode-JFrame\\form");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                jd.dispose();
            }
        });
        jd.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cpp.exit = true;
                try {
                    cpp.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                jd.dispose();
            }
        });
    }
}
