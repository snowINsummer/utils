package qa.httpClient;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import qa.exception.HTTPException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


/**

 * http连接、抓取管理类

 */
@SuppressWarnings(value={"unchecked", "deprecation"})
public class HttpClientUtilTemp {
    
    //连接池里的最大连接数
    public static final int MAX_TOTAL_CONNECTIONS = 100;
    //每个路由的默认最大连接数
    public static final int MAX_ROUTE_CONNECTIONS = 50;
    //连接超时时间
    public static final int CONNECT_TIMEOUT = 30000;
    //套接字超时时�?10分钟
    public static final int SOCKET_TIMEOUT = 60000;
    //连接池中 连接请求执行被阻塞的超时时间
    public static final long CONN_MANAGER_TIMEOUT = 60000;

    private HttpClient httpClient = null;

    public HttpClientUtilTemp(){
        httpClient = (HttpClient)getOneHttpClient();
    }

    public ResponseInfo executeGet(String url) throws HTTPException {
        return executeGet(url,true);
    }

    public ResponseInfo executeGet(String url,boolean doesShutdown) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
//        HttpClient httpClient = null;
        HttpGet httpGet = new HttpGet(url);
//        httpGet.setHeader("Cookie", "JSESSIONID=EE57BF8D9A732E6DE36DC59367CF1D2F-n2.a");
//        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
        	
