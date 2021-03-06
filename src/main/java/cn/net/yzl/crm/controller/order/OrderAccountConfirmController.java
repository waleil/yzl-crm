package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderAccountConfirmClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.service.order.OrderAccountConfirmService;
import cn.net.yzl.crm.service.product.ImageService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.order.model.vo.order.OrderAccountConfirmVO;
import cn.net.yzl.product.model.vo.image.ImageDTO;
import cn.net.yzl.product.model.vo.image.ImageVO;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 收款确认单表(OrderAccountConfirm)表控制层
 *
 * @author chengyu
 * @since 2021-02-02 13:52:37
 */

@Api(tags = "结算中心")
@RestController
@RequestMapping("orderAccountConfirm")
public class OrderAccountConfirmController {


    @Autowired
    private FastdfsUtils fastdfsUtils;

    @Resource
    private OrderAccountConfirmClient client;

    @Resource
    private OrderAccountConfirmService service;

    @Resource
    private FastDFSConfig fastDFSConfig;

    @Autowired
    private ImageService imageService;

    @ApiOperation(value = "上传图片")
    @PostMapping("v1/uploadImage")
    public ComResponse<List<String>> uploadImage(@RequestPart MultipartFile[] files) throws Exception {

        List<String> list = new ArrayList<>();
        String userId = QueryIds.userNo.get();
        if (StringUtils.isBlank(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"非法的用户名！请检查您的登录状态！");
        }
        if (files.length == 0||files.length>15) {//开始判断
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "文件数量为"+files.length+",需要为1-15张！");
        }else {
            for (MultipartFile file : files) {//循环
                if (file.isEmpty()){//非空
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"存在空文件！");
                }else {
                    long size = file.getSize();//以Byte为单位
                    String fileName = file.getOriginalFilename();

                        if (size > 2<<20) {//大小，最大2M
                            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片大小超过2M限制！");
                        } else {
                            //判断后缀
                            if (!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")&&!fileName.endsWith("jpeg")&&!fileName.endsWith(".gif")){
                                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png/jpeg格式文件");
                            }

                            //全部通过后上传
                            list.add(this.upload(file));
                        }


                }
            }
        }
        return ComResponse.success(list).setMessage(fastDFSConfig.getUrl()+"/");
    }


    @ApiOperation(value = "根据订单号查询收款确认单")
    @GetMapping("v1/selectByOrderNo")
    public ComResponse<OrderAccountConfirmVO> selectByOrderNo(@RequestParam("orderNo")
                                                                @NotNull(message = "订单编号不能为空")
                                                                @ApiParam(name = "orderNo", value = "订单编号", required = true) String orderNo) {

        ComResponse<OrderAccountConfirmVO> result = this.client.selectByOrderNo(orderNo);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(result.getCode())) {
            throw new BizException(result.getCode(),result.getMessage());
        }
        result.getData().setDsfPath(fastDFSConfig.getUrl());
        return result;
    }


    /**
     * 保存确认收款记录
     *
     * @param
     * @return 单条数据
     */
    @ApiOperation(value = "保存收款确认单")
    @PostMapping("v1/saveOrderAccountConfirm")
    public ComResponse<Boolean> saveOrderAccountConfirm(@RequestBody @Valid OrderAccountConfirmVO vo) {
        return this.service.saveOrderAccountConfirm(vo);
    }

    private String upload(MultipartFile file)  throws Exception{

        StorePath storePath = fastdfsUtils.upload(file);
        String filePath = storePath.getFullPath();
        return filePath;
    }

}