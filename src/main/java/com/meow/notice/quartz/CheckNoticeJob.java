package com.meow.notice.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meow.notice.utils.FileUtil;
import com.meow.notice.utils.MailUtil;
import com.meow.notice.utils.NoticeUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckNoticeJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(CheckNoticeJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("*************begin check notice**********" + sdf.format(new Date()) + "**************");
		checkNewNotice();
		logger.info("*************check notice end**********" + sdf.format(new Date()) + "**************");
	}

	private static void checkNewNotice() {
		JSONArray newNotices = NoticeUtil.getAllNotice();

		JSONArray oldNotices = null;
		String readNotices = FileUtil.readFromFile();
		logger.info("readNotices:" + readNotices);
		if ((readNotices != null) && (readNotices != "") && (!readNotices.equals("?"))) {
			oldNotices = JSON.parseArray(readNotices);
		} else {
			oldNotices = new JSONArray();
		}
		String newNote = "";
		if (newNotices != null) {
			for (Object newObj : newNotices) {
				JSONObject newJSONObj = (JSONObject) newObj;
				if (!oldNotices.contains(newJSONObj)) {
					newNote = newNote + newJSONObj.getString("date") + " " + newJSONObj.getString("title") + "\\n\\r";
				}
			}
		}
		logger.info("newNote:" + newNote);
		if (newNote != "") {
			MailUtil.sendMail("The Good News Rent Notice!!", newNote);

			FileUtil.saveToFile(newNotices.toJSONString());
		}
	}
}
