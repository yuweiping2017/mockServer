# mockServer


1����ܽ���
Ϊ�Զ��������ṩһ��mockserverƽ̨��֧��json��ʽ�Ľӿڡ��û�ֻ��Ҫ�ϴ���Ӧ��ģ�壬mock�����Զ����ɣ�����ģ�����÷��ض�Ӧ����Ӧ����
ģ���������֧��������Լ��Զ��庯�����Զ��庯����Ҫ�ֶ�����jar��������Ŀ����ʹ�ã�Ŀǰ��ܸ���ɿ��������ںܶ�bug���������Ϊ��ʵ�ֹ��ܺܶ�ط����ö���д������Ҫ������Ż��������ٽ����Ż�

2��ģ���﷨
{
	"/login": [    //����·��
		{
			"head": {  //������Ϣͷ
				"http": 200
			},
			"body": {  //������Ϣ��
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
			"matchRequest": { //������Ϣ��ƥ�����
				"name":"yu"
			}
		},
		{
			"head": {
				"http": 200
			},
			"body": {
				"success": "exec(com.lucky.Util.CommonUtil.getReturn(hellomock)|***.jar)"//֧��ִ�е���****.jar�еķ���
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
				"success": "exec(com.mock.util.CommonUtil.getCurrentTime())"//����ִ��ϵͳ�ķ���
			},
			"matchRequest": {
				"name": "yu2"
			}
		}
	]

}
3��ƥ�����
ƥ�����Ϊ��������·��ƥ�䣬һ��·����������ж��ģ�塣�����Ҫ��������·����������Ϣ��ƥ�䣬��������Ϣƥ��matchRequest�ֶ�
exec(***)��ʾִ��ĳ��������exec(randomNum)��ʾ����һ������ַ���  exec(com.mock.util.CommonUtil.getCurrentTime())��ʾ
ִ��com.mock.util.CommonUtil.getCurrentTime()����������֧���Զ��ԡ�ʹ���Զ���ķ�����Ҫ���ϴ���Ӧ��jar exec(com.lucky.Util.CommonUtil.getReturn(hellomock)|***.jar)"��ʾִ��***.jar���е�com.lucky.Util.CommonUtil.getReturn(hellomock)����
