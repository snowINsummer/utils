package qa.httpClient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import qa.exception.HTTPException;
import qa.utils.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by zhangli on 24/4/2017.
 */
@SuppressWarnings(value={"unchecked", "deprecation"})
public class HttpClientUtil {

    //连接超时时间
    public static final int CONNECT_TIMEOUT = 30000;
    //套接字超时时间10分钟
    public static final int SOCKET_TIMEOUT = 60000;
    //连接池中 连接请求执行被阻塞的超时时间
    public static final long CONN_MANAGER_TIMEOUT = 60000;

    private HttpClient httpClient = null;

    public HttpClientUtil(){
        httpClient = getOneHttpClient();
    }

    /**
     * GET请求，关闭连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executeGet(String url) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executeGetRequest(url,true,null);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * GET请求，关闭连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executeGetWithHeaders(String url, Map<String,String> mapHeaders) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executeGetRequest(url,true, mapHeaders);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * GET请求，保持连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executeGetKeepConn(String url) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executeGetRequest(url,false,null);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * GET请求，保持连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executeGetKeepConnWithHeaders(String url, Map<String,String> mapHeaders) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executeGetRequest(url,false, mapHeaders);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * HttpGet
     * @param url
     * @param doesShutdown
     * @param mapHeader
     * @return
     * @throws HTTPException
     */
    private ResponseInfo executeGetRequest(String url, boolean doesShutdown, Map<String,String> mapHeader) throws HTTPException {
        HttpGet httpGet = new HttpGet(url);
        if (null != mapHeader){
            for(Map.Entry<String,String> entry : mapHeader.entrySet()){
                httpGet.setHeader(entry.getKey(),entry.getValue());
            }
        }
        return getHttpResponse(httpGet, doesShutdown);
    }


    /**
     * PUT请求，关闭连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePut(String url,String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePutRequest(url,true,null, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * PUT请求，关闭连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePutWithHeaders(String url, Map<String,String> mapHeaders,String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePutRequest(url,true, mapHeaders, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * PUT请求，保持连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePutKeepConn(String url,String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePutRequest(url,false,null, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * PUT请求，保持连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePutKeepConnWithHeaders(String url, Map<String,String> mapHeaders,String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePutRequest(url,false, mapHeaders, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * HttpPut
     * @param url
     * @param doesShutdown
     * @param mapHeader
     * @return
     * @throws HTTPException
     */
    private ResponseInfo executePutRequest(String url, boolean doesShutdown, Map<String,String> mapHeader,String json) throws HTTPException {
        HttpPut httpPut = new HttpPut(url);
        if (null != mapHeader){
            for(Map.Entry<String,String> entry : mapHeader.entrySet()){
                httpPut.setHeader(entry.getKey(),entry.getValue());
            }
        }
        if (null != json && !StringUtil.isEmpty(json)){
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            httpPut.setEntity(se);
        }
        return getHttpResponse(httpPut, doesShutdown);
    }


    /**
     * PATCH请求，关闭连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePatch(String url, String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePatchRequest(url,true,null,json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * PATCH请求，关闭连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePatchWithHeaders(String url, Map<String,String> mapHeaders,String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePatchRequest(url,true, mapHeaders,json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * PATCH请求，保持连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePatchKeepConn(String url, String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePatchRequest(url,false,null, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * PATCH请求，保持连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePatchKeepConnWithHeaders(String url, Map<String,String> mapHeaders, String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePatchRequest(url,false, mapHeaders,json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * HttpPatch
     * @param url
     * @param doesShutdown
     * @param mapHeader
     * @return
     * @throws HTTPException
     */
    private ResponseInfo executePatchRequest(String url, boolean doesShutdown, Map<String,String> mapHeader, String json) throws HTTPException {
        HttpPatch httpPatch = new HttpPatch(url);
        if (null != mapHeader){
            for(Map.Entry<String,String> entry : mapHeader.entrySet()){
                httpPatch.setHeader(entry.getKey(),entry.getValue());
            }
        }
        if (null != json && !StringUtil.isEmpty(json)){
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            httpPatch.setEntity(se);
        }
        return getHttpResponse(httpPatch, doesShutdown);
    }


    /**
     * POST请求，关闭连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePost(String url, String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePostRequest(url,true,null, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * POST请求，关闭连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePostWithHeaders(String url, Map<String,String> mapHeaders, String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePostRequest(url,true, mapHeaders, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;

    }

    /**
     * POST请求，保持连接，无headers
     * @param url
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePostKeepConn(String url, String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePostRequest(url,false,null, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * POST请求，保持连接，有headers
     * @param url
     * @param mapHeaders
     * @return
     * @throws HTTPException
     */
    public ResponseInfo executePostKeepConnWithHeaders(String url, Map<String,String> mapHeaders, String json) throws HTTPException {
        long startTime = System.currentTimeMillis();
        ResponseInfo responseInfo = executePostRequest(url,false, mapHeaders, json);
        long endTime = System.currentTimeMillis();
        long executeTime = (endTime-startTime);
        responseInfo.setTime(executeTime);
        return responseInfo;
    }

