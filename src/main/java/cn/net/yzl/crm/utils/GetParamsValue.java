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

    public static Map<String,String> getOrderDetailMap() {
        Map<String,String> map = new HashMap<>();
        map.put("odTicketInfoCode","门票编号");
        map.put("odTicketName","门票名称");
        map.put("odScenicCode","门票景区编号");
        map.put("odScenic","门票景区名称");
        map.put("odCategoryCode","门票票种编号");
        map.put("ticketAmount","门票实付金额");
        map.put("odSalesMethodsCode","门票售出方式编号");
        map.put("odSalesMethods","门票售出方式");
        return map;
    }

    public static Map<String,String> getOrderMap() {
        Map<String,String> map = new HashMap<>();
        map.put("toScenicCode","景区编号");
        map.put("toScenic","景区名称");
        map.put("toSalesMethodsCode","售出方式编号");
        map.put("toSalesMethods","售出方式");
        map.put("toCreateUserCode","售票员编号");
        map.put("toCreateUser","售票员名称");
        map.put("toOrderTypeCode","订单类型编号");
        map.put("toValidTime","游玩开始日期");
        map.put("toTicketWindow","售票窗口");
        map.put("toOrderType","订单类型");
        map.put("toQuantity","商品数量");
        map.put("amount","实付金额");
        map.put("toPayMethodCode","支付方式编号");
        map.put("toPayMethod","支付方式名称");
        map.put("toTicketWindowCode","窗口编号");
        map.put("toTicketWindowLocation","窗口位置");
        map.put("timeSlot","游玩时段");
        map.put("orderAmount","订单总金额");
        return map;
    }


    public static void main(String[] args) {
        Map<String,Object> notNullValidate = new HashMap<>();
        notNullValidate.put("msg", "缺少必填参数或参数值为null:" + "toCreateUserCode toQuantity".trim());
        System.out.println(changeAllFieldResult(notNullValidate,getOrderMap()).toString());
    }
}
