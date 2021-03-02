package cn.net.yzl.crm.service.score.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.score.ScoreDetailClient;
import cn.net.yzl.crm.client.score.ScoreProductClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.score.ScoreService;
import cn.net.yzl.score.model.dto.*;
import cn.net.yzl.score.model.vo.*;
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
    public ComResponse<Page<MyScoreDetailDTO>> myExchangeRecords(String staffNo, Integer pageSize, Integer pageNo) {
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
    public ComResponse<Void> delete(ProductDelVO vo) {
        return scoreProductClient.delete(vo);
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

    /**
     * @description 修改商品上下架状态
     * @author Majinbao
     * @date 2021/2/27 11:43
     */
    @Override
    public ComResponse<Void> changeStatus(ChangeProductStatusVO vo) {
        return scoreProductClient.changeStatus(vo);
    }

    /**
     * @description 分页查询员工积分信息
     * @author Majinbao
     * @date 2021/3/2 10:38
     */
    @Override
    public ComResponse<Page<ScoreManageDTO>> scoreManagePage(ManageSelectVO vo) {
        return scoreDetailClient.scoreManagePage(vo);
    }

    @Override
    public ComResponse<Page<MyExchangeRecordDTO>> exchangeRecords(String userNo, Integer pageSize, Integer pageNo) {
        ComResponse<Page<MyExchangeRecordDTO>> pageComResponse = scoreDetailClient.exchangeRecords(userNo, pageSize, pageNo);
        //增加fastdfs信息
        if (pageComResponse != null) {
            pageComResponse.getData().getItems().stream().forEach(s->{
                s.setFastDFSUrl(fastDFSConfig.getUrl());
            });
        }
        return pageComResponse;
    }


    @Override
    public ComResponse<Void> changeScoreStaffStatus(DisableScoreVO vo) {
        return scoreDetailClient.changeStaffScoreStatus(vo);
    }

}
