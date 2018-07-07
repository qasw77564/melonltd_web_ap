package com.melonltd.naber.rdbms.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.melonltd.naber.rdbms.model.bean.FoodInfo;

public interface FoodInfoDao extends JpaRepository<FoodInfo, String> {

	@Query("SELECT a FROM FoodInfo a WHERE a.enable='Y' AND a.status = ?1 AND a.categoryUUID = ?2")
	public List<FoodInfo> findByStatusAndCategoryUUID(String status, String categoryUUID);
	
	@Query("SELECT a FROM FoodInfo a WHERE a.enable='Y' AND a.status = 'OPEN' AND a.foodUUID = ?1 ORDER BY a.createDate DESC")
	public FoodInfo findByFoodUUID(String foodUUID);

	@Query("SELECT f FROM FoodInfo f, CategoryRel r WHERE f.foodUUID IN(?1) AND f.status='OPEN' AND f.enable='Y' AND r.status='OPEN' AND r.enable='Y' AND f.categoryUUID=r.categoryUUID")
	public List<FoodInfo> findStatusByFoodUUIDs(List<String> foodUUIDs);
	
	@Query("SELECT a FROM FoodInfo a WHERE a.categoryUUID = ?1")
	public List<FoodInfo> findBycategoryUUID (String categoryUUID);
	
	@Query("SELECT f FROM FoodInfo f, CategoryRel c WHERE c.categoryUUID=?1 AND c.restaurantUUID=?2 AND c.categoryUUID=f.categoryUUID AND c.enable='Y' AND f.enable='Y' ORDER BY f.createDate DESC ")
	public List<FoodInfo> findBycategoryUUIDAndRestaurantUUID (String categoryUUID, String restaurantUUID);
	
	@Query("SELECT f FROM FoodInfo f, CategoryRel c WHERE f.categoryUUID=c.categoryUUID AND f.foodUUID =?1 AND c.restaurantUUID=?2 AND c.enable='Y' AND f.enable='Y'")
	public FoodInfo findByFoodUUIDAndRestaurantUUID(String foodUUID, String restaurantUUID);
	
	@Transactional
	@Modifying
	@Query("UPDATE FoodInfo a SET a.photo=?1 WHERE a.foodUUID=?2")	
	public void updatePhoto(String photo, String foodUUID);
	
	
}