    /**
     * HttpPost
     * @param url
     * @param doesShutdown
     * @param mapHeader
     * @param json
     * @return
     * @throws HTTPException
     */
    private ResponseInfo executePostRequest(String url, boolean doesShutdown, Map<String, String> mapHeader, String json) throws HTTPException {
        HttpPost httpPost = new HttpPost(url);
        if (null != mapHeader){
            for(Map.Entry<String,String> entry : mapHeader.entrySet()){
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        if (null != json && !StringUtil.isEmpty(json)){
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            httpPost.setEntity(se);
        }
        return getHttpResponse(httpPost, doesShutdown);
    }


    public ResponseInfo getHttpResponse(HttpRequestBase httpRequest, boolean doesShutdown) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
            if(200 == statusCode) {
                String allHtml = readHtmlContentFromEntity(httpResponse.getEntity());
                responseInfo.setContent(allHtml);
            }else{
                responseInfo.setContent(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HTTPException(e.getMessage());
        }finally{
            if(httpRequest!=null){
                httpRequest.releaseConnection();
                httpRequest.abort();
            }
            if(httpClient!=null){
                if (doesShutdown){
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }

    public void termination(){
        if(httpClient!=null){
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**

     * 从response返回的实体中读取页面代码

     * @param httpEntity Http实体

     * @return 页面代码

     * @throws ParseException

     * @throws IOException
     */
    private String readHtmlContentFromEntity(HttpEntity httpEntity) throws ParseException, IOException {
        if(httpEntity==null){
            return null;
        }
        String html = "";

        Header header = httpEntity.getContentEncoding();

        if(httpEntity.getContentLength() < 2147483647L){            //EntityUtils无法处理ContentLength超过2147483647L的Entity
            if(header != null && "gzip".equals(header.getValue())){
                html = EntityUtils.toString(new GzipDecompressingEntity(httpEntity), HTTP.UTF_8);
            } else {
                html = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            }

        } else {
            InputStream in = httpEntity.getContent();
            if(header != null && "gzip".equals(header.getValue())){
                html = unZip(in, ContentType.getOrDefault(httpEntity).getCharset().toString());
            } else {
                html = readInStreamToString(in, ContentType.getOrDefault(httpEntity).getCharset().toString());
            }
            if(in != null){
                in.close();
            }

        }
        return html;
    }

    /**

     * 解压服务器返回的gzip

     * @param in 抓取返回的InputStream

     * @param charSet 页面内容编码

     * @return 页面内容的String格式

     * @throws IOException

     */

    private String unZip(InputStream in, String charSet) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        GZIPInputStream gis = null;

        try {

            gis = new GZIPInputStream(in);

            byte[] _byte = new byte[1024];

            int len = 0;

            while ((len = gis.read(_byte)) != -1) {

                baos.write(_byte, 0, len);

            }

            String unzipString = new String(baos.toByteArray(), charSet);

            return unzipString;

        } finally {

            if (gis != null) {

                gis.close();

            }

            if(baos != null){

                baos.close();

            }

        }

    }
    /**

     * 读取InputStream

     * @param in InputStream

     * @return 从流中读取的String

     * @throws IOException

     */

    private String readInStreamToString(InputStream in, String charSet) throws IOException {

        StringBuilder str = new StringBuilder();

        String line;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charSet));

        while((line = bufferedReader.readLine()) != null){

            str.append(line);

            str.append("\n");

        }

        if(bufferedReader != null) {

            bufferedReader.close();

        }

        return str.toString();

    }


    private synchronized HttpClient getOneHttpClient(){
        HttpParams parentParams = new BasicHttpParams();
        parentParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        parentParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);

        parentParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);

        parentParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
        parentParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        parentParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);

        List<BasicHeader> collection = new ArrayList<>();
        collection.add(new BasicHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"));
        collection.add(new BasicHeader("Accept", "text/html,application/json,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        collection.add(new BasicHeader("Accept-Language", "zh-cn,zh,en-US,en;q=0.5"));
        collection.add(new BasicHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7"));
        collection.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
        parentParams.setParameter(ClientPNames.DEFAULT_HEADERS, collection);
        DefaultHttpClient httpClient = new DefaultHttpClient(parentParams);
        httpClient.getCookieStore().getCookies();
        return httpClient;
    }
}
