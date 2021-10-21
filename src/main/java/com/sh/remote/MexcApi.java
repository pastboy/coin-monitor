package com.sh.remote;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sh.util.DingDingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MexcApi {

    public void queryAllSymbols() {
        try {
            String result = HttpUtil.get("https://www.mexc.com/open/api/v2/market/symbols", null, 10000);

            JSONObject jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("queryAllSymbols failed ", e);
        }
    }

    public void queryDepth(String symbol) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("symbol", symbol);
            params.put("depth", 10);
            String result = HttpUtil.get("https://www.mexc.com/open/api/v2/market/depth", params, 10000);

            JSONObject jsonObject = JSONObject.parseObject(result);

            if ("200".equalsIgnoreCase(jsonObject.getString("code"))) {
                JSONObject data = jsonObject.getJSONObject("data");

                JSONArray array = data.getJSONArray("bids");
                for (int i = 0; i < array.size() ; i++) {
                    Double price = array.getJSONObject(i).getDouble("price");
                    Double quantity = array.getJSONObject(i).getDouble("quantity");
                    if (price.compareTo(new Double(0.6)) > 0) {
                        DingDingUtil.sendMsg("交易对：" + symbol + "，价格：" + price);
                    }
                }
            }



        } catch (Exception e) {
            log.error("queryDepth failed ", e);
        }
    }
}
