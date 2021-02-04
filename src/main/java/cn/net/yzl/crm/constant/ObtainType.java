package cn.net.yzl.crm.constant;

/**
 * 顾客账户-账户操作(充值,消费,退回)
 * 
 * @author zhangweiwei
 * @date 2021年1月26日,下午9:14:08
 */
public interface ObtainType {
	/** 退回 */
	int OBTAIN_TYPE_1 = 1;
	/** 消费 */
	int OBTAIN_TYPE_2 = 2;
	/** 充值 */
	int OBTAIN_TYPE_3 = 3;
	/** 退回 */
	String OBTAIN_TYPE_1_STRING = "1";
	/** 消费 */
	String OBTAIN_TYPE_2_STRING = "2";
	/** 充值 */
	String OBTAIN_TYPE_3_STRING = "3";
}
