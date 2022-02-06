package io.itpl.apilab.data;

public class ApiResponse {
    private int status;
    private Object payload;
    private int responseTime;
    private String message;
    private long start;
    private long finish;
    public static ApiResponse init(){
        ApiResponse response = new ApiResponse();
        response.start = System.currentTimeMillis();
        return response;
    }
    public void submit(Object payload){
        this.finish =  System.currentTimeMillis();
        this.payload = payload;
        this.responseTime = (int) (finish-start);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
