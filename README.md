TodoApp_RESTful
===============

This provides a JSON RESTful web service for a todo app.It uses Jersey framework for webservice and Mongodb as datastore
The app provides following functionalites for todo items:
- Get
- Delete
- Update
- Mark as done/undone ( Sends a sms upon done using Twilio api)
- Search( using searchbox.io api)

Base url is :"http://todoapp-basicshopping.rhcloud.com/TodoApp/rest/todo"
Use TodoClient.java to test the application.

Api Description
===============

****
URL: /createlist
Request type: POST
Post parameters:
     Json paramaters passed as String.
                  title: Title of todo item
				  body: Content of todo item
Output:
     JSON object as string.
	     title : Title of todo item created
		 body: Content of todo item
		 todoid: Id to identify the todo item
		 done: Boolean indicating task done or not
		 Status: Http status code
		 Message: Success/Failure message
****

****
URL: /getbyid/{todoid}
Request type: GET
URL parameters: 
        todoid : int containing id of todo item to get
Output:
     JSON object as string.
	     title : Title of todo item 
		 body: Content of todo item
		 todoid: Id to identify the todo item
		 done: Boolean indicating task done or not
		 Status: Http status code
		 Message: Success/Failure message
****

****
URL: /getbytitle/{title}
Request type: GET
URL parameters: 
        title : String containing title of todo item to get
Output:
     JSON object as string.
	     title : Title of todo item 
		 body: Content of todo item
		 todoid: Id to identify the todo item
		 done: Boolean indicating task done or not
		 Status: Http status code
		 Message: Success/Failure message
****

****
URL: /getlist/all
Request type: GET
URL parameters: 
        None
Output:
     JSON object as string.
	     title : Array of Title of todo items 
		 body: Array of Content of todo items
		 todoid: Array of Ids to identify the todo item
		 done: Array of Booleans indicating task done or not
		 Status: Http status code
		 Message: Success/Failure message
****

****
URL: /removebyid/{todoid}
Request type: DELETE
URL parameters: 
        todoid : int containing id of todo item to delete
Output:
     JSON object as string.
	     Status: Http status code
		 Message: Success/Failure message
****

****
URL: /removebytitle/{title}
Request type: DELETE
URL parameters: 
        title : String containing title of todo item to delete
Output:
     JSON object as string.
	     Status: Http status code
		 Message: Success/Failure message
****

****
URL: /removeall
Request type: DELETE
URL parameters: 
        None
Output:
     JSON object as string.
	     Status: Http status code
		 Message: Success/Failure message
****

****
URL: /updatebyid/{todoid}
Request type: PUT
URL parameters: 
        todoid : int containing id of todo item to update
PUT parameters:
     JSON object as string.
        title: new title 
		body: new body		
Output:
     JSON object as string.
	    Status: Http status code
		Message: Success/Failure message
****

****
URL: /updatebytitle/{title}
Request type: PUT
URL parameters: 
        title : String containing title of todo item to update
PUT parameters:
     JSON object as string.
        title: new title 
		body: new body		
Output:
     JSON object as string.
	    Status: Http status code
		Message: Success/Failure message
****

****
URL: /markcompletedid/{todoid}
Request type: PUT
URL parameters: 
        todoid : int containing id of todo item to mark as done
PUT parameters:
        phnNumber : Phone number to send sms after marking item as done	
Output:
     JSON object as string.
	    Status: Http status code
		Message: Success/Failure message
****

****
URL: /markcompletedtitle/{title}
Request type: PUT
URL parameters: 
        title : String containing id of todo item to mark as done
PUT parameters:
        phnNumber : Phone number to send sms after marking item as done	
Output:
     JSON object as string.
	    Status: Http status code
		Message: Success/Failure message
****

****
URL: /search/{query}
Request type: PUT
URL parameters: 
        query : String containing text to be searched
Output:
     JSON object as string.
	    title : Array of Title of todo items 
		body: Array of Content of todo items
		todoid: Array of Ids to identify the todo item
		done: Array of Booleans indicating task done or not
	    Status: Http status code
		Message: Success/Failure message
****

Future Enhancements:
====================
A gui for the web service
Index to be updated when data is updated
Deleting index based on id and title
Store data in todo java class

