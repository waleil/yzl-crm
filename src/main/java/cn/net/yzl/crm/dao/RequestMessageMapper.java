package cn.net.yzl.crm.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import cn.net.yzl.crm.model.RequestMessage;

/**
 * 本地消息表 Mapper
 * 
 * @author zhangweiwei
 * @date 2021年1月29日,下午3:40:59
 */
@Mapper
public interface RequestMessageMapper {
	/**
	 * 插入本地消息数据
	 * 
	 * @param message 本地消息数据
	 * @return 插入行数
	 * @author zhangweiwei
	 * @date 2021年1月29日,下午6:50:24
	 */
	@Insert("insert into request_message (message_code,bus_code,request_url,call_back_url,request_param,create_code,update_code) values (#{messageCode},#{busCode},#{requestUrl},#{callBackUrl},#{requestParam},#{createCode},#{updateCode})")
	int insert(RequestMessage message);
}
