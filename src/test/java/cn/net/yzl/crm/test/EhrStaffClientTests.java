package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.enums.WorkOrderType;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月24日,下午1:40:31
 */
@SpringBootTest
public class EhrStaffClientTests {
	@Resource
	private EhrStaffClient ehrStaffClient;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testGetDepartByIdAndGetDetailsByNo() {
		ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(1650);
		ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo("5085");
		System.out.println("response = " + dresponse);
		System.out.println("detailsByNo = " + detailsByNo);

	}

	@Test
	public void testGetDetailsByNoAndGetData() {
		ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo("11803");
		if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {
			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
		}
		System.out.println(userNo.getData());
	}

	@Test
	public void testGetDetailsByNo() {
		try {
			String staffno = "7018";
			System.err.println(this.ehrStaffClient.getDetailsByNo(staffno).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDepartById() {
		try {
			Integer departid = 1557;
			System.err.println(this.ehrStaffClient.getDepartById(departid));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetListByBusinessAttrId() {
		try {
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					this.ehrStaffClient.getListByBusinessAttrId(WorkOrderType.WORK_ORDER_TYPE_1.getId())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
