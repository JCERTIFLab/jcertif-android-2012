package com.jcertif.android.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.jcertif.android.Application;

/**
 * REST client encapsulation.
 * The execution is done in a new thread
 * from : http://lukencode.com/2010/04/27/calling-web-services-in-android-using-httpclient/
 *
 */
public class RestClient extends AsyncTask<String, Void, String>
{
 
    private int responseCode;
    private String message;
 
    private String response;
 
    public String getResponse() {
        return response;
    }
 
    public String getErrorMessage() {
        return message;
    }
 
    public int getResponseCode() {
        return responseCode;
    }
 
    private void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();
        //force JSON format
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-type", "application/json");
 
        HttpResponse httpResponse;
 
        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
 
            HttpEntity entity = httpResponse.getEntity();
 
            if (entity != null) {
 
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
 
                // Closing the input stream will trigger connection release
                instream.close();
            }
 
        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }


    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e)
        {
        	throw new RuntimeException(e);
        } finally 
        {
            try {
                is.close();
            } catch (IOException e) 
            {
                Log.w(Application.NAME, "RestClient : Cannot close stream", e);
            }
        }
        return sb.toString();
    }

	@Override
	protected String doInBackground(String... url) {
		HttpGet request = new HttpGet(url[0]);
        executeRequest(request, url[0]);
		return response;
	}
}