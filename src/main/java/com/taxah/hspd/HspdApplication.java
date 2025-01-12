package com.taxah.hspd;

import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class HspdApplication { // Historical stock prices data Application

	public static void main(String[] args) {
		SpringApplication.run(HspdApplication.class, args);

//		//Тест equals and hashcode
//		StockResponseData aapl = StockResponseData.builder()
//				.ticker("AAPL")
//				.build();
//		StockResponseData aapl2 = StockResponseData.builder()
//				.ticker("AAPL")
//				.build();
//		LocalDate now = LocalDate.now();
//		Result first = Result.builder()
//				.id(1L)
//				.date(now)
//				.open(BigDecimal.ONE)
//				.close(BigDecimal.TEN)
//				.low(BigDecimal.ONE)
//				.high(BigDecimal.TEN)
//				.stockResponseData(aapl)
//				.build();
//		Result second = Result.builder()
//				.id(null)
//				.date(now)
//				.open(BigDecimal.ONE)
//				.close(BigDecimal.TEN)
//				.low(BigDecimal.ONE)
//				.high(BigDecimal.TEN)
//				.stockResponseData(aapl2)
//				.build();
//		System.out.println(first.equals(second));
	}

}
