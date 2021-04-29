import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.Properties

import com.alibaba.fastjson.JSON
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

//创建样例类
case class ceshi
(
  distinct_id :String,_track_id :String,time :String,$app_version:String,$lib :String,$lib_version :String,$manufacturer :String,$model :String,
  $os :String,$os_version :String,$app_id :String,$screen_width :String,$screen_height :String,$screen_name :String,$store_channel :String,
  $network_type :String,$event_duration :String,$title :String,$url :String,$referrer :String,param_id :String,param_name :String,
  param_url :String,param_type :String,param_order :String,params :String,screen_id :String,store_no :String,page_type :String,channel_type :String,
  $login_id :String,register_type :String,search_word :String,has_result :String,search_time :String,result_time :String,charge_type :String,
  course_frstcate :String,course_currentcate: String,course_id :String,course_name :String,period_id :String,period_name :String,
  type1 :String,login_id1 :String,anonymous_id :String,event :String,_flush_time :String,$lib_method:String,$lib_detail:String,
  index :String,name :String,id :String,url :String,category:String,keyword :String,option_type :String,course_jindu :String,from :String,
  course_id1:String,course_name1:String,price:String,date:String,shop_id:String,shop_name:String,attached_card_id1 :String,
  attached_card_name1 :String,context_id :String,type2:String,total_duration:String,screen_type:String,money:String,course_price:String,
  course_source:String,course_class_index:String,course_class_name:String,share_type:String,taocan_id:String,taocan_price:String,
  sum_money:String,sum_bei:String,card_id:String,taocan_type:String,yhq_denomination:String,order_id:String,product_id:String,
  payment:String,is_have_material:String,duration:String,page_id :String,element_id :String
)

