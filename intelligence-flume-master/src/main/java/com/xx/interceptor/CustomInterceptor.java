package com.xx.interceptor;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xx.interceptor.util.Constant;
import com.xx.interceptor.util.GZipUtil;
import org.apache.avro.data.Json;
import org.apache.commons.compress.utils.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
//import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
/**
 * 自定义拦截器，实现Interceptor接口，并且实现其抽象方法
 */

public class CustomInterceptor implements Interceptor {
    //打印日志，便于测试方法的执行顺序
    private static final Logger logger = LogManager.getLogger(CustomInterceptor.class);
    //自定义拦截器参数，用来接收自定义拦截器flume配置参数
    private static String param = "";
    /**
     * 拦截器构造方法，在自定义拦截器静态内部类的build方法中调用，用来创建自定义拦截器对象。
     */
    public CustomInterceptor() {
        System.out.printf("----------自定义拦截器构造方法执行 \n");
    }
    /**
     * 该方法用来初始化拦截器，在拦截器的构造方法执行之后执行，也就是创建完拦截器对象之后执行
     */
    @Override
    public void initialize() {
        System.out.printf("----------自定义拦截器的initialize方法执行 \n");
    }
    /**
     * 用来处理每一个event对象，该方法不会被系统自动调用，一般在 List<Event> intercept(List<Event> events) 方法内部调用。
     *
     * @param event
     * @return
     */
    @Override
    public Event intercept(Event event) {
        String eventBody = new String(event.getBody(), Charsets.UTF_8);
        if (eventBody == null) return null;
        BASE64Decoder decoder = new BASE64Decoder();
        String data = "解密失败！";
        String method = "";
        try {
//            System.out.println("源数据：" + eventBody+" \n");
            if (eventBody.contains(Constant.DEFAULT_REQUEST_POST_URL)){
                method = Constant.METHOD_POST;
            }else if (eventBody.contains(Constant.DEFAULT_REQUEST_GET_URL)){
                method = Constant.METHOD_GET;
            }
            if (eventBody.length() == 0){
                System.out.println("不是采集url地址=========");
                return null;
            }

            // 通过分隔符分割数据
            String[] split = eventBody.split(Constant.DEFAULT_INDEXS_SEPARATOR);
            String preData = "";
            boolean isData = false;
            boolean isGzip = false;
            boolean isIos = false;
            if (eventBody.contains("ios") || eventBody.contains("iOS") || eventBody.contains("IOS")){
                isIos = true;
            }
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
                        if (preData.contains("&"+Constant.PARAM_GZIP)){
                            String regexGip = "&"+Constant.PARAM_GZIP;
                            String tempData = preData;
                            preData = tempData.split(regexGip)[0];
                        }
                        isData = true;
                        break;
                    }
                }
            }
            if (!isData) return null;
//            System.out.println("解析后数据：" + preData+" \n");
            if (isIos){
                preData = java.net.URLDecoder.decode(preData, "utf-8");
//                System.out.println("IOS第一次转译后数据：" + preData+" \n");
                if (preData.contains("%")){
                    preData = java.net.URLDecoder.decode(preData, "utf-8");
                }
            }else {
                preData = java.net.URLDecoder.decode(preData, "utf-8");
            }
//            System.out.println("转译后数据：" + preData+" \n");
            if (isGzip){
                data = GZipUtil.uncompress(preData);
            }else {
                data=new String(decoder.decodeBuffer(preData), "UTF-8");
            }

//            System.out.println("解密：" + data);
            logger.info("log解密：" + data);
        } catch (Exception e) {
            System.out.println("解密异常  \n");
            //e.printStackTrace();
        }
//        if (data.startsWith("[")){
//
//        }
        if (data == null) return null;
        //测试版本  
//        if (!data.contains("3.6.2")) return null;

        event.setBody(data.getBytes());
        //System.out.printf("----------接收到的自定义拦截器参数值param值为：" + param +" \n");
        /*
        	这里编写event的处理代码
         */


        return event;
    }
    /**
     * 用来处理一批event对象集合，集合大小与flume启动配置有关，和transactionCapacity大小保持一致。一般直接调用 Event intercept(Event event) 处理每一个event数据。
     *
     * @param events
     * @return
     */
    @Override
    public List<Event> intercept(List<Event> events) {
        //System.out.printf("----------intercept(List<Event> events)方法执行  \n");
        /*
        	这里编写对于event对象集合的处理代码，一般都是遍历event的对象集合，对于每一个event对象，调用 Event intercept(Event event) 方法，然后根据返回值是否为null，
        	来将其添加到新的集合中。
         */
//        System.out.printf("----------events长度:"+events.size()+"  \n");
        List<Event> results = new ArrayList<>();
        Event event;
        for (Event e : events) {
            event = intercept(e);
            if (event != null) {
                String tmpEventBody = new String(event.getBody(), Charsets.UTF_8);
                if (tmpEventBody != null && tmpEventBody.startsWith("[")){
//                    System.out.printf("----------是array数组   \n");
                    JsonArray array = new JsonParser().parse(tmpEventBody).getAsJsonArray();
//                    System.out.printf("----------array 长度:"+array.size()+"  \n");
                    for (int i = 0; i < array.size(); i++){
                        JsonObject jsonObject = array.get(i).getAsJsonObject();
                        Event splitEvent = new SimpleEvent();
                        splitEvent.setBody(new Gson().toJson(jsonObject).getBytes());

                        results.add(splitEvent);
                    }
                }else{
                    results.add(event);
                }
            }
        }
//        System.out.printf("----------results长度:"+results.size()+"  \n");
        return results;
    }
    /**
     * 该方法主要用来销毁拦截器对象值执行，一般是一些释放资源的处理
     */
    @Override
    public void close() {
        System.out.printf("----------自定义拦截器close方法执行  \n");
    }
    /**
     * 通过该静态内部类来创建自定义对象供flume使用，实现Interceptor.Builder接口，并实现其抽象方法
     */
    public static class Builder implements Interceptor.Builder {

        /**
         * 该方法主要用来返回创建的自定义类拦截器对象
         *
         * @return
         */
        @Override
        public Interceptor build() {
            System.out.printf("----------build方法执行  \n");
            return new CustomInterceptor();
        }
        /**
         * 用来接收flume配置自定义拦截器参数
         *
         * @param context 通过该对象可以获取flume配置自定义拦截器的参数
         */
        @Override
        public void configure(Context context) {
            System.out.printf("----------configure方法执行  \n");
            /*
            通过调用context对象的getString方法来获取flume配置自定义拦截器的参数，方法参数要和自定义拦截器配置中的参数保持一致+
             */
            param = context.getString("param");
        }
    }
}
