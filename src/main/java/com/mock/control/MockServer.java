package com.mock.control;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mock.util.CommonUtil;
import com.mock.util.FileUtils;

public class MockServer extends HttpServlet {

	public static Map<String, String> template;
	// public static String path = System.getProperty("user.dir") +
	// "\\upload\\template.json";

	public static String path = "E:/test/mockupload";

	public MockServer() {

		/*
		 * 读取模板，将模板数据加载进map,加载完的模板格式为： {
		 * "uri1":[{"head":{"http":200},"body":{"success":"true"},"match":{}},//返回固定响应
		 * {"head":{"http":302},"body":{"success":"exec(randomStr5)"},"match":{}},//
		 * 返回随机字符串
		 * {"head":{"http":200},"body":{"success":"exec(randomStr5)"},"match":{"name":
		 * "yu"}},//根据请求消息匹配返回
		 * {"head":{"http":200},"body":{"success":"exec(com.abc.funcation(ss,dd))"},
		 * "match":{"name":"yu"}} ],
		 * "uri2":[{"head":{"http":200},"body":{"success":"true"},"match":{}},//返回固定响应
		 * {"head":{"http":302},"body":{"success":"exec(randomStr5)"},"match":{}},//
		 * 返回随机字符串
		 * {"head":{"http":200},"body":{"success":"exec(randomStr5)"},"match":{"name":
		 * "yu"}},//根据请求消息匹配返回
		 * {"head":{"http":200},"body":{"success":"exec(com.abc.funcation(ss,dd))"},
		 * "match":{"name":"yu"}} ]
		 * 
		 * }
		 */

		// 1、解析模板文件
		// 2、转换成对应的map
		// this.template = getAreas();
	}

