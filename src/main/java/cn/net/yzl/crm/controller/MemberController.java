package cn.net.yzl.crm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.common.util.YLoggerUtil;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.product.DiseaseClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.dto.member.*;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.vo.MemberAndAddWorkOrderVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectSelectVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectUpdateVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import cn.net.yzl.crm.customer.vo.member.MemberGrandSelectVo;
import cn.net.yzl.crm.customer.vo.work.MemberWorkOrderInfoVO;
import cn.net.yzl.crm.customer.vo.work.WorkOrderBeanVO;
import cn.net.yzl.crm.dto.dmc.MemberLevelResponse;
import cn.net.yzl.crm.dto.dmc.PageModel;
import cn.net.yzl.crm.dto.member.CallInfoDTO;
import cn.net.yzl.crm.dto.member.MemberDiseaseDto;
import cn.net.yzl.crm.dto.member.MemberServiceJournery;
import cn.net.yzl.crm.dto.member.MemberServiceJourneryDto;
import cn.net.yzl.crm.dto.member.customerJourney.MemberCustomerJourneyDto;
import cn.net.yzl.crm.dto.member.customerJourney.QueryWorkOrderVo;
import cn.net.yzl.crm.dto.staff.StaffCallRecord;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.dto.workorder.MemberFirstCallDetailsDTO;
import cn.net.yzl.crm.model.customer.MemberReferral;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.micservice.WorkOrderClient;
import cn.net.yzl.crm.service.micservice.member.MemberPhoneFien;
import cn.net.yzl.crm.service.micservice.member.MemberProductEffectFien;
import cn.net.yzl.crm.service.micservice.member.MemberTypeFien;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.PortraitOrderDetailDTO;
import cn.net.yzl.product.model.vo.product.dto.DiseaseMainInfo;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.workorder.model.db.WorkOrderDisposeFlowSubBean;
import cn.net.yzl.workorder.model.vo.WorkOrderFlowVO;
import cn.net.yzl.workorder.model.vo.WorkOrderVo;
import cn.net.yzl.workorder.utils.MonggoDateHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "顾客管理")
@RestController
@RequestMapping(value = MemberController.PATH)
public class MemberController {
    public static final String PATH = "member";
    @Autowired
    MemberFien memberFien;
    @Autowired
    DiseaseClient diseaseClient;
    @Autowired
    MemberPhoneFien memberPhoneFien;

    @Autowired
    WorkOrderClient workOrderClient;
    @Autowired
    MemberProductEffectFien memberProductEffectFien;

    @Autowired
    MemberTypeFien memberTypeFien;
    @Autowired
    private cn.net.yzl.crm.service.micservice.EhrStaffClient ehrStaffClient;

    @Autowired
    ActivityClient activityClient;


    @ApiOperation(value = "顾客列表-分页查询顾客列表")
    @PostMapping("v1/listPage")
    public ComResponse<Page<Member>> listPage( @RequestBody MemberSerchConditionDTO dto) {
        return memberFien.listPage(dto);
    }


    @ApiOperation(value = "保存顾客信息")
    @PostMapping("v1/save")
    public GeneralResult<Boolean> save(@RequestBody Member dto) {
        memberFien.save(dto);
        return GeneralResult.success();
    }

