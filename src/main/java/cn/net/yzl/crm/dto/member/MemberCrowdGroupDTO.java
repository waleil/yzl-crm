package cn.net.yzl.crm.dto.member;

import cn.net.yzl.crm.customer.mongomodel.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("顾客圈选")
@Data
public class MemberCrowdGroupDTO {
    @ApiModelProperty("群组id")
    private String crowd_id;

    @ApiModelProperty("群组名称")
    private String crowd_name;

    @ApiModelProperty("群组描述")
    private String description;

    @ApiModelProperty("生效时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date effective_date;

    @ApiModelProperty("失效时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date expire_date;

    @ApiModelProperty("性别： 0男，1女，-1不做条件判断")
    private Integer sex;

    @ApiModelProperty("顾客年龄段")
    private List<Member_Age> age;

    @ApiModelProperty("顾客圈选地区")
    private List<crowd_area> areas;

    @ApiModelProperty("圈选顾客级别")
    private List<crowd_base_value> member_grade;

    @ApiModelProperty("顾客类型")
    private List<crowd_member_type> member_type;

    @ApiModelProperty("活跃度")
    private List<crowd_activity_degree> active_degree;

    @ApiModelProperty("首次下单时间距离今天多少天")
    private Integer first_order_to_days;

    @ApiModelProperty(value = "真正首单金额")
    private Integer first_order_am;

    @ApiModelProperty("最后一次下单时间距离今天多少天")
    private Integer last_order_to_days;

    @ApiModelProperty("生日月份,1 一月份，2二月份，3三月份")
    private List<crowd_base_value> member_month;

    @ApiModelProperty("是否有微信，1有，0没有")
    private Integer wechat;

    @ApiModelProperty("是否有qq，1有，0没有")
    private Integer qq;

    @ApiModelProperty("是否有邮箱，1有，0没有")
    private Integer email;

    @ApiModelProperty("获客媒体")
    private List<crowd_media> mediaList;

    @ApiModelProperty("广告")
    private List<crowd_adver> advers;


    @ApiModelProperty("是否为会员，1是，0不是")
    private Integer vip;

    @ApiModelProperty("是否拥有红包，1是，0不是，-1不统计")
    private Integer red_bag;

    @ApiModelProperty("是否拥有积分，1是，0不是，-1不统计")
    private Integer integral;

    @ApiModelProperty("是否拥有优惠券，1是，0不是，-1不统计")
    private Integer ticket;

    @ApiModelProperty("是否拥有储值金额，1是，0不是，-1不统计")
    private Integer recharge;

    @ApiModelProperty("方便接电话时间")
    private List<crowd_action> phone_time;

    @ApiModelProperty("性格偏好")
    private List<crowd_action> nature;

    @ApiModelProperty("响应时间")
    private List<crowd_action> response_time;

    @ApiModelProperty("坐席性格偏好")
    private List<crowd_action> staff_sex;

    @ApiModelProperty("综合行为")
    private List<crowd_action> actions;

    @ApiModelProperty("下单行为")
    private List<crowd_action> order_action;

    @ApiModelProperty("活动偏好")
    private List<crowd_action> active_like;

    @ApiModelProperty("支付方式：0=货到付款，1=款到发货，-1不做条件判断")
    private List<crowd_base_value> pay_type;

    @ApiModelProperty("支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款")
    private List<crowd_base_value> pay_form;

    @ApiModelProperty("签收时间截止今日，-1表示不做条件统计")
    private Integer sign_date_to_days;

    @ApiModelProperty("订单状态：0.话务待审核 1.话务未通过 2. 物流部待审核 3.物流部审核未通过  4..物流已审核 5.已退 6.部分退 7.订单已取消 8.订单已完成 9.拒收")
    private List<crowd_base_value> order_state;

    @ApiModelProperty("是否活动订单，1是，0否，-1不做统计查询")
    private Integer active_order;

    @ApiModelProperty("活动")
    private List<crowd_active> activeList;

    @ApiModelProperty("订单来源: 0=电销事业中心，1=OTC ，2=淘宝 ，3=京东 ，4=自建app")
    private List<crowd_base_value> order_source;

    @ApiModelProperty("商品")
    private List<crowd_product> products;

    @ApiModelProperty("支付状态：1已支付，0未支付，-1表示不做统计查询")
    private Integer pay_state;

    @ApiModelProperty("物流状态，-1不做统计查询")
    private List<crowd_base_value> logistics_state;

    @ApiModelProperty("物流公司")
    private List<crowd_base_value> logistics_company_id;

    @ApiModelProperty(value = "累计消费金额")
    private Integer total_amount;

    @ApiModelProperty("订单总金额")
    private Integer order_total_amount;

    @ApiModelProperty("订单应收金额")
    private Integer order_rec_amount;

    @ApiModelProperty("订单最高金额")
    private Integer order_high_am;

    @ApiModelProperty("订单最低金额")
    private Integer order_low_am;

    @ApiModelProperty("是否下单: 1是，0否，-1不做条件判断")
    private Integer have_order;

    @ApiModelProperty("圈选病症")
    private List<crowd_disease> diseases;
}
