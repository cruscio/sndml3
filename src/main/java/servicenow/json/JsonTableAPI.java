package servicenow.json;

import servicenow.core.*;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.slf4j.Logger;

public class JsonTableAPI extends TableAPI {

	final URI uri;

	final private Logger logger = Log.logger(this.getClass());
	
	public JsonTableAPI(Table table) {
		super(table);
		String path = table.getName() + ".do?JSONv2";
		this.uri = session.getURI(path);
		logger.debug(Log.INIT, this.uri.toString());
	}

	public KeySet getKeys() throws IOException {
		return getKeys(null);
	}
		
	public KeySet getKeys(EncodedQuery query) throws IOException {
		Log.setMethodContext("getKeys");
		JSONObject requestObj = new JSONObject();
		requestObj.put("sysparm_action",  "getKeys");
		if (!EncodedQuery.isEmpty(query))
			requestObj.put("sysparm_query", query.toString());
		JSONObject responseObj = getResponseJSON(uri, HttpMethod.POST, requestObj);
		assert responseObj.has("records");
		KeySet keys = new KeySet(responseObj, "records");
		return keys;
	}

	public Record getRecord(Key sys_id) throws IOException {
		Log.setMethodContext("get");
		Parameters params = new Parameters();
		params.add("sysparm_action", "get");
		params.add("sysparm_sys_id",  sys_id.toString());
		JSONObject requestObj = params.toJSON();		
		JSONObject responseObj = getResponseJSON(uri, HttpMethod.POST, requestObj);
		assert responseObj.has("records");
		RecordList recs = new RecordList(table, responseObj, "records");
		assert recs != null;
		if (recs.size() == 0) return null;
		return recs.get(0);
	}

	public RecordList getRecords(KeySet keys, boolean displayValue) throws IOException {
		EncodedQuery query = keys.encodedQuery();
		return getRecords(query, displayValue);
	}
	
	public RecordList getRecords(EncodedQuery query, boolean displayValue) throws IOException {
		Parameters params = new Parameters();
		params.add("displayvalue", displayValue ? "all" : "false");
		if (!EncodedQuery.isEmpty(query))
			params.add("sysparm_query", query.toString());
		return getRecords(params);
	}

	public RecordList getRecords(Parameters params) throws IOException {
		Log.setMethodContext("getRecords");
		JSONObject requestObj = params.toJSON();
		requestObj.put("sysparm_action", "getRecords");
		JSONObject responseObj = getResponseJSON(uri, HttpMethod.POST, requestObj);
		assert responseObj.has("records");
		return new RecordList(table, responseObj, "records");
	}
		
	public InsertResponse insertRecord(Parameters fields) throws IOException {
		Log.setMethodContext("insert");
		JSONObject requestObj = fields.toJSON();
		requestObj.put("sysparm_action", "insert");
		JSONObject responseObj = getResponseJSON(uri, HttpMethod.POST, requestObj);
		RecordList list = new RecordList(table, responseObj, "records");
		assert list.size() > 0;
		assert list.size() == 1;
		return list.get(0);
	}
	
	public boolean deleteRecord(Key key) throws IOException {
		Log.setMethodContext("deleteRecord");
		JSONObject requestObj = new JSONObject(); 
		requestObj.put("sysparm_action", "deleteRecord");
		requestObj.put("sysparm_sys_id",  key.toString());
		JSONObject responseObj = getResponseJSON(uri, HttpMethod.POST, requestObj);
		RecordList list = new RecordList(table, responseObj, "records");
		if (list.size() == 0) return false;
		if (list.size() > 1) throw new JsonResponseException(responseObj);
		if (list.get(0).getKey().equals(key)) return true;
		throw new JsonResponseException(responseObj);
	}

	public void updateRecord(Key key, Parameters fields) throws IOException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("updateRecord");		
	}

	@Override
	public TableReader getDefaultReader() throws IOException {
		return new JsonKeyedReader(this.table);
	}

}
