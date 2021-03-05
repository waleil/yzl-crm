package cn.net.yzl.crm.service.impl.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.order.OutStoreWarningClient;
import cn.net.yzl.crm.dao.OutStoreWarningMapper;
import cn.net.yzl.crm.dto.ehr.StaffDetailDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OutStoreWarningService;
import cn.net.yzl.order.model.vo.MailVo;
import cn.net.yzl.order.model.vo.order.OutStoreWarningDTO;
import cn.net.yzl.order.util.SendTask;
import cn.net.yzl.order.util.SmsSendUtils;

/**
 * @author zhouchangsong
 */
@Service
public class OutStoreWarningServiceImpl implements OutStoreWarningService {

	@Autowired
	private OutStoreWarningMapper outStoreWarningMapper;
	@Autowired
	private EhrStaffClient ehrStaffClient;
	@Autowired
	private OutStoreWarningClient outStoreWarningClient;

	@Override
	public ComResponse<Boolean> sendOutStoreWarningMsg() {
		ComResponse<OutStoreWarningDTO> detail = outStoreWarningClient.getOutStoreWarningDetail();
		if (Optional.ofNullable(detail.getData()).isPresent()) {

			List<String> mobile = new ArrayList<>();
			List<String> email = new ArrayList<>();
			// TODO 等待菜单权限标识(使用prems标识)
			List<Integer> ids = outStoreWarningMapper.getRoleIdsByMenuPerms(Arrays.asList("ddckyj"));
			if (ids.size() > 0) {
				// 用户code
				List<String> userCodes = outStoreWarningMapper.getUserCodesByRoleIds(ids);
				// 用户信息
				ComResponse<List<StaffDetailDto>> staffNos = ehrStaffClient.getByStaffNos(userCodes);
				if (Optional.ofNullable(staffNos.getData()).map(List::isEmpty).isPresent()) {
					staffNos.getData().forEach(entity -> {
						// TODO 测试时只给苏曼发邮件、发短信
						if ("苏曼".equals(entity.getName())) {
							if (!StringUtils.isEmpty(entity.getEmail())) {
								email.add(entity.getEmail());
							}
							if (!StringUtils.isEmpty(entity.getPhone())) {
								mobile.add(entity.getPhone());
							}
						}
					});
				}
			}
			OutStoreWarningDTO dto = detail.getData();

			String orderNos = "";
			if (!StringUtils.isEmpty(dto.getLastCollectionTimeWarning())) {
				orderNos = dto.getLastCollectionTimeWarning();
			}

			if (!StringUtils.isEmpty(dto.getLastShippingTimeWarning())) {
				if (!StringUtils.isEmpty(orderNos)) {
					orderNos = orderNos + ",";
				}
				orderNos = orderNos + dto.getLastCollectionTimeWarning();
			}
			// 短信
			if (dto.getSendType().equals(1) || dto.getSendType().equals(3)) {
				// todo 暂时截取200个字符长度，因为短信不支持过长
				String substring = orderNos.substring(0, 200);
				if (!StringUtils.isEmpty(dto.getLastCollectionTimeWarning())) {
					SmsSendUtils.batchSend(String.join(",", mobile),
							SmsSendUtils.OUT_STORE_WARNING_SMS_TEMPLATE.replace("#orderNo#", substring));
				}
				if (!StringUtils.isEmpty(dto.getLastShippingTimeWarning())) {
					SmsSendUtils.batchSend(String.join(",", mobile),
							SmsSendUtils.OUT_STORE_WARNING_SMS_TEMPLATE.replace("#orderNo#", substring));
				}

			}
			// 邮件
			if (dto.getSendType().equals(2) || dto.getSendType().equals(3)) {
				List<MailVo> mailVos = new ArrayList<>();

				if (!StringUtils.isEmpty(orderNos)) {
					String finalOrderNos = orderNos;
					email.forEach(m -> mailVos.add(new MailVo(m, "出库预警",
							SmsSendUtils.OUT_STORE_WARNING_SMS_TEMPLATE.replace("#orderNo#", finalOrderNos))));
				}
				SendTask.runTask(mailVos);
			}

			return ComResponse.success(true);
		}
		return ComResponse.fail(detail.getCode(), detail.getMessage());
	}
}
