/*
 * Desc - This class is a backend for a todo list .It has the following functionalities:
 * Create Todo Item,Delete based on id,title or all,Get based on id,title or all,
 * Delete based on id,title and all,search using searchbox.io,Update based on id and title
 * Author - Sagar Dhedia
*/

package com.todo;
import com.mongodb.*;

import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.twilio.sdk.TwilioRestException;
import com.utils.*;

@Path("/todo")
public class TodoAppImpl{

	private Properties conf;
	static final String sCollection="todolist";
	//static final String sDB="todolist";
	static final String sDB="todoapp";
	//static final String sHost="localhost";
	static final String sHost="127.5.150.130";
	private DBCollection collection;
	protected Mongo m;
	public final SearchClient sc;

	public TodoAppImpl()
	{
		this.sc = new SearchClient();
	}

	/* Description  - Creates connection to MongoDB
	 * Input - None 
	 * Output - None
	 */
	private DBCollection connect()
	{
		//Setup the conf paramters
		try
		{	
			//Create the connection
			m = new Mongo(sHost,27017);
			DB db = m.getDB(sDB);
			if (!db.authenticate("admin", "rF-Qag1rv4Un".toCharArray()))
			{
				  throw new MongoException("unable to authenticate");
			}
			collection = db.getCollection(sCollection);
            
			if (collection == null)
				throw new RuntimeException("Missing collection: "
						+ conf.getProperty(sCollection));

			return collection;
		} 
		catch (Exception ex) 
		{
			// should never get here unless no directory is available
			throw new RuntimeException("Unable to connect to mongodb on "
					+ conf.getProperty(sHost));
		}
	}


	/*
	 * Description  - Creates a new todo item entry in database
	 * Input - String in json format that contains the title and body 
	 * Output - String in json format giving the item created ,status and message
	 */
	@Path("/createlist")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String createList(String todo) throws JSONException
	{
		System.out.println("Adding new todo list.." + todo);
		JSONObject json=null;
		try 
		{
			json = new JSONObject(todo);
			DBCollection col = connect();
			int todoid = getLastListId(col);
			json.put("todoid", ++todoid);

			if (json != null)
			{
				DBObject dob = convertToMongo(json);
				col.insert(dob);
				boolean indexSuccess = this.sc.createIndex(json.toString());
				json.put("Return Code", "201");
				json.put("Message", "Created");
				json.put("Index Created", indexSuccess);
			}
			else
			{					
				json.put("Return Code", "204");
				json.put("Message", "Not Created");
			}
		} 
		catch (Exception e)
		{
			json = new JSONObject();
			json.put("Return Code", "501");
			json.put("Message", "Not Created");
			e.printStackTrace();
		}
		return json.toString();
	}


