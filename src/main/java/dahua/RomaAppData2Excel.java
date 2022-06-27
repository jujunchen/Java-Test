package dahua;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.ThirdRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjujun
 * @date 2021/10/9
 */
public class RomaAppData2Excel {

    public static void main(String[] args) {

        String appListUrl = "https://roma-suse.gtdreamlife.com/roma/api/default/appcenter/apps";
        String appSecurityUrl = "https://roma-suse.gtdreamlife.com/roma/api/default/appcenter/appkeys";

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Cookie", "JSESSIONID=69FD974CC86FBC32533048868A582CA76103FC54A13CA213; _ROMA_LANG=zh-CN; _ROMA_SSO_SID=795DCD4ED2A1BDE31A89D44CCE1BFFB6; csrf_header=X-CSRF-TOKEN; csrf_param=_csrf; csrf_token=C145EE8579774CBF2C127E1DA095E79DA06C878278C92C1A; org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=zh-CN; isLastLoginDateShown=true");
        headersMap.put("X-CSRF-TOKEN", "C145EE8579774CBF2C127E1DA095E79DA06C878278C92C1A");
        String appListRes = new ThirdRequest(appListUrl).addHeaders(headersMap).body("{\"pageNum\":1,\"pageSize\":200}").execute();
        List<InnerAppList> appList = JSON.parseArray(JSON.parseObject(appListRes).getJSONArray("list").toJSONString(), InnerAppList.class);
        for (InnerAppList innerApp : appList) {
            JSONObject appSecurityQuery = JSON.parseObject("{\"appOid\":116,\"encrypted\":\"false\"}");
            appSecurityQuery.put("appOid", innerApp.getAppOid());
            appListRes = new ThirdRequest(appSecurityUrl).addHeaders(headersMap).body(appSecurityQuery.toJSONString()).execute();
            String appToken = JSON.parseObject(appListRes).getJSONObject("appKeys").getString("appToken");
            innerApp.setAppToken(appToken);
        }
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> firstMap = new HashMap<>();
        firstMap.put("app_key", "");
        firstMap.put("app_name", "");
        firstMap.put("app_secret", "");
        String sheet = "sheet1";
        for (InnerAppList innerApp : appList) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("app_key", innerApp.getAppId());
            dataMap.put("app_name", innerApp.getAppName());
            dataMap.put("app_secret", innerApp.getAppToken());
            dataList.add(dataMap);
        }
        ExcelWriter excelWriter = new ExcelWriter("/Users/chenjujun/Desktop/roma应用.xlsx",sheet);
        excelWriter.write(dataList);
        excelWriter.flush();
    }

    /**
     * InnerRomaAppData2Excel
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InnerAppList {
        private String appId;
        private String appOid;
        private String appName;
        private String appToken;
    }
}
