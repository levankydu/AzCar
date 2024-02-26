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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name= "tbwards")
public class Ward implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 9189349922910364256L;

	@Id
	private String code;	
	private String district_code;
	private String name;
	private String name_en;
	private String full_name;
	private String full_name_en;
	private String code_name;
}