object JieXiJson {
  def main(args: Array[String]): Unit = {
    //获取流处理
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //隐式转换
    import org.apache.flink.api.scala._
    //checkpoint配置
    env.enableCheckpointing(100);
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500);
    env.getCheckpointConfig.setCheckpointTimeout(60000);
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1);
    env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);;

    //设置并行度
    env.setParallelism(1)

    var params = ParameterTool.fromArgs(args);
    //设置EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //连接kafka
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "kafka path")//kafka的地址
    properties.setProperty("group.id", "test")
    properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    //获取kafka生产者消息
    val stream = env.addSource(new FlinkKafkaConsumer011[String]("gather-data", new SimpleStringSchema(), properties))

      stream.print()
	  
	 //进行数据Json解析ETL
    val DateStream = stream.map(line => {

      val distinct_id = JSON.parseObject(line).getString("distinct_id")
      val _track_id = JSON.parseObject(line).getString("_track_id")
      val time1 = JSON.parseObject(line).getString("time")
      //val time = new Date(time1)


            import java.text.SimpleDateFormat
            val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val time = format.format(time1.toLong)

      val lib = JSON.parseObject(line).getString("lib")
      val $app_version = JSON.parseObject(lib).getString("$app_version")
      val $lib = JSON.parseObject(lib).getString("$lib")
      val $lib_version = JSON.parseObject(lib).getString("$lib_version")
      val properties = JSON.parseObject(line).getString("properties")
      val $manufacturer = JSON.parseObject(properties).getString("$manufacturer")
      val $model = JSON.parseObject(properties).getString("$model")
      val $os = JSON.parseObject(properties).getString("$os")
      val $os_version = JSON.parseObject(properties).getString("$os_version")
      val $app_id = JSON.parseObject(properties).getString("$app_id")
      val $screen_width = JSON.parseObject(properties).getString("$screen_width")
      val $screen_height = JSON.parseObject(properties).getString("$screen_height")
      val $screen_name = JSON.parseObject(properties).getString("$screen_name")
      val $store_channel = JSON.parseObject(properties).getString("$store_channel")
      val $network_type = JSON.parseObject(properties).getString("$network_type")
      val $event_duration = JSON.parseObject(properties).getString("$event_duration")
      val $title = JSON.parseObject(properties).getString("$title")
      val $url = JSON.parseObject(properties).getString("$url")
      val $referrer = JSON.parseObject(properties).getString("$referrer")
      val param_id = JSON.parseObject(properties).getString("param_id")
      val param_name = JSON.parseObject(properties).getString("param_name")
      val param_url = JSON.parseObject(properties).getString("param_url")
      val param_type = JSON.parseObject(properties).getString("param_type")
      val param_order = JSON.parseObject(properties).getString("param_order")
      val screen_id = JSON.parseObject(properties).getString("screen_id")
      val store_no = JSON.parseObject(properties).getString("store_no")
      val page_type = JSON.parseObject(properties).getString("page_type")
      val channel_type = JSON.parseObject(properties).getString("channel_type")
      val $login_id = JSON.parseObject(properties).getString("$login_id")
      val register_type = JSON.parseObject(properties).getString("register_type")
      val search_word = JSON.parseObject(properties).getString("search_word")
      val has_result = JSON.parseObject(properties).getString("has_result")
      val search_time = JSON.parseObject(properties).getString("search_time")
      val result_time = JSON.parseObject(properties).getString("result_time")
      val charge_type = JSON.parseObject(properties).getString("charge_type")
      val course_frstcate = JSON.parseObject(properties).getString("course_frstcate")
      val course_currentcate = JSON.parseObject(properties).getString("course_currentcate")
      val course_id = JSON.parseObject(properties).getString("course_id")
      val course_name = JSON.parseObject(properties).getString("course_name")
      val period_id = JSON.parseObject(properties).getString("period_id")
      val period_name = JSON.parseObject(properties).getString("period_name")
      val type1 = JSON.parseObject(line).getString("type1")
      val login_id1 = JSON.parseObject(line).getString("login_id")
      val anonymous_id = JSON.parseObject(line).getString("anonymous_id")
      val event = JSON.parseObject(line).getString("event")
      val _flush_time = JSON.parseObject(line).getString("_flush_time")
      val $lib_method = JSON.parseObject(lib).getString("$lib_method")
      val $lib_detail = JSON.parseObject(lib).getString("$lib_detail")

      var params = "{}";
      if(JSON.parseObject(properties).getString("params")!=null){
        params=JSON.parseObject(properties).getString("params")
      }
      val index = JSON.parseObject(params).getString("index")
      val name = JSON.parseObject(params).getString("name")
      val id = JSON.parseObject(params).getString("id")
      val url = JSON.parseObject(params).getString("url")
      val category = JSON.parseObject(params).getString("category")
      val keyword = JSON.parseObject(params).getString("keyword")
      val option_type = JSON.parseObject(params).getString("option_type")
      val course_jindu = JSON.parseObject(params).getString("course_jindu")
      val from = JSON.parseObject(params).getString("from")
      val course_id1 = JSON.parseObject(params).getString("course_id")
      val course_name1 = JSON.parseObject(params).getString("course_name")
      val price = JSON.parseObject(params).getString("price")
      val date = JSON.parseObject(params).getString("date")
      val shop_id = JSON.parseObject(params).getString("shop_id")
      val shop_name = JSON.parseObject(params).getString("shop_name")
      val attached_card_id1 = JSON.parseObject(params).getString("attached_card_id1")
      val attached_card_name1 = JSON.parseObject(params).getString("attached_card_name")
      val context_id = JSON.parseObject(params).getString("context_id")
      val type2 = JSON.parseObject(params).getString("type")
      val total_duration = JSON.parseObject(params).getString("total_duration")
      val screen_type = JSON.parseObject(params).getString("screen_type")
      val money = JSON.parseObject(params).getString("money")
      val course_price = JSON.parseObject(params).getString("course_price")
      val course_source = JSON.parseObject(params).getString("course_source")
      val course_class_index = JSON.parseObject(params).getString("course_class_index")
      val course_class_name = JSON.parseObject(params).getString("course_class_name")
      val share_type = JSON.parseObject(params).getString("share_type")
      val taocan_id = JSON.parseObject(params).getString("taocan_id")
      val taocan_price = JSON.parseObject(params).getString("taocan_price")
      val sum_money = JSON.parseObject(params).getString("sum_money")
      val sum_bei = JSON.parseObject(params).getString("sum_bei")
      val card_id = JSON.parseObject(params).getString("card_id")
      val taocan_type = JSON.parseObject(params).getString("taocan_type")
      val yhq_denomination = JSON.parseObject(params).getString("yhq_denomination")
      val order_id = JSON.parseObject(params).getString("order_id")
      val product_id = JSON.parseObject(params).getString("product_id")
      val payment = JSON.parseObject(params).getString("payment")
      val is_have_material = JSON.parseObject(params).getString("is_have_material")
      val duration = JSON.parseObject(properties).getString("duration")
      val page_id = JSON.parseObject(properties).getString("page_id")
      val element_id = JSON.parseObject(properties).getString("element_id")



      //解析数据放入样例类
      ceshi(
        distinct_id,_track_id,time.toString,$app_version,$lib,$lib_version,$manufacturer,$model,$os,$os_version,$app_id,
        $screen_width,$screen_height,$screen_name,$store_channel,$network_type,$event_duration,$title,$url,$referrer,
        param_id,param_name,param_url,param_type,param_order,params,screen_id,store_no,page_type,channel_type,$login_id,
        register_type,search_word,has_result,search_time,result_time,charge_type,course_frstcate,course_currentcate,
        course_id,course_name,period_id,period_name,type1,login_id1,anonymous_id,event,_flush_time,$lib_method,$lib_detail,
        index,name,id,url,category,keyword,option_type,course_jindu,from,course_id1,course_name1,price,date,shop_id,shop_name,
        attached_card_id1,attached_card_name1,context_id,type2,total_duration,screen_type,money,course_price,course_source,
        course_class_index,course_class_name,share_type,taocan_id,taocan_price,sum_money,sum_bei,card_id,taocan_type,
        yhq_denomination,order_id,product_id,payment,is_have_material,duration,page_id,element_id
      )

    })



    //数据sink到mysql
    //DateStream.addSink(new tomysql())

    //数据sink到hbase
    //DateStream.addSink(new HbaseSink())

    //写入hdfs
    //stream.writeAsText("hdfs://hadoop102:9000/output/result.txt",WriteMode.OVERWRITE)

    env.execute()

  }
}

 class tomysql() extends RichSinkFunction[ceshi]{
  //定义连接
  var conn: Connection = _

  //定义预编译器——更新
  var updateStmt: PreparedStatement = _

  //定义预编译器——插入
  var insertStmt: PreparedStatement = _

  override def open(parameters: Configuration): Unit = {

    conn = DriverManager.getConnection("jdbc:mysql://?","","")

    //插入预编译器
    insertStmt = conn.prepareStatement("INSERT INTO maidian (distinct_id,track_id,time1,app_version,lib,lib_version,manufacturer," +
      "model,os,os_version,app_id,screen_width,screen_height,screen_name,store_channel,network_type,event_duration," +
      "title,url,referrer,param_id,param_name,param_url,param_type,param_order,params,screen_id,store_no," +
      "page_type,channel_type,login_id,register_type,search_word,has_result,search_time,result_time," +
      "charge_type,course_frstcate,course_currentcate,course_id,course_name,period_id,period_name,type1," +
      "login_id1,anonymous_id,event,flush_time,lib_method,lib_detail,index1,name,id,url1,category,keyword," +
      "option_type,course_jindu,from1,course_id1,course_name1,price,date,shop_id,shop_name,attached_card_id1," +
      "attached_card_name1,context_id,type2,total_duration,screen_type,money,course_price,course_source,course_class_index," +
      "course_class_name,share_type,taocan_id,taocan_price,sum_money,sum_bei,card_id,taocan_type,yhq_denomination,order_id," +
      "product_id,payment,is_have_material,duration) VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
      "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
      "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
      "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
      "?,?,?,?,?,?,?,?,?)")


  }

  override def invoke(value: ceshi, context: SinkFunction.Context[_]): Unit = {

    insertStmt.setString(1,value.distinct_id)
    insertStmt.setString(2,value._track_id)
    insertStmt.setString(3,value.time)
    insertStmt.setString(4,value.$app_version)
    insertStmt.setString(5,value.$lib)
    insertStmt.setString(6,value.$lib_version)
    insertStmt.setString(7,value.$manufacturer)
    insertStmt.setString(8,value.$model)
    insertStmt.setString(9,value.$os)
    insertStmt.setString(10,value.$os_version)
    insertStmt.setString(11,value.$app_id)
    insertStmt.setString(12,value.$screen_width)
    insertStmt.setString(13,value.$screen_height)
    insertStmt.setString(14,value.$screen_name)
    insertStmt.setString(15,value.$store_channel)
    insertStmt.setString(16,value.$network_type)
    insertStmt.setString(17,value.$event_duration)
    insertStmt.setString(18,value.$title)
    insertStmt.setString(19,value.$url)
    insertStmt.setString(20,value.$referrer)
    insertStmt.setString(21,value.param_id)
    insertStmt.setString(22,value.param_name)
    insertStmt.setString(23,value.param_url)
    insertStmt.setString(24,value.param_type)
    insertStmt.setString(25,value.param_order)
    insertStmt.setString(26,"value.params")
    insertStmt.setString(27,value.screen_id)
    insertStmt.setString(28,value.store_no)
    insertStmt.setString(29,value.page_type)
    insertStmt.setString(30,value.channel_type)
    insertStmt.setString(31,value.$login_id)
    insertStmt.setString(32,value.register_type)
    insertStmt.setString(33,value.search_word)
    insertStmt.setString(34,value.has_result)
    insertStmt.setString(35,value.search_time)
    insertStmt.setString(36,value.result_time)
    insertStmt.setString(37,value.charge_type)
    insertStmt.setString(38,value.course_frstcate)
    insertStmt.setString(39,value.course_currentcate)
    insertStmt.setString(40,value.course_id)
    insertStmt.setString(41,value.course_name)
    insertStmt.setString(42,value.period_id)
    insertStmt.setString(43,value.period_name)
    insertStmt.setString(44,value.type1)
    insertStmt.setString(45,value.login_id1)
    insertStmt.setString(46,value.anonymous_id)
    insertStmt.setString(47,value.event)
    insertStmt.setString(48,value._flush_time)
    insertStmt.setString(49,value.$lib_method)
    insertStmt.setString(50,value.$lib_detail)
    insertStmt.setString(51,value.index)
    insertStmt.setString(52,value.name)
    insertStmt.setString(53,value.id)
    insertStmt.setString(54,value.url)
    insertStmt.setString(55,value.category)
    insertStmt.setString(56,value.keyword)
    insertStmt.setString(57,value.option_type)
    insertStmt.setString(58,value.course_jindu)
    insertStmt.setString(59,value.from)
    insertStmt.setString(60,value.course_id1)
    insertStmt.setString(61,value.course_name1)
    insertStmt.setString(62,value.price)
    insertStmt.setString(63,value.date)
    insertStmt.setString(64,value.shop_id)
    insertStmt.setString(65,value.shop_name)
    insertStmt.setString(66,value.attached_card_id1)
    insertStmt.setString(67,value.attached_card_name1)
    insertStmt.setString(68,value.context_id)
    insertStmt.setString(69,value.type2)
    insertStmt.setString(70,value.total_duration)
    insertStmt.setString(71,value.screen_type)
    insertStmt.setString(72,value.money)
    insertStmt.setString(73,value.course_price)
    insertStmt.setString(74,value.course_source)
    insertStmt.setString(75,value.course_class_index)
    insertStmt.setString(76,value.course_class_name)
    insertStmt.setString(77,value.share_type)
    insertStmt.setString(78,value.taocan_id)
    insertStmt.setString(79,value.taocan_price)
    insertStmt.setString(80,value.sum_money)
    insertStmt.setString(81,value.sum_bei)
    insertStmt.setString(82,value.card_id)
    insertStmt.setString(83,value.taocan_type)
    insertStmt.setString(84,value.yhq_denomination)
    insertStmt.setString(85,value.order_id)
    insertStmt.setString(86,value.product_id)
    insertStmt.setString(87,value.payment)
    insertStmt.setString(88,value.is_have_material)
    insertStmt.setString(89,value.duration)



    insertStmt.execute()
  }
  override def close(): Unit = {
    insertStmt.close()
    conn.close()
  }
}

