package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.crm.client.order.SplitStoreRuleClient;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.crm.dto.order.SplitStoreRulePageDTO;
import cn.net.yzl.crm.service.SplitStoreRuleService;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.utils.BeanCopyUtils;
import cn.net.yzl.model.pojo.StorePo;
import cn.net.yzl.order.model.vo.order.SplitStoreProvinceNamesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhouchangsong
 */
@Service
public class SplitStoreRuleServiceImpl implements SplitStoreRuleService {

    @Autowired
    private StoreFeginService storeFeginService;

    @Autowired
    private SplitStoreRuleClient splitStoreRuleClient;

    @Override
    public ComResponse<Page<SplitStoreRulePageDTO>> getSplitStoreRuleList(Integer pageSize, Integer pageNo) {
        Page<SplitStoreRulePageDTO> page = new Page<>();
        //仓库分页信息
        ComResponse<Page<StorePo>> storeListPage = storeFeginService.selectStoreListPage(pageNo, pageSize, null, null, null);
        if (!storeListPage.getCode().equals(200)) {
            return ComResponse.success(page);
        }
        if (storeListPage.getData().getItems().size() == 0) {
            return ComResponse.success(page);
        }
        page.setPageParam(BeanCopyUtils.transfer(storeListPage.getData().getPageParam(), PageParam.class));
        List<String> storeNoList = storeListPage.getData().getItems().stream().map(StorePo::getNo).collect(Collectors.toList());
        //省市信息
        ComResponse<List<SplitStoreProvinceNamesDTO>> province = splitStoreRuleClient.getStoreProvinceByStoreNoList(storeNoList);
        if (!province.getCode().equals(200)) {
            return ComResponse.success(new Page<>());
        }

        List<SplitStoreRulePageDTO> dtos = new ArrayList<>();
        storeListPage.getData().getItems().forEach(entity -> {
            SplitStoreRulePageDTO transfer = BeanCopyUtils.transfer(entity, SplitStoreRulePageDTO.class);
            transfer.setStoreNo(entity.getNo());
            transfer.setStoreName(entity.getName());
            dtos.add(transfer);
        });
        //若仓库覆盖省市信息为空
        if (province.getData().size() == 0) {
            page.setItems(dtos);
            return ComResponse.success(page);
        }
        Map<String, List<String>> provinceDTOMap = province.getData().stream().collect(Collectors.toMap(SplitStoreProvinceNamesDTO::getStoreNo, s->{
            List<String> list = new ArrayList<>();
            list.add(s.getProvinceNames());
            return list;
        }));
        dtos.forEach(dto -> dto.setProvinceNames(Optional.ofNullable(provinceDTOMap.get(dto.getStoreNo())).map(s-> {
            List<String> strings = Arrays.asList(String.join(",", s).split(","));
            return strings.stream().distinct().collect(Collectors.joining(","));
//            s.stream().map(SplitStoreProvinceNamesDTO::getProvinceNames).distinct().collect(Collectors.joining(","));
        }).orElse(null)));
        page.setItems(dtos);
        return ComResponse.success(page);
    }




























}
