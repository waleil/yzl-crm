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
            Integer financialOwner = 123;
            //生成批次号
            String batchNo = SnowFlakeUtil.getId() + "";
            String productCodes = dto.getProducts().stream().map(Product4OrderDTO::getProductCode)
                    .collect(Collectors.joining(","));

            List<ProductDTO> productDTOS = searchProducts(productCodes,dto.getProducts().size());
            Map<String, ProductDTO> collect = productDTOS.stream()
                    .collect(Collectors.toMap(ProductDTO::getProductCode, product4OrderDTO -> product4OrderDTO));
            //组织商品明细
            List<OrderTempProduct> orderTempProducts = new ArrayList<>();
            dto.getProducts().forEach(map ->{
                ProductDTO productDTO = collect.get(map.getProductCode());
                OrderTempProduct product = new OrderTempProduct();

                product.setOrderTempProductCode(SnowFlakeUtil.getId()+"");
                product.setOrderTempCode(batchNo);
                product.setProductCode(map.getProductCode());
                product.setProductBarCode(productDTO.getBarCode());
                product.setProductName(productDTO.getName());
                product.setCount(map.getCount());
                product.setPrice(productDTO.getSalePrice());
                product.setSpec(productDTO.getTotalUseNum().toString());
                product.setUnit(productDTO.getUnit());
                orderTempProducts.add(product);
            });

            //查询群组信息
            List<CrowdGroup> groups = searchGroups(dto.getCustomerGroupIds());
            //组织群组信息
            List<OrderTemp> list = mkOrderTemp(groups,batchNo,dto,departId,wordCode,financialOwner);

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
            reduceVOS.forEach(map ->{
                ProductDTO productDTO = collect.get(map.getProductCode());
                if(map.getNum() >productDTO.getStock()){
                    throw new BizException(ResponseCodeEnums.RESUME_EXIST_ERROR_CODE.getCode(),"库存不足，商品名称：" + productDTO.getName());
                }

            });
            vo.setProductReduceVOS(reduceVOS);


            //调用扣减内存接口（根据批次号扣减库存）
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


        return ComResponse.success(true);
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
    List<OrderTemp> mkOrderTemp(List<CrowdGroup> groups,String batchNo ,NewOrderDTO dto,int departId,Integer wordCode,Integer financialOwner){
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
            orderTemp.setFinancialOwner(financialOwner);
            list.add(orderTemp);


        });
        return list;
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


}
