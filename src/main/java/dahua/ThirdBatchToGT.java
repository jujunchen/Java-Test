package dahua;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * JSON 转 Excel
 * @author chenjujun
 * @date 12/8/20
 */
public class ThirdBatchToGT {

    public static void main(String[] args) throws InterruptedException {
        ExcelReader excelReader = new ExcelReader("/Users/chenjujun/绿城资料/项目资料/衢州斗潭/邻礼通/望江苑小区.xlsx", 0);
        List<Map<String, Object>> excelData = excelReader.readAll();
        System.out.printf("一共%s条数据\n", excelData.size());

        for (Map<String, Object> excelDatum : excelData) {
            ThirdSpaceAddVO thirdSpaceAddVO = new ThirdSpaceAddVO();
            thirdSpaceAddVO.setProjectCode("wlsq03");
            thirdSpaceAddVO.setCommunityName("望江苑");
            thirdSpaceAddVO.setThirdCommunityId("1000498");
            thirdSpaceAddVO.setBuild(excelDatum.get("blocks").toString());
            thirdSpaceAddVO.setUnit(excelDatum.get("units").toString());
            thirdSpaceAddVO.setHouse(excelDatum.get("rooms").toString());
            thirdSpaceAddVO.setThirdHouseId(excelDatum.get("id").toString());
            thirdSpaceAddVO.setThirdSource("LLT");
            String body = JSON.toJSONString(thirdSpaceAddVO);
            HttpRequest httpRequest = HttpRequest.post("https://opengw.gtdreamlife.com/open/inner/third/space/add");
            httpRequest.header("GreenShop-ID", "iocApp");
            httpRequest.header("GreenShop-APPKEY", "Apk+JHrJorAOjsGYE2mb7w==");

            String result = httpRequest.body(body).execute().body();
            System.out.println(result);
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }

    @Data
    static class ThirdSpaceAddVO {
        /**
             * 项目名称
             */
            private String projectName;

            /**
             * 项目编号
             */
            private String projectCode;

            /**
             * 园区名称
             */
            private String communityName;

            /**
             * 园区id
             */
            private String thirdCommunityId;

            /**
             * 幢
             */
            private String build;

            /**
             * 幢id
             */
            private String thirdBuildId;

            /**
             * 单元
             */
            private String unit;

            /**
             * 单元id
             */
            private String thirdUnitId;

            /**
             * 室
             */
            private String house;

            /**
             * 第三方房屋id
             */
            private String thirdHouseId;

            /**
             * 空间来源
             */
            private String thirdSource; 
    }

}


