package cn.net.yzl.crm.service.impl;

import cn.net.yzl.crm.dao.CityTMapper;
import cn.net.yzl.crm.dao.IBaseDAO;
import cn.net.yzl.crm.model.CityT;
import cn.net.yzl.crm.service.ICityTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CityTServiceImpl implements ICityTService {
    @Autowired
    private CityTMapper dao;
    @Override
    public IBaseDAO<CityT, Integer> getDao() {
        return dao;
    }


}
