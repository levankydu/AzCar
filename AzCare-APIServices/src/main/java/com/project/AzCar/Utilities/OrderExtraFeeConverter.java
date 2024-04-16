package com.project.AzCar.Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OrderExtraFeeConverter implements AttributeConverter<OrderExtraFee, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(OrderExtraFee extraFee) {
		try {
			return objectMapper.writeValueAsString(extraFee);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting extraFee to JSON", e);
		}
	}

	@Override
	public OrderExtraFee convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, OrderExtraFee.class);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting JSON to extraFee", e);
		}
	}
}