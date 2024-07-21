package com.easybanks.accounts.service;

import com.easybanks.accounts.dto.AccountDto;
import com.easybanks.accounts.dto.CustomerDto;
import com.easybanks.accounts.entity.Accounts;
import com.easybanks.accounts.entity.Customer;

public interface IAccountsService {
	
	void createAccount(CustomerDto accountDto);
	
	CustomerDto fetchAccount(String mobileNumber);
	
	boolean updateAccount(CustomerDto customerDto);
	
	boolean deleteAccount(String mobileNumber);
}
