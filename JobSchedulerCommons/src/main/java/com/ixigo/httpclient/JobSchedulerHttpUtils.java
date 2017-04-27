package com.ixigo.httpclient;

import com.ixigo.constants.jobexecutor.RestURIConstants;
import com.ixigo.exception.InternalServerException;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.CommonExceptionCodes;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
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
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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
            throw new ServiceException(CommonExceptionCodes.HTTP_CLIENT_EXCEPTION.code(),
                    CommonExceptionCodes.HTTP_CLIENT_EXCEPTION.message() + "Unable to execute http request. Error: " + ue.getMessage());
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
        headers.put(RequestHeaders.CONTENT_TYPE, RestURIConstants.APPLICATION_JSON);
        headers.put(RequestHeaders.ACCEPT, RestURIConstants.APPLICATION_JSON);
        return headers;
    }

    public static String getBaseURI(String httpMode, String serverIP, String port, String uri) {
        return new StringBuilder()
                .append(httpMode.toString())
                .append(serverIP)
                .append(":")
                .append(port)
                .append(uri).toString();
    }

    public static <T, R> R processHttpRequest(String completeURL,
                                              Class<R> responseType,
                                              T request,
                                              HttpMethod method) throws IOException {
        return processHttpRequest(completeURL,
                responseType,
                request,
                null,
                method);
    }

    public static <T, R> R processHttpRequest(String completeURL,
                                              Class<R> responseType,
                                              T request,
                                              Map<String, String> headers,
                                              HttpMethod method) throws IOException {

        final Map<String, Object> parameters = getParams(request);
        try {
            return mapper.readValue(
                    processHttpRequest(completeURL,
                            parameters,
                            headers,
                            method),
                    responseType);
        } catch (JsonParseException | JsonMappingException je) {
            throw new ServiceException(CommonExceptionCodes.HTTP_CLIENT_EXCEPTION.code(),
                    "Could not parse response into specified response type. " + "Error: " + je);
        } catch (IOException ioe) {
            throw new InternalServerException();
        }
    }

    // general HTTP Sender
    public static String processHttpRequest(String completeURL,
                                            Map<String, Object> params,
                                            Map<String, String> customHeaders,
                                            HttpMethod method) throws IOException {

        Map<String, String> headers = getDefaultHeader();
        if (customHeaders != null) {
            headers.putAll(customHeaders);
        }

        HttpResponse<JsonNode> result = executeHttpMethod(completeURL, params, headers, method);
        if (result == null) {
            return null;
        }
        if (result.getStatus() != 200) {
            String exceptionResponse = result.getBody().toString();
            throw new ServiceException(CommonExceptionCodes.HTTP_CLIENT_EXCEPTION.code(),
                    CommonExceptionCodes.HTTP_CLIENT_EXCEPTION.message() + "Error: " + exceptionResponse);
        }
        return result.getBody().toString();
    }
}