class HbaseSink() extends RichSinkFunction[ceshi]{
  var conn:Connection = _

  override def open(parameters: Configuration): Unit = {
    //配置
    val conf = HBaseConfiguration.create()
    conf.set(HConstants.ZOOKEEPER_QUORUM,"?")
    conf.set(HConstants.ZOOKEEPER_CLIENT_PORT,"2181")
    //实例化
    val conn = ConnectionFactory.createConnection(conf)

  }

  override def invoke(value: ceshi, context: SinkFunction.Context[_]): Unit = {
    val table = conn.getTable(TableName.valueOf("maidian"))

    val put = new Put(Bytes.toBytes(1001))

    put.add(Bytes.toBytes("f1"),Bytes.toBytes("distinct_id"),Bytes.toBytes(value.distinct_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("_track_id"),Bytes.toBytes(value._track_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("time"),Bytes.toBytes(value.time.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("app_version"),Bytes.toBytes(value.$app_version.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("lib"),Bytes.toBytes(value.$lib.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("lib_version"),Bytes.toBytes(value.$lib_version.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("manufacturer"),Bytes.toBytes(value.$manufacturer.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("model"),Bytes.toBytes(value.$model.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("os"),Bytes.toBytes(value.$os.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("os_version"),Bytes.toBytes(value.$os_version.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("app_id"),Bytes.toBytes(value.$app_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("screen_width"),Bytes.toBytes(value.$screen_width.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("screen_height"),Bytes.toBytes(value.$screen_height.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("screen_name"),Bytes.toBytes(value.$screen_name.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("store_channel"),Bytes.toBytes(value.$store_channel.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("network_type"),Bytes.toBytes(value.$network_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("event_duration"),Bytes.toBytes(value.$event_duration.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("title"),Bytes.toBytes(value.$title.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("url"),Bytes.toBytes(value.$url.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("referrer"),Bytes.toBytes(value.$referrer.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("param_id"),Bytes.toBytes(value.param_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("param_name"),Bytes.toBytes(value.param_name.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("param_url"),Bytes.toBytes(value.param_url.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("param_type"),Bytes.toBytes(value.$model.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("param_order"),Bytes.toBytes(value.param_order.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("params"),Bytes.toBytes(value.params.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("screen_id"),Bytes.toBytes(value.screen_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("store_no"),Bytes.toBytes(value.store_no.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("page_type"),Bytes.toBytes(value.page_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("channel_type"),Bytes.toBytes(value.channel_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("login_id"),Bytes.toBytes(value.$login_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("register_type"),Bytes.toBytes(value.register_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("search_word"),Bytes.toBytes(value.search_word.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("has_result"),Bytes.toBytes(value.has_result.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("search_time"),Bytes.toBytes(value.search_time.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("result_time"),Bytes.toBytes(value.result_time.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("charge_type"),Bytes.toBytes(value.charge_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_frstcate"),Bytes.toBytes(value.course_frstcate.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_currentcate"),Bytes.toBytes(value.course_currentcate.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_id"),Bytes.toBytes(value.course_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_name"),Bytes.toBytes(value.course_name.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("period_id"),Bytes.toBytes(value.period_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("period_name"),Bytes.toBytes(value.period_name.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("type1"),Bytes.toBytes(value.type1.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("login_id"),Bytes.toBytes(value.$login_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("login_id1"),Bytes.toBytes(value.login_id1.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("anonymous_id"),Bytes.toBytes(value.anonymous_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("event"),Bytes.toBytes(value.event.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("_flush_time"),Bytes.toBytes(value._flush_time.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("lib_method"),Bytes.toBytes(value.$lib_method.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("lib_detail"),Bytes.toBytes(value.$lib_detail.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("index"),Bytes.toBytes(value.index.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("name"),Bytes.toBytes(value.name.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("id"),Bytes.toBytes(value.id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("url"),Bytes.toBytes(value.url.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("category"),Bytes.toBytes(value.category.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("keyword"),Bytes.toBytes(value.keyword.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("charge_type"),Bytes.toBytes(value.charge_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("option_type"),Bytes.toBytes(value.option_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_jindu"),Bytes.toBytes(value.course_jindu.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("from"),Bytes.toBytes(value.from.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_id1"),Bytes.toBytes(value.course_id1.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_name1"),Bytes.toBytes(value.course_name1.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("price"),Bytes.toBytes(value.price.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("date"),Bytes.toBytes(value.date.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("shop_id"),Bytes.toBytes(value.shop_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("shop_name"),Bytes.toBytes(value.shop_name.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("attached_card_id1"),Bytes.toBytes(value.attached_card_id1.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("attached_card_name1"),Bytes.toBytes(value.attached_card_name1.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("context_id"),Bytes.toBytes(value.context_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("type2"),Bytes.toBytes(value.type2.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("total_duration"),Bytes.toBytes(value.total_duration.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("screen_type"),Bytes.toBytes(value.screen_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("money"),Bytes.toBytes(value.money.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_price"),Bytes.toBytes(value.course_price.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_source"),Bytes.toBytes(value.course_source.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_class_index"),Bytes.toBytes(value.course_class_index.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("course_class_name"),Bytes.toBytes(value.course_class_name.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("charge_type"),Bytes.toBytes(value.charge_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("share_type"),Bytes.toBytes(value.share_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("taocan_id"),Bytes.toBytes(value.taocan_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("taocan_price"),Bytes.toBytes(value.taocan_price.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("sum_money"),Bytes.toBytes(value.sum_money.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("sum_bei"),Bytes.toBytes(value.sum_bei.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("card_id"),Bytes.toBytes(value.card_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("taocan_type"),Bytes.toBytes(value.taocan_type.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("yhq_denomination"),Bytes.toBytes(value.yhq_denomination.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("order_id"),Bytes.toBytes(value.order_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("product_id"),Bytes.toBytes(value.product_id.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("payment"),Bytes.toBytes(value.payment.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("is_have_material"),Bytes.toBytes(value.is_have_material.toString))
    put.add(Bytes.toBytes("f1"),Bytes.toBytes("duration"),Bytes.toBytes(value.duration.toString))

    table.put(put)
  }

  override def close(): Unit = {
    conn.close()
  }
}
