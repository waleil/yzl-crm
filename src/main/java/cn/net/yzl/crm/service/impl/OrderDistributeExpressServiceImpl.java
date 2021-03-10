package cn.net.yzl.crm.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.client.store.OrderDistributeExpressFeignService;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.controller.store.listen.ExpressExcelListener;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.OrderDistributeExpressService;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.model.dto.express.ExpressImportModel;
import cn.net.yzl.model.dto.express.ImportExpressAllInfo;
import cn.net.yzl.model.vo.InventoryProductVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderDistributeExpressServiceImpl implements OrderDistributeExpressService {

    @Autowired
    private OrderDistributeExpressFeignService orderDistributeExpressFeignService;
    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Override
    public ComResponse readExpressExcelInfo(MultipartFile file) {

        //创建监听器
        ExpressExcelListener expressExcelListener = new ExpressExcelListener();
        ExcelReader excelReader = null;
        try {
            //读取excel
            excelReader = EasyExcel.read(file.getInputStream(), ExpressImportModel.class, expressExcelListener).build();
            excelReader.readAll();
            //获取错误信息
            Map<String, String> errorMessageMap = expressExcelListener.getErrorMessageMap();
            if (!CollectionUtils.isEmpty(errorMessageMap)){
                return ComResponse.fail(ResponseCodeEnums.EXCEL_HEAD_ERROR.getCode(),errorMessageMap.get("msg"));
            }
            ImportExpressAllInfo importExpressAllInfo = new ImportExpressAllInfo();
            List<ExpressImportModel> expressImportModels = expressExcelListener.getExpressImportModels();
            if(CollectionUtils.isEmpty(expressImportModels)){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"表格数据为空!");
            }
            for (ExpressImportModel expressImportModel : expressImportModels) {
                if (StrUtil.isBlank(expressImportModel.getExpressNum())){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"快递单号不可为空!");
                }
                if (StrUtil.isBlank(expressImportModel.getDeliverCode())){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"发货码不可为空!");
                }
            }

            importExpressAllInfo.setList(expressImportModels);
            StaffImageBaseInfoDto user = getUser();
            importExpressAllInfo.setUserNo(user.getStaffNo());
            importExpressAllInfo.setUserName(user.getName());
            importExpressAllInfo.setDepartId(String.valueOf(user.getDepartId()));
            //获取数据
            log.info("导入数据解析结果:{}",expressImportModels);
            return orderDistributeExpressFeignService.readExpressExcelInfo(importExpressAllInfo);

        } catch (IOException e) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_TYPE_ERROR_CODE.getCode(),ResponseCodeEnums.PARAMS_TYPE_ERROR_CODE.getMessage());
        }

    }

    private StaffImageBaseInfoDto getUser(){
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> user = ehrStaffClient.getDetailsByNo(userNo);
        log.info("user信息:{}", JsonUtil.toJsonStr(user));
        StaffImageBaseInfoDto data = user.getData();
        if(data != null){
            return data;
        }else {
            throw new BizException(ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE);
        }
    }
}
