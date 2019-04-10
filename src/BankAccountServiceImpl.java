package com.capgemini.bankapp.service.impl;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

import com.capgemini.banapp.exception.BankAccountNotFoundException;
import com.capgemini.banapp.exception.LowBalanceException;
import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.dao.impl.BankAccountDaoImpl;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
//import com.capgemini.bankapp.util.DbUtil;

public class BankAccountServiceImpl implements BankAccountService {

	 private BankAccountDao bankAccountDao;
	// static final Logger logger = Logger.getLogger(BankAccountServiceImpl.class);

	public BankAccountServiceImpl(BankAccountDao bankAccountDao) {
		this.bankAccountDao = bankAccountDao;
	}
	
	@Override
	public double checkBalance(long accountId) throws BankAccountNotFoundException {

		double balance = bankAccountDao.getBalance(accountId);
		if (balance >= 0)
			return balance;
		throw new BankAccountNotFoundException("BankAccount doesn't exist..");
	}

	@Override
	public double withdraw(long accountId, double amount) throws LowBalanceException, BankAccountNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountNotFoundException("Bank Account doesn't exist..");
		else if (balance - amount >= 0) {
			balance = balance - amount;
			bankAccountDao.updateBalance(accountId, balance);
			return balance;
		} else {
			throw new LowBalanceException("You don't have sufficient fund");
		}
	}

	public double withdrawForFundTransfer(long accountId, double amount)
			throws LowBalanceException, BankAccountNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountNotFoundException("Bank Account doesn't exist..");
		else if (balance - amount >= 0) {
			balance = balance - amount;
			bankAccountDao.updateBalance(accountId, balance);
			return balance;
		} else {
			throw new LowBalanceException("You don't have sufficient fund");
		}
	}

	@Override
	public double deposit(long accountId, double amount) throws BankAccountNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountNotFoundException("Bank Account doesn't exist");
		balance = balance + amount;
		bankAccountDao.updateBalance(accountId, balance);
		return balance;
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {
		boolean result = bankAccountDao.addNewBankAccount(account);
		return result;
	}

	@Override
	public boolean deleteBankAccount(long accountId) throws BankAccountNotFoundException {
		boolean result = bankAccountDao.deleteBankAccount(accountId);
		return result;
		//throw new BankAccountNotFoundException("Bank Account doesn't exist.");
	}
	@Override
	public List<BankAccount> findAllBankAccounts() {
		return bankAccountDao.findAllBankAccounts();
	}
	@Override
	public boolean updateAccount(long accountId, String accountHolderName, String accountType) throws BankAccountNotFoundException{
		boolean result = bankAccountDao.updateAccountDetails(accountId, accountHolderName, accountType);
		return result;
	}
	
	
	@Override
	public BankAccount searchAccount(long accountId) throws BankAccountNotFoundException {
		BankAccount account = bankAccountDao.searchAccount(accountId);
		if (account == null)
			throw new BankAccountNotFoundException("Bank Account doesn't exist");
		return account;
	}
	
	@Override
	@Transactional(rollbackFor=BankAccountNotFoundException.class)
	public double fundTransfer(long fromAccount, long toAccount, double amount) throws LowBalanceException, BankAccountNotFoundException 
	{
		try 
		{
			double newBalance = withdrawForFundTransfer(fromAccount, amount);
			deposit(toAccount, amount);
			//bankAccountDao.commit();
			return newBalance;
		} 
		catch (LowBalanceException | BankAccountNotFoundException e) {
			//bankAccountDao.rollback();
			// logger.error("Exception", e);
			throw e;
		}
	}
}
