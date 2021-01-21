package cn.net.yzl.crm.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * bean拷贝工具类
 *
 * @author 周长松
 */
public class BeanCopyUtils {
    /**
     * 若bean中有Date类型的属性，且可能为null时，可能会抛出一个异常：org.apache.commons.beanutils.ConversionException: No value specified for 'Date'，
     * ConvertUtils.register(new DateConverter(null), java.util.Date.class);//添加这一行代码，重新注册一个转换器
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T transfer(Object text, Class<T> clazz) {
        T t = null;
        //若为null的话，会自作多情的给默认值
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        try {
            t = clazz.newInstance();
            org.apache.commons.beanutils.BeanUtils.copyProperties(t, text);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static <T> List<T> transferList(List<?> objs, Class<T> clazz) {
        if (CollectionUtils.isEmpty(objs)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (Object each : objs) {
            result.add(transfer(each, clazz));
        }
        return result;
    }

    /**
     * jsonobject转实体类
     *
     * @param jsonObj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonObjToBean(String jsonObj, Class<T> clazz) {
        T t;
        JSONObject jsonObject = JSONObject.parseObject(jsonObj);
        t = JSON.toJavaObject(jsonObject, clazz);
        return t;
    }

    /**
     * jsonarray转实体类
     *
     * @param jsonArray
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonArrayToBean(String jsonArray, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        JSONArray array = JSONArray.parseArray(jsonArray);
        array.forEach(obj -> {
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(obj));
            T t = JSON.toJavaObject(jsonObject, clazz);
            result.add(t);
        });
        return result;
    }

    /**
     * 使用Introspector，对象转换为map集合
     *
     * @param beanObj javabean对象
     * @return map集合
     */
    public static Map<String, Object> beanToMap(Object beanObj) {

        if (null == beanObj) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(beanObj) : null;
                map.put(key, value);
            }

            return map;
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public static <T> void nullToEmpty(T bean) {
        Field[] field = bean.getClass().getDeclaredFields();
        //遍历所有属性
        for (int j = 0; j < field.length; j++) {
            //获取属性的名字
            String name = field[j].getName();
            //将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            //获取属性的类型
            String type = field[j].getGenericType().toString();
            //如果type是类类型，则前面包含"class "，后面跟类名
            if ("class java.lang.String".equals(type)) {
                try {
                    Method mGet = bean.getClass().getMethod("get" + name);
                    String value = (String) mGet.invoke(bean);    //调用getter方法获取属性值
                    if (value == null || "".equals(value)) {
                        Method mSet = bean.getClass().getMethod("set" + name, new Class[]{String.class});
                        mSet.invoke(bean, new Object[]{new String("")});
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
