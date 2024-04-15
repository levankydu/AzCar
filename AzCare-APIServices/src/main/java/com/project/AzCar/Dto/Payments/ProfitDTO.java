package com.project.AzCar.Dto.Payments;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitDTO {
	private BigDecimal profit;
	private List<String> dayList;

	private List<BigDecimal> totalIn;

	private List<BigDecimal> totalOut;

}
