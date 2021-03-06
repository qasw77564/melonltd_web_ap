package com.melonltd.naber.rdbms.model.req.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

public class FoodItemVo implements Serializable {
	private static final long serialVersionUID = 8854246756125375923L;

	private String food_uuid;
	private String category_name;
	private String food_name;
	private String food_photo;
	private String price;

	private List<ItemVo> scopes = Lists.<ItemVo>newArrayList();
	private List<ItemVo> opts = Lists.<ItemVo>newArrayList();
	private List<DemandsItemVo> demands = Lists.<DemandsItemVo>newArrayList();

	public String getFood_uuid() {
		return food_uuid;
	}

	public void setFood_uuid(String food_uuid) {
		this.food_uuid = food_uuid;
	}

	public String getFood_name() {
		return food_name;
	}

	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}

	public String getFoodPhoto() {
		return food_photo;
	}

	public void setFoodPhoto(String food_photo) {
		this.food_photo = food_photo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<ItemVo> getScopes() {
		return scopes;
	}

	public void setScopes(List<ItemVo> scopes) {
		this.scopes = scopes;
	}

	public List<ItemVo> getOpts() {
		return opts;
	}

	public void setOpts(List<ItemVo> opts) {
		this.opts = opts;
	}

	public List<DemandsItemVo> getDemands() {
		return demands;
	}

	public void setDemands(List<DemandsItemVo> demands) {
		this.demands = demands;
	}
	
	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass()).add("food_uuid", food_uuid).add("food_name", food_name)
				.add("food_photo", food_photo).add("price", price).add("scopes", scopes).add("opts", opts)
				.add("demands", demands).toString();
	}

}