	public Map<String, String> getAreas() {
		// String path =
		// getClass().getClassLoader().getResource("template.json").toString();
		// path = path.replace("\\", "/");
		// if (path.contains(":")) {
		// path = path.replace("file:/","");
		// }
		// String path = System.getProperty("user.dir") + "\\template.json";
		System.out.println(path);
		JSONObject jsonObject = null;
		Map<String, String> params = new HashMap<>();
		try {
			String input = FileUtils.readFileToString(path, "UTF-8");
			// jsonObject = JSONObject.parseObject(input);
			// System.out.println(jsonObject.toString());
			params = JSONObject.parseObject(input, new TypeReference<Map<String, String>>() {
			});
			System.out.println(params.toString());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return params;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

		this.template = getAreas();
		System.out.println("开始get请求.......");
		// 1、获取请求路径
		String uri = req.getRequestURI().substring(11);
		System.out.println("uri==========" + uri);
		// 2、获取请求body
		ServletInputStream ris;
		try {
			JSONObject jsStr0 = new JSONObject();
			Enumeration<String> e = req.getParameterNames();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				jsStr0.put(key, req.getParameter(key));
			}

			// ris = req.getInputStream();
			// StringBuilder content = new StringBuilder();
			// byte[] b = new byte[1024];
			// int lens = -1;
			// while ((lens = ris.read(b)) > 0) {
			// content.append(new String(b, 0, lens));
			// }
			// String strcont = content.toString();
			// System.out.println("strcont======" + strcont);
			// JSONObject jsStr0 = JSONObject.parseObject(strcont);
			System.out.println("请求消息为：" + jsStr0.toJSONString());

			// 3、根据路径匹配对应的路径
			String remessage = match(uri, jsStr0);
			// 4、获取响应消息头以及body
			setRespHead(remessage, resp);
			String messagebody = JSONObject.parseObject(remessage).getString("body");
			// 开始组装响应消息===================
			// resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json; charset=utf-8");
			// 根据不同的接口码，返回不同的对象
			PrintWriter out = resp.getWriter();
			out.append(messagebody);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String match(String uri, JSONObject json) {

		String resStr = "";
		// 匹配路径
		if (template.containsKey(uri)) {
			String res = template.get(uri);
			// 字符串转换成jsonarray
			JSONArray jarr = JSONArray.parseArray(res);
			// 遍历jsonarray,获取返回消息内容
			for (int i = 0; i < jarr.size(); i++) {
				JSONObject js = JSONObject.parseObject(jarr.get(i).toString());
				// 匹配是否需要根据请求消息体匹配
				if (isContenReqBody(json, js)) {
					resStr = js.toJSONString();
					continue;
				}

			}

			// 判断是否需要执行随机数或自动以函数
			if (!"".equals(resStr)) {
				String body = JSONObject.parseObject(resStr).getString("body");
				// Pattern pattern =
				// Pattern.compile("exec\\((\\w*(\\.?\\w*)*(\\()?(\\w*,?)?(\\w*)?(\\))?)\\)");
				Pattern pattern = Pattern
						.compile("exec\\((\\w*(\\.?\\w*)*(\\()?(\\w*,?)?(\\w*)?(\\)?(\\|\\w*.jar)?)?)\\)");
				Matcher m = pattern.matcher(resStr);
				int i = 0;
				while (m.find()) {
					i++;
					String funname = m.group();
					String dd = execStr(funname);
					resStr = resStr.replace(
							resStr.substring(resStr.indexOf("exec(", i), resStr.indexOf("exec(", i) + funname.length()),
							dd);
				}

			}

		} else {
			return "模板不存在，请检查模板路径是否配置正确！";
		}

		return resStr;
	}

	/*
	 * 执行exec里面的方法
	 */
	public String execStr(String funName) {
		// Pattern pattern =
		// Pattern.compile("\\((\\w*(\\.?\\w*)*(\\()?(\\w*,?)?(\\w*)?(\\))?)\\)");
		Pattern pattern = Pattern.compile("\\((\\w*(\\.?\\w*)*(\\()?(\\w*,?)?(\\w*)?(\\)?(\\|\\w*.jar)?)?)\\)");
		String funcationname = null;
		Matcher m1 = pattern.matcher(funName);
		while (m1.find()) {
			funcationname = m1.group(1);
		}

		String execre = "";
		switch (funcationname) {

		case "randomStr":
			execre = CommonUtil.getRandomStr();
			break;
		case "randomNum":
			execre = CommonUtil.getRandomNum().toString();
			break;
		default:
			execre = execFun(funcationname);
			break;
		}
		return execre;

	}

	public String execFun(String path) {
		String result = "";
		if (path.contains(".jar")) {
			return execOutJarFun(path);
		}
		String packagename = path.substring(0, path.lastIndexOf("."));
		String functionName = path.substring(path.lastIndexOf(".") + 1, path.indexOf("("));
		String[] params = path.substring(path.indexOf("(") + 1, path.indexOf(")")).split(",");

		try {
			Object server = Class.forName(packagename).newInstance();
			Class[] getParameterTypes = null;
			if (params.length >= 1 && params[params.length - 1] != null && !"".equals(params[params.length - 1])) {
				int paramscount = params.length;
				// 赋值数组，定义类型
				getParameterTypes = new Class[paramscount];
				for (int i = 0; i < paramscount; i++) {
					getParameterTypes[i] = String.class;
				}
			}
			// Method method = getMethod(server.getClass().getMethods(),
			// functionName,getParameterTypes);
			Method method = server.getClass().getMethod(functionName, getParameterTypes);
			if (method == null) {
				System.out.println("调用方法失败，方法不存在或者参数不正确...请检查！");
			}
			Object str = getParameterTypes == null ? method.invoke(server, null) : method.invoke(server, params);
			// Object str = method.invoke(server, params);
			if (str == null) {
				result = "调用异常，返回结果是null";
			} else {
				result = str.toString();
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public String execOutJarFun(String path) {
		String result = "";
		// jar
		String jarName = path.substring(path.indexOf("|")+1);
		String newpath = path.substring(0, path.indexOf("|"));
		String packagename = newpath.substring(0, newpath.lastIndexOf("."));
		String functionName = newpath.substring(newpath.lastIndexOf(".") + 1, newpath.indexOf("("));
		String[] params = newpath.substring(newpath.indexOf("(") + 1, newpath.indexOf(")")).split(",");
		try {
			File jarFile = new File("E:/test/mockupload/jar/" + jarName);
			URL url = jarFile.toURI().toURL();
			ClassLoader classloader = new URLClassLoader(new URL[] { url });
			Class[] getParameterTypes = null;
			if (params.length >= 1 && params[params.length - 1] != null && !"".equals(params[params.length - 1])) {
				int paramscount = params.length;
				// 赋值数组，定义类型
				getParameterTypes = new Class[paramscount];
				for (int i = 0; i < paramscount; i++) {
					getParameterTypes[i] = String.class;
				}
			}
			Object server = classloader.loadClass(packagename).newInstance();
			Method method = server.getClass().getMethod(functionName, getParameterTypes);
			Object str = getParameterTypes == null ? method.invoke(server, null) : method.invoke(server, params);
			// Object str = method.invoke(server, params);
			if (str == null) {
				result = "调用异常，返回结果是null";
			} else {
				result = str.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isContenReqBody(JSONObject reqbody, JSONObject tempbody) {
		boolean flag = false;
		if (tempbody.containsKey("matchRequest") && !"".equals(tempbody.getString("matchRequest"))) {
			JSONObject requestmatch = JSONObject.parseObject(tempbody.getString("matchRequest"));
			// Iterator<String> mm = requestmatch.keySet().iterator();

			for (Entry<String, Object> entry : requestmatch.entrySet()) {
				if (reqbody.containsKey(entry.getKey()) && reqbody.getString(entry.getKey()).equals(entry.getValue())) {
					flag = true;
				}
			}

		} else {
			flag = true;
		}
		return flag;

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

		this.template = getAreas();

		System.out.println("开始post请求.......");
		// 1、获取请求路径
		String uri = req.getRequestURI().substring(11);
		// 2、获取请求body
		ServletInputStream ris;
		try {
			ris = req.getInputStream();
			StringBuilder content = new StringBuilder();
			byte[] b = new byte[1024];
			int lens = -1;
			while ((lens = ris.read(b)) > 0) {
				content.append(new String(b, 0, lens));
			}
			String strcont = content.toString();
			JSONObject jsStr0 = JSONObject.parseObject(strcont);
			System.out.println("请求消息为：" + jsStr0.toJSONString());

			// 3、根据路径匹配对应的路径
			String remessage = match(uri, jsStr0);
			// 4、获取响应消息头以及body
			setRespHead(remessage, resp);
			String messagebody = JSONObject.parseObject(remessage).getString("body");
			// 开始组装响应消息===================
			// resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json; charset=utf-8");
			// 根据不同的接口码，返回不同的对象
			PrintWriter out = resp.getWriter();
			out.append(messagebody);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 设置响应消息头
	public void setRespHead(String message, HttpServletResponse resp) {

		try {
			JSONObject head = JSONObject.parseObject(message);
			if (head.containsKey("head") && "" != head.getString("head")) {
				Map<String, String> params = JSONObject.parseObject(head.getString("head"),
						new TypeReference<Map<String, String>>() {
						});
				for (Entry<String, String> entry : params.entrySet()) {
					if ("http".equals(entry.getKey())) {
						resp.setStatus(Integer.parseInt(entry.getValue()));
					}
					resp.addHeader(entry.getKey(), entry.getValue());
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// MockServer mock = new MockServer();
		// String input = mock.getAreas().toString();

		// String resStr =
		// "body\"exec(randomStr):{\"sucexec(com.abc.funcation())cess\":\"exec(com.abc.funcation(ss,dd))\"}}";
		// Pattern pattern =
		// Pattern.compile("exec\\((\\w*(\\.?\\w*)*(\\()?(\\w*,?)?(\\w*)?(\\))?)\\)");
		//
		// Matcher m = pattern.matcher(resStr);
		// int i=0;
		// while (m.find()) {
		// i++;
		// System.out.println(i);
		// String funname = m.group();
		// System.out.println(funname);
		// resStr = resStr.replace(resStr.substring(resStr.indexOf("exec(", i),
		// resStr.indexOf("exec(", i)+funname.length()), "###");
		// System.out.println(resStr);
		// }
		//
		// Pattern pattern1 =
		// Pattern.compile("\\((\\w*(\\.?\\w*)*(\\()?(\\w*,?)?(\\w*)?(\\))?)\\)");
		// String resStr1 = "exec(com.abc.funcation(ss,dd))";
		// Matcher m1 = pattern1.matcher(resStr1);
		// while (m1.find()) {
		//
		// String funname1 = m1.group(1);
		// System.out.println(funname1);
		//
		// }
		//

		String path = "com.lucky.Util.CommonUtil.getReturn(hellomock)|LuckyFrame.jar";
		// 调用非静态方法用到
//		String packagename = result.substring(0, result.lastIndexOf("."));
//		String functionName = result.substring(result.lastIndexOf(".") + 1, result.indexOf("("));
//		String[] params = result.substring(result.indexOf("(") + 1, result.indexOf(")")).split(",");
//		
		String jarName = path.substring(path.indexOf("|"));
		System.out.println(jarName);
		String newpath = path.substring(0, path.indexOf("|"));
		System.out.println(newpath);
		String packagename = newpath.substring(0, newpath.lastIndexOf("."));
		String functionName = newpath.substring(newpath.lastIndexOf(".") + 1, newpath.indexOf("("));
		String[] params = newpath.substring(newpath.indexOf("(") + 1, newpath.indexOf(")")).split(",");
		System.out.println(packagename);
		System.out.println(functionName);
		System.out.println(params[0]);

		String fff = "/mockserver/login";
		System.out.println(fff.substring(11));


	}

}
