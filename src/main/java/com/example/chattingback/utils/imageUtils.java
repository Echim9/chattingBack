package com.example.chattingback.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@Component
public class imageUtils {


    private static final int WIDTH = 220;

    /**
     * 根据图片路径读取图片到缓
     *
     * @param fileName 图片路径
     * @return
     */
    public static BufferedImage readImage(String fileName) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(fileName));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return bi;
    }

    /**
     * 根据文件对象读取到缓
     *
     * @param file 文件对象
     * @return
     */
    public static BufferedImage readImage(File file) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return bi;
    }

    /**
     * 将缓存图片输出到文件
     *
     * @param im         缓存图片
     * @param formatName 文件格式(后缀:jpg)
     * @param fileName   文件路径
     * @return
     */
    public static boolean writeImage(RenderedImage im, String formatName, String fileName) {
        boolean result = false;
        try {
            result = ImageIO.write(im, formatName, new File(fileName));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

    /**
     * 将缓存图片输出到文件
     *
     * @param im         缓存图片
     * @param formatName 文件格式(后缀:jpg)
     * @param targetFile 目标文件�?
     * @return
     */
    public static boolean writeImage(RenderedImage im, String formatName, File targetFile) {
        boolean result = false;
        try {
            result = ImageIO.write(im, formatName, targetFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

    /**
     * 将缓存图片输出到输出
     *
     * @param im
     * @param formatName
     * @param stream
     * @return
     */
    public static boolean writeImage(RenderedImage im, String formatName, OutputStream stream) {
        boolean result = false;
        try {
            result = ImageIO.write(im, formatName, stream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

    /**
     * 输出为JPEG格式图片
     *
     * @param im
     * @param fileName
     * @return
     */
    public static boolean writeJPEGImage(RenderedImage im, String fileName) {
        return writeImage(im, "JPEG", fileName);
    }

    /**
     * 输出为JPEG格式
     *
     * @param im
     * @param targetFile
     * @return
     */
    public static boolean writeJPEGImage(RenderedImage im, File targetFile) {
        return writeImage(im, "JPEG", targetFile);
    }

    public static boolean writeGIFImage(RenderedImage im, String fileName) {
        return writeImage(im, "GIF", fileName);
    }

    public static boolean writeGIFImage(RenderedImage im, File targetFile) {
        return writeImage(im, "GIF", targetFile);
    }

    public static boolean writePNGImage(RenderedImage im, String fileName) {
        return writeImage(im, "PNG", fileName);
    }

    public static boolean writePNGImage(RenderedImage im, File targetFile) {
        return writeImage(im, "PNG", targetFile);
    }

    public static boolean writeBMPImage(RenderedImage im, String fileName) {
        return writeImage(im, "BMP", fileName);
    }

    public static boolean writeBMPImage(RenderedImage im, File targetFile) {
        return writeImage(im, "BMP", targetFile);
    }

    /**
     * 复制图片到目标文�?
     *
     * @param file       源文�?
     * @param targetFile 目标文件
     * @return 真为成功
     */
    public static boolean copyImage(File file, File targetFile) {
        String postfix = getPostfix(file);
        if ("JPEG".equalsIgnoreCase(postfix) || "GPG".equalsIgnoreCase(postfix)) {
            return writeJPEGImage(writeThumb(file), targetFile);
        }
        if ("GIF".equalsIgnoreCase(postfix)) {
            return writeGIFImage(writeThumb(file), targetFile);
        }
        if ("PNG".equalsIgnoreCase(postfix)) {
            return writePNGImage(writeThumb(file), targetFile);
        }
        if ("BMP".equalsIgnoreCase(postfix)) {
            return writeBMPImage(writeThumb(file), targetFile);
        }
        return writeJPEGImage(writeThumb(file), targetFile);
    }

    /**
     * 复制图片文件
     *
     * @param file       源文�?
     * @param targetFile 目标文件
     * @param width      宽度
     * @return
     */
    public static boolean copyImage(File file, File targetFile, int width) {
        String postfix = getPostfix(file);
        if ("JPEG".equalsIgnoreCase(postfix) || "GPG".equalsIgnoreCase(postfix)) {
            return writeJPEGImage(writeThumb(file, width), targetFile);
        }
        if ("GIF".equalsIgnoreCase(postfix)) {
            return writeGIFImage(writeThumb(file, width), targetFile);
        }
        if ("PNG".equalsIgnoreCase(postfix)) {
            return writePNGImage(writeThumb(file, width), targetFile);
        }
        if ("BMP".equalsIgnoreCase(postfix)) {
            return writeBMPImage(writeThumb(file, width), targetFile);
        }
        return writeJPEGImage(writeThumb(file, width), targetFile);
    }

    /**
     * 得到文件后缀名称
     *
     * @param file 文件
     * @return
     */
    public static String getPostfix(File file) {
        String postfix = file.getAbsolutePath();
        postfix = postfix.substring(postfix.lastIndexOf(".") + 1).toUpperCase();
        return postfix;
    }

    /**
     * 宽度超过默认值后，得到缩放图�?
     *
     * @param file 文件
     * @return
     */
    public static BufferedImage writeThumb(File file) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
            int x = 0, y = 0;
            if (bi.getWidth() > WIDTH) {
                x = WIDTH;
                y = Math.round(bi.getHeight() / (bi.getWidth() / (x * 1.0f)));
            } else {
                x = bi.getWidth();
                y = bi.getHeight();
            }
            return getZoomImage(bi, x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }

    /**
     * 宽度超过默认值后，得到缩放图
     *
     * @param file  文件
     * @param width 宽度
     * @return 缓存图片
     */
    public static BufferedImage writeThumb(File file, int width) {
        int x = 0, y = 0;
        if (width > WIDTH) {
            x = WIDTH;
            y = Math.round(width / (width / (x * 1.0f)));
        } else {
            x = width;
            y = width;
        }
        return getZoomImage(file, x, y);
    }

    /**
     * 根据文件路径和宽度高度得到缩放图
     *
     * @param path   路径
     * @param width  宽度
     * @param height 高度
     * @return 缓存图片
     */
    public static BufferedImage getZoomImage(String path, int width, int height) {
        return getZoomImage(new File(path), width, height);
    }

    /**
     * 得到缩放图片
     *
     * @param file   文件
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage getZoomImage(File file, int width, int height) {
        BufferedImage bi;
        BufferedImage oimage = null;
        try {
            bi = ImageIO.read(file);
            return getZoomImage(bi, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oimage;
    }

    /**
     * 得到缩放图片
     *
     * @param img    图片
     * @param width  宽度
     * @param height 高度
     * @return
     */
    public static BufferedImage getZoomImage(Image img, int width, int height) {
        BufferedImage oimage = null;
        Image temp = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        oimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        oimage.getGraphics().drawImage(temp, 0, 0, null);
        oimage.getGraphics().dispose();
        return oimage;
    }

    //实现人员签名图片合并
    public static String doImageMerging(String docIDs) {
//C:softgzex5X5.2.7_devX5.2.7apache-tomcatwebapps.... untimeBusinessServer
        String path = "";
        String[] tempPaths = path.split("webapps");
// 在内存中创建一个文件对象，注意：此时还没有在硬盘对应目录下创建实实在在的文件
        String pathFile = tempPaths[0] + "personImages";
//测试
        String fileNameTemap = "bbbb";
        crateFiles(pathFile, fileNameTemap);
//根据docId下载图片
        String[] images = docIDs.split(",");
        String image1 = pathFile + "//" + createImageName() + ".jpg";//图片1地址
        try {
            download(images[0], new File(image1));
        } catch (Exception e1) {
// TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String image2 = pathFile + "//" + createImageName() + ".jpg";
        ;//图片2地址
        try {
            download(images[1], new File(image2));
        } catch (Exception e1) {
// TODO Auto-generated catch block
            e1.printStackTrace();
        }
// 读取待合并的文件
        BufferedImage bi1 = null;
        BufferedImage bi2 = null;
// 调用mergeImage方法获得合并后的图像
        BufferedImage destImg = null;
// System.out.println("图片合并的情况：");
        try {
            bi2 = getBufferedImage(image1);
            bi1 = getBufferedImage(image2);
        } catch (IOException e) {
            e.printStackTrace();
        }
// 调用mergeImage方法获得合并后的图像
        try {
            destImg = mergeImage(bi1, bi2, false);//true为水平，false为垂直
        } catch (IOException e) {
            e.printStackTrace();
        }
// 保存图像
        String savePath = pathFile + "//";//文件存放路径
        String fileName = createImageName() + ".jpg";//文件名称
        String fileType = "jpg";//文件格式
        saveImage(destImg, savePath, fileName, fileType);
        System.out.println("图片合并完毕!");
        return savePath + fileName;
    }

    //创建文件夹
    private static void crateFiles(String path, String fileName) {
        File f = new File(path, fileName);
        if (f.exists()) {
// 文件已经存在，输出文件的相关信息
            System.out.println(f.getAbsolutePath());
            System.out.println(f.getName());
            System.out.println(f.length());
        } else {
// 先创建文件所在的目录
            f.getParentFile().mkdirs();
            try {
// 创建新文件
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("创建新文件时出现了错误。。。");
                e.printStackTrace();
            }
        }
    }

    //下载图片
    public static void download(String urlString, File filename) throws Exception {
        System.out.println("download xml start.......");
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        OutputStream os = new FileOutputStream(filename);
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        is.close();
        System.out.println("download xml over.......");
    }

    /**
     * @param fileUrl 文件绝对路径或相对路径
     * @return 读取到的缓存图像
     * @throws IOException 路径错误或者不存在该文件时抛出IO异常
     */
    public static BufferedImage getBufferedImage(String fileUrl)
            throws IOException {
        File f = new File(fileUrl);
        return ImageIO.read(f);
    }

    /**
     * @param savedImg 待保存的图像
     * @param saveDir  保存的目录
     * @param fileName 保存的文件名，必须带后缀，比如 "beauty.jpg"
     * @param format   文件格式：jpg、png或者bmp
     * @return
     */
    public static boolean saveImage(BufferedImage savedImg, String saveDir,
                                    String fileName, String format) {
        boolean flag = false;
// 先检查保存的图片格式是否正确
        String[] legalFormats = {"jpg", "JPG", "png", "PNG", "bmp", "BMP"};
        int i = 0;
        for (i = 0; i < legalFormats.length; i++) {
            if (format.equals(legalFormats[i])) {
                break;
            }
        }
        if (i == legalFormats.length) { // 图片格式不支持
            System.out.println("不是保存所支持的图片格式!");
            return false;
        }
// 再检查文件后缀和保存的格式是否一致
        String postfix = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (!postfix.equalsIgnoreCase(format)) {
            System.out.println("待保存文件后缀和保存的格式不一致!");
            return false;
        }
        String fileUrl = saveDir + fileName;
        File file = new File(fileUrl);
        try {
            flag = ImageIO.write(savedImg, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
     * mergeImage方法不做判断，自己判断。
     *
     * @param img1         待合并的第一张图
     * @param img2         带合并的第二张图
     * @param isHorizontal 为true时表示水平方向合并，为false时表示垂直方向合并
     * @return 返回合并后的BufferedImage对象
     * @throws IOException
     */
    public static BufferedImage mergeImage(BufferedImage img1,
                                           BufferedImage img2, boolean isHorizontal) throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();
// 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
// 生成新图片
        BufferedImage DestImage = null;
        if (isHorizontal) { // 水平方向合并
            DestImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
        } else { // 垂直方向合并
            Graphics2D g2d = null;
            if (w1 > w2) {
                DestImage = new BufferedImage(w1, h1 + h2,
                        BufferedImage.TYPE_INT_RGB);//TYPE_INT_RGB
                g2d = DestImage.createGraphics();
                g2d.setPaint(Color.WHITE);
                g2d.fillRect(0, 0, w1, h1 + h2);
                g2d.dispose();
            } else {
                DestImage = new BufferedImage(w2, h1 + h2,
                        BufferedImage.TYPE_INT_RGB);//TYPE_INT_RGB
                g2d = DestImage.createGraphics();
                g2d.setPaint(Color.WHITE);
                g2d.fillRect(0, 0, w2, h1 + h2);
                g2d.dispose();
            }
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(0, h1, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
        }
        return DestImage;
    }

    //随机生成图片名称
    public static String createImageName() {
// 创建 GUID 对象
        UUID uuid = UUID.randomUUID();
// 得到对象产生的ID
        String a = uuid.toString();
// 转换为大写
        a = a.toUpperCase();
// 替换 -
        a = a.replaceAll("-", "");
        return a;
    }


    //传入file进行格式转化
    public static File transferToFile(MultipartFile multipartFile) {
//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file=File.createTempFile(filename[0], filename[1] + ".");
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}

