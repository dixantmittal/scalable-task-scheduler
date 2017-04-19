package com.ixigo.httpclient;

import com.ixigo.common.core.exception.HttpTransportException;
import com.ixigo.common.core.transport.HttpSender;
import com.ixigo.constants.RestURIConstants;
import com.ixigo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 18/04/17.
 */
@Slf4j
public class JobSchedulerHttpUtils {
    private static HttpSender httpSender = HttpSender.newInstance();

    private static String executeHttpMethod(String baseURI,
                                            Map<String, String> parameters,
                                            Map<String, String> header,
                                            HttpMethod method) throws ServiceException, HttpTransportException {
        String result;
        switch (method) {
            case GET:
                result = httpSender.executeGet(baseURI, parameters, header);
                break;
            case PUT:
                result = httpSender.executePut(baseURI, createStringEntity(parameters), null, header);
                break;
            case POST:
                result = httpSender.executePostNotForm(baseURI, createStringEntity(parameters), null, header);
                break;
            default:
                throw new UnsupportedOperationException(
                        "Server doesn't support http method: " + method);
        }
        return result;
    }

    private static <T> Map<String, String> getParams(T request) {
        Map<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        map = (Map<String, String>) mapper.convertValue(request, HashMap.class);
        return map;
    }

    private static Map<String, String> getDefaultHeader() {

        Map<String, String> header = new HashMap<>();
        header.put(RequestHeaders.CONTENT_TYPE.toString(),
                RestURIConstants.APPLICATION_JSON);
        header.put(RequestHeaders.ACCEPT.toString(),
                RestURIConstants.APPLICATION_JSON);

        return header;
    }

    public static String getBaseURI(HttpMode httpMode, String serverIP, String port, String uri) {
        return new StringBuilder()
                .append(httpMode.toString())
                .append(serverIP)
                .append(":")
                .append(port)
                .append(uri).toString();
    }

    private static String createStringEntity(Map<String, String> params) {

        JSONObject keyArg = new JSONObject();
        for (Map.Entry<String, String> pEntry : params.entrySet()) {
            keyArg.put(pEntry.getKey(), pEntry.getValue());
        }
        try {
            return new String(keyArg.toJSONString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T, R> R processHttpRequest(String baseURI, Class<R> responseType, T request, HttpMethod method)
            throws HttpTransportException, IOException {

        final Map<String, String> headers = getDefaultHeader();
        final Map<String, String> parameters = getParams(request);

        String result = executeHttpMethod(baseURI, parameters, headers, method);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(result, responseType);
    }
}