        	httpResponse=httpClient.execute(httpGet);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
//			System.out.println(HttpClientUtil.readHtmlContentFromEntity(httpGet.getEntity()));
//            System.out.println(statusCode);
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
            if(httpGet!=null){
                httpGet.releaseConnection();
                httpGet.abort();
            }
            if(httpClient!=null){
                if (doesShutdown){
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }
    public ResponseInfo executeGetsetJson(String url) throws HTTPException {
        return executeGet(url,true);
    }
    public ResponseInfo executeGetsetJson(String url,boolean doesShutdown) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
//        HttpClient httpClient = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");

//        httpGet.setHeader("Cookie", "JSESSIONID=EE57BF8D9A732E6DE36DC59367CF1D2F-n2.a");
//        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
//			System.out.println(HttpClientUtil.readHtmlContentFromEntity(httpGet.getEntity()));
//            System.out.println(statusCode);
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
            if(httpGet!=null){
                httpGet.releaseConnection();
                httpGet.abort();
            }
            if(httpClient!=null){
                if (doesShutdown){
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }

    public ResponseInfo executeGetsetJson(String url,boolean doesShutdown, String cookies) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
//        HttpClient httpClient = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Cookie", "JSESSIONID="+cookies);
//        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
//			System.out.println(HttpClientUtil.readHtmlContentFromEntity(httpGet.getEntity()));
//            System.out.println(statusCode);
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
            if(httpGet!=null){
                httpGet.releaseConnection();
                httpGet.abort();
            }
            if(httpClient!=null){
                if (doesShutdown){
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }

    /**
     * 定义header,当前方法，只给EVC用
     */
    public ResponseInfo executePostDefine(String url,String Ev_A) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        //设置头信息
        httpPost.setHeader("Ev-A",Ev_A);
//        httpPost.setHeader("Content-Type", "application/json");
        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
            if(200 == statusCode) {
                String allHtml = readHtmlContentFromEntity(httpResponse.getEntity());
//                System.out.println(allHtml);
                responseInfo.setContent(allHtml);
            }else{
                responseInfo.setContent(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HTTPException(e.getMessage());
        }finally{
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                httpClient.getConnectionManager().shutdown();
            }
        }
        return responseInfo;
    }

    public ResponseInfo executeGetDefine(String url,boolean doesShutdown) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
//        HttpClient httpClient = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("EV-A", "3");

//        httpGet.setHeader("Cookie", "JSESSIONID=EE57BF8D9A732E6DE36DC59367CF1D2F-n2.a");
//        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
//			System.out.println(HttpClientUtil.readHtmlContentFromEntity(httpGet.getEntity()));
//            System.out.println(statusCode);
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
            if(httpGet!=null){
                httpGet.releaseConnection();
                httpGet.abort();
            }
            if(httpClient!=null){
                if (doesShutdown){
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }

    
    public ResponseInfo executePost(String url) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
            if(200 != statusCode) {
                String allHtml = readHtmlContentFromEntity(httpResponse.getEntity());
//                System.out.println(allHtml);
                responseInfo.setContent(allHtml);
            }else{
                responseInfo.setContent(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HTTPException(e.getMessage());
        }finally{
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                httpClient.getConnectionManager().shutdown();
            }
        }
        return responseInfo;
    }

    public ResponseInfo executePost(String url, String jSon) throws HTTPException {
        return executePost(url, jSon, true);
    }

    public ResponseInfo executePost(String url, String jSon, boolean doesShutdown) throws HTTPException {
        //        url = "http://sd00014.envisioncn.com:8080/rating/ws/rest/doc/computeCount";
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
//        httpPost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
        httpPost.setHeader("Content-Type", "application/json");
//        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        HttpResponse httpResponse=null;
        try {
            StringEntity se = new StringEntity(jSon, HTTP.UTF_8);
            httpPost.setEntity(se);
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
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
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                if (doesShutdown) {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }

    /**
     * 用于/doc/uploadFile 测试
     * @param url
     * @param map
     * @param file
     * @param doesShutdown
     * @return
     */
    public ResponseInfo executePost(String url,String cookie, HashMap<String,Object> map, File file, boolean doesShutdown) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("eos_auth", cookie);
        HttpResponse httpResponse=null;
        try {

            MultipartEntity entity = new MultipartEntity();
            for(Map.Entry entry : map.entrySet()){
                String key = entry.getKey().toString();
                entity.addPart(key, new StringBody(entry.getValue().toString(),ContentType.create("text/plain")));
            }
            if (null != file){
                entity.addPart("file", new FileBody(file));
            }
//            StringEntity se = new StringEntity(jSon, HTTP.UTF_8);
            httpPost.setEntity(entity);
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
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
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                if (doesShutdown) {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }


    /**
     * 用于/doc/uploadFile 测试
     * @param url
     * @param map
     * @param file
     * @param doesShutdown
     * @return
     */
    public ResponseInfo executePost(String url, HashMap<String,Object> map, File file, boolean doesShutdown) throws HTTPException {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Content-Type", "application/json");
        HttpResponse httpResponse=null;
        try {

            MultipartEntity entity = new MultipartEntity();
            for(Map.Entry entry : map.entrySet()){
                String key = entry.getKey().toString();
                    entity.addPart(key, new StringBody(entry.getValue().toString(),ContentType.create("text/plain")));
            }
            entity.addPart("file", new FileBody(file));

//            StringEntity se = new StringEntity(jSon, HTTP.UTF_8);
            httpPost.setEntity(entity);
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
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
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                if (doesShutdown) {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return responseInfo;
    }

    /**
     * 带cookie的方式执行post请求
     * @param url
     * @param cookie
     * @param jSon
     * @return
     */
    public ResponseInfo executePost(String url, String cookie, String jSon) throws HTTPException {
        //        url = "http://sd00014.envisioncn.com:8080/rating/ws/rest/doc/computeCount";
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
//        httpPost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
        httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpPost.addHeader("Connection", "keep-alive");
        httpPost.addHeader("content-type", "application/json");
        httpPost.addHeader("eos_auth", cookie);
        httpPost.addHeader("accept", "application/json");
        httpPost.addHeader("Cache-Control", "no-cache");
//        httpPost.addHeader("Referer", "https://eosintegration.envisioncn.com/energyos/stationconfig.html?locale=zh-CN");
        HttpResponse httpResponse=null;
        try {
            StringEntity se = new StringEntity(jSon, HTTP.UTF_8);
            httpPost.setEntity(se);
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
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
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                httpClient.getConnectionManager().shutdown();
            }
        }
        return responseInfo;
    }


    private synchronized HttpClient getOneHttpClient(){
    	 HttpParams parentParams = new BasicHttpParams(); 
    	 parentParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
         parentParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);

         parentParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);

         parentParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
         parentParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
         parentParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);
         //设置头信�?模拟浏览�?

         List<BasicHeader> collection = new ArrayList<BasicHeader>();
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
                html = EntityUtils.toString(new GzipDecompressingEntity(httpEntity),HTTP.UTF_8);
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

     * 解压服务器返回的gzip浿

     * @param in 抓取返回的InputStream浿

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

     * 读取InputStream�?

     * @param in InputStream�?

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
    
    public HttpClient getHttpClient(){
    	return httpClient;
    }
    

}