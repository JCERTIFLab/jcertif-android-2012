package com.jcertif.android.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

import com.jcertif.android.app.Application;


/**
 * 
 * REST client encapsulation.
 * The execution is done in a new thread
 * from : http://lukencode.com/2010/04/27/calling-web-services-in-android-using-httpclient/
 * 
 * @author Yakhya DABO
 *
 */
public class RestClient1 extends AsyncTask<Void, Void, String>
{
 
	//----
	// Enum
	//----
	public enum RequestMethod
	{
		GET,
		POST
	}
	
    private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;
    
    private int responseCode;
    private String message;
    private String url;
    private String response;
    private RequestMethod requestMethod = RequestMethod.GET;   

	public RestClient1(String url){
    	params = new ArrayList<NameValuePair>();
    	headers = new ArrayList<NameValuePair>();
    	this.url = url;
	}
	
	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}
	
    public String getResponse() {
        return response;
    }
 
    public String getErrorMessage() {
        return message;
    }
 
    public int getResponseCode() {
        return responseCode;
    }
 
    public void Execute() throws Exception
    {
    	//add parameters
        String combinedParams = "";
        if(!params.isEmpty()){
            combinedParams += "?";
            for(NameValuePair p : params)
            {
                String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                if(combinedParams.length() > 1)
                {
                    combinedParams  +=  "&" + paramString;
                }
                else
                {
                    combinedParams += paramString;
                }
            }
        }
        
        switch(requestMethod) {
            case GET:
            {
                HttpGet request = new HttpGet(url + combinedParams);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                executeRequest(request, url);
                break;
            }
            case POST:
            {
                HttpPost request = new HttpPost(url + combinedParams);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }
                
                executeRequest(request, url);
                break;
            }
        }
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
	protected String doInBackground(Void... url) {
		//HttpGet request = new HttpGet(url[0]);
        // executeRequest(request, url[0]);
		try {
			Execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
}