package femi.core.utils.helpers.http;

import femi.core.utils.SSLUtils;
import femi.core.utils.report.AllureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private int TIMEOUT = 360000;
    private int READ_TIMEOUT = 360000;

    static Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    public String sendHTTPSPostRequestAndGetResponse(String url, String body, Headers headers) throws IOException {
        LOGGER.info("END POINT: " + url);
        SSLUtils.trustAllHostNames();
        SSLUtils.trustAllHttpsCertificates();
        StringBuilder response = new StringBuilder();
        HttpsURLConnection conn = null;
        try{
            URL apiUrl = new URL(url);
            conn = (HttpsURLConnection)apiUrl.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            addHeadersIntoRequest(conn,headers);

            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
    } catch (IOException e) {
            AllureHelper.logRequestResponseAsXML("Response:", "Failed to get response due to error: " + e.toString());
            throw new IOException(e.getMessage() + " " + url);
    }finally{
            if(conn != null){
                conn.disconnect();
            }
        }
    return response.toString();
    }

    public String sendHTTPPostRequestAndGetResponse(String url, String body, Headers headers) throws IOException {
        LOGGER.info("END POINT: " + url);
        SSLUtils.trustAllHostNames();
        SSLUtils.trustAllHttpsCertificates();
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL apiUrl = new URL(url);
            conn = (HttpURLConnection)apiUrl.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            addHeadersIntoRequest(conn,headers);

            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
        } catch (IOException e) {
            AllureHelper.logRequestResponseAsXML("Response:", "Failed to get response due to error: " + e.toString());
            throw new IOException(e.getMessage() + " " + url);
        }finally{
            if(conn != null){
                conn.disconnect();
            }
        }
        return response.toString();
    }

    private static void addHeadersIntoRequest(HttpURLConnection connection, Headers headers){
        headers.forEach((k,v)-> connection.setRequestProperty(k.toString() ,v.toString()));
    }
}
