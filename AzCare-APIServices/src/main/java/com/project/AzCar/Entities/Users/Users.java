package com.project.AzCar.Entities.Users;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.AzCar.Entities.Cars.CarModelList;
import com.project.AzCar.Entities.Locations.Addreess;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "tbusers")
@Entity

public class Users implements Serializable{

	private static final long serialVersionUID = -2727541742627038300L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Transient
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST,targetEntity = CarModelList.class)
	private List<CarModelList> cars = new ArrayList<>();
	private String firstName;
	private String lastName;

	private String phone;
	private String email;
	private String password;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", referencedColumnName = "id") })
	private List<Roles> roles = new ArrayList<>();
	private Date dob;
	private boolean isEnabled;
	private String gender;
	private BigDecimal balance;
	private int score;
	@Column(length = 1024)
	private String image;
	@Transient
	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
	private List<Addreess> address;

	public Users(String firstName, String email, String password, List<Roles> roles) {
		this.firstName = firstName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	@Transient
	public boolean hasRole(String roleName) {
		if (roleName == null) {
			return false;
		}
		for (Roles role : roles) {
			if (roleName.equals(role.getName())) {
				return true;
			}
		}
		return false;
	}
}
