/*
 * Copyright (c)
 * Sree  Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Http;

import java.util.HashMap;
import java.util.Map;


public class RequestParams {
    
    private String uri;
    private String method = "GET";
    private Map<String, String> params = new HashMap<>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value){

        params.put(key, value);

    }
}
