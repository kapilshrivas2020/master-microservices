package com.easybanks.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Accounts extends BaseEntity{
	
	
	private Long customerId;
	
	@Id
	private Long accountNumber;
	
	private String accountType;
	
	private String branchAddress;

}
