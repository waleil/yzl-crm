package cn.net.yzl.crm.service;

import cn.net.yzl.crm.dto.region.ProvinceTResDTO;
import cn.net.yzl.crm.model.Province;
import cn.net.yzl.crm.model.ProvinceT;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Optional;


public interface ProvinceTService {
    PageInfo<ProvinceTResDTO> findPage(Map<String,Object> params);//int page, int pageSize

    Optional<ProvinceTResDTO> getById(Integer id);

    Integer insert(ProvinceT province);
}
