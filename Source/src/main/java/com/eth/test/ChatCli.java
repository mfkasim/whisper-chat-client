package com.eth.test;

import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.eth.api.RequestFailedException;
import com.eth.api.WhisperApi;
import com.eth.utils.StringUtils;

public class ChatCli {

	private static String nickname;
	private static String password;
	private static String topic;
	private static String topicAsHex;

	public static void main(String[] args) {
		Console console = System.console();
		System.out.print("Greeting, Sir...\nPlease enter your nickname:");
		nickname = console.readLine();

		System.out.print("Please enter the symmetric password as string:");
		password = new String(console.readPassword());// s.next();

		System.out.print("Please enter the topic you want:");
		topic = console.readLine(); // s.next();

		System.out.println("topicAsHex=" + topicAsHex);

		try {
			String symKeyId = WhisperApi.generateSymKeyFromPassword(password);
//			System.out.println("symKeyId=" + symKeyId);

			String filterId = WhisperApi.newMessageFilterWithStringTopic(topic, symKeyId);
//			System.out.println("filterId=" + filterId);

			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.scheduleAtFixedRate(new ChatMessagesCheckTask(filterId), 100, 1000, TimeUnit.MILLISECONDS);

			String command;
			while (!(command = console.readLine()).equals("q")) {
				if(command.isEmpty())
				{
					
					continue;
				}
				String messageToSend = nickname + ":" + command;
				String postResult = WhisperApi.post(messageToSend, symKeyId, topic);
//				System.out.println("postResult=" + postResult);

			}

			executorService.shutdown();

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
