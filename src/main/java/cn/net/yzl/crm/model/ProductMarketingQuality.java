package cn.net.yzl.crm.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * <p>
 * 产品营销话术质检表
 * </p>
 *
 * @author liufaguan
 * @since 2020-12-25
 */
@ApiModel(value="产品营销话术质检")
@Data
public class ProductMarketingQuality extends Model<ProductMarketingQuality> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String productMarketingCode;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String productCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 触发条件：1.未检测到 2.已检测到
     */
    @ApiModelProperty(value = "触发条件")
    private String triggerFlag;

    /**
     * 营销话术
     */
    @ApiModelProperty(value = "营销话术")
    private String marketingContent;

    /**
     * 质检关键词
     */
    @ApiModelProperty(value = "质检关键词")
    private String keyword;

    /**
     * 处罚说明
     */
    @ApiModelProperty(value = "处罚说明")
    private String punishDescription;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operationName;

    /**
     * 操作人编码
     */
    @ApiModelProperty(value = "操作人编码")
    private String operationCode;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
