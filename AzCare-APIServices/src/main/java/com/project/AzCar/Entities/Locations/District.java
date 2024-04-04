package com.project.AzCar.Entities.Locations;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="tbdistricts")
public class District implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -5839000534796823465L;

	@Id
	private String code;
	private String province_code;
	private String name;
	private String name_en;
	private String full_name;
	private String full_name_en;
	private String code_name;
}
