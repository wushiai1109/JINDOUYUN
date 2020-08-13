package com.jindouyun.wx.task;

import com.jindouyun.core.system.SystemConfig;
import com.jindouyun.core.task.Task;
import com.jindouyun.common.util.BeanUtil;
import com.jindouyun.db.domain.JindouyunOrder;
import com.jindouyun.db.domain.JindouyunOrderGoods;
import com.jindouyun.db.service.JindouyunGoodsProductService;
import com.jindouyun.db.service.JindouyunOrderGoodsService;
import com.jindouyun.db.service.JindouyunOrderService;
import com.jindouyun.db.util.OrderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName OrderUnpaidTask
 * @Description
 * @Author Bruce
 * @Date 2020/7/25 3:22 下午
 */
public class OrderUnpaidTask extends Task {
    private final Log logger = LogFactory.getLog(OrderUnpaidTask.class);
    private int orderId = -1;

    public OrderUnpaidTask(Integer orderId, long delayInMilliseconds){
        super("OrderUnpaidTask-" + orderId, delayInMilliseconds);
        this.orderId = orderId;
    }

    //30分钟超时
    public OrderUnpaidTask(Integer orderId){
        super("OrderUnpaidTask-" + orderId, SystemConfig.getOrderUnpaid() * 60 * 1000);
        this.orderId = orderId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---订单超时未付款---" + this.orderId);

        JindouyunOrderService orderService = BeanUtil.getBean(JindouyunOrderService.class);
        JindouyunOrderGoodsService orderGoodsService = BeanUtil.getBean(JindouyunOrderGoodsService.class);
        JindouyunGoodsProductService productService = BeanUtil.getBean(JindouyunGoodsProductService.class);

        JindouyunOrder order = orderService.findById(this.orderId);
        if(order == null){
            return;
        }
        if(!OrderUtil.isCreateStatus(order)){
            return;
        }

        // 设置订单已取消状态
        order.setOrderStatus(OrderUtil.STATUS_AUTO_CANCEL);
        order.setEndTime(LocalDateTime.now());
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("更新数据已失效");
        }

        // 商品货品数量增加
        Integer orderId = order.getId();
        List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.queryByOid(orderId);
        for (JindouyunOrderGoods orderGoods : orderGoodsList) {
            Integer productId = orderGoods.getProductId();
            Short number = orderGoods.getNumber();
            if (productService.addStock(productId, number) == 0) {
                throw new RuntimeException("商品货品库存增加失败");
            }
        }
        logger.info("系统结束处理延时任务---订单超时未付款---" + this.orderId);
    }
}
