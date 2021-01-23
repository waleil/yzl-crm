package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.common.util.SnowFlakeUtil;
import cn.net.yzl.crm.client.order.NewOrderClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.micservice.MemberGroupFeign;
import cn.net.yzl.crm.service.order.INewOrderService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.order.model.db.order.OrderTemp;
import cn.net.yzl.order.model.db.order.OrderTempProduct;
import cn.net.yzl.order.model.vo.order.CustomerGroup;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;
import cn.net.yzl.order.model.vo.order.Product4OrderDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDTO;
import cn.net.yzl.product.model.vo.product.vo.BatchProductVO;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductReduceVO;
import io.swagger.models.auth.In;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewOrderServiceImpl implements INewOrderService {
    private static final Logger log = LoggerFactory.getLogger( NewOrderServiceImpl.class );

    @Value("${api.gateway.url}")
    private String path;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RestTemplate template;

    @Autowired
    private NewOrderClient newOrderClient;

    ThreadLocal<List<Map<String,Object>>> local = new ThreadLocal<>();
    @Autowired
    private ProductClient productClient;
    @Autowired
    private MemberGroupFeign memberFien;

    @Autowired
    private EhrStaffClient ehrStaffClient;


    /**
     * 新建订单
     */
    @Override
    public ComResponse<Boolean> newOrder(NewOrderDTO dto) {
        ComResponse  orderRes = null;
        try{
            local.set(new ArrayList<>());
            //查询坐席时间
            ComResponse<StaffImageBaseInfoDto> response = ehrStaffClient.getDetailsByNo(dto.getUserNo());
            if(response.getCode().compareTo(Integer.valueOf(200))!=0){
                throw new BizException(response.getCode(),response.getMessage());
            }
            Integer wordCode = response.getData().getWorkCode();
            Integer departId = response.getData().getDepartId();
            //todo 根据部门编号获取财务归属
            //生成批次号
            String batchNo = SnowFlakeUtil.getId() + "";
            String productCodes = dto.getProducts().stream().map(Product4OrderDTO::getProductCode)
                    .collect(Collectors.joining(","));

            List<ProductDTO> productDTOS = searchProducts(productCodes,dto.getProducts().size());
            Map<String, Product4OrderDTO> collect = dto.getProducts().stream()
                    .collect(Collectors.toMap(Product4OrderDTO::getProductCode, product4OrderDTO -> product4OrderDTO));
            //组织商品明细
            List<OrderTempProduct> orderTempProducts = new ArrayList<>();
            productDTOS.forEach(map ->{
                OrderTempProduct product = new OrderTempProduct();

                product.setOrderTempProductCode(SnowFlakeUtil.getId()+"");
                product.setOrderTempCode(batchNo);
                product.setProductCode(map.getProductCode());
                product.setProductBarCode(map.getBarCode());
                product.setProductName(map.getName());
                product.setCount(collect.get(map.getProductCode()).getCount());
                product.setPrice(map.getSalePrice());
                product.setSpec(map.getTotalUseNum().toString());
                product.setUnit(map.getUnit());
                orderTempProducts.add(product);
            });

            //查询群组信息
            List<CrowdGroup> groups = searchGroups(dto.getCustomerGroupIds());
            //组织群组信息
            List<OrderTemp> list = new ArrayList<>();
            groups.forEach(map ->{
                OrderTemp orderTemp = new OrderTemp();
                orderTemp.setOrderTempCode(batchNo);
                orderTemp.setGroupId(map.get_id());
                orderTemp.setGroupCount(map.getPerson_count());
                orderTemp.setOprCount(0);
                orderTemp.setSuccessCount(0);
                orderTemp.setFailCount(0);
                orderTemp.setTotalAmt(dto.getTotalOrderAMT());
                orderTemp.setDiscount(0.00);
                orderTemp.setPfee(dto.getPfee());
                orderTemp.setRemark(dto.getRemark());
                orderTemp.setRelationOrder(dto.getRelationOrder());
                orderTemp.setExpressFlag(dto.getAssignExpressFlag());
                orderTemp.setExpressCode(dto.getExpressCode());
                orderTemp.setOprStats(0);
                orderTemp.setCreateCode(dto.getUserNo());
                orderTemp.setCreateName(dto.getUserName());
                orderTemp.setUpdateCode(dto.getUserNo());
                orderTemp.setUpdateName(dto.getUserName());
                orderTemp.setDepartId(departId);
                orderTemp.setWorkCode(wordCode.toString());
                //todo
                orderTemp.setFinancialOwner(123);


            });
            //人数
            Integer count = groups.stream().mapToInt(CrowdGroup ::getPerson_count).sum();
            BatchProductVO vo = new BatchProductVO();
            vo.setBatchNo(batchNo);
            List<ProductReduceVO> reduceVOS = new ArrayList();
            dto.getProducts().forEach(map -> {
                ProductReduceVO reduceVO = new ProductReduceVO();
                reduceVO.setProductCode(map.getProductCode());
                reduceVO.setNum(map.getCount() * count);
                reduceVOS.add(reduceVO);

            });
            //校验库存

            vo.setProductReduceVOS(reduceVOS);
//            reduceVOS.forEach();


            //todo 调用扣减内存接口（根据批次号扣减库存）
            orderRes = productClient.productReduce(vo);
            if(orderRes.getCode().compareTo(200) == 0){
                Map map = new HashMap();
                map.put("url",path + "/productServer/product/v1/increaseStock");
                map.put("parm",vo);
                local.get().add(map);

            }

            //调用新建订单接口
            orderRes = newOrderClient.newOrder(dto);

           if( orderRes.getCode().compareTo(Integer.valueOf(200))!=0){
               throw new BizException(orderRes.getCode(),orderRes.getMessage());
           }


        }catch (BizException e){
            log.error(e.getMessage(),e);
            //开始回退
            revert(local.get());
            throw new BizException(e.getCode(),e.getMessage());
        }


        return null;
    }

    /**
     * 组合成最终处理的dto类，用于发送订单服务
     */
    public NewOrderDTO mkFinalOrderInfo(NewOrderDTO dto){
        //校验商品是否合规
        if(dto.getProducts() == null || dto.getProducts().size() == 0){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"商品不能为空");
        }
        String products = dto.getProducts().stream().map(Product4OrderDTO::getProductCode).collect(Collectors.joining(","));
//        List<Product4OrderDTO> list = searchProducts(products,dto.getProducts());
//        dto.setProducts(list);

        //查询顾客群信息
        if(dto.getCustomerGroupIds() == null || dto.getCustomerGroupIds().size() == 0 ){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"顾客群不能为空");
        }

