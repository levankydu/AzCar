package com.project.AzCar.Entities.Locations;

import java.io.Serializable;
import java.util.List;

import com.project.AzCar.Entities.Users.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="tbaddress")
public class Addreess implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -618553071289003810L;

	@Id
	private int id;
	private String Street;
	private String houseNo;
	private String idCtiy;
	private String idDistrict;
	private String idWard;
	
	@Transient
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "address_user")
	private List<Users> users;
	
	
	
}
