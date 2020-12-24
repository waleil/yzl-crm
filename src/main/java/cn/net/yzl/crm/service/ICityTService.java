package cn.net.yzl.crm.service;

import cn.net.yzl.crm.dao.CityTMapper;
import cn.net.yzl.crm.dao.IBaseDAO;
import cn.net.yzl.crm.dto.region.CityTResDTO;
import cn.net.yzl.crm.dto.region.ProvinceTResDTO;
import cn.net.yzl.crm.model.CityT;
import cn.net.yzl.crm.model.ProvinceT;
import com.github.pagehelper.PageInfo;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;


public interface ICityTService extends IBaseService<CityT,Integer>{

}