//        List<CustomerGroup> groups = searchGroups(dto.getCustomerGroupIds());

        //计算各各个商品所需所需数量
        //各个群组 累计人数
//        int count = dto.getGroups().stream().collect(Collectors.summingInt(CustomerGroup::getGroupcount));
        OrderProductVO vo = new OrderProductVO();
        List<ProductReduceVO> reduceVOS = new ArrayList<>();

        dto.getProducts().forEach(map -> {
            ProductReduceVO reduceVO = new ProductReduceVO();
            reduceVO.setProductCode(map.getProductCode());
//            reduceVO.setNum(map.getCount() * count);
            reduceVOS.add(reduceVO);

        });
        vo.setProductReduceVOS(reduceVOS);

        //生成订单号 HK+userNo +年月日时分秒毫秒+ 6位数字
        String seq = String.format("%06d",redisUtil.incr("OrderSeq",1000));
        String orderNo = "HK" + DateFormatUtil.dateToString(new Date(),"YYYYMMDDHHmmssSSS") + seq;
        dto.setOrderTempNo(orderNo);

        return dto;
    }
//    /**
//     * 计算需要扣减的库存信息,并校验库存
//     */
//    public BatchProductVO caculateStackInfo(List<OrderTempProduct> list){
//
//        OrderProductVO vo = new OrderProductVO();
//        List<ProductReduceVO> reduceVOS = new ArrayList<>();
//
//        list.forEach(map -> {
//            ProductReduceVO reduceVO = new ProductReduceVO();
//            reduceVO.setProductCode(map.getProductCode());
////            reduceVO.setNum(map.getCount() * count);
//            reduceVOS.add(reduceVO);
//
//        });
//        vo.setProductReduceVOS(reduceVOS);
//        //校验库存
////        checkStore(reduceVOS,dto.getProducts());
//        return vo;
//    }



    /**
     * 校验库存的方法
     * @param reduceVOS 需要扣减库存的商品列表
     * @param products 商品列表，存储商品库存
     */
    private void checkStore(List<ProductReduceVO> reduceVOS, List<Product4OrderDTO> products) {
//        products.forEach(map ->{
//            reduceVOS.forEach(item ->{
//                if(map.getProductCode().equals(item.getProductCode())){
//                    //当库存为-1时，无需校验
//                    if(map.getStock() != -1){
//                        if(item.getNum()>map.getStock()){
//                            throw new BizException(ResponseCodeEnums.RESUME_EXIST_ERROR_CODE.getCode(),"库存不足");
//                        }
//                    }
//                }
//
//
//            });
//        });
    }

    /**
     * 执行失败回调方法
     * @param list
     */
    private void revert(List<Map<String,Object>> list){
        if(list != null && list.size()>0){
            list.forEach(map ->{
                sentHttpRequest(map.get("url").toString(),map.get("parm"));
            });
        }

    }



    /**
     * 发送http请求，用于回调
     */
    protected void sentHttpRequest(String url,Object param){
        ComResponse responseBean = template.postForObject(url, param, ComResponse.class);
    }

    /**
     * 查询群组信息
     * @param groupCodes
     * @return
     */
    protected List<CrowdGroup> searchGroups(List<String> groupCodes){
        String groupsStr = groupCodes.stream().collect(Collectors.joining(","));
        //查询顾客群组接口
        ComResponse<List<CrowdGroup>> response = memberFien.getCrowdGroupList(groupsStr);
        if(response.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(response.getCode(),response.getMessage());
        }
        if(response.getData() !=null && (groupCodes.size() != response.getData().size())){
            throw new BizException(ResponseCodeEnums.REPEAT_ERROR_CODE.getCode(),"部分群组已失效");
        }

       return response.getData();
    }

    /**
     * 查询商品列表
     * @param productCodes
     * @param
     * @return
     */
    protected List<ProductDTO> searchProducts(String productCodes,int prodCnt){
        ComResponse<List<ProductDTO>> prd = productClient.queryByCodes(productCodes);
        if(prd.getCode().compareTo(200)!=0){
            throw new BizException(prd.getCode(),prd.getMessage());
        }
        if(prd.getData() !=null && (prodCnt != prd.getData().size())){
            throw new BizException(ResponseCodeEnums.REPEAT_ERROR_CODE.getCode(),"部分商品已下架");
        }

        return prd.getData();
    }

    @Override
    public void oprData(List list) {

    }


    /**
     * excel数据格式化
     * @param newOrderExcelInDTOS
     * @return
     */
