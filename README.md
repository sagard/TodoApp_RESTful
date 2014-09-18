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
URL: /createlist,
Request type: POST,
Post parameters:
     Json paramaters passed as String.
                  title: Title of todo item
				  body: Content of todo item,
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
URL: /getbyid/{todoid},
Request type: GET,
URL parameters: 
        todoid : int containing id of todo item to get,
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
URL: /getbytitle/{title},
Request type: GET,
URL parameters: 
        title : String containing title of todo item to get,
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
URL: /getlist/all,
Request type: GET,
URL parameters: 
        None,
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
URL: /removebyid/{todoid},
Request type: DELETE,
URL parameters: 
        todoid : int containing id of todo item to delete,
Output:
     JSON object as string.
	     Status: Http status code
		 Message: Success/Failure message
****

****
URL: /removebytitle/{title},
Request type: DELETE,
URL parameters: 
        title : String containing title of todo item to delete,
Output:
     JSON object as string.
	     Status: Http status code
		 Message: Success/Failure message
****

****
URL: /removeall,
Request type: DELETE,
URL parameters: 
        None,
Output:
     JSON object as string.
	     Status: Http status code
		 Message: Success/Failure message
****

****
URL: /updatebyid/{todoid},
Request type: PUT,
URL parameters: 
        todoid : int containing id of todo item to update,
PUT parameters:
     JSON object as string.
        title: new title 
		body: new body,		
Output:
     JSON object as string.
	    Status: Http status code
		Message: Success/Failure message
****

****
URL: /updatebytitle/{title},
Request type: PUT,
URL parameters: 
        title : String containing title of todo item to update,
PUT parameters:
     JSON object as string.
        title: new title 
		body: new body	,	
Output:
     JSON object as string.
	    Status: Http status code
		Message: Success/Failure message
****

****
URL: /markcompletedid/{todoid},
Request type: PUT,
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
URL: /markcompletedtitle/{title},
Request type: PUT,
URL parameters: 
        title : String containing id of todo item to mark as done,
PUT parameters:
        phnNumber : Phone number to send sms after marking item as done	,
Output:
     JSON object as string.
	    Status: Http status code
		Message: Success/Failure message
****

****
URL: /search/{query},
Request type: PUT,
URL parameters: 
        query : String containing text to be searched,
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

Sample output from TodoClient.java class
================================
Created todo id: {"body":"buy grocery","title":"test blog","todoid":3,"Message":"Created","Return Code":"201","Index Created":true}
get results: {"body":["updated body"],"title":["updated test blog"],"todoid":[1],"done":[true]}
get results by title: {"body":["buy grocery"],"title":["test blog"],"todoid":[3],"done":[false]}
get all results: {"body":["updated body","updated body","buy grocery"],"title":["updated test blog","updated test blog","test blog"],"todoid":[1,2,3],"done":[true,true,false]}
delete by id results: {"Status Code":"200","Status String":"Success"}
delete results: {"Status Code":"200","Status String":"Success"}
Created todo id: {"body":"buy grocery","title":"test blog","todoid":3,"Message":"Created","Return Code":"201","Index Created":true}
remove all: {"Status Code":"200","Status String":"Success"}
Created todo id: {"body":"buy grocery","title":"test blog","todoid":1,"Message":"Created","Return Code":"201","Index Created":true}
Created todo id: {"body":"buy grocery","title":"test blog","todoid":2,"Message":"Created","Return Code":"201","Index Created":true}
update by id result: {"StatusCode":"200","body":"updated body","title":"updated test blog","Status String":"Success"}
update res from title: {"StatusCode":"200","body":"updated body","title":"updated test blog","Status String":"Success"}
mark completed by id res: {"StatusCode":"200","done":true,"Status String":"Success"}
mark completed by title res: {"StatusCode":"200","done":true,"Status String":"Success"}
search results: {"_shards":{"total":1,"failed":0,"successful":1},"hits":{"total":2,"hits":[{"_type":"todo","_source":{"title":"{\"body\":\"buy grocery\",\"title\":\"test blog\",\"todoid\":1}"},"_id":"jA27pBNnRsqpFeFIuyqO9A","_index":"todoindex","_score":0.18579215},{"_type":"todo","_source":{"title":"{\"body\":\"buy grocery\",\"title\":\"test blog\",\"todoid\":2}"},"_id":"ggPzKwMpQ5m_zq4wNrchqg","_index":"todoindex","_score":0.18579215}],"max_score":0.18579215},"timed_out":false,"took":0}


