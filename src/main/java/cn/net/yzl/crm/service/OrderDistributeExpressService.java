package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import org.springframework.web.multipart.MultipartFile;

public interface OrderDistributeExpressService {

    ComResponse readExpressExcelInfo(MultipartFile file);
}
