package com.cfth.qrgenerator;

import com.cfth.utils.Utils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 * 从文件创建一组二维码并保存到本地
 */
public class CreateQRcodePicByFile {
    static Settings setting = Settings.getInstance();

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
        new ProgressDialog(list, folder).jd.setVisible(true);
    }
}

/**
 * 生成二维码进度，主要逻辑在此处理。
 */
class ProgressDialog {
    JDialog jd = new JDialog();
    Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
    Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
    int screenWidth = screenSize.width; // 获取屏幕的宽
    int screenHeight = screenSize.height; // 获取屏幕的高

    ProgressDialog(ArrayList<String> list, final String folder) {
        jd.setSize(300, 160);
        jd.setTitle("提示");
        jd.setModal(true);
        jd.setLocation(screenWidth / 2 - jd.getWidth() / 2, screenHeight / 2 - jd.getHeight() / 2);
        jd.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel("正在生成图片");
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
        final CreatePicProgress cpp = new CreatePicProgress(progressBar, button, list, folder);
        cpp.start();
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("explorer.exe " + Utils.getCurrentPath() + File.separator + folder);
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