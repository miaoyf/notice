package com.meow.notice.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class NoticeUtil {
	private static Logger logger = LoggerFactory.getLogger(NoticeUtil.class);
	private static final String URL = "http://www.bphc.cn/article/list/b02e7e29e33642f789e4d1e41db08b7d.html";

	public static JSONArray getAllNotice() {
		JSONArray noticeList = new JSONArray();
		try {
			Document doc = Jsoup.connect(URL).get();
			Elements ulList = doc.select(".publicity ul li a");
			for (Element node : ulList) {
				JSONObject notice = new JSONObject();

				Elements dateElements = node.select("div span");
				Element day = (Element) dateElements.get(0);
				Element month = (Element) dateElements.get(1);
				notice.put("date", month.html() + day.html());

				Elements titleElements = node.select("div h2");
				String title = titleElements.first().html();
				notice.put("title", title);
				noticeList.add(notice);
			}
		} catch (Exception e) {
			logger.error("Get New Notice Exception:", e);
		}
		return noticeList;
	}
}
