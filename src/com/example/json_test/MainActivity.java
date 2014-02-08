package com.example.json_test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void runTest(View view) {
		EditText to = (EditText) findViewById(R.id.to);
    	EditText from = (EditText) findViewById(R.id.from);
    	    	
    	String request = new HttpUtil(this, to.getText().toString(), from.getText().toString()).getHttp();
    	
    	Logger.getLogger(Logger.class.getName()).log(Level.INFO, request);
    	Toast.makeText(getApplicationContext(), "Calculating Route" , Toast.LENGTH_LONG).show();
    	
    	JSONHelper json = new JSONHelper();
    	//json.execute("http://open.mapquestapi.com/guidance/v1/route?key=Fmjtd%7Cluur29a2ng%2Cax%3Do5-90z0qa&from=santa%20cruz,%20ca&to=san%20diego,%20ca&narrativeType=text&fishbone=false");
    	json.execute(request);
	}
	
	class JSONHelper extends AsyncTask<String, String, String> {

	    @Override
	    protected String doInBackground(String... uri) {
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        
	        //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 3000);
	        //HttpConnectionParams.setSoTimeout(httpclient.getParams(), 10000);
	        
	        String responseString = null;
	        try {
	            response = httpclient.execute(new HttpGet(uri[0]));
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException ex) {
	        	Logger.getLogger(Logger.class.getName()).log(Level.SEVERE,ex.getMessage());
	        } catch (IOException ex) {
	        	Logger.getLogger(Logger.class.getName()).log(Level.SEVERE,ex.getMessage());
	        }
	        return responseString;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	//super.onPostExecute(result);
	    	
	    	//OGS returns some messy JSON so we need to clean it up first
	    	result = result.split("\\(")[1];
	    	result = result.split("\\)")[0];
	    	
	    	JsonParser parser = new JsonParser();
	    	JsonObject obj = parser.parse(result).getAsJsonObject();
	    	
	    	Gson gson = new Gson();
	    	GuidanceData data = gson.fromJson(obj.get("guidance"), GuidanceData.class);
	    	
	    	EditText respField = (EditText) findViewById(R.id.JSONfield);
	    	//respField.setText("success!");
	    	//respField.setText(route.toString());
	    	respField.setText(data.toString());
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
