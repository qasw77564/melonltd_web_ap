package com.melonltd.naber.rdbms.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.melonltd.naber.rdbms.model.bean.OrderInfo;

public interface OrderInfoDao extends JpaRepository<OrderInfo, String> {
	
	@Query("SELECT o FROM OrderInfo o WHERE (o.fetchDate BETWEEN ?2 AND ?3) AND o.enable='Y' AND o.restaurantUUID=?4 AND o.status!='FAIL' AND o.accountUUID IN(SELECT a.accountUUID FROM AccountInfo a WHERE a.phone LIKE %?1)")
	public List<OrderInfo> findByPhoneAndBetweenDate (String phone, String startDate, String endDate, String restaurantUUID);
	
	@Query("SELECT o FROM OrderInfo o, AccountInfo a WHERE o.status =?2 AND o.enable='Y' AND (o.fetchDate BETWEEN ?3 AND ?4) AND o.restaurantUUID = a.restaurantUUID AND a.accountUUID=?1 ")
	public Page<OrderInfo> findByOrderStatusAndBetweenDate (String accountUUID, String status,String startDate, String endDate, Pageable pageable);
	
	@Query("SELECT o FROM OrderInfo o WHERE o.restaurantUUID=?1 AND (o.updateDate BETWEEN ?2 AND ?3) AND o.status IN('UNFINISH','PROCESSING','CAN_FETCH','CANCEL')")
	public List<OrderInfo> findUnfinishProcessingCanFetchByBetweenDate(String restaurantUUID, String startDate, String endDate);
	
	@Query("SELECT o FROM OrderInfo o WHERE o.enable='Y' AND (o.fetchDate BETWEEN ?1 AND ?2) AND o.status IN('UNFINISH','PROCESSING','CAN_FETCH')")
	public List<OrderInfo> findUnfinishProcessingCanFetchByBetweenDate(String startDate, String endDate);
	
	@Query("SELECT o FROM OrderInfo o WHERE o.status='UNFINISH' AND o.fetchDate>?1")
	public List<OrderInfo> findByUnfinshAndMoreThanNowOrders(String date, Sort sort);
	
}