    @ApiOperation(value = "保存转介绍用户")
    @PostMapping("v1/saveMemberReferral")
    public ComResponse<Boolean> saveMemberReferral(@Validated @RequestBody MemberReferral memberReferralVO) throws IllegalAccessException {
        String staffNo= QueryIds.userNo.get();
        //根据员工编码获取部门
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(staffNo);
        YLoggerUtil.infoLog("根据员工编码获取部门 Response", JsonUtil.toJsonStr(detailsByNo));
        StaffImageBaseInfoDto data = detailsByNo.getData();
        if(null == data){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"获取当前用户信息:"+detailsByNo.getMessage());
        }
        Integer departId = data.getDepartId();//部门id
        if(null == departId){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"暂无部门数据");
        }

        MemberAndAddWorkOrderVO vo = new MemberAndAddWorkOrderVO();
        Member member = new Member();
        member.setMember_name(memberReferralVO.getMemberName());
        member.setSex(memberReferralVO.getSex());
        member.setAge(memberReferralVO.getAge());

        List<MemberPhone> memberPhoneList = new ArrayList<>();
        MemberPhone memberPhone = new MemberPhone();
        memberPhone.setPhone_number(memberReferralVO.getMemberPhone());
        memberPhone.setCreator_no(staffNo);
        memberPhone.setUpdator_no(staffNo);
        memberPhoneList.add(memberPhone);
        member.setMemberPhoneList(memberPhoneList);

        member.setEmail(memberReferralVO.getEmail());
        member.setQq(memberReferralVO.getQq());
        member.setWechat(memberReferralVO.getWechat());
        member.setProvince_code(memberReferralVO.getProvinceCode());
        member.setProvince_name(memberReferralVO.getProvinceName());

        member.setCity_code(memberReferralVO.getCityCode());
        member.setCity_name(memberReferralVO.getCityName());
        member.setArea_code(memberReferralVO.getAreaCode());
        member.setArea_name(memberReferralVO.getAreaName());

        member.setAddress(memberReferralVO.getAddress());

        member.setCreator_no(staffNo);
        member.setUpdator_no(staffNo);
        if (data != null) {
            member.setCreator_name(data.getName());
            member.setUpdator_name(data.getName());
        }
        //设置会员信息
        vo.setMemberVO(member);

        WorkOrderBeanVO workOrderBeanVO = new WorkOrderBeanVO();
        workOrderBeanVO.setMemberName(memberReferralVO.getMemberName());
        workOrderBeanVO.setCalledPhone(memberReferralVO.getMemberPhone());
        workOrderBeanVO.setActivity(memberReferralVO.getActivity());
        workOrderBeanVO.setAdvId(memberReferralVO.getAdvId());
        workOrderBeanVO.setAdvName(memberReferralVO.getAdvName());
        workOrderBeanVO.setAllocateStatus(memberReferralVO.getAllocateStatus());
        workOrderBeanVO.setMediaId(memberReferralVO.getMediaId());
        workOrderBeanVO.setMediaName(memberReferralVO.getMediaName());

        if (data != null) {
            workOrderBeanVO.setDeptId(data.getDepartId());
            workOrderBeanVO.setDeptName(data.getDepartName());
            workOrderBeanVO.setCreateName(data.getName());
            workOrderBeanVO.setUpdateName(data.getName());
        }
        workOrderBeanVO.setCreateId(staffNo);
        workOrderBeanVO.setUpdateId(staffNo);
        //设置工单信息
        vo.setWorkOrderBeanVO(workOrderBeanVO);
        ComResponse<Boolean> response = memberFien.saveMemberReferral(vo);
        if (response.getCode() != 200) {
            return ComResponse.fail(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),response.getMessage());
        }
        return ComResponse.success(true);
    }

    @ApiOperation(value = "修改顾客信息")
    @PostMapping("v1/updateByMemberCard")
    public GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto) {
        GeneralResult<Boolean> result = memberFien.updateByMemberCard(dto);
        return result;
    }

    @ApiOperation(value = "获取顾客信息")
    @GetMapping("v1/getMember")
    public GeneralResult<Member> getMember(@RequestParam("memberCard") String memberCard) {
        return memberFien.getMember(memberCard);
    }

    @ApiOperation("获取顾客联系方式信息，包括手机号，座机号")
    @GetMapping("/v1/getMemberPhoneList")
    public GeneralResult<List<MemberPhone>> getMemberPhoneList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        GeneralResult<List<MemberPhone>> result = memberFien.getMemberPhoneList(member_card);
        return result;
    }

    /**
     * 获取顾客联系方式信息，包括手机号，座机号
     *
     * @param phone
     * @return
     */
    @ApiOperation("根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）")
    //@GetMapping("/v1/getMemberByPhone")
    @GetMapping("/v1/getMemberByphoneNumber")
    public ComResponse<Member> getMemberByPhone(
            @RequestParam("phone")
            @NotBlank(message = "phone不能为空")
            @ApiParam(name = "phone", value = "手机号", required = true)
                    String phone) {
        ComResponse<Member> result = memberPhoneFien.getMemberByphoneNumber(phone);
        return result;
    }

    /**
     * 设置顾客为会员
     *
     * @param member_card
     * @return
     */
    @ApiOperation("设置顾客为会员")
    @GetMapping("/v1/setMemberToVip")
    public GeneralResult setMemberToVip(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        memberFien.setMemberToVip(member_card);
        return GeneralResult.success();
    }

    /**
     * 获取顾客购买商品
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客购买商品")
    @GetMapping("v1/getMemberProductEffectList")
    public GeneralResult<List<MemberProductEffect>> getMemberProductEffectList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        GeneralResult<List<MemberProductEffect>> result = memberFien.getMemberProductEffectList(member_card);
        return result;
    }

    /**
     * 获取顾客咨询商品
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客咨询商品")
    @GetMapping("v1/getProductConsultationList")
    public GeneralResult<List<ProductConsultation>> getProductConsultationList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        GeneralResult<List<ProductConsultation>> result = memberFien.getProductConsultationList(member_card);
        return result;
    }

    @Autowired
    private OrderSearchClient orderSearchClient;
//@Autowired
//private WorkOrderClient workOrderClient;
    @ApiOperation("顾客画像-服务旅程")
    @GetMapping("v1/getMemberServiceJourney")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<MemberServiceJourneryDto> getMemberServiceJourney(String memberCard) {

        MemberServiceJourneryDto memberServiceJourneryDto = new MemberServiceJourneryDto();

        // 从 订单获取 顾客的 时间
        ComResponse<List<WorkOrderFlowVO>> listComResponse = workOrderClients.userRoute(memberCard);
        List<WorkOrderFlowVO> workOrderFlowVOList = listComResponse.getData();
        if(CollectionUtil.isEmpty(workOrderFlowVOList)){
            return ComResponse.nodata();
        }
        //

        workOrderFlowVOList= orderdingWorkOrderFlowVOList(workOrderFlowVOList);
        // 获取数量
        List<WorkOrderFlowVO> collect = workOrderFlowVOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(WorkOrderFlowVO::getStaffNo))), ArrayList::new));
        memberServiceJourneryDto.setStaffNum(collect.size());
        List list = new ArrayList<MemberServiceJournery>();
        // 从订单获取顾客的消费情况
        workOrderFlowVOList.forEach(workOrderFlowVO -> {
            Date endTime = workOrderFlowVO.getEndTime(); // 开始时间
            Date startTime = workOrderFlowVO.getStartTime(); // 结束时间
            String staffNo = workOrderFlowVO.getStaffNo(); // 员工编号
            MemberServiceJournery memberServiceJournery = new MemberServiceJournery();
            memberServiceJournery.setStaffNo(staffNo);
            memberServiceJournery.setStartTime(startTime);
            memberServiceJournery.setEndTime(endTime);
            String startTimeStr = startTime == null ? null : DateFormatUtil.dateToString(startTime, DateFormatUtil.UTIL_FORMAT);
            String endTimeStr= endTime == null ? DateFormatUtil.dateToString(new Date(), DateFormatUtil.UTIL_FORMAT) : DateFormatUtil.dateToString(endTime, DateFormatUtil.UTIL_FORMAT);
            ComResponse<String> stringComResponse = orderSearchClient.selectSalesQuota(memberCard, staffNo, startTimeStr, endTimeStr);
            if(stringComResponse.getData()!=null){
                memberServiceJournery.setTotalPrice(Double.parseDouble(stringComResponse.getData()));
            }
            list.add(memberServiceJournery);

        });
        memberServiceJourneryDto.setMemberServiceJourneryList(list);
        return ComResponse.success(memberServiceJourneryDto);
    }


    // 对顾客的维护的  坐席 排序
    public static List<WorkOrderFlowVO> orderdingWorkOrderFlowVOList(List<WorkOrderFlowVO> workOrderFlowVOList) {
        String staffNo=null;
        List<WorkOrderFlowVO> result = new ArrayList<>();
        List<WorkOrderFlowVO> list = new ArrayList<>();
        for (int i = 0; i < workOrderFlowVOList.size(); i++) {
            WorkOrderFlowVO  workOrderFlowVO=  workOrderFlowVOList.get(i);
            String staffNo1 = workOrderFlowVO.getStaffNo();
//            Date startTime = workOrderFlowVO.getStartTime();
//            Date endTime = workOrderFlowVO.getEndTime();

            if(StrUtil.isBlank(staffNo)){
                staffNo=staffNo1;
                if (workOrderFlowVOList.size() == 1) {
                    return workOrderFlowVOList;
                }

            }else if (!staffNo1.equals(staffNo) ){
                // 处理
                WorkOrderFlowVO workOrderFlowVO1 = new WorkOrderFlowVO();
                BeanUtil.copyProperties(list.get(list.size()-1),workOrderFlowVO1);
                workOrderFlowVO1.setEndTime(list.get(0).getEndTime());
                result.add(workOrderFlowVO1);

                // 需要清空 list 并取出list的数据
                list= new ArrayList<WorkOrderFlowVO>();
                staffNo=staffNo1;
                if(i==workOrderFlowVOList.size()-1){
                    result.add(workOrderFlowVO);
                }
            }else if (i==workOrderFlowVOList.size()-1){
                // 如果是最后一个
                WorkOrderFlowVO workOrderFlowVO1 = new WorkOrderFlowVO();
                BeanUtil.copyProperties(workOrderFlowVO,workOrderFlowVO1);
                workOrderFlowVO1.setEndTime(list.get(0).getEndTime());
                result.add(workOrderFlowVO1);
            }
            list.add(workOrderFlowVO);

        }
        return result;
    }

//    public static void main(String[] args) {
//        WorkOrderFlowVO  w1 = new WorkOrderFlowVO();
//        w1.setStaffNo("1");
//        w1.setStartTime(DateFormatUtil.stringToDate("20210-1-10",DateFormatUtil.SQL_FORMAT));
//        w1.setEndTime(DateFormatUtil.stringToDate("20210-1-11",DateFormatUtil.SQL_FORMAT));
//        WorkOrderFlowVO  w2 = new WorkOrderFlowVO();
//        w2.setStaffNo("1");
//        w2.setStartTime(DateFormatUtil.stringToDate("20210-1-13",DateFormatUtil.SQL_FORMAT));
//        w2.setEndTime(DateFormatUtil.stringToDate("20210-1-14",DateFormatUtil.SQL_FORMAT));
//        WorkOrderFlowVO  w3 = new WorkOrderFlowVO();
//        w3.setStaffNo("2");
//        w3.setStartTime(DateFormatUtil.stringToDate("20210-1-16",DateFormatUtil.SQL_FORMAT));
//        w3.setEndTime(DateFormatUtil.stringToDate("20210-1-17",DateFormatUtil.SQL_FORMAT));
//        WorkOrderFlowVO  w4 = new WorkOrderFlowVO();
//        w4.setStaffNo("2");
//        w4.setStartTime(DateFormatUtil.stringToDate("20210-1-19",DateFormatUtil.SQL_FORMAT));
//        w4.setEndTime(DateFormatUtil.stringToDate("20210-1-20",DateFormatUtil.SQL_FORMAT));
//
//        WorkOrderFlowVO  w5 = new WorkOrderFlowVO();
//        w5.setStaffNo("1");
//        w5.setStartTime(DateFormatUtil.stringToDate("20210-1-22",DateFormatUtil.SQL_FORMAT));
//        w5.setEndTime(DateFormatUtil.stringToDate("20210-1-25",DateFormatUtil.SQL_FORMAT));
//
//
//        WorkOrderFlowVO  w6 = new WorkOrderFlowVO();
//        w6.setStaffNo("1");
//        w6.setStartTime(DateFormatUtil.stringToDate("20210-1-27",DateFormatUtil.SQL_FORMAT));
//        w6.setEndTime(DateFormatUtil.stringToDate("20210-1-28",DateFormatUtil.SQL_FORMAT));
//        List<WorkOrderFlowVO> list = new ArrayList<>();
//        list.add(w1);list.add(w2);list.add(w3);list.add(w4);list.add(w5);list.add(w6);
//
//
//        System.err.println(JSONUtil.toJsonStr(orderdingWorkOrderFlowVOList(list)));
//    }


    @Autowired
    private cn.net.yzl.crm.client.workorder.WorkOrderClient workOrderClients;


    @ApiOperation("顾客画像-顾客旅程")
    @GetMapping("v1/getCustomerJourney")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "year", value = "年份", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页,默认1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数，默认15", dataType = "Integer", paramType = "query")
    })
    public ComResponse<Page<MemberCustomerJourneyDto>> getCustomerJourney(QueryWorkOrderVo queryVo) {
        // 获取工单信息
        List<MemberCustomerJourneyDto>  list = null;
        Page<MemberCustomerJourneyDto> page = new Page<>();
        String year = queryVo.getYear();
        ComResponse<Page<WorkOrderVo>> listComResponse = workOrderClients.queryWorkOrder(queryVo);
        if(StringUtils.isEmpty(year)){
            year = DateFormatUtil.dateToString(new Date(),"yyyy");
        }

        if(listComResponse == null || listComResponse.getData()==null || CollectionUtil.isEmpty(listComResponse.getData().getItems())){
            return ComResponse.success(page).setMessage(year);
        }
        List<WorkOrderVo> data = listComResponse.getData().getItems();
        String sourcesJson = JSONUtil.toJsonStr(data);
        list = JsonUtil.jsonToList(sourcesJson, MemberCustomerJourneyDto.class);
        // 获取订单信息
        for (MemberCustomerJourneyDto memberCustomerJourneyDto : list) {
            String id = memberCustomerJourneyDto.get_id();
            Integer workOrderCode = memberCustomerJourneyDto.getWorkOrderCode();
            ComResponse<List<PortraitOrderDetailDTO>> portraitOrderDetail = orderSearchClient.getPortraitOrderDetail(workOrderCode + "", id);
            List<PortraitOrderDetailDTO> data1 = portraitOrderDetail.getData();
            if(!StringUtils.isEmpty(data1)){
                memberCustomerJourneyDto.setPortraitOrderDetailList(data1);
            }

        }
        Date start = null;
        Date end = null;
        PageParam pageParam = listComResponse.getData().getPageParam();

        String yearFirstDay = year + "-01-01 00:00:00";
        String nextYearFirstDay =  Integer.valueOf(year)+1  + "-01-01 00:00:00";
        //时间为空，则最大时间为下一年的第一天0点
        if (StringUtils.isEmpty(queryVo.getEndDate())) {
            end = DateUtil.parse(nextYearFirstDay, "yyyy-MM-dd HH:mm:ss");
        }else{
            end = DateUtil.parse(queryVo.getEndDate(), "yyyy-MM-dd HH:mm:ss");
        }

        //当只有一页时 和 查询最后一页时，设置开始时间为本年第一天开始
        if (pageParam.getPageTotal() == 1 || pageParam.getPageNo() == pageParam.getPageTotal()){
            start = DateUtil.parse(yearFirstDay, "yyyy-MM-dd HH:mm:ss");
        }else{
            //开始时间  = 工单的最小创建时间
            start = DateUtil.parse(DateUtil.format(data.get(data.size()-1).getCreateTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        }
        // 获取会员等级
        MemberGrandSelectVo vo = new MemberGrandSelectVo();
        vo.setMemberCard(queryVo.getMemberCard());
        vo.setStartDate(start);
        vo.setEndDate(end);
        ComResponse<List<MemberGradeRecordDto>> memberGradeRecordList = memberFien.getMemberGradeRecordListByTimeRange(vo);
        if(memberGradeRecordList.getData()!=null && memberGradeRecordList.getData().size()>0){
            for (MemberGradeRecordDto datum : memberGradeRecordList.getData()) {
                MemberCustomerJourneyDto memberCustomerJourneyDto = new MemberCustomerJourneyDto();
                memberCustomerJourneyDto.setWorkOrderType(3);
                memberCustomerJourneyDto.setCreateTime(datum.getCreateTime());
                memberCustomerJourneyDto.setMemberGradeRecordDto(datum);
                list.add(memberCustomerJourneyDto);
            }
        }

        //获取首次呼入
//        ComResponse<MemberFirstCallDetailsDTO> cardResult = workOrderClient.getCallInDetailsByMemberCard(queryVo.getMemberCard());
//        if (cardResult.getData() != null) {
//            MemberFirstCallDetailsDTO cardResultData = cardResult.getData();
//            if (cardResultData.getCreateTime() != null) {
//                MemberCustomerJourneyDto memberCustomerJourneyDto = new MemberCustomerJourneyDto();
//                memberCustomerJourneyDto.setWorkOrderType(4);
//                memberCustomerJourneyDto.setCreateTime(cardResultData.getCreateTime());
//                memberCustomerJourneyDto.setMemberFirstCallDetailsDTO(cardResultData);
//                list.add(memberCustomerJourneyDto);
//            }
//        }

        //设置会员级别编号转成名称
        if (CollectionUtil.isNotEmpty(list)) {
            //获取DMC会员级别
            PageModel pageModel = new PageModel();
            pageModel.setPageNo(1);
            pageModel.setPageSize(20);
            Map<String, String> gradeMap = new HashMap<>();
            ComResponse<Page<MemberLevelResponse>> levelPages = activityClient.getMemberLevelPages(pageModel);
            if (levelPages != null && levelPages.getData() != null && CollectionUtil.isNotEmpty(levelPages.getData().getItems())){
                List<MemberLevelResponse> items = levelPages.getData().getItems();
                for (MemberLevelResponse item : items) {
                    gradeMap.put(String.valueOf(item.getMemberLevelGrade()), item.getMemberLevelName());
                }
            }

            for (MemberCustomerJourneyDto journeyDto : list) {
                WorkOrderDisposeFlowSubBean subBean = journeyDto.getWorkOrderDisposeFlowSubBean();
                if (subBean != null) {
                    subBean.setMGradeCode(gradeMap.get(subBean.getMGradeCode()));
                }
            }
        }

        page.setPageParam(pageParam);
        // 根据时间排序
        list = list.stream().sorted(Comparator.comparing(MemberCustomerJourneyDto::getCreateTime).reversed()).collect(Collectors.toList());
        page.setItems(list);
        return ComResponse.success(page).setMessage(year);
    }


    @ApiOperation("顾客画像-顾客旅程-首次呼入")
    @GetMapping("v1/getCallInDetailsByMemberCard")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<MemberCustomerJourneyDto> getCallInDetailsByMemberCard(String memberCard) {
        //获取首次呼入
        MemberCustomerJourneyDto memberCustomerJourneyDto = null;
        ComResponse<MemberFirstCallDetailsDTO> cardResult = workOrderClient.getCallInDetailsByMemberCard(memberCard);
        if (cardResult.getData() != null) {
            MemberFirstCallDetailsDTO cardResultData = cardResult.getData();
            if (cardResultData.getCreateTime() != null) {
                memberCustomerJourneyDto = new MemberCustomerJourneyDto();
                memberCustomerJourneyDto.setWorkOrderType(4);
                memberCustomerJourneyDto.setCreateTime(cardResultData.getCreateTime());
                memberCustomerJourneyDto.setMemberFirstCallDetailsDTO(cardResultData);
            }
        }
        return ComResponse.success(memberCustomerJourneyDto).setMessage(DateFormatUtil.dateToString(new Date(),"yyyy"));
    }

    @ApiOperation("顾客画像-获取顾客病症(诊疗结果)")
    @GetMapping("v1/getMemberDisease")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<List<MemberDiseaseCustomerDto>> getMemberDisease(String memberCard) {
        ComResponse<List<MemberDiseaseCustomerDto>> result = memberFien.getMemberDisease(memberCard);
        if(result==null || result.getData()==null){
            return ComResponse.nodata();
        }
        List<MemberDiseaseCustomerDto> data1 = result.getData();
        // 获取 病症的父信息
        for (MemberDiseaseCustomerDto datum : data1) {
            int diseaseId = datum.getDiseaseId();
            ComResponse<List<DiseaseMainInfo>> listComResponse = diseaseClient.queryHierarchy(diseaseId + "");
            if(listComResponse!=null && listComResponse.getData()!=null && listComResponse.getData().size()>0){
                List<DiseaseMainInfo> data = listComResponse.getData();
                datum.setDiseasePid(data.get(0).getId());
                datum.setDiseasePname(data.get(0).getName());
            }
        }
        Map<Integer, List<MemberDiseaseCustomerDto>> collect = data1.stream().collect(Collectors.groupingBy(MemberDiseaseCustomerDto::getDiseasePid));
        List<MemberDiseaseCustomerDto> list = new ArrayList<>();
        for (Integer integer : collect.keySet()) {
            MemberDiseaseCustomerDto memberDiseaseCustomerDto = new MemberDiseaseCustomerDto();
            memberDiseaseCustomerDto.setDiseaseId(integer);
            memberDiseaseCustomerDto.setDiseaseName(collect.get(integer).get(0).getDiseasePname());
            memberDiseaseCustomerDto.setChild(collect.get(integer));
            list.add(memberDiseaseCustomerDto);
        }
        return ComResponse.success(list);
    }

    @ApiOperation("诊疗结果-新增顾客病症")
    @PostMapping("v1/insertMemberDisease")
    public ComResponse<Integer> getMemberDisease(@RequestBody @Validated MemberDiseaseDto memberDiseaseDto) {
        Integer id = null;
        String staffNo= QueryIds.userNo.get();
        ComResponse<Integer> diseaseMainInfoComResponse = diseaseClient.artificialSeatInput(memberDiseaseDto.getParDiseaseId(), memberDiseaseDto.getDiseaseName(), staffNo,memberDiseaseDto.getMemberCard());
        if(diseaseMainInfoComResponse!=null && diseaseMainInfoComResponse.getCode()==200 && diseaseMainInfoComResponse.getData()!=null){
            id=diseaseMainInfoComResponse.getData();
        }else{
            return ComResponse.fail(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getMessage());
        }
        memberDiseaseDto.setDiseaseId(id);
        memberDiseaseDto.setCreateNo(staffNo);
        ComResponse<Integer> integerComResponse = memberFien.insertMemberDisease(memberDiseaseDto);
        if(integerComResponse==null || integerComResponse.getCode()!=200){
            return integerComResponse;
        }
        return ComResponse.success(integerComResponse.getData());
    }


    @ApiOperation(value = "顾客收货地址-添加顾客收货地址", notes = "顾客收货地址-添加顾客收货地址")
    @RequestMapping(value = "/v1/addReveiverAddress", method = RequestMethod.POST)
    public ComResponse<String> addReveiverAddress(@RequestBody  @Validated ReveiverAddressInsertVO reveiverAddressInsertVO) throws IllegalAccessException {
        return memberFien.addReveiverAddress(reveiverAddressInsertVO);
    }

    @ApiOperation(value = "顾客收货地址-更新收货地址", notes = "顾客收货地址-更新收货地址")
    @RequestMapping(value = "/v1/updateReveiverAddress", method = RequestMethod.POST)
    public ComResponse<String> updateReveiverAddress(@RequestBody @Validated ReveiverAddressUpdateVO reveiverAddressUpdateVO) throws IllegalAccessException {
        return memberFien.updateReveiverAddress(reveiverAddressUpdateVO);
    }

    @ApiOperation(value = "顾客收货地址-获取顾客收货地址", notes = "顾客收货地址-获取顾客收货地址")
    @RequestMapping(value = "/v1/getReveiverAddress", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<List<ReveiverAddressDto>> getReveiverAddress(String memberCard) {
        return memberFien.getReveiverAddress(memberCard);
    }



    @ApiOperation("保存顾客购买能力")
    @PostMapping("/v1/saveMemberOrderStat")
    public GeneralResult saveMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat) {
        if (memberOrderStat == null) throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        if (memberOrderStat.getId() == 0) {
            memberFien.addMemberOrderStat(memberOrderStat);
        } else {
            memberFien.updateMemberOrderStat(memberOrderStat);
        }
        return GeneralResult.success();
    }
@Autowired
private ProductClient productClient;
    @ApiOperation("获取顾客购买能力")
    @GetMapping("/v1/getMemberOrderStat")
    public GeneralResult<MemberOrderStat> getMemberOrderStat(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card
    ) {
        GeneralResult<MemberOrderStat> result = memberFien.getMemberOrderStat(member_card);
        if(result.getData()!=null){
            MemberOrderStat data = result.getData();
            // 首次的购买商品编号
            String first_buy_product_code = data.getFirst_buy_product_code();
            ComResponse<List<ProductMainDTO>> listComResponse = productClient.queryByProductCodes(first_buy_product_code);
            if(listComResponse.getData()!=null && listComResponse.getData().size()>0){
                StringBuffer str = new StringBuffer();
                listComResponse.getData().forEach(productMainDTO -> {
                    String name = productMainDTO.getName();
                    if(StrUtil.isNotBlank(name)){
                        str.append(name+",");
                    }
                });
                if(str.toString().lastIndexOf(",")>0){
                    data.setFirstBuyProductNames(str.substring(0,str.length()-1));
                }
            }

            //最后一次购买的商品标号
            String last_buy_product_code = data.getLast_buy_product_code();
            listComResponse = productClient.queryByProductCodes(last_buy_product_code);
            if(listComResponse.getData()!=null && listComResponse.getData().size()>0){
                StringBuffer str = new StringBuffer();
                listComResponse.getData().forEach(productMainDTO -> {
                    String name = productMainDTO.getName();
                    if(StrUtil.isNotBlank(name)){
                        str.append(name+",");
                    }
                });
                if(str.toString().lastIndexOf(",")>0){
                    data.setLastBuyProductNames(str.substring(0,str.length()-1));
                }
            }
        }

        return result;
    }

//    @ApiOperation("保存顾客行为偏好")
//    @PostMapping("/v1/saveMemberAction")
//    public GeneralResult saveMemberAction(@RequestBody MemberAction memberAction) {
//        if (memberAction == null || StringUtil.isNullOrEmpty(memberAction.getMember_card()))
//            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
//        GeneralResult<MemberAction> result = memberFien.getMemberAction(memberAction.getMember_card());
//        if (result.getData() == null) {
//            memberFien.addMemberAction(memberAction);
//        } else {
//            memberFien.updateMemberAction(memberAction);
//        }
//        return GeneralResult.success();
//    }
//
//    @ApiOperation("获取顾客行为偏好")
//    @GetMapping("/v1/getMemberAction")
//    public GeneralResult getMemberAction(
//            @RequestParam("member_card")
//            @NotBlank(message = "member_card不能为空")
//            @ApiParam(name = "member_card", value = "会员卡号", required = true)
//                    String member_card
//    ) {
//        GeneralResult<MemberAction> result = memberFien.getMemberAction(member_card);
//        return result;
//    }

    @ApiOperation("获取沟通记录")
    @PostMapping("/v1/getCallRecard")
    public ComResponse<Page<StaffCallRecord>> getCallRecard(@RequestBody CallInfoDTO callInfoDTO) {
        if (callInfoDTO == null || callInfoDTO.getPageSize() == 0 || callInfoDTO.getPageNo() == 0) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse<Page<StaffCallRecord>> response = workOrderClient.getCallRecord(callInfoDTO);
        Page<StaffCallRecord> data = response.getData();
        if(!StringUtils.isEmpty(data) && !StringUtils.isEmpty(data.getItems()) && data.getItems().size() > 0){
            data.getItems().stream().forEach(staffCallRecord -> {
                staffCallRecord.setConversionDuration(MonggoDateHelper.getDate(staffCallRecord.getDuration()));
            });
        }
        return response;
    }

    @ApiOperation("获取顾客行为偏好字典数据")
    //@GetMapping("/v1/getMemberActions")
    public ComResponse getMemberActions() {
        return ComResponse.success(memberFien.getMemberActions());
    }



    @ApiOperation(value = "顾客账户-获取顾客账户信息", notes = "顾客账户-获取顾客账户信息")
    @RequestMapping(value = "/getMemberAmount", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
    })
    ComResponse<MemberAmountDto> getMemberAmount(@RequestParam("memberCard") String  memberCard) {
        return memberFien.getMemberAmount(memberCard);
    }
    @ApiOperation(value = "顾客账户-获取余额明细", notes = "顾客账户-获取余额明细")
    @RequestMapping(value = "/getMemberAmountDetailList", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "timeFlag", value = "时间标识(1:最近三个月,2:三个月以前的)", required = true, dataType = "Int", paramType = "query"),
    })
    ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(@RequestParam("memberCard") String  memberCard, @RequestParam("timeFlag") Integer timeFlag) throws ParseException {
        return memberFien.getMemberAmountDetailList(memberCard,timeFlag);
    }

    /**
     * 修改顾客服用效果记录
     *
     * @param productEffects
     * @return
     */
    @ApiOperation(value = "修改顾客服用效果记录", notes = "修改顾客服用效果记录")
    @PostMapping(value = "/v1/batchModifyProductEffect")
    public ComResponse batchModifyProductEffect(
            @RequestBody List<MemberProductEffectUpdateVO> productEffects) {
        ComResponse result = memberProductEffectFien.batchModifyProductEffect(productEffects);
        return result;
    }


    @ApiOperation(value = "获取顾客服用效果记录", notes = "获取顾客服用效果记录")
    @PostMapping(value = "/v1/getProductEffects")
    public ComResponse<List<MemberProductEffectDTO>> getProductEffects(
            @RequestBody MemberProductEffectSelectVO productEffect) {
        ComResponse<List<MemberProductEffectDTO>> result = memberProductEffectFien.getProductEffects(productEffect);
        return result;
    }


    @ApiOperation(value = "顾客类别查询", notes = "顾客类别查询")
    @GetMapping(value = "/v1/queryMemberType")
    public ComResponse<List<MemberTypeDTO>> queryMemberType() {
        ComResponse<List<MemberTypeDTO>> result = memberTypeFien.queryMemberType();
        return result;
    }

    @ApiOperation(value = "顾客管理-处理工单时更新顾客信息",notes = "顾客管理-处理工单时更新顾客信息")
    @RequestMapping(value = "/v1/dealWorkOrderUpdateMemberData", method = RequestMethod.POST)
    public ComResponse<Boolean> dealWorkOrderUpdateMemberData(@RequestBody @Validated  MemberWorkOrderInfoVO workOrderInfoVO) {
        ComResponse<Boolean> result = memberFien.dealWorkOrderUpdateMemberData(workOrderInfoVO);
        return result;
    }




}
