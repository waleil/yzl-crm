package cn.net.yzl.crm.service.impl.order;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            //角色ID
            List<Integer> ids = outStoreWarningMapper.getRoleIdsByMenuPerms("");
            if (ids.size() > 0) {
                //用户code
                List<String> userCodes = outStoreWarningMapper.getUserCodesByRoleIds(ids);
                //用户信息
                ComResponse<List<StaffDetailDto>> staffNos = ehrStaffClient.getByStaffNos(userCodes);
                if (Optional.ofNullable(staffNos.getData()).map(List::isEmpty).isPresent()) {
                    staffNos.getData().forEach(entity -> {
                        email.add(entity.getEmail());
                        mobile.add(entity.getPhone());
                    });
                }
            }
            OutStoreWarningDTO dto = detail.getData();
            //短信
            if (dto.getSendType().equals(1) || dto.getSendType().equals(3)) {
                SmsSendUtils.batchSend(String.join(",", mobile), SmsSendUtils.OUT_STORE_WARNING_SMS_TEMPLATE.replace("#orderNo#", dto.getLastCollectionTimeWarning()));
                SmsSendUtils.batchSend(String.join(",", mobile), SmsSendUtils.OUT_STORE_WARNING_SMS_TEMPLATE.replace("#orderNo#", dto.getLastShippingTimeWarning()));
            }
            //TODO 邮件，没有员工邮件地址
            if (dto.getSendType().equals(2) || dto.getSendType().equals(3)) {
                List<MailVo> mailVos = new ArrayList<>();
                email.forEach(m -> mailVos.add(new MailVo(m, "出库预警", SmsSendUtils.OUT_STORE_WARNING_SMS_TEMPLATE.replace("#orderNo#", dto.getLastCollectionTimeWarning()))));
                SendTask.runTask(mailVos);
            }

            return ComResponse.success();
        }
        return ComResponse.fail(detail.getCode(), detail.getMessage());
    }
}
