package cn.net.yzl.crm.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品库存
 * 
 * @author zhangweiwei
 * @date 2021年3月3日,下午3:37:36
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductStock {
	private String code;
	private String name;
	private Integer stock;
}
