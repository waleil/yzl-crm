package cn.net.yzl.crm.service.score.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.score.ScoreDetailClient;
import cn.net.yzl.crm.client.score.ScoreProductClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.score.ScoreService;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import cn.net.yzl.score.model.dto.ScoreProductDetailDTO;
import cn.net.yzl.score.model.dto.ScoreProductMainInfoDTO;
import cn.net.yzl.score.model.vo.ExchangeVO;
import cn.net.yzl.score.model.vo.ScoreProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreDetailClient scoreDetailClient;

    @Autowired
    private ScoreProductClient scoreProductClient;

    @Autowired
    private FastDFSConfig fastDFSConfig;

    /**
     * @description 我的积分兑换明细
     * @author Majinbao
     * @date 2021/2/26 16:18
     */
    @Override
    public ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(String staffNo, Integer pageSize, Integer pageNo) {
        return StringUtils.isEmpty(staffNo)?
                ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"用户id不能为空！")
                :scoreDetailClient.myExchangeRecords(staffNo, pageSize==null?10:pageSize, pageNo==null?1:pageNo);
    }

//    @Override
//    public ComResponse<String> uploadScoreProductFile(MultipartFile file) {
//        return scoreProductClient.uploadScoreProductFile(file);
//    }

    /**
     * @description 分页查询商品总览
     * @author Majinbao
     * @date 2021/2/26 16:18
     */
    @Override
    public ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(Integer pageSize, Integer pageNo, Boolean hide) {
        ComResponse<Page<ScoreProductMainInfoDTO>> p = scoreProductClient.queryPage(pageSize == null ? 10 : pageSize,
                pageNo == null ? 1 : pageNo,
                hide);
        if (p.getData()!= null && !CollectionUtils.isEmpty(p.getData().getItems())) {
            p.getData().getItems().forEach(item ->{
                item.setFastDFSUrl(fastDFSConfig.getUrl());
            });
        }
        return p;
    }

    /**
     * @description 根据id查询积分商品详情
     * @author Majinbao
     * @date 2021/2/26 16:18
     */
    @Override
    public ComResponse<ScoreProductDetailDTO> queryDetail(Integer id) {
        ComResponse<ScoreProductDetailDTO> s = scoreProductClient.queryDetail(id);
        if (s.getData() != null) {
            s.getData().setFastDFSUrl(fastDFSConfig.getUrl());
        }
        return s;
    }

    /**
     * @description 编辑积分商品
     * @author Majinbao
     * @date 2021/2/26 16:18
     */
    @Override
    public ComResponse<Void> edit(ScoreProductVO vo) {
        return scoreProductClient.edit(vo);
    }

    /**
     * @description 删除积分商品信息
     * @author Majinbao
     * @date 2021/2/26 16:19
     */
    @Override
    public ComResponse<Void> delete(Integer id, HttpServletRequest request) {
        String userNo = request.getHeader("userNo");

        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"获取用户身份失败，请尝试重新登陆！");
        }

        return scoreProductClient.delete(id,userNo);
    }

    /**
     * @description 兑换商品
     * @author Majinbao
     * @date 2021/2/26 16:19
     */
    @Override
    public ComResponse<Void> exchange(ExchangeVO vo) {
        return scoreProductClient.exchange(vo);
    }

    /**
     * @description 查询我的积分
     * @author Majinbao
     * @date 2021/2/26 16:27
     */
    @Override
    public ComResponse<Integer> myScore(String staffNo) {
        return scoreProductClient.myScore(staffNo);
    }

}
