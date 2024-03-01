package com.project.AzCar.Entities.Cars;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbbrandimages")
public class BrandImages implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -4404387433056113055L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String ImageUrl;
	
	@Transient
	@OneToMany(mappedBy = "brandImages", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private int brandId;
}
