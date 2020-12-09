package cn.net.yzl.crm.service.impl;

import cn.net.yzl.crm.dao.CityTMapper;
import cn.net.yzl.crm.dao.ProvinceTMapper;
import cn.net.yzl.crm.dto.region.CityTResDTO;
import cn.net.yzl.crm.dto.region.ProvinceTResDTO;
import cn.net.yzl.crm.model.CityT;
import cn.net.yzl.crm.model.ProvinceT;
import cn.net.yzl.crm.service.CityTService;
import cn.net.yzl.crm.service.ProvinceTService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CityTServiceImpl implements CityTService {


    @Autowired
    private CityTMapper dao;

    @Override
    public PageInfo<CityT> findPage(Map<String, Object> params) {
        params.computeIfAbsent("currentPage", k -> 1);
        params.computeIfAbsent("pageSize", k -> 10);
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageHelper.startPage(currentPage,pageSize );
        List<CityT> cityList = dao.selectList(params);
        return  new PageInfo<>(cityList);
    }

    @Override
    public Optional<CityT> getById(Integer id) {
        return Optional.ofNullable(dao.selectById(id));
    }

    @Override
    public Integer insert(CityT city) {
        return dao.insert(city);
    }

}
