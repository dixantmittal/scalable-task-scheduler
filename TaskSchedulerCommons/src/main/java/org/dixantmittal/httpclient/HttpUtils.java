package org.dixantmittal.httpclient;

import org.dixantmittal.constants.jobexecutor.RestURIConstants;
import org.dixantmittal.exception.GenericException;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.exception.codes.CommonExceptionCodes;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class HttpUtils {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
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
            throw new GenericException(CommonExceptionCodes.HTTP_CLIENT_EXCEPTION.code(),
                    "Unable to execute http request. Error: " + ue);
        }
    }

    private static <T> Map<String, Object> getParams(T request) {
        Map<String, Object> map;
        map = mapper.convertValue(request, HashMap.class);
        return map;
    }

    private static Map<String, String> getDefaultHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put(RequestHeaders.CONTENT_TYPE, RestURIConstants.APPLICATION_JSON);
        headers.put(RequestHeaders.ACCEPT, RestURIConstants.APPLICATION_JSON);
        return headers;
    }

    public static <T, R> R processHttpRequest(String completeURL,
                                              Class<R> responseType,
                                              T request,
                                              HttpMethod method) {
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
                                              HttpMethod method) {

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
            throw new ServiceException((result.getStatus() + result.getStatusText()),
                    exceptionResponse);
        }
        return result.getBody().toString();
    }

    public static class URLBuilder {
        private String httpMode;
        private String serverIP;
        private String port;
        private String uri;

        private URLBuilder() {
            httpMode = "";
            serverIP = "";
            port = "";
            uri = "";
        }

        public static URLBuilder newURL() {
            return new URLBuilder();
        }

        public URLBuilder withServerIp(String serverIp) {
            serverIP = serverIp;
            return this;
        }

        public URLBuilder withServerPort(String port) {
            this.port = port;
            return this;
        }

        public URLBuilder withHttpMode(String httpMode) {
            this.httpMode = httpMode;
            return this;
        }

        public URLBuilder withURI(String uri) {
            this.uri = uri;
            return this;
        }

        public String build() {
            String serverAddress = serverIP;
            if (StringUtils.isNotBlank(port)) {
                serverAddress = serverAddress + ":" + port;
            }
            return new StringBuilder()
                    .append(httpMode)
                    .append(serverAddress)
                    .append(uri)
                    .toString();
        }
    }
}
