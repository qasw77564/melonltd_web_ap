package com.melonltd.naber.endpoint.controller.seller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.melonltd.naber.endpoint.util.Base64Service;
import com.melonltd.naber.endpoint.util.JsonHelper;
import com.melonltd.naber.rdbms.model.service.RestaurantInfoService;
import com.melonltd.naber.rdbms.model.vo.RespData;
import com.melonltd.naber.rdbms.model.vo.RespData.ErrorType;
import com.melonltd.naber.rdbms.model.vo.RespData.Status;
import com.melonltd.naber.rdbms.model.vo.RestaurantInfoVo;

@Controller
@RequestMapping(value = { "" }, produces = "application/x-www-form-urlencoded;charset=UTF-8;")
public class ChangedDateRangeController {
	private static final Logger LOGGERO = LoggerFactory.getLogger(ChangedDateRangeController.class);
	
	@Autowired
	private RestaurantInfoService restaurantInfoService;
	
	@ResponseBody
	@PostMapping(value = "seller/change/daily/business/time")
	public ResponseEntity<String> updateOrder(HttpServletRequest httpRequest,
			@RequestParam(value = "data", required = false) String data) {
		String request = Base64Service.decode(data);
		RestaurantInfoVo req = JsonHelper.json(request, RestaurantInfoVo.class);
		
		ErrorType errorType = checkReqData(req);
		LinkedHashMap<String, Object> map = null;
		
		if (ObjectUtils.allNotNull(errorType)) {
			map = RespData.of(Status.FALSE, errorType, null);
		}else {
			String accountUUID = httpRequest.getHeader("Authorization");
			RestaurantInfoVo vo = restaurantInfoService.findByAccountUUID(accountUUID);
			if (!ObjectUtils.allNotNull(vo)) {
				map = RespData.of(Status.FALSE, ErrorType.DATABASE_NULL, null);
			}else {
				RestaurantInfoVo newVo = restaurantInfoService.changeCanStoreRange(vo,req.getCan_store_range());
				if (ObjectUtils.allNotNull(newVo)) {
					map = RespData.of(Status.TRUE, null, newVo.getCan_store_range());
				}else {
					map = RespData.of(Status.FALSE, ErrorType.UPDATE_ERROR, null);
				}
			}
		}
		String result = Base64Service.encode(JsonHelper.toJson(map));
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	private static ErrorType checkReqData(RestaurantInfoVo req) {
		if (!ObjectUtils.allNotNull(req)) {
			return ErrorType.INVALID;
		}
		
		if (!ObjectUtils.allNotNull(req.getCan_store_range())) {
			return ErrorType.INVALID;
		}
		
		if (req.getCan_store_range().size() == 0) {
			return ErrorType.INVALID;
		}
		
//		try {
//			List<DateRangeVo> rangeVos = JsonHelper.jsonArray(req.getDate(), DateRangeVo[].class);
//			if (rangeVos.size() == 0) {
//				return ErrorType.INVALID;
//			}
//		}catch (Exception e) {
//			return ErrorType.INVALID;
//		}
		
		return null;
	}
}