package com.mock.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
public class CommonUtil {
	
	
	public  static String getRandomStr() {
		int length = 10;
		//����һ���ַ�����A-Z��a-z��0-9����62λ��
	    String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
	    //��Random���������
	        Random random=new Random();  
	        StringBuffer sb=new StringBuffer();
	        //����Ϊ����ѭ������
	        for(int i=0; i<length; ++i){
	          //����0-61������
	          int number=random.nextInt(62);
	          //������������ͨ��length�γ��ص�sb��
	          sb.append(str.charAt(number));
	        }
	        //�����ص��ַ�ת�����ַ���
	        return sb.toString();

	}
	
	public static String getRandomNum() {
		Random random=new Random();
		random.nextInt(10000000);
		return String.valueOf(random.nextInt(10000000)+10000000);
	}
	
	
	public static String getRandomNum5() {
		Random random=new Random();
		random.nextInt(10000);
		return String.valueOf(random.nextInt(10000)+10000);
	}
	
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//�������ڸ�ʽ
		String date = df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ�䣬Ҳ��ʹ�õ�ǰʱ���
		return date;
	}
	
	public static String getFormatTime(String fromat,int days) {
		String date = "";
		 Date d=new Date(); 
		 
	
		try {
		SimpleDateFormat df = new SimpleDateFormat(fromat);//�������ڸ�ʽ		
//		date= df.format(new Date(d.getTime()+ days * 24 * 60 * 60 *1000));
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DAY_OF_YEAR, days);
//		ca.add(Calendar.MONTH, +days/30);
		date = df.format(ca.getTime());
		}catch (Exception e) {
			// TODO: handle exception
		
		}
		return date;
	}
	
	public static String getIndexID() {
		
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getFormatTime("yyyyMMdd",143));
		System.out.println(getRandomNum());
		
	}

}
