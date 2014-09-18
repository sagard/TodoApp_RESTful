/*
 * Desc - This used searchbox.io to index and search the data based on query passed
 * Author - Sagar Dhedia
 */
package com.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import io.searchbox.client.*;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;


public class SearchClient {
	//Name of the index and type of index
	public static final String TODO_INDEX = "todoindex";
	public static final String TODO_TYPE = "todo";

	//My api key from searchbox
	private final String SearchBoxUrl = "https://site:32d22401915b8cac404e9b3320bbdb49@bofur-us-east-1.searchly.com";

	private final JestClient searchClient;

	public SearchClient()
	{
		HttpClientConfig clientConfig =  new HttpClientConfig.Builder(SearchBoxUrl)
		.multiThreaded(true)
		.build();

		// Construct a new Jest client according to configuration via factory
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(clientConfig);
		this.searchClient = factory.getObject();
		try 
		{
			this.searchClient.execute(new CreateIndex.Builder("todoindex").build());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}


	}

	/*
	 * Desc - Create document index
	 * Input - Jsonobject as string that contains the todo item info
	 * Output - Boolean indicating if the index was created or not
	 */
	public boolean createIndex(String todo) throws Exception 
	{
		System.out.println("createindex start..");
		Map<String, String> source = new LinkedHashMap<String,String>();
		source.put("title", todo);
		// Creates the document index
		Index index = new Index.Builder(source).index(TODO_INDEX).type(TODO_TYPE).build();
		JestResult res = this.searchClient.execute(index);
		return res.isSucceeded();
	}

	/*
	 * Desc - Search for the query in index
	 * Input - String that contains query to be searched
	 * Output - String containing the results
	 */
	public String search(String query) throws Exception
	{
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "title^3", "body"));
		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(TODO_INDEX)
				.build();
		JestResult res = this.searchClient.execute(search);
		String todoItems= res.getJsonString().toString();
		return todoItems;
	}

	/*
	 * Desc - Search for the query in index
	 * Input - None
	 * Output - Returns all index
	 */
	public List<String> getAll() throws Exception {
		// just request *all* todos
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		Search search = new Search.Builder(searchSourceBuilder.toString())
		.addIndex(TODO_INDEX)
		.build();

		JestResult result = this.searchClient.execute(search);
		System.out.println("get all results:" + result.getJsonString());
		return result.getSourceAsObjectList(String.class);
	}

	/*
	 * Desc - Delete all index
	 * Input - None
	 * Output - Boolean indicating if indexes where deleted or not
	 */
	public boolean delete() throws Exception 
	{
		JestResult res = this.searchClient.execute(new Delete.Builder("")
							.index(TODO_INDEX)
							.type(TODO_TYPE)
							.build());
		return res.isSucceeded();
	}
}