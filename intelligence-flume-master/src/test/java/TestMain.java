import com.xx.interceptor.util.Constant;
import com.xx.interceptor.util.FileUtil;
import com.xx.interceptor.util.GZipUtil;
import sun.misc.BASE64Decoder;

import java.io.File;

public class TestMain {
    public final static String POSTCODE_PATH = "C:\\tmp\\xx\\read.txt";

    public static void main(String[] args) {
        File file = new File(POSTCODE_PATH);
        String strTxt = FileUtil.realTxt(file);
        BASE64Decoder decoder = new BASE64Decoder();
        String data = "解密失败！";
        String method = "";

        try {
            System.out.println("源数据：" + strTxt+" \n");
            if (strTxt.contains(Constant.DEFAULT_REQUEST_POST_URL)){
                method = Constant.METHOD_POST;
            }else if (strTxt.contains(Constant.DEFAULT_REQUEST_GET_URL)){
                method = Constant.METHOD_GET;
            }
            if (strTxt.length() == 0){
                System.out.println("不是采集url地址=========");
                return;
            }

            // 通过分隔符分割数据
            String[] split = strTxt.split(Constant.DEFAULT_INDEXS_SEPARATOR);
            String preData = "";
            boolean isData = false;
            boolean isGzip = false;
            for (int i = 0; i < split.length; i++) {
                //如果是GET方式，参数是在request节点中 url结尾中有"  HTTP/1.1"，
                if(Constant.METHOD_GET.equals(method)){
                    if (split[i].contains(Constant.PARAM_DATA)  || split[i].contains(Constant.PARAM_DATA_LIST)){
                        String regexStr = split[i].contains(Constant.PARAM_DATA)?Constant.PARAM_DATA:Constant.PARAM_DATA_LIST;
                        preData = split[i].split(regexStr)[1];
                        //是否zgip压缩
                        if (preData.contains(Constant.PARAM_GZIP)){
                            isGzip = true;
                        }
                        preData = preData.split(Constant.DEFAULT_FIELD_SEPARATOR)[0];
                        isData = true;
                        break;
                    }
                }
                //POST方式，参数在dm节点中
                if (Constant.METHOD_POST.equals(method)){
                    if (split[i].contains(Constant.PARAM_DATA)  || split[i].contains(Constant.PARAM_DATA_LIST)){
                        //是否zgip压缩
                        if (split[i].contains(Constant.PARAM_GZIP)){
                            isGzip = true;
                        }
                        String regexStr = split[i].contains(Constant.PARAM_DATA)?Constant.PARAM_DATA:Constant.PARAM_DATA_LIST;
                        preData = split[i].split(regexStr)[1];
                        isData = true;
                        break;
                    }
                }
            }
            if (!isData) return ;

            System.out.println("解析后数据：" + preData+" \n");
            preData = java.net.URLDecoder.decode(preData, "utf-8");
            System.out.println("转译后数据：" + preData+" \n");
            String key = "";// 自定义密钥
            if (isGzip){
                data = GZipUtil.uncompress(preData);
            }else {
                data=new String(decoder.decodeBuffer(preData), "UTF-8");
            }

            System.out.println("解密：" + data);
        } catch (Exception e) {
            System.out.println("解密异常  \n");
            //e.printStackTrace();
        }
    }
}
