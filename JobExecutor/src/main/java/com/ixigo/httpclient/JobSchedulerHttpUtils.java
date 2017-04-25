package com.ixigo.httpclient;

import com.ixigo.constants.RestURIConstants;
import com.ixigo.exception.ExceptionResponse;
import com.ixigo.exception.ServiceException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 18/04/17.
 */
@Slf4j
public class JobSchedulerHttpUtils {
    private static HttpResponse<JsonNode> executeHttpMethod(String baseURI,
                                                            Map<String, Object> parameters,
                                                            Map<String, String> headers,
                                                            HttpMethod method) throws ServiceException {
        try {
            switch (method) {
                case GET:
                    return Unirest.get(baseURI)
                            .headers(headers)
                            .queryString(parameters)
                            .asJson();
                case PUT:
                    return Unirest.put(baseURI)
                            .headers(headers)
                            .body(new JSONObject(parameters))
                            .asJson();
                case POST:
                    return Unirest.post(baseURI)
                            .headers(headers)
                            .body(new JSONObject(parameters))
                            .asJson();
                case DELETE:
                    return Unirest.delete(baseURI)
                            .headers(headers)
                            .queryString(parameters)
                            .asJson();
                default:
                    throw new UnsupportedOperationException(
                            "Server doesn't support http method: " + method);
            }
        } catch (UnirestException ue) {
            throw new UnsupportedOperationException(
                    "Server doesn't support http method: " + method);
        }
    }

    private static <T> Map<String, Object> getParams(T request) {
        Map<String, Object> map;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        map = mapper.convertValue(request, HashMap.class);
        return map;
    }

    private static Map<String, String> getDefaultHeader() {

        Map<String, String> headers = new HashMap<>();
        headers.put(RequestHeaders.CONTENT_TYPE.toString(),
                RestURIConstants.APPLICATION_JSON);
        headers.put(RequestHeaders.ACCEPT.toString(),
                RestURIConstants.APPLICATION_JSON);

        return headers;
    }

    public static String getBaseURI(HttpMode httpMode, String serverIP, String port, String uri) {
        return new StringBuilder()
                .append(httpMode.toString())
                .append(serverIP)
                .append(":")
                .append(port)
                .append(uri).toString();
    }

    public static <T, R> R processHttpRequest(String baseURI, Class<R> responseType, T request, HttpMethod method)
            throws IOException {

        final Map<String, String> headers = getDefaultHeader();
        final Map<String, Object> parameters = getParams(request);

        HttpResponse<JsonNode> result = executeHttpMethod(baseURI, parameters, headers, method);
        if (result == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (result.getStatus() != 200) {
            ExceptionResponse exceptionResponse = mapper.readValue(result.getBody().toString(), ExceptionResponse.class);
            throw new ServiceException(exceptionResponse.getCode(), exceptionResponse.getMessage());

        }
        return mapper.readValue(result.getBody().toString(), responseType);
    }

    // More general HTTP Sender
    public static String processHttpRequest(String baseURI, Map<String, Object> params, HttpMethod method)
            throws IOException {

        final Map<String, String> headers = getDefaultHeader();

        HttpResponse<JsonNode> result = executeHttpMethod(baseURI, params, headers, method);
        if (result == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (result.getStatus() != 200) {
            ExceptionResponse exceptionResponse = mapper.readValue(result.getBody().toString(), ExceptionResponse.class);
            throw new ServiceException(exceptionResponse.getCode(), exceptionResponse.getMessage());
        }
        return result.getBody().toString();
    }
}
