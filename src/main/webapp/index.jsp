<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.io.File"%>

<!DOCTYPE HTML>
<html>
<head>
<title>恒大Mock平台</title>


</head>
<body>
	<h1 style="color: red">欢迎访问恒大Mock平台</h1>
	<form action="/mockserver/UploadHandleServlet"
		enctype="multipart/form-data" method="post">
		<span>上传模板文件：</span> <input type="file" name="file" value="上传.json文件"
			style="color: green"><br /> <br /> <input type="submit"
			value="上传" style="color: green">
	</form>
	<br />
	<br />
	<br />
	<br />
	<form action="/mockserver/UploadJarHandleServlet"
		enctype="multipart/form-data" method="post">
		<span>上传自定义方法（jar）：</span> <input type="file" name="file"
			value="上传.jar文件" style="color: green"><br /> <br /> <input
			type="submit" value="上传" style="color: green">
	</form>
	<br /> 
	<table width="550px">
		<tr align="left">
			<th>模板文件列表</th>
		</tr>
		<%
		
		//String path = "D:/mockupload";
		String path = "E:/test/mockupload";
	//	List<String> list = new ArrayList<>();
		File f = new File(path);
		 if (!f.exists()) {
	            System.out.println(path + " not exists");
	            return;
	        }

	        File fa[] = f.listFiles();
	        for (int i = 0; i < fa.length; i++) {
	            File fs = fa[i];
	        	if(fs.isFile()){          
//	        }

//		for(int i=0;i<list.size();i++)
	//		{
		%>
		<tr align="left">
			<td><a target="_self" href="<%="/filelist/"+ fs.getName()%>"><%=fs.getName()%></a></td>
		</tr>
		<%
		}
}
		%>
	</table>
	<br />
	
	<table width="550px">
		<tr align="left">
			<th>jar文件列表</th>
		</tr>
		<%
		
		//String path1 = "D:/mockupload/jar";
		String path1 = "E:/test/mockupload/jar";
	//	List<String> list = new ArrayList<>();
		File f1 = new File(path1);
		 if (!f1.exists()) {
	            System.out.println(path1 + " not exists");
	            return;
	        }

	        File fa1[] = f1.listFiles();
	        for (int j = 0; j < fa1.length; j++) {
	            File fs1 = fa1[j];
	             
//	        }

//		for(int i=0;i<list.size();i++)
	//		{
		%>
		<tr align="left">
			<td><a href="#"><%=fs1.getName()%></a></td>
		</tr>
		<%

}
		%>
	</table>
		<br />	<br />
	<div style="font-size: 12px; color: blue">
		模板使用说明：<br> 文件格式必须为json格式，文件后缀可以是.json .txt 必须为文本格式。如下样例说明 <br>
	</div>
	<textarea rows="50" cols="150" style="font-size: 12px; color: blue">
匹配规则为根据请求路径匹配，一个路径下面可以有多个模板。如果需要根据请求路径和请求消息体匹配，请求消息匹配matchRequest字段
exec(***)表示执行某个方法。exec(randomNum)表示生成一个随机字符串  exec(com.mock.util.CommonUtil.getCurrentTime())表示
执行com.mock.util.CommonUtil.getCurrentTime()方法，方法支持自动以。使用自定义的方法需要先上传对应的jar报


{
	"/login": [    //访问路径
		{
			"head": {  //返回消息头
				"http": 200
			},
			"body": {  //返回消息体
				"success": "true"
			}
			
		},
		{
			"head": {
				"http": 302
			},
			"body": {
				"success": "exec(randomNum)"
			},
			"matchRequest": { //根据消息体匹配规则
				"name":"yu"
			}
		},
		{
			"head": {
				"http": 200
			},
			"body": {
				"success": "exec(randomStr)"
			},
			"matchRequest": {
				"name": "yu1"
			}
		},
		{
			"head": {
				"http": 200
			},
			"body": {
				"success": "exec(com.mock.util.CommonUtil.getCurrentTime())"
			},
			"matchRequest": {
				"name": "yu2"
			}
		}
	]

}
</textarea>

</body>
</html>