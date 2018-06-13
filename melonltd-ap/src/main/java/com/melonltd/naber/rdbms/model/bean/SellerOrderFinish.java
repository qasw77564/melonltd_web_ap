package com.melonltd.naber.rdbms.model.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;


@Entity
@Table(name = "seller_order_finish")
public class SellerOrderFinish implements Serializable {
	private static final long serialVersionUID = -7662206313159475007L;
	
	private String orderUUID;
	private String accountUUID;
	private String restaurantUUID;
	private String userMessage;
	private String createDate;
	private String updateDate;
	private String orderPrice;
	private String orderBonus;
	private String fetchDate;
	private String orderData;
	private String status;
	private String enable;

	@Id
	@Column(name = "order_uuid", unique = true, nullable = false)
	public String getOrderUUID() {
		return orderUUID;
	}

	@Column(name = "account_uuid")
	public String getAccountUUID() {
		return accountUUID;
	}

	@Column(name = "restaurant_uuid")
	public String getRestaurantUUID() {
		return restaurantUUID;
	}

	@Column(name = "user_message")
	public String getUserMessage() {
		return userMessage;
	}

	@Column(name = "create_date")
	public String getCreateDate() {
		return createDate;
	}

	@Column(name = "update_date")
	public String getUpdateDate() {
		return updateDate;
	}

	@Column(name = "order_price")
	public String getOrderPrice() {
		return orderPrice;
	}

	@Column(name = "order_bonus")
	public String getOrderBonus() {
		return orderBonus;
	}

	@Column(name = "fetch_date")
	public String getFetchDate() {
		return fetchDate;
	}

	@Column(name = "order_data")
	public String getOrderData() {
		return orderData;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	@Column(name = "enable")
	public String getEnable() {
		return enable;
	}

	public void setOrderUUID(String orderUUID) {
		this.orderUUID = orderUUID;
	}

	public void setAccountUUID(String accountUUID) {
		this.accountUUID = accountUUID;
	}

	public void setRestaurantUUID(String restaurantUUID) {
		this.restaurantUUID = restaurantUUID;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public void setOrderBonus(String orderBonus) {
		this.orderBonus = orderBonus;
	}

	public void setFetchDate(String fetchDate) {
		this.fetchDate = fetchDate;
	}

	public void setOrderData(String orderData) {
		this.orderData = orderData;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass()).add("orderUUID", orderUUID).add("accountUUID", accountUUID)
				.add("restaurantUUID", restaurantUUID).add("userMessage", userMessage).add("createDate", createDate)
				.add("updateDate", updateDate).add("orderPrice", orderPrice).add("orderBonus", orderBonus)
				.add("fetchDate", fetchDate).add("orderData", orderData).add("status", status).add("enable", enable)
				.toString();
	}

}