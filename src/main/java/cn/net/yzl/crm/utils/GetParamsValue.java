package cn.net.yzl.crm.utils;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 将参数字段,转化为对应的描述
 * <p>
 * Author:刘发冠
 * Date:2019/12/17 10:18
 */
public class GetParamsValue {


    public static Map<String,Object> changeAllFieldResult(Map<String,Object> validateMap,Map<String,String> paramsMap) {
        Map<String,Object> rMap = new HashMap<>(validateMap);
        String msg = "";
        String msgs = String.valueOf(rMap.get("msg"));
        if (StringUtils.isNotEmpty(msgs) && msgs.indexOf(":") > -1) {
            String[] checkMsg = msgs.split(":");
            if (checkMsg.length >1 && checkMsg[1].length() >0 && checkMsg[1].indexOf(" ") > -1) {
                String[] keys = checkMsg[1].split(" ");
                for (String key : keys) {
                    msg += paramsMap.get(key) + " ";
                }
            } else {
                msg += paramsMap.get(checkMsg[1]);
            }
            rMap.put("msg",checkMsg[0]+":'"+msg+"'");
        }
        return rMap;
    }

    public static String getValuesFromMap(Map<String,String> map, String key) {
        if (map == null || map.size() ==0) {
            return "";
        } else {
            return map.get(key);
        }
    }

    public static Map<String,String> getStaffTalkMap() {
        Map<String,String> map = new HashMap<>();
        map.put("staffTalkCode","编号");
        map.put("qualityDepartmentCode","质检中心编号");
        map.put("qualityDepartmentName","质检中心名称");
        map.put("qualityName","质检名称");
        map.put("keyword","质检关键词");
        map.put("punishDescription","处罚说明");
        return map;
    }

    public static Map<String,String> getProductMarketingMap() {
        StringBuffer staffTalk = new StringBuffer("productCode,productName," +
                "marketingContent,keyword,punishDescription" );
        Map<String,String> map = new HashMap<>();
        map.put("productMarketingCode","编号");
        map.put("productCode","商品编号");
        map.put("productName","商品名称");
        map.put("marketingContent","营销准则");
        map.put("keyword","质检关键词");
        map.put("punishDescription","处罚说明");
        return map;
    }

    public static Map<String,String> getWordQualityMap() {
        StringBuffer staffTalk = new StringBuffer("qualityDepartmentCode,qualityDepartmentName," +
                "prohibitedLevel,keyword,punishDescription" );
        Map<String,String> map = new HashMap<>();
        map.put("wordCode","编号");
        map.put("qualityDepartmentCode","质检中心编号");
        map.put("qualityDepartmentName","质检中心");
        map.put("prohibitedLevel","违禁级别");
        map.put("keyword","质检关键词");
        map.put("punishDescription","处罚说明");
        return map;
    }






    public static void main(String[] args) {
        Map<String,Object> notNullValidate = new HashMap<>();
        notNullValidate.put("msg", "缺少必填参数或参数值为null:" + "toCreateUserCode toQuantity".trim());
        System.out.println(changeAllFieldResult(notNullValidate,getStaffTalkMap()).toString());
    }
}
