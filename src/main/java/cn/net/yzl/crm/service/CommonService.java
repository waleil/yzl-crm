package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.product.ProduceDto;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/16 23:12
 * @desc: //TODO  请说明该类的用途
 **/
public interface CommonService {
    ComResponse<List<ProduceDto>> selectProduct();

}
