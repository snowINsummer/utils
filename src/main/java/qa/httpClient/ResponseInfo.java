package qa.httpClient;

import java.util.Map;

/**
 * Created by snow.zhang on 2015/9/15.
 */
public class ResponseInfo {


    /**
     * HTTP Status Code
     */
    private int status = -1;
    /**
     * Response Content
     */
    private String content = "";
    /**
     * Execute Time
     * second
     */
    private double time = -1;

    private Map<String, String> headers;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public double getTime() {
        return time;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
