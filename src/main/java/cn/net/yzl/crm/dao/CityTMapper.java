package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.dto.region.CityTResDTO;
import cn.net.yzl.crm.model.CityT;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CityTMapper {
    Integer deleteById(Integer id);

    Integer insert(CityT record);

    CityT selectById(Integer id);

    Integer update(CityT record);

    List<CityT> selectList(@Param("params") Map<String,Object> params);
}