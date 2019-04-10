package com.capgemini.bankapp.row;

import com.capgemini.bankapp.client.*;
import com.capgemini.bankapp.model.*;
import org.springframework.jdbc.core.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankMapper implements RowMapper<BankAccount>
{
	public BankAccount mapRow(ResultSet rs ,int rowNum) throws SQLException
	{
		long accountId = rs.getLong(1);
		String accountHolderName = rs.getString(2);
		String accountType = rs.getString(3);
		Double accountBalance = rs.getDouble(4);
		BankAccount account = new BankAccount(accountId, accountHolderName, accountType, accountBalance);
		return account;
	}
}