	/*
	 * Description  - Get a todo item from database
	 * Input - int that has todoid to get
	 * Output - String in json format giving the todo item ,status and message
	 */	
	@Path("/getbyid/{todoid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getListById(@PathParam("todoid") int todoid) 
	{
		System.out.println("todo id:" + todoid);
		BasicDBObject query = new BasicDBObject("todoid", todoid);
		JSONObject res = getFromDB(query);
		return res.toString();
	}


	/*
	 * Description  - Get a todo item from database
	 * Input - String that has tile to get 
	 * Output - String in json format giving the todo item ,status and message
	 */
	@Path("/getbytitle/{title}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getListByTitle(@PathParam("title") String title)
	{
		System.out.println("get by title:" + title);
		BasicDBObject query = new BasicDBObject("title", title);
		JSONObject res = getFromDB(query);
		return res.toString();
	}

	/*
	 * Description  - Get all todo items from database
	 * Input - None
	 * Output - String in json format giving all todo items ,status and message
	 */
	@Path("/getlist/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllLists() 
	{
		System.out.println("get all lists..");
		BasicDBObject query = new BasicDBObject();
		JSONObject res = getFromDB(query);
		return res.toString();
	}

	/*
	 * Description  - Gets the data by querying the database
	 * Input - BasicDBObject that contains the query to run against the database 
	 * Output - Json object giving todo item/items ,status and message
	 */
	private JSONObject getFromDB(BasicDBObject query)
	{
		DBCollection col = connect();
		System.out.println("Query is " + query);
		DBCursor cursor = col.find(query);

		JSONObject json = new JSONObject();
		if (cursor.size() == 0) 
		{
			try
			{
				json.put("Status Code", "404");
				json.put("Status String", "Resource Not Found");
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			return json;
		} 
		else 
		{
			JSONObject obj = cursorToJson(cursor);
			return obj;
		}
	}


	/*
	 * Description  - Delete the todoitem from database
	 * Input - int that has id of item to be deleted
	 * Output - String in json format status and message
	 */	
	@Path("/removebyid/{todoid}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteById(@PathParam("todoid") int todoid)
	{
		System.out.println("deleting the todo item" + todoid);
		BasicDBObject b = new BasicDBObject("todoid", todoid);
		JSONObject res = deleteFromDB(b);
		return res.toString();
	}


	/*
	 * Description  - Delete the todoitem from database
	 * Input - String that has title of item to be deleted
	 * Output - String in json format status and message
	 */
	@Path("/removebytitle/{title}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteByTitle(@PathParam("title") String title) 
	{
		System.out.println("deleting the todo item by title");
		BasicDBObject b = new BasicDBObject("title", title);
		JSONObject res = deleteFromDB(b);
		return res.toString();
	}


	/*
	 * Description  - Delete all todoitems from database
	 * Input - none
	 * Output - String in json format that has status and message
	 */
	@Path("/removeall")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAll() 
	{
		System.out.println("deleting all todo items");
		BasicDBObject b = new BasicDBObject();
		JSONObject res = deleteFromDB(b);
		if(res.get("Status Code").equals("200"))
		{
			System.out.println("in if..");
			boolean deleteIndex=false;
			try
			{
				deleteIndex = this.sc.delete();
			} 
			catch (Exception e) {
				res.put("Index Deleted", deleteIndex);
				return res.toString();
			}
		}
		return res.toString();
	}

	/*
	 * Description  - Delete the todoitem by querying against the database
	 * Input - BasicDBObject that contains the query to run against the database
	 * Output - JSON Object that has status and message
	 */	
	public JSONObject deleteFromDB(BasicDBObject b)
	{
		JSONObject json = new JSONObject();
		try 
		{
			DBCollection col = connect();
			DBCursor cursor = col.find(b);
			if (cursor.size() >= 1) 
			{	
				System.out.println("remove query" + b);
				col.remove(b);
				json.put("Status Code", "200");
				json.put("Status String", "Success");
			}
			else 
			{
				System.out.println("Not found");
				json.put("Status Code", "404");
				json.put("Status String", "Resource Not Found");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return json;
	}


	/*
	 * Description  - Update the todoitem
	 * Input - int id of todoitem that needs to be updated,String in json format of data to be updated
	 * Output - String in json format that has status and message
	 */
	@Path("/updatebyid/{todoid}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateListbyId(@PathParam("todoid") int todoid,String todo) throws JSONException
	{
		JSONObject json=null;
		JSONObject res=null;
		try
		{
			json = new JSONObject(todo);
			if (json != null)
			{
				//search for the id ..
				BasicDBObject bd = new BasicDBObject("todoid", todoid);
				res = updateDB(bd,json);
			}
		} 
		catch (JSONException e) 
		{
			res = new JSONObject();
			res.put("Status Code", "404");
			res.put("Status String", "Resource Not Found");
			e.printStackTrace();
		}

		return res.toString();		
	}


	/*
	 * Description  - Update the todoitem
	 * Input - String title of todoitem that needs to be updated,String in json format of data to be updated
	 * Output - String in json format that has status and message
	 */
	@Path("/updatebytitle/{title}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateListbyTitle(@PathParam("title") String title,String todo) throws JSONException {
		JSONObject json=null;
		JSONObject res=null;
		try
		{
			json = new JSONObject(todo);
			if (json != null)
			{
				//search for the id ..
				BasicDBObject bd = new BasicDBObject("title", title);
				res = updateDB(bd,json);
			}
		} 
		catch (JSONException e) 
		{
			res = new JSONObject();
			res.put("Status Code", "404");
			res.put("Status String", "Resource Not Found");
			e.printStackTrace();
		}

		return res.toString();		
	}

	/*
	 * Description  - Mark the todoitem as done
	 * Input - int id of todoitem that needs to be marked done,String that has phnnumber to send message when it is updated
	 * Output - String in json format that has status and message
	 */
	@Path("/markcompletedid/{todoid}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public String markCompletedbyId(@PathParam("todoid") int todoid,String phnNumber) throws JSONException, TwilioRestException
	{
		JSONObject res=null;
		try
		{
			//search for the id ..
			BasicDBObject bd = new BasicDBObject("todoid", todoid);
			JSONObject json = new JSONObject();
			json.put("done",true);
			res = updateDB(bd,json);
			if(res.get("StatusCode").equals("200"))
			{
				String msg = "Task" + todoid + " is completed";
				TwilioSmsSender.sendSms(phnNumber, msg);
			}
		} 
		catch (JSONException e) 
		{
			res = new JSONObject();
			res.put("Status Code", "404");
			res.put("Status String", "Resource Not Found");
			e.printStackTrace();
		}
		return res.toString();		
	}

	/*
	 * Description  - Mark the todoitem as done
	 * Input - String title of todoitem that needs to be marked done,String that has phnnumber to send message when it is updated
	 * Output - String in json format that has status and message
	 */
	@Path("/markcompletedtitle/{title}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public String markCompletedbyTitle(@PathParam("title") String title,String phnNumber) throws JSONException, TwilioRestException
	{
		JSONObject res=null;
		try
		{
			//search for the id ..
			BasicDBObject bd = new BasicDBObject("title", title);
			JSONObject json = new JSONObject();
			json.put("done",true);
			res = updateDB(bd,json);
			if(res.get("StatusCode").equals("200"))
			{
				String msg = "Task" + title + " is completed";
				TwilioSmsSender.sendSms(phnNumber, msg);
			}
		} 
		catch (JSONException e) 
		{
			res = new JSONObject();
			res.put("Status Code", "404");
			res.put("Status String", "Resource Not Found");
			e.printStackTrace();
		}
		return res.toString();		
	}

	/*
	 * Description  - Updates the fields in database
	 * Input - BasicDBObject to search for todoitem that needs to be updated,JsonObject of data to be updated
	 * Output - String in json format that has status and message
	 */
	private JSONObject updateDB(BasicDBObject b,JSONObject json)
	{
		try 
		{
			DBCollection col = connect();
			DBCursor cursor = col.find(b);
			if (cursor.size() >= 1) 
			{	
				//Convert the fields to be updated in mongo format
				DBObject dob=convToMongoForUpdate(json);
				//Get a updated set object
				BasicDBObject updateObj = new BasicDBObject();
				updateObj.put("$set", dob);
				//Update the field found (id passed)
				col.update(cursor.next(),updateObj);
				json.put("StatusCode", "200");
				json.put("Status String", "Success");
			}
			else 
			{
				json.put("Status Code", "404");
				json.put("Status String", "Resource Not Found");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return json;
	}


	/*
	 * Description  - Search for data using index
	 * Input - String that has data to be searched
	 * Output - String in json format that has todoitems,status and message
	 */	
	@Path("/search/{query}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String search(@PathParam("query") String query) throws Exception {
		System.out.println("search .. " + query);
		this.sc.getAll();

		String res = this.sc.search(query);
		if(res == null)
		{
			JSONObject json = new JSONObject();
			json.put("Status Code", "404");
			json.put("Status String", "Data not found");
			return json.toString();
		}
		System.out.println("search results .. " + res);

		return res;
	}	

	/*
	 * Description  - Gets the last id inserted into database to increment the id
	 * Input - DBCollection collection object
	 * Output - int that gives the last inserted todo id
	 */	
	public int getLastListId(DBCollection col)
	{	
		int id;
		//get the last id inserted in collection(todolist)
		BasicDBObject query = new BasicDBObject("todoid", -1);		
		DBCursor cursor = col.find().sort(query).limit(1);
		if(cursor.size()==0)
		{
			return 0;
		}
		else
		{
			//get the id and return it back
			DBObject data = cursor.next();
			String todoid = data.get("todoid").toString();
			id = Integer.parseInt(todoid);
		}
		return id;
	}

	/*
	 * Description  - Converts the json object to DBObject to insert into mongodb
	 * Input - JSONObject that has todo item data
	 * Output - DBObject which can be inserted into mongodb
	 */	
	private DBObject convertToMongo(JSONObject todo)
	{
		BasicDBObject dbobj = new BasicDBObject();
		try 
		{
			dbobj.append("todoid", todo.getInt("todoid"));
			if (todo.has("title") && todo.getString("title") != null)
				dbobj.append("title", todo.getString("title"));
			if (todo.has("body") && todo.getString("body") != null)
				dbobj.append("body", todo.getString("body"));
			//starting status set to false
			dbobj.append("done", false);

		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return dbobj;
	}

	/*
	 * Description  - Converts the DBObject to insert into mongodb for update queries
	 * Input - JSONObject that has todo item data
	 * Output - DBObject which can be inserted into mongodb
	 */		
	private DBObject convToMongoForUpdate(JSONObject todo) 
	{
		BasicDBObject dbobj = new BasicDBObject();
		try 
		{   
			if (todo.has("title") && todo.getString("title") != null)
				dbobj.append("title", todo.getString("title"));
			if (todo.has("body") && todo.getString("body") != null)
				dbobj.append("body", todo.getString("body"));
			if (todo.has("done") && todo.getBoolean("done") != false)
				dbobj.append("done", todo.getBoolean("done"));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return dbobj;
	}

	/*
	 * Description  - Converts the DBCursor back to JSONObject
	 * Input - DBCursor cursor 
	 * Output - JSONObject containing todo item
	 */	
	private JSONObject cursorToJson(DBCursor cursor) 
	{
		int todoid=0;
		String title = "", body = "";
		boolean done=false;
		System.out.println("---> cursor size: " + cursor.size());
		int size = cursor.size();
		JSONObject obj = new JSONObject();
		for (int n = 0; n < size; n++) 
		{
			BasicDBObject data = (BasicDBObject)cursor.next();
			System.out.println("data:" + data);

			todoid =  data.getInt("todoid");
			title =  data.getString("title");			
			body =  data.getString("body");
			done =  data.getBoolean("done", done); 
			System.out.println("id:" + todoid + "title: " + title + "body:" + body);
			try
			{
				obj.append("todoid", todoid);
				obj.append("title", title);
				obj.append("body", body);
				obj.append("done", done);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return obj;
	}

}
