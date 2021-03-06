package com.melonltd.naber.rdbms.model.facade.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mchange.lang.IntegerUtils;
import com.melonltd.naber.endpoint.util.Tools;
import com.melonltd.naber.rdbms.model.bean.OrderInfo;
import com.melonltd.naber.rdbms.model.bean.OrderLog;
import com.melonltd.naber.rdbms.model.bean.SellerOrderFinish;
import com.melonltd.naber.rdbms.model.bean.UserOrderInfo;
import com.melonltd.naber.rdbms.model.service.AccountInfoService;
import com.melonltd.naber.rdbms.model.service.OrderInfoService;
import com.melonltd.naber.rdbms.model.service.OrderLogService;
import com.melonltd.naber.rdbms.model.service.SellerOrderFinishService;
import com.melonltd.naber.rdbms.model.service.UserOrderInfoService;
import com.melonltd.naber.rdbms.model.type.Enable;
import com.melonltd.naber.rdbms.model.type.OrderStatus;
import com.melonltd.naber.rdbms.model.vo.AccountInfoVo;
import com.melonltd.naber.rdbms.model.vo.OrderVo;

@Service("changeOrdarService")
public class ChangeOrdarService {
	private static final Logger LOGGERO = LoggerFactory.getLogger(ChangeOrdarService.class);
	
	private static List<OrderStatus> FINISH_TYPE = OrderStatus.getSellerFinishType();
	private static List<OrderStatus> UPDATE_TYPE = OrderStatus.getSellerUpdateType();
	private static List<OrderStatus> SELLER_CANCEL = OrderStatus.getSellerCancelType();
	
	@Autowired
	private AccountInfoService accountInfoService;
	@Autowired
	private OrderInfoService orderInfoService;
	@Autowired
	private UserOrderInfoService userOrderInfoService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private SellerOrderFinishService sellerOrderFinishService;

