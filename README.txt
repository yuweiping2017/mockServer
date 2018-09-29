# mockServer


1、框架介绍
为自动化测试提供一个mockserver平台。支持json格式的接口。用户只需要上传对应的模板，mock服务自动生成，根据模板配置返回对应的响应请求。
模板里面参数支持随机数以及自定义函数，自定义函数需要手动传入jar包。因项目急需使用，目前框架刚完成开发，存在很多bug。代码仅仅为了实现功能很多地方配置都是写死的需要极大的优化。后续再进行优化

2、模板语法
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
				"success": "exec(com.lucky.Util.CommonUtil.getReturn(hellomock)|***.jar)"//支持执行导入****.jar中的方法
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
				"success": "exec(com.mock.util.CommonUtil.getCurrentTime())"//可以执行系统的方法
			},
			"matchRequest": {
				"name": "yu2"
			}
		}
	]

}
3、匹配规则
匹配规则为根据请求路径匹配，一个路径下面可以有多个模板。如果需要根据请求路径和请求消息体匹配，请求体消息匹配matchRequest字段
exec(***)表示执行某个方法。exec(randomNum)表示生成一个随机字符串  exec(com.mock.util.CommonUtil.getCurrentTime())表示
执行com.mock.util.CommonUtil.getCurrentTime()方法，方法支持自动以。使用自定义的方法需要先上传对应的jar exec(com.lucky.Util.CommonUtil.getReturn(hellomock)|***.jar)"表示执行***.jar包中的com.lucky.Util.CommonUtil.getReturn(hellomock)方法
