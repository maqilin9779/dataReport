1.本项目主要功能：
	集客数据直采（集团投诉、集中化故障工单）、
	com.boco.data.commonfault包
	com.boco.data.complaint包
	
	家宽投诉数据采集、
	com.boco.data.jkcomplaint包
	
	电子运维报表管理模块短信提醒（只实现了针对月报表的短信提醒，其余周、季度、半年、年待实现）
	com.boco.data.reportmanagement包
	
	com.boco.data.freamework包下是一些系统共用的Utill类方法
	com.boco.data.base.dao包下是spring封装的jdbc访问数据库的一些方法
	业务功能需求的配置文件统一放在src/main/resource/config下
	
 后期若有类似需求也可在该项目实现。
	该项目总体架构ssm，其中
	集客数据直采（集团投诉、集中化故障工单）、家宽投诉数据采集 功能采用spring封装的jdbc访问数据库
	电子运维报表管理模块短信提醒采用mybatis访问数据库。
	
2.修改数据采集的时间及上传ftp时间，修改applicationContext-quartz.xml中配置的
	<!-- 每天01:01分执行 -->
	<value>0 01 01 * * ? *</value>
3.集客数据直采（集团投诉、集中化故障工单）、家宽投诉数据采集功能类似
      方法入口：在applicationContext-quartz.xml中
  	<!-- 集团投诉执行的类 -->
    <bean id="groupComplaintDataReport"
    	class="com.boco.data.complaint.quartz.GroupComplaintDataReport"></bean>
    <!-- 故障工单执行的类 -->
    <bean id="commonFaultDataReport"
    	class="com.boco.data.commonfault.quartz.CommonFaultDataReport"></bean>
     <!-- 家宽投诉执行的类 -->
    <bean id="jkComplaintDataRepor"
    class="com.boco.data.jkcomplaint.quartz.JkComplaintDataReport"></bean>
    <!-- 报表短信执行的类 -->
    <bean id="smsDataRepor"
    class="com.boco.data.reportmanagement.quartz.ReportSmsDayTask"></bean>

4.该项目部署在10.233.13.21 服务器 /opt/apache-tomcat-6.0.43/webapps下
      集客数据直采生成的数据文件在/opt/data/jt_groupcomplaint_centralcommonfault
      家宽投诉生成的文件在/opt/data/jt_jiakuang
      需要定时手动清理这两个路径下的文件，修改生成的文件路径在下面三个配置文件修改。
      commonFault-datareport.xml
      groupComplaint-datareport.xml
 	  jkcomplaint-sql.xml
 	  
5.关于服务器/opt/apache-tomcat-6.0.43/logs下日志文件太多，可以在停止tomcat服务后直接全部删除该目录下的文件

6.关于在/opt/apache-tomcat-6.0.43/bin 下使用命令./shutdown.sh停止tomcat服务停不了
     用ps -ef|grep tomcat 查看服务/opt/apache-tomcat-6.0.43服务依然存在
     可以用命令kill -9 进程号，强制停止tomcat服务。
     进程号是上面ps -ef|grep tomcat命令查出来的。
	
 
	