//    protected List<NewOrderDTO> formateData(List<NewOrderExcelInDTO> newOrderExcelInDTOS) {
//        List<NewOrderDTO> result = new ArrayList<>();
//        for (NewOrderExcelInDTO newOrderExcelInDTO : newOrderExcelInDTOS) {
//            NewOrderDTO newOrderDTO = new NewOrderDTO();
//            List<CustomerGroup> groups = new ArrayList<>();
//            List<Product4OrderDTO> products = new ArrayList<>();
//            List<String> groupids = new ArrayList<>();
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getGroupid1())) {
//                groupids.add(newOrderExcelInDTO.getGroupid1());
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getGroupid2())) {
//                groupids.add(newOrderExcelInDTO.getGroupid2());
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getGroupid3())) {
//                groupids.add(newOrderExcelInDTO.getGroupid3());
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getGroupid4())) {
//                groupids.add(newOrderExcelInDTO.getGroupid4());
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getGroupid5())) {
//                groupids.add(newOrderExcelInDTO.getGroupid5());
//            }
//
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getProductNo1())) {
//                if (newOrderExcelInDTO.getProductCont1() != null) {
//                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "商品编号和商品数量必须成对出现");
//                }
//                Product4OrderDTO product = new Product4OrderDTO();
//                product.setProductCode(newOrderExcelInDTO.getProductNo1());
//                product.setCount(newOrderExcelInDTO.getProductCont1());
//                products.add(product);
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getProductNo2())) {
//                if (newOrderExcelInDTO.getProductCont2() != null) {
//                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "商品编号和商品数量必须成对出现");
//                }
//                Product4OrderDTO product = new Product4OrderDTO();
//                product.setProductCode(newOrderExcelInDTO.getProductNo2());
//                product.setCount(newOrderExcelInDTO.getProductCont2());
//                products.add(product);
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getProductNo3())) {
//                if (newOrderExcelInDTO.getProductCont3() != null) {
//                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "商品编号和商品数量必须成对出现");
//                }
//                Product4OrderDTO product = new Product4OrderDTO();
//                product.setProductCode(newOrderExcelInDTO.getProductNo3());
//                product.setCount(newOrderExcelInDTO.getProductCont3());
//                products.add(product);
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getProductNo4())) {
//                if (newOrderExcelInDTO.getProductCont4() != null) {
//                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "商品编号和商品数量必须成对出现");
//                }
//                Product4OrderDTO product = new Product4OrderDTO();
//                product.setProductCode(newOrderExcelInDTO.getProductNo4());
//                product.setCount(newOrderExcelInDTO.getProductCont4());
//                products.add(product);
//            }
//            if (!StringUtils.isEmpty(newOrderExcelInDTO.getProductNo5())) {
//                if (newOrderExcelInDTO.getProductCont5() != null) {
//                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "商品编号和商品数量必须成对出现");
//                }
//                Product4OrderDTO product = new Product4OrderDTO();
//                product.setProductCode(newOrderExcelInDTO.getProductNo5());
//                product.setCount(newOrderExcelInDTO.getProductCont5());
//                products.add(product);
//            }
//            newOrderDTO.setGroups(groups);
//            newOrderDTO.setProducts(products);
//            result.add(newOrderDTO);
//        }
//        return result;
//    }
}
