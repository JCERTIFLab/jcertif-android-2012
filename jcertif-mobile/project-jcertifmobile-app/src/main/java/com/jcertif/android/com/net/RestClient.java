package com.jcertif.android.com.net;

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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.jcertif.android.JCApplication;

/**
 * REST client encapsulation
 * from : http://lukencode.com/2010/04/27/calling-web-services-in-android-using-httpclient/
 * @author Yakhya DABO
 *
 */
public class RestClient 
{
	//----
	// Enum
	//----
	public enum RequestMethod
	{
		GET,
		POST
	}
	
	//----
	// Attributes
	//----
	
    private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;

    private String url;

    private int responseCode;
    private String message;

    private String response;

    private HttpContext localContext;
    
    /**
     * Constructor
     * @param url
     */
	public RestClient(String url){
    	params = new ArrayList<NameValuePair>();
    	headers = new ArrayList<NameValuePair>();
    	this.url = url;
	}

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	//-------------------------------------------------------------------------
    //
    // Public interface
    //
    //-------------------------------------------------------------------------
	
    public void AddParam(String name, String value){
        params.add(new BasicNameValuePair(name, value));
    }


    
    public void AddHeader(String name, String value){
        headers.add(new BasicNameValuePair(name, value));
    }
    
    /**
     * added by Yakhya.DABO to allow url extension
     * @param path
     */
    
    public void appendPath(String path){
    	url +="/"+path;
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
    
    public void Execute(RequestMethod method) throws Exception
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
        
        switch(method) {
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

    //-------------------------------------------------------------------------
    //
    // Internal methods
    //
    //-------------------------------------------------------------------------
    private void executeRequest(HttpUriRequest request, String url)
    {
    	Log.e(this.getClass().getSimpleName(), "Calling WS : " + request.getURI());
       
    	 HttpClient client = new DefaultHttpClient();
         //force JSON format
         request.addHeader("Accept", "application/json");
         request.addHeader("Content-type", "application/json");
         
        HttpResponse httpResponse;

        try {
            
        	
            httpResponse = client.execute(request,localContext);
            
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            Log.i("RestClient", "http Status : " + responseCode);
            
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                
                // Closing the input stream will trigger connection release
                instream.close();
            }
            
        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            throw new RuntimeException(e);
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            throw new RuntimeException(e);
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
                Log.w("RestClient", "RestClient : Cannot close stream", e);
            }
        }
        return sb.toString();
    }
}