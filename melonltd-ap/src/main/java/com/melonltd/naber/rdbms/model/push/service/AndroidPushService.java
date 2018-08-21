package com.melonltd.naber.rdbms.model.push.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.melonltd.naber.endpoint.util.JsonHelper;
import com.melonltd.naber.rdbms.model.type.DeviceCategory;
import com.melonltd.naber.rdbms.model.vo.NotificationVo;

@Service("androidPushService")
@PropertySource("classpath:/config.properties")
public class AndroidPushService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AndroidPushService.class);

	@Value("${google.fcm.server.key}")
	private String SERVER_KEY;
	private static String serverKey;

	// private static URL url = new URL("https://fcm.googleapis.com/fcm/send");

	@PostConstruct
	public void init() {
		this.serverKey = this.SERVER_KEY;
	}

	public void pushForAndroids(List<NotificationVo> notificationVos) {

		for (NotificationVo a : notificationVos) {
			try {
				String pushMessage = JsonHelper.toJson(a);
				URL url = new URL("https://fcm.googleapis.com/fcm/send");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Authorization", "key=" + this.serverKey);
				conn.setRequestProperty("Accept-Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(pushMessage.getBytes("UTF-8"));
				LOGGER.info("pusp status ResponseCode: {}, ResponseMessage: {}", conn.getResponseCode(),
						conn.getResponseMessage());
			} catch (Exception e) {
				LOGGER.error("push error , error: {}", e.getMessage());
			}
		}
	}

	public void pushForIOSAPNs(List<NotificationVo> notificationVos) {
		for (NotificationVo a : notificationVos) {
			a.setNotification(new NotificationVo.Notify(a.getData().get("title"),a.getData().get("message")));
			a.setAndroid(null);
			try {
				String pushMessage = JsonHelper.toJson(a);
				System.out.println(pushMessage);
				URL url = new URL("https://fcm.googleapis.com/fcm/send");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Authorization", "key=" + this.serverKey);
				conn.setRequestProperty("Accept-Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(pushMessage.getBytes("UTF-8"));
				LOGGER.info("pusp status ResponseCode: {}, ResponseMessage: {}", conn.getResponseCode(),
						conn.getResponseMessage());
			} catch (Exception e) {
				LOGGER.error("push error , error: {}", e.getMessage());
			}
		}
	}
	
	
	public void pushs(List<NotificationVo> notificationVos, DeviceCategory category) {
		for (NotificationVo a : notificationVos) {
			if (category == DeviceCategory.IOS) {
				a.setNotification(new NotificationVo.Notify(a.getData().get("title"),a.getData().get("message")));
				a.setAndroid(null);	
			}
			
			try {
				String pushMessage = JsonHelper.toJson(a);
				System.out.println(pushMessage);
				URL url = new URL("https://fcm.googleapis.com/fcm/send");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Authorization", "key=" + this.serverKey);
				conn.setRequestProperty("Accept-Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(pushMessage.getBytes("UTF-8"));
				LOGGER.info("pusp status ResponseCode: {}, ResponseMessage: {}", conn.getResponseCode(),
						conn.getResponseMessage());
			} catch (Exception e) {
				LOGGER.error("push error , error: {}", e.getMessage());
			}
		}
	}
}
