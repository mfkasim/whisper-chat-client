package com.eth.api;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eth.utils.StringUtils;

public class WhisperApi {

	private static final String PROTOCOL = "http";
	private static final String HOST = "localhost";
	private static final String PORT = "8545";
	private static final String ETH_URL_STR = PROTOCOL + "://" + HOST + ":" + PORT;

	private static URL ethUrl;

	public static String post(String payload, String symKeyId, String topic) throws RequestFailedException {

		return postWithHexData(StringUtils.toHex(payload), symKeyId, StringUtils.getTopicAsHex(topic));

	}

	public static String postWithHexData(String payloadAsHex, String symKeyId, String topicAsHex)
			throws RequestFailedException {

		String result;
		JSONObject jsonRpcRequestObject = getBasicJsonRpcObject(WhisperMethod.SHH_POST.getMethodName(), 1);
		JSONArray paramsArray = new JSONArray();
		JSONObject paramsObject = new JSONObject();
		paramsObject.put("topic", topicAsHex);
		paramsObject.put("symKeyId", symKeyId);
		paramsObject.put("payload", payloadAsHex);
		paramsObject.put("powTarget", 2.01);
		paramsObject.put("powTime", 2);
		
		paramsArray.put(paramsObject);
		jsonRpcRequestObject.put("params", paramsArray);

		try {
			JSONObject resultAsJson = sendEthereumRequest(jsonRpcRequestObject);

			// System.out.println("resultAsJson=" + resultAsJson);
			result = resultAsJson.getString("result");

		} catch (Exception e) {
			throw new RequestFailedException(e);
		}

		return result;

	}

	public static WhisperMessage[] getFilterMessages(String filterId)
			throws MalformedURLException, IOException, RequestFailedException {

		WhisperMessage[] messageList;

		JSONArray resultAsJsonArray;
		JSONObject jsonRpcRequestObject = getBasicJsonRpcObject(WhisperMethod.SHH_GET_FILTER_MESSAGES.getMethodName(),
				1);
		JSONArray paramsArray = new JSONArray();
		paramsArray.put(filterId);
		jsonRpcRequestObject.put("params", paramsArray);

		try {
			JSONObject resultAsJson = sendEthereumRequest(jsonRpcRequestObject);
			// System.out.println("resultAsJson="+resultAsJson);
			resultAsJsonArray = resultAsJson.getJSONArray("result");
			messageList = new WhisperMessage[resultAsJsonArray.length()];

			for (int i = 0; i < resultAsJsonArray.length(); i++) {
				messageList[i] = new WhisperMessage();
				messageList[i].setPayload(resultAsJsonArray.getJSONObject(i).getString("payload"));
				messageList[i].setTimestamp(resultAsJsonArray.getJSONObject(i).getLong("timestamp"));
				messageList[i].setTopic(resultAsJsonArray.getJSONObject(i).getString("topic"));

			}

		} catch (Exception e) {
			throw new RequestFailedException(e);
		}

		return messageList;

	}

	public static String newMessageFilterWithStringTopic(String topic, String symKeyId)
			throws MalformedURLException, IOException, RequestFailedException {
		String topicAsHex = StringUtils.getTopicAsHex(topic);

		return newMessageFilter(topicAsHex, symKeyId);
	}

	public static String newMessageFilter(String topicAsHex, String symKeyId)
			throws MalformedURLException, IOException, RequestFailedException {

		String result;
		JSONObject jsonRpcRequestObject = getBasicJsonRpcObject(WhisperMethod.SHH_NEW_MESSAGE_FILTER.getMethodName(),
				1);
		JSONArray paramsArray = new JSONArray();
		JSONObject paramsObject = new JSONObject();
		paramsObject.put("topics", new JSONArray().put(topicAsHex));
		paramsObject.put("symKeyId", symKeyId);

		paramsArray.put(paramsObject);
		jsonRpcRequestObject.put("params", paramsArray);

		try {
			JSONObject resultAsJson = sendEthereumRequest(jsonRpcRequestObject);
			result = resultAsJson.getString("result");

		} catch (Exception e) {
			throw new RequestFailedException(e);
		}

		return result;

	}

	public static String generateSymKeyFromPassword(String password)
			throws MalformedURLException, IOException, RequestFailedException {

		String result;
		JSONObject jsonRpcRequestObject = getBasicJsonRpcObject(
				WhisperMethod.SHH_GENERATE_SYM_KEY_FROM_PASSWORD.getMethodName(), 1);
		JSONArray paramsArray = new JSONArray();
		paramsArray.put(password);
		jsonRpcRequestObject.put("params", paramsArray);

		try {
			JSONObject resultAsJson = sendEthereumRequest(jsonRpcRequestObject);
			result = resultAsJson.getString("result");

		} catch (Exception e) {
			throw new RequestFailedException(e);
		}

		return result;

	}

	private static JSONObject sendEthereumRequest(JSONObject ethereumJsonRpcObject)
			throws MalformedURLException, IOException {

		HttpURLConnection httpConnection = getHttpConnection();

		httpConnection.setDoOutput(true);
		httpConnection.setDoInput(true);

		httpConnection.setRequestMethod("POST");

		byte[] bodyAsByteArray = ethereumJsonRpcObject.toString().getBytes();
		httpConnection.addRequestProperty("Content-Length", Integer.toString(bodyAsByteArray.length));
		writeByteArrayBody(httpConnection, bodyAsByteArray);

		httpConnection.connect();

		// int responseCode = httpConnection.getResponseCode();

		return new JSONObject(readStringResponse(httpConnection));

	}

	private static URL getEthereumUrl() throws MalformedURLException {
		if (ethUrl == null)
			ethUrl = new URL(ETH_URL_STR);
		return ethUrl;
	}

	private static JSONObject getBasicJsonRpcObject(String method, int id) {
		JSONObject jsonRpcObject = new JSONObject();
		jsonRpcObject.put("jsonrpc", "2.0");
		jsonRpcObject.put("method", method);
		jsonRpcObject.put("id", id);
		return jsonRpcObject;
	}

	private static HttpURLConnection getHttpConnection() throws MalformedURLException, IOException {

		HttpURLConnection httpConnection = (HttpURLConnection) getEthereumUrl().openConnection();
		httpConnection.addRequestProperty("Content-Type", "application/json");
		return httpConnection;
	}

	private static void writeByteArrayBody(HttpURLConnection httpURLConnection, byte[] postQuery) throws IOException {
		OutputStream os = httpURLConnection.getOutputStream();
		BufferedOutputStream writer = new BufferedOutputStream(os);
		writer.write(postQuery);
		writer.flush();
		writer.close();
		os.close();
	}

	private static String readStringResponse(HttpURLConnection httpURLConnection) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String responseAsString = response.toString();
		return responseAsString;
	}
}
