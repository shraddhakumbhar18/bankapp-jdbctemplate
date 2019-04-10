package com.capgemini.bankapp.dao.impl;

import com.capgemini.bankapp.row.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.model.BankAccount;
import org.springframework.dao.*;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException.*;
import com.capgemini.banapp.exception.*;


public class BankAccountDaoImpl implements BankAccountDao 
{
	public JdbcTemplate jdbcTemplate;
	
	public BankAccountDaoImpl(JdbcTemplate jdbcTemplate)
	{
		 this.jdbcTemplate = jdbcTemplate; 
	}

	@Override
	public double getBalance(long accountId) 
	{
		String query = "SELECT account_balance FROM bankaccount WHERE account_id= ?";
		double balance = -1;
		try  
		{
			 balance = jdbcTemplate.queryForObject(query ,new Object[] {accountId} , Double.class);	
		} 
		catch (Exception e) 
		{
			System.out.println("Low balance exception");
		}
                return balance;
	}


	@Override
	public void updateBalance(long accountId, double newBalance) {
		String query = "UPDATE bankaccount SET account_balance='"+newBalance+"' WHERE account_id='"+accountId+"' ";
		int result = jdbcTemplate.update(query);

	}
	@Override
	public boolean addNewBankAccount(BankAccount account) {

		String query = "INSERT INTO bankaccount(customer_name,account_type,account_balance) VALUES ('"+account.getAccountHolderName()+"','"+account.getAccountType()+"','"+account.getAccountBalance()+"')";
		int result=jdbcTemplate.update(query); 
		if(result==1)
		{
                  return true;
		}
                else
		{
                 return false;
		}
		
	}

	@Override
	public boolean updateAccountDetails(long accountId, String accountHolderName, String accountType){
		String query = "UPDATE bankaccount SET customer_name='"+accountHolderName+"' ,account_type='"+accountType+"' WHERE account_id='"+accountId+"'";
		int result = jdbcTemplate.update(query);
		if(result == 1) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean deleteBankAccount(long accountId) {
		String query = "DELETE FROM bankaccount WHERE account_id=" +accountId ;
		int result = jdbcTemplate.update(query);
		if(result == 1) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public List<BankAccount> findAllBankAccounts() {
		String query = "SELECT * FROM bankaccount";
		List<BankAccount> accounts = jdbcTemplate.query(query,new BankMapper());
		return accounts;
	}

	@Override
	public BankAccount searchAccount(long accountId) throws BankAccountNotFoundException{
	BankAccount details=null;
	try
	{
		String query = "SELECT * FROM bankaccount WHERE account_id=?";
		 details=jdbcTemplate.queryForObject(query ,new Object[] {accountId} , (resultSet,rowNum)->{
		long accountId1 = resultSet.getLong(1);
		String accountHolderName = resultSet.getString(2);
		String accountType = resultSet.getString(3);
		double accountBalance = resultSet.getDouble(4);
		BankAccount account= new BankAccount(accountId1, accountHolderName, accountType, accountBalance);
		return account;
		});	
		
	}
	catch(EmptyResultDataAccessException ex)
	{	
		BankAccountNotFoundException re = new BankAccountNotFoundException("Bank Account Not Found");
		ex.initCause(re);
		throw re;
	}
	return details;
}
}
