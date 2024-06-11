package util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * @author chenjujun
 * @date 11/23/20
 */
@Slf4j
public class ThirdRequest {

    private static final String APPLICATION_JSON = "application/json";

    private HttpRequest httpRequest;
    private String url;
    private String body;

    public ThirdRequest(String url) {
        this.httpRequest = new HttpRequest(url);
        this.url = url;
        this.httpRequest.timeout(60 * 1000);
    }


    public ThirdRequest method(Method method) {
        httpRequest = httpRequest.method(method);
        return this;
    }

    public ThirdRequest form(Map<String, Object> params) {
//        this.body = MapUtil.join(params, "&", "=", "");
        httpRequest = httpRequest.form(params);
        return this;
    }

    public ThirdRequest form(File file) {
        httpRequest = httpRequest.form("file", file);
        return this;
    }

    public ThirdRequest form2(String params) {
        httpRequest = httpRequest.form(params);
        return this;
    }

    public ThirdRequest body(String body) {
        if (StringUtils.isBlank(body)) {
            return this;
        }
        httpRequest = httpRequest.body(body, APPLICATION_JSON);
        this.body = body;
        return this;
    }

    public ThirdRequest body(String body, String contentType) {
        if (StringUtils.isBlank(body)) {
            return this;
        }
        httpRequest = httpRequest.body(body, contentType);
        this.body = body;
        return this;
    }

    public ThirdRequest addHeaders(Map<String, String> headersMap) {
        httpRequest = httpRequest.addHeaders(headersMap);
        return this;
    }

    public String execute() {
        if (log.isDebugEnabled()) {
            log.debug("发送报文，url={}，body={}", url, body);
        }

        String data = "";
        try {
            data = httpRequest.execute().body();
        } catch (IORuntimeException ex) {
            log.warn(ex.getMessage(), ex);
            throw new RuntimeException("请求超时", ex);
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
            throw new RuntimeException("第三方接口异常-" + ex.getMessage());
        }

        if (log.isDebugEnabled()) {
            log.debug("响应内容，data={}", data);
        }
        return data;
    }

    public HttpResponse executeResponse() {
        if (log.isDebugEnabled()) {
            log.debug("发送报文，url={}，body={}", url, body);
        }

        HttpResponse response = httpRequest.execute();

        if (log.isDebugEnabled()) {
            log.debug("响应内容，data={}", response.body());
        }
        return response;
    }
}
