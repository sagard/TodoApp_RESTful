/*
 * Desc - Uses twilio api to send sms when a todo item is marked as done
 * Author - Sagar Dhedia
 */
package com.utils;
import java.util.Map;
import java.util.HashMap;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class TwilioSmsSender {

	// My twilio id and token 
	public static final String ACCOUNT_SID = "AC7a269fc99d8470cc3b29079a23e3c4fa";
	public static final String AUTH_TOKEN = "2d59250c2d265dd879b5ae1bf188c90b";

	public  static void sendSms(String toPhnNumber,String msg) throws TwilioRestException {

		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		Account account = client.getAccount();

		MessageFactory messageFactory = account.getMessageFactory();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//Needs to be verified number for trial accounts
		params.add(new BasicNameValuePair("To", toPhnNumber)); 
		// My twilio test number
		params.add(new BasicNameValuePair("From", "+16504698609")); 
		params.add(new BasicNameValuePair("Body", msg));
		Message sms = messageFactory.create(params);
	}
}

