package com.capgemini.bankapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


import com.capgemini.banapp.exception.BankAccountNotFoundException;
import com.capgemini.banapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BankAccountClient 
{

	// static final Logger logger = Logger.getLogger(BankAccountClient.class);

	public static void main(String[] args) 
	{
		int choice;
		String accountHolderName;
		String accountType;
		double accountBalance;
		double amount;
		long accountId;
		long fromAccount;
		long toAccount;
		

		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		BankAccountService bankService= (BankAccountService)context.getBean("bankAccountServiceImpl");

		
		// BankAccountService bankService = new BankAccountServiceImpl();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) 
		{
			while (true) 
			{
				System.out.println("1.Add New BankAccount\n2.Withdraw\n3.Deposit\n4.Check Balance");
				System.out.println("5.Fund Transfer\n6.Delete BankAccount\n7.Display BankAccount Details");
				System.out.println("8.Search BankAccount\n9.Update Account\n10.Exit");

				System.out.print("Enter your choice :");
				choice = Integer.parseInt(reader.readLine());

				switch (choice) 
				{
				case 1:
					System.out.println("Enter Account Holder Name");
					accountHolderName = reader.readLine();
					System.out.println("Enter Account Type");
					accountType = reader.readLine();
					System.out.println("Enter Account Balance");
					accountBalance = Double.parseDouble(reader.readLine());
					BankAccount account = new BankAccount(accountHolderName, accountType, accountBalance);
					if (bankService.addNewBankAccount(account)) 
					{
						System.out.println("Account created successfully\n");
					} 
					else 
					{
						System.out.println("failed to create\n");
					}
					break;

				case 2:
					System.out.println("Enter Account Id");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter Amount");
					amount = Double.parseDouble(reader.readLine());
					
					try 
					{
						double balance = bankService.withdraw(accountId, amount);
						System.out.println("Transaction successful" + balance);
					} 
					catch (LowBalanceException e) 
					{
						System.out.println("Low Balance Exception");
						// logger.error("Withdraw failed", e);
					} 
					catch (BankAccountNotFoundException e) 
					{
						System.out.println("Bank Account not found");
						// logger.error("Account Not Found", e);
					}
					break;

				case 3:
					System.out.println("Enter Account Id");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter Amount");
					amount = Double.parseDouble(reader.readLine());
					
					try 
					{
						double balance =  bankService.deposit(accountId, amount);
						System.out.println("Transaction successful=" + balance);
					} 
					catch (BankAccountNotFoundException e2) 
					{
						System.out.println("Bank Account not found");
						// logger.error("Account Not Found", e2);
					}
					break;
					
				case 4:
					System.out.println("Enter account Id");
					accountId = Long.parseLong(reader.readLine());
					double accountBalance1;
					try 
					{
						accountBalance1 = bankService.checkBalance(accountId);
						System.out.println("Current Balance =" + accountBalance1);
					} 	
					catch (BankAccountNotFoundException e1) 
					{
						System.out.println("Bank Account not found");
						// logger.error("Account Not Found", e1);
					}
					break;
					
				case 5:
					System.out.println("Enter Account Id");
					fromAccount = Long.parseLong(reader.readLine());
					System.out.println("Enter Account Id of recipent");
					toAccount = Long.parseLong(reader.readLine());
					System.out.println("Enter Amount");
					amount = Double.parseDouble(reader.readLine());

					try 
					{
						double balance1;
						try 
						{
							balance1 = bankService.fundTransfer(fromAccount, toAccount, amount);
							System.out.println("Transcation successful" + balance1);
						} 
						catch (BankAccountNotFoundException e) 
						{
							System.out.println(e.getMessage());
							// logger.error("Account Not Found", e);
						}
					} 
					catch (LowBalanceException e) 
					{
						System.out.println(e.getMessage());
					}
					break;
				case 6:
					System.out.println("Enter account Id");
					accountId = Long.parseLong(reader.readLine());
					boolean result;
					try 
					{
						result = bankService.deleteBankAccount(accountId);
						if (result == true) 
						{
							System.out.println("Account deleted Successfully");
						} 
						else 
						{
							System.out.println("failed to delete");
						}
					} 
					catch (BankAccountNotFoundException e) 
					{
							System.out.println("Bank Account Not Found Exception");
							// logger.error("Account Not Found",e);
					}
					break;

				case 7:
					List<BankAccount> accountList = bankService.findAllBankAccounts();
					for (BankAccount bankAccount : accountList) 
					{
						System.out.println(bankAccount);
					}
					break;
				case 8:
					System.out.println("Enter account Id");
					accountId = Long.parseLong(reader.readLine());
					BankAccount bankAccount;
					try 
					{
						bankAccount = bankService.searchAccount(accountId);
						System.out.println(bankAccount);
					} 
					catch (BankAccountNotFoundException e) 
					{
						System.out.println("Bank Account Not Found Exception");
						// logger.error("Account Not Found",e);
					}
					break;

				case 9:
					System.out.println("Enter Account Id to update Account Details");
					accountId = Long.parseLong(reader.readLine());
					
					try
					{
						System.out.println("Enter Account Holder Name");
						accountHolderName = reader.readLine();
						System.out.println("Enter Account Type");
						accountType = reader.readLine();
						boolean updateResult = bankService.updateAccount(accountId, accountHolderName, accountType);
						if (updateResult == true) 
						{
							System.out.println("Details successfully Update");
						} 
						else 
						{
							System.out.println("updation failed");
						}
					}
					catch (BankAccountNotFoundException e) 
					{
						// logger.error("Account Not Found",e);
						System.out.println("Account Not Found");
					}
					break;
				
				case 10:
					System.out.println("Thanks for banking with us");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			// logger.error("Exception :", e);
		}
	}
}
