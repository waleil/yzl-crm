package cn.net.yzl.crm.service.impl.order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.SplitStoreRuleClient;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.crm.dto.order.SplitStoreRulePageDTO;
import cn.net.yzl.crm.service.SplitStoreRuleService;
import cn.net.yzl.model.pojo.StorePo;
import cn.net.yzl.order.model.vo.order.SplitStoreProvinceNamesDTO;

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
		// 仓库分页信息
		ComResponse<Page<StorePo>> storeListPage = storeFeginService.selectStoreListPage(pageNo, pageSize, null, null,
				null, 1);
		if (!storeListPage.getCode().equals(200)) {
			return ComResponse.success(page);
		}
		if (storeListPage.getData().getItems().size() == 0) {
			return ComResponse.success(page);
		}
		page.setPageParam(storeListPage.getData().getPageParam());
		List<String> storeNoList = storeListPage.getData().getItems().stream().map(StorePo::getNo)
				.collect(Collectors.toList());
		// 省市信息
		ComResponse<List<SplitStoreProvinceNamesDTO>> province = splitStoreRuleClient
				.getStoreProvinceByStoreNoList(storeNoList);
		if (!province.getCode().equals(200)) {
			return ComResponse.success(new Page<>());
		}

		List<SplitStoreRulePageDTO> dtos = storeListPage.getData().getItems().stream().map(entity -> {
			SplitStoreRulePageDTO transfer = new SplitStoreRulePageDTO();
			transfer.setMType(entity.getMType());
			transfer.setSType(entity.getSType());
			transfer.setStoreNo(entity.getNo());
			transfer.setStoreName(entity.getName());
			return transfer;
		}).collect(Collectors.toList());
		// 若仓库覆盖省市信息为空
		if (province.getData().size() == 0) {
			page.setItems(dtos);
			return ComResponse.success(page);
		}
		Map<String, List<String>> collect = province.getData().stream()
				.collect(Collectors.groupingBy(SplitStoreProvinceNamesDTO::getStoreNo,
						Collectors.mapping(SplitStoreProvinceNamesDTO::getProvinceNames, Collectors.toList())));
		dtos.forEach(dto -> dto.setProvinceNames(Optional.ofNullable(collect.get(dto.getStoreNo())).map(s -> {
			List<String> strings = Arrays.asList(String.join(",", s).split(","));
			return strings.stream().distinct().collect(Collectors.joining(","));
		}).orElse(null)));
		page.setItems(dtos);
		return ComResponse.success(page);
	}

}
