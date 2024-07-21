package com.easybanks.accounts.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.easybanks.accounts.constant.AccountsConstants;
import com.easybanks.accounts.dto.AccountDto;
import com.easybanks.accounts.dto.CustomerDto;
import com.easybanks.accounts.entity.Accounts;
import com.easybanks.accounts.entity.Customer;
import com.easybanks.accounts.exception.CustomerAlreadyExistsException;
import com.easybanks.accounts.exception.ResourceNotFoundException;
import com.easybanks.accounts.mapper.AccountsMapper;
import com.easybanks.accounts.mapper.CustomerMapper;
import com.easybanks.accounts.repository.AccountsRepository;
import com.easybanks.accounts.repository.CustomerRepository;
import com.easybanks.accounts.service.IAccountsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountsService implements IAccountsService{

	private AccountsRepository accountsRepository;
	
	private CustomerRepository customerRepository;
	

	@Override
	public void createAccount(CustomerDto customerDto) {
		// TODO Auto-generated method stub
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
		
		Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        
//        customer.setCreatedAt(LocalDateTime.now());
//        customer.setCreatedBy("Anonymous");
		Customer savedCustomer = customerRepository.save(customer);
		accountsRepository.save(createNewAccount(savedCustomer));
		
	}


	private Accounts createNewAccount(Customer customer) {
		// TODO Auto-generated method stub
		Accounts newAccount = new Accounts();
		newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
//        newAccount.setCreatedAt(LocalDateTime.now());
//        newAccount.setCreatedBy("Anonymous");
        return newAccount;
		
	}


	@Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountDto(AccountsMapper.mapToAccountsDto(accounts, new AccountDto()));
        return customerDto;
    }
	
	
	@Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountDto accountsDto = customerDto.getAccountDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }


	@Override
	public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
	
	
	

}
