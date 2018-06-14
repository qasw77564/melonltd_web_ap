package com.melonltd.naber.rdbms.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melonltd.naber.rdbms.model.bean.CategoryFoodRel;
import com.melonltd.naber.rdbms.model.dao.CategoryFoodRelDao;
import com.melonltd.naber.rdbms.model.vo.CategoryFoodRelVo;

@Service("categoryFoodRelSerice")
public class CategoryFoodRelSerice {

	@Autowired
	private CategoryFoodRelDao categoryFoodRelDao;

	public List<CategoryFoodRelVo> findByStatusAndCategoryUUID(String status, String categoryUUID) {
		List<CategoryFoodRel> list = categoryFoodRelDao.findByStatusAndCategoryUUID(status, categoryUUID);
		return CategoryFoodRelVo.valueOfArray(list, false);
	}

	public CategoryFoodRelVo findByFoodUUID(String foodUUID) {
		CategoryFoodRel info = categoryFoodRelDao.findByFoodUUID(foodUUID);
		return CategoryFoodRelVo.valueOf(info, true);
	}

	public int getFoodStatusOpenByUUIDs(List<String> foodUUIDs) {
		List<CategoryFoodRel> list = categoryFoodRelDao.findStatusByFoodUUIDs(foodUUIDs);
		return list.size();
	}

	public List<CategoryFoodRel> findBycategoryUUID(String categoryUUID) {
		List<CategoryFoodRel> list = categoryFoodRelDao.findBycategoryUUID(categoryUUID);
		return list;
	}
	
	public void saves(List<CategoryFoodRel> infos) {
		categoryFoodRelDao.save(infos);
	}
}