	public OrderVo updateOrder(String orderUUID, OrderStatus changeStatus) {
		OrderVo infoVo = orderInfoService.findOne(orderUUID);
//		OrderVo logVo = orderLogService.findOne(orderUUID);
		OrderVo userLogVo = userOrderInfoService.findOne(orderUUID);

		if (ObjectUtils.allNotNull(infoVo, userLogVo)) {
			// 訂單目前狀態
			OrderStatus orderStatus = OrderStatus.of(infoVo.getStatus());

			// 更新時間GMT
			String date = Tools.getNowGMT();

			OrderLog orderLog = newOrderLog(infoVo, changeStatus, date, Enable.Y);
			UserOrderInfo userOrderLog = newUserOrderInfo(infoVo, changeStatus, date, Enable.Y);

			// 處理結束的訂單
			if (FINISH_TYPE.contains(changeStatus)) {
				AccountInfoVo accout = accountInfoService.getCacheBuilderByKey(infoVo.getAccount_uuid(),false);
				
				boolean status = true;
				if (OrderStatus.FINISH.equals(changeStatus)) {
					// TODO 如果是完成訂單必須計算紅利給USER  
					int bonusSum = IntegerUtils.parseInt(infoVo.getOrder_bonus(), 0) + IntegerUtils.parseInt(accout.getBonus(), 0);
					status = accountInfoService.updateBonus(String.valueOf(bonusSum), accout.getAccount_uuid());
					LOGGERO.debug("processing order FINISH update user bonus ,user uuid {}, user bonus sum {}, order status:{}, change status:{}, order uuid:{}, date:{}", accout.getAccount_uuid(), bonusSum, orderStatus, changeStatus, orderUUID, date);
				}else if (SELLER_CANCEL.contains(changeStatus)) {
					// 取消或跑單，還回紅利給 USER
					int userUse = IntegerUtils.parseInt(accout.getUse_bonus(), 0);
					int oresrUse = IntegerUtils.parseInt(infoVo.getUse_bonus(), 0);
					if (oresrUse != 0 || oresrUse <= userUse) {
						int useBonusSum = userUse - oresrUse;
						status = accountInfoService.updateUseBonus(String.valueOf(useBonusSum),  accout.getAccount_uuid());
						LOGGERO.debug("processing order CANCEL, FAIL update user use bonus ,user uuid {}, user use bonus sum {}, order status:{}, change status:{}, order uuid:{}, date:{}", accout.getAccount_uuid(), useBonusSum, orderStatus, changeStatus, orderUUID, date);
					}
				}
				
				if (status) {
					OrderInfo orderInfo = newOrderInfo(infoVo, changeStatus, date, Enable.N);
					SellerOrderFinish finish = newOrderFinish(infoVo, changeStatus, date, Enable.Y);
					LOGGERO.debug("Processing order FINISH , order status:{}, change status:{}, order uuid:{}, date:{}",orderStatus,changeStatus,orderUUID, date);
					orderInfoService.save(orderInfo);
					userOrderInfoService.save(userOrderLog);
					orderLogService.save(orderLog);
					return sellerOrderFinishService.save(finish);
				}else {
					LOGGERO.debug("處理訂單處於完成計算紅利更新使用者紅利失敗 , order status:{}, change status:{}, order uuid:{}, date:{}", orderStatus, changeStatus, orderUUID, date);
					return null;
				}
				
//				OrderInfo orderInfo = newOrderInfo(infoVo, changeStatus, date, Enable.N);
//				SellerOrderFinish finish = newOrderFinish(infoVo, changeStatus, date, Enable.Y, accout);
//				LOGGERO.debug("處理結束的訂單 , order status:{}, change status:{}, order uuid:{}, date:{}",orderStatus,changeStatus,orderUUID, date);
//				orderInfoService.save(orderInfo);
//				userOrderLogService.save(userOrderLog);
//				orderLogService.save(orderLog);
//				return sellerOrderFinishService.save(finish);

			} else if (UPDATE_TYPE.contains(changeStatus)) {
				OrderInfo orderInfo = newOrderInfo(infoVo, changeStatus, date, Enable.Y);
				// 如果訂單處於為處理 只能更改狀態處理中或可領取
				if (!ObjectUtils.notEqual(orderStatus, OrderStatus.UNFINISH)) {
					LOGGERO.debug("處理訂單處於為未處理 , order status:{}, change status:{}, order uuid:{}, date:{}",orderStatus,changeStatus,orderUUID, date);
					orderInfoService.save(orderInfo);
					orderLogService.save(orderLog);
					return userOrderInfoService.save(userOrderLog);
				}

				// 如果訂單處於為製作中 只能更改狀態可領取，不可返回未處理
				if (!ObjectUtils.notEqual(orderStatus, OrderStatus.PROCESSING) && !ObjectUtils.notEqual(changeStatus, OrderStatus.CAN_FETCH)) {
					LOGGERO.debug("處理訂單處於為製作中 , order status:{}, change status:{}, order uuid:{}, date:{}",orderStatus,changeStatus,orderUUID, date);
					orderInfoService.save(orderInfo);
					orderLogService.save(orderLog);
					return userOrderInfoService.save(userOrderLog);
				}
				LOGGERO.debug("處理訂單處於為製作中 , order status:{}, change status:{}, order uuid:{}, date:{}",orderStatus,changeStatus,orderUUID, date);
				return null;
				// 如果訂單處於為可領取 只能更改狀態結束
			}else {
				LOGGERO.error("處理訂單狀態失敗 , order status:{}, change status:{}, order uuid:{}, date:{}",orderStatus,changeStatus,orderUUID, date);
				return null;
			}
		}
		LOGGERO.error("處理訂單狀態失敗 資料錯誤，查無該訂單資訊 oredr uuid :{}",orderUUID);
		return null;
	}

	private static OrderInfo newOrderInfo(OrderVo vo, OrderStatus status, String date, Enable enable) {
		OrderInfo info = OrderInfo.of(vo);
		info.setUpdateDate(date);
		info.setStatus(status.name());
		info.setEnable(enable.name());
		return info;
	}

	private static OrderLog newOrderLog(OrderVo vo, OrderStatus status, String date, Enable enable) {
		OrderLog info = OrderLog.of(vo);
		info.setUpdateDate(date);
		info.setStatus(status.name());
		info.setEnable(enable.name());
		return info;
	}

	private static UserOrderInfo newUserOrderInfo(OrderVo vo, OrderStatus status, String date, Enable enable) {
		UserOrderInfo info = UserOrderInfo.of(vo);
		info.setUpdateDate(date);
		info.setStatus(status.name());
		info.setEnable(enable.name());
		return info;
	}

	private static SellerOrderFinish newOrderFinish(OrderVo vo, OrderStatus status, String date, Enable enable) {
		SellerOrderFinish info = new SellerOrderFinish();
		info.setOrderUUID(vo.getOrder_uuid());
		info.setAccountUUID(vo.getAccount_uuid());
		info.setRestaurantUUID(vo.getRestaurant_uuid());
		info.setUserMessage(vo.getUser_message());
		info.setCreateDate(date);
		info.setUpdateDate(date);
		info.setOrderType(vo.getOrder_type());
		info.setOrderPrice(vo.getOrder_price());
		info.setUseBonus(vo.getUse_bonus());
		info.setOrderBonus(vo.getOrder_bonus());
		info.setFetchDate(vo.getFetch_date());
		info.setOrderData(vo.getOrder_data());
		info.setStatus(status.name());
		info.setEnable(enable.name());
		return info;
	}

}
