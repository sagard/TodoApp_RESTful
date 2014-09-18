/**
 * Desc - This is a client class that calls the different restful api to perform the operations
 * Author - Sagar Dhedia
 */

package com.todo;

import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TodoClient {
	//Configuration parameters:
	static ClientConfig clientConfig=null;
	static Client client = null;
	static WebResource webResource = null;
	static final String baseUrl = "http://todoapp-basicshopping.rhcloud.com/TodoApp/rest/todo";

	public void getbyid()
	{
		int todoid= 1;
		String url = baseUrl + "/getbyid/";
		webResource = client.resource(url + todoid);
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").get(String.class));
			System.out.println("get results: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getbytitle()
	{
		String title= "test%20blog";
		String url = baseUrl + "/getbytitle/";
		webResource = client.resource(url + title);
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").get(String.class));
			System.out.println("get results by title: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getall()
	{
		String url = baseUrl + "/getlist/all";
		webResource = client.resource(url);
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").get(String.class));
			System.out.println("get all results: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void search(String query)
	{
		String url = baseUrl + "/search/";
		webResource = client.resource(url + query);
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").get(String.class));
			System.out.println("search results: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deletebyid()
	{
		int todoid= 1;
		String url = baseUrl + "/removebyid/";
		webResource = client.resource(url + todoid);
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").delete(String.class));
			System.out.println("delete by id results: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deletebytitle()
	{
		String title = "test%20blog";
		String url = baseUrl + "/removebytitle/";
		webResource = client.resource(url + title);
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").delete(String.class));
			System.out.println("delete results: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeall()
	{
		String url = baseUrl + "/removeall";
		webResource = client.resource(url);
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").delete(String.class));
			System.out.println("remove all: " + obj);
		} catch (Exception e) {
			System.out.println("remove all error: ");

			e.printStackTrace();
		}
	}

	public void updatebyid() throws JSONException
	{
		int todoid= 1;
		String url = baseUrl + "/updatebyid/";
		webResource = client.resource(url + todoid);
		JSONObject object=new JSONObject();
		object.put("title","updated test blog");
		object.put("body","updated body");
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").put(String.class,object.toString()));
			System.out.println("update by id result: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatebytitle() throws JSONException
	{
		String testblog = "test%20blog";
		String url = baseUrl + "/updatebytitle/";
		webResource = client.resource(url + testblog);
		JSONObject object=new JSONObject();
		object.put("title","updated test blog");
		object.put("body","updated body");
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").put(String.class,object.toString()));
			System.out.println("update res from title: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void markCompletedId() throws JSONException
	{
		int todoid= 2;
		String url = baseUrl + "/markcompletedid/";
		webResource = client.resource(url + todoid);
		String phnNumber= "+10000000000"; //needs to be verified number you can add my num for testing
		JSONObject object=new JSONObject();
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").put(String.class,phnNumber));
			System.out.println("mark completed by id res: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void markCompletedTitle() throws JSONException
	{
		String title = "updated%20test%20blog";
		String url = baseUrl + "/markcompletedtitle/";
		webResource = client.resource(url + title);
		JSONObject object=new JSONObject();
		String phnNumber= "+16505268358";
		try {
			JSONObject obj = new JSONObject(webResource.accept("application/json").type("application/json").put(String.class,phnNumber));
			System.out.println("mark completed by title res: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws JSONException
	{
		try {
			String url = baseUrl + "/createlist";
			webResource = client.resource(url);      	
			JSONObject object=new JSONObject();
			object.put("title","test blog");
			object.put("body","buy grocery");
			JSONObject obj = new JSONObject(webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class,object.toString()));
			System.out.println("Created todo id: " + obj);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws JSONException {
		TodoClient tc = new TodoClient();
		clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		client = Client.create(clientConfig);
		tc.create();
		tc.getbyid();
		tc.getbytitle();
		tc.getall();
		tc.deletebyid();
		tc.deletebytitle();
		tc.create();
		tc.removeall();
		tc.create();
		tc.create();
		tc.updatebyid();
		tc.updatebytitle();
		tc.markCompletedId();
		tc.markCompletedTitle();
		tc.search("test");

	}

}
