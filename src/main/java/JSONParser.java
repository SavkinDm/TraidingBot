import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static String json1 = "";
    static JSONObject Objec = null;
    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        // Making HTTP request
        try {
            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {

        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {

        }

        // return JSON String
        return jObj;

    }

    public JSONObject httpsConnection(String url, List<NameValuePair> params) throws IOException {

        URL u = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
        String query="";
        ///
        for (int i=0; i<params.size(); i++){
            if (i>0){
                query +="&";
            }
            query += params.get(i).getName()+"="+ URLEncoder.encode(params.get(i).getValue(),"UTF-8");

        }
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-length", String.valueOf(query.length()));
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        DataOutputStream output = new DataOutputStream(conn.getOutputStream());
        output.writeBytes(query);
        output.close();
        ///


        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String inputLine;
        StringBuilder sb1 = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            sb1.append(inputLine + "\n");
        }
        json1 = sb1.toString();
        br.close();
        isr.close();
        is.close();
        conn.disconnect();

        try {
            Objec = new JSONObject(json1);
        } catch (JSONException e) { }

    return Objec;
    }

}


