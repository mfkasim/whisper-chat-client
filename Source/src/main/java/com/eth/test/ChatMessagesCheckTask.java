package com.eth.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eth.api.RequestFailedException;
import com.eth.api.WhisperApi;
import com.eth.api.WhisperMessage;
import com.eth.utils.StringUtils;

public class ChatMessagesCheckTask implements Runnable{

	String filterId; 
	public static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS");
	public ChatMessagesCheckTask(String filterId)
	{
		this.filterId=filterId;
		
		
	}
	
	public void run() {
		
		try {
			WhisperMessage[] messageList = WhisperApi.getFilterMessages(filterId);
			for(WhisperMessage message:messageList)
			{
				System.out.print(sdf.format(new Date(message.getTimestamp()*1000)));
				System.out.println(" | "+ StringUtils.fromHex(message.getPayload()));
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequestFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
