package cn.net.yzl.crm.service;

import cn.net.yzl.crm.dto.region.CityTResDTO;
import cn.net.yzl.crm.dto.region.ProvinceTResDTO;
import cn.net.yzl.crm.model.CityT;
import cn.net.yzl.crm.model.ProvinceT;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Optional;


public interface CityTService {
    PageInfo<CityT> findPage(Map<String, Object> params);//int page, int pageSize

    Optional<CityT> getById(Integer id);

    Integer insert(CityT city);
}
