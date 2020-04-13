package com.ischoolbar.programmer.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/** 

* @author 作者 郭小雨

* @version 创建时间：2020年3月18日 下午5:31:21 

* 类说明 

*/
public class DateFormatConverter implements Converter<String, Date>{
	
	public Date convert(String source){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
