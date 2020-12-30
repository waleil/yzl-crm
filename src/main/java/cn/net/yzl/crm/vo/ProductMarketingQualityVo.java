package cn.net.yzl.crm.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ Author     ：liufaguan
 * @ Date       ：2020/12/29 18:05
 * @ Description：产品营销话术质检Vo
 * @Version: 1
 */
@ApiModel(value="产品营销话术质检")
@Data
public class ProductMarketingQualityVo {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号",required = true)
    private String productMarketingCode;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号",required = true)
    private String productCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 触发条件：1.未检测到 2.已检测到
     */
    @ApiModelProperty(value = "触发条件：1.未检测到 2.已检测到")
    private String triggerFlag;

    /**
     * 营销话术
     */
    @ApiModelProperty(value = "营销话术",required = true)
    private String marketingContent;

    /**
     * 质检关键词
     */
    @ApiModelProperty(value = "质检关键词",required = true)
    private String keyword;

    /**
     * 处罚说明
     */
    @ApiModelProperty(value = "处罚说明",required = true)
    private String punishDescription;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人",required = true)
    private String operationName;

    /**
     * 操作人编码
     */
    @ApiModelProperty(value = "操作人编码",required = true)
    private String operationCode;

}
