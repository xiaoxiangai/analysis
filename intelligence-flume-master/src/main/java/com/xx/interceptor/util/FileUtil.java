package com.xx.interceptor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: 大雄
 * @date: 2018/2/26
 * @time: 16:32
 * @description: 文件操作工具类
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final String tmpPath = System.getProperty("user.dir")+ File.separator + "filetmp";
    /**
     * 获取临时文件路径
     * @param fileName 文件名称
     * @return
     */
    public static String getTmpFilePath(String fileName){
        return tmpPath + File.separator + fileName;
    }

    /**
     * 获得指定文件的byte数组
     * @param filePath 文件绝对路径
     * @return
     */
    public static byte[] file2Byte(String filePath){
        ByteArrayOutputStream bos=null;
        BufferedInputStream in=null;
        try{
            File file=new File(filePath);
            if(!file.exists()){
                throw new FileNotFoundException("file not exists");
            }
            bos=new ByteArrayOutputStream((int)file.length());
            in=new BufferedInputStream(new FileInputStream(file));
            int buf_size=1024;
            byte[] buffer=new byte[buf_size];
            int len=0;
            while(-1 != (len=in.read(buffer,0,buf_size))){
                bos.write(buffer,0,len);
            }
            return bos.toByteArray();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        finally{
            try{
                if(in!=null){
                    in.close();
                }
                if(bos!=null){
                    bos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static byte[] loadRawDataFromURL(String u) throws Exception {
        URL url = new URL(u);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream is = conn.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final int BUFFER_SIZE = 2*1024;
        final int EOF = -1;

        int c;
        byte[] buf = new byte[BUFFER_SIZE];

        while (true) {
            c = bis.read(buf);
            if (c == EOF)
                break;

            baos.write(buf, 0, c);
        }

        conn.disconnect();
        is.close();

        byte[] data = baos.toByteArray();
        baos.flush();

        return data;
    }


    /**
     * 移动文件到另一个目录
     *
     * @param s          移动文件
     * @param targetPath 目标路径
     * @param newName    新文件夹名称，如果为null,或者“”则不修改名称
     */
    public static File mvFile(File s, String targetPath, String newName) {
        if (newName == null || "".equals(newName)) {
            newName = s.getName();
        }
        File file = newFile(targetPath, newName);
        s.renameTo(file);
        return file;
    }

    public static File getFileByFullPath(String fullPath) {
        File file = null;
        if (fullPath != null && !"".equals(fullPath)) {
            file = new File(fullPath);
        }
        return file;
    }

    /**
     * 复制文件
     *
     * @param s          原文件
     * @param targetPath 目标路径  例如：D:\tmp\zipUtil1
     */
    public static void copyFile(File s, String targetPath) {
        if(s.isDirectory()){
            return;
        }
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        File t = newFile(targetPath, s.getName());
        if(!t.exists()){
            try {
                if(!t.getParentFile().exists()) {
                    //如果目标文件所在的目录不存在，则创建父目录
                    System.out.println("目标文件所在目录不存在，准备创建它！");
                    if(!t.getParentFile().mkdirs()) {
                        System.out.println("创建目标文件所在目录失败！");
                    }
                }
                t.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * 在路径下新建空白文件，如果文件存在，则是获取文件
     *
     * @param targetPath D:\tmp\zipUtil1
     * @param fileName   aa.txt
     * @return
     */
    public static File newFile(String targetPath, String fileName) {
        String filePath = targetPath + fileName;
        File t = new File(filePath);
        return t;
    }

    /**
     * 创建文件
     * @param file
     * @return
     * @throws IOException
     *  String filePath = "C:/temp/a/b/c/d/e/f/g.txt";
     *  File file = new File(filePath);
     */
    public static boolean createFile(File file) throws IOException {
        if(! file.exists()) {
            makeDir(file.getParentFile());
        }
        return file.createNewFile();
    }

    /**
     * 创建文件夹
     * @param dir 文件夹名称
     */
    public static void makeDir(File dir) {
        if(! dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }


    /**
     * 生成预报文件
     * strData:预报数据;
     * strFilePath:文件路径和文件名称;
     */
    public static boolean generateFile(String strData, String strFilePath) throws Exception {
        //BaseLogger.info(strData);
        File file = new File(strFilePath);
        /* if (file.exists()) {
            file.delete();
        }*/
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] bt = strData.getBytes();
            fos.write(bt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("test1");
        System.out.println("test2");
        System.out.println("test3");
        return true;
    }
    /**
     * 生成预报文件,UUTF-8编码
     * strData:预报数据;
     * strFilePath:文件路径和文件名称;
     */
    public static boolean generateFileUTF8(String strData, String strFilePath) {
        return generateFileEncode(strData,strFilePath,"UTF-8");
    }
    /**
     * 生成预报文件,制定编码
     * strData:预报数据;
     * strFilePath:文件路径和文件名称;
     */
    public static boolean generateFileEncode(String strData, String strFilePath,String encode)  {
        //BaseLogger.info(strData);
        File file = new File(strFilePath);
        /* if (file.exists()) {
            file.delete();
        }*/
        OutputStreamWriter osw=null;
        try {
            osw = new OutputStreamWriter((new FileOutputStream(file)), encode);
            osw.write(strData);
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(osw!=null){
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 生成预报文件,制定编码
     * strData:预报数据;
     * strFilePath:文件路径和文件名称;
     */
    public static boolean generateFileEncode(List<String> dataList, String strFilePath,String encode,String line)  {
        //BaseLogger.info(strData);
        File file = new File(strFilePath);
        /* if (file.exists()) {
            file.delete();
        }*/
        OutputStreamWriter osw=null;
        try {
            osw = new OutputStreamWriter((new FileOutputStream(file)), encode);
            for(String strData:dataList){
                osw.write(strData);
                osw.write(line);
            }
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(osw!=null){
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param s
     */
    public static void deleteFile(File s) {
        if (s.isFile() && s.exists()) {
            boolean a = s.delete();
            //BaseLogger.info("文件删除："+a);
        }
    }

    /**
     * 取得此路径下的所有文件
     *
     * @param path
     * @return
     */
    public static File[] getFiles(String path) {
        File fileUpload = new File(path);
        File[] tempList = fileUpload.listFiles();
        return tempList;
    }

    /**
     * 读取文件，把读到的内容填写到List里
     *
     * @param s
     * @return
     */
    public static List<String> realFileTxt(File s) {
        if (s.isFile() && s.exists()) {
            ArrayList<String> arrayList = new ArrayList<String>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(s));
                String str = null;
                while ((str = br.readLine()) != null) {//使用readLine方法，一次读一行
                    arrayList.add(str);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        }
        return null;
    }

    /**
     * 读取文件，把读到的内容填写到List里
     *
     * @param s
     * @return
     */
    public static String realTxt(File s) {
        if (s.isFile() && s.exists()) {
            StringBuffer strings = new StringBuffer();
            try {
                BufferedReader br = new BufferedReader(new FileReader(s));
                String str = null;
                while ((str = br.readLine()) != null) {//使用readLine方法，一次读一行
                    strings.append(str);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strings.toString();
        }
        return null;
    }


    public static boolean isExistsFile(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static void main(String[] args) {
//        File file = new File("C:\\tmp\\zipUtil1\\SAMPLE_89_CI_17304700_1392050584184.txt");
//        if (file.isFile() && file.exists()) {
//            copyFile(file, "C:\\tmp\\zipCopy");
//            List<String> arrayList = realFileTxt(file);
//            if (arrayList != null && arrayList.size() > 0) {
//                for (int i = 0; i < arrayList.size(); i++) {
//                    logger.info(arrayList.get(i));
//                }
//            }
//            deleteFile(file);
//        }

    }

}
