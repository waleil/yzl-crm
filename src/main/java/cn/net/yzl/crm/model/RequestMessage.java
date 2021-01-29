package cn.net.yzl.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 本地消息表
 * 
 * @author zhangweiwei
 * @date 2021年1月29日,下午3:44:00
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {
	private Integer id;
	private String messageCode;
	private String busCode;
	private String requestUrl;
	private String callBackUrl;
	private String requestParam;
	private String createCode;
	private String updateCode;
	private Integer state;
}
