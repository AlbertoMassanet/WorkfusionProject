package com.workfusion.entities;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ds_uc_uc_wf_accounts_queue_v1_0")
public class Account {
	
	public static final String nameTable = "ds_uc_uc_wf_accounts_queue_v1_0";
	public static final String account_id_COLUMN = "id_account";
	public static final String id_client_COLUMN = "id_client";
	public static final String amount_COLUMN = "amount";
	public static final String status_COLUMN = "status";
	public static final String is_updated_COLUMN = "is_Updated";
	
	@DatabaseField(columnName = id_client_COLUMN, dataType = DataType.STRING)
	private String ClientID;
	@DatabaseField(columnName = account_id_COLUMN, dataType = DataType.STRING)
	private String accountID;
	@DatabaseField(columnName = amount_COLUMN, dataType = DataType.STRING)
	private String amount;
	@DatabaseField(columnName = status_COLUMN, dataType = DataType.STRING)
	private String status;
	@DatabaseField(columnName = is_updated_COLUMN, dataType = DataType.BOOLEAN_INTEGER)
	private boolean isUpdated;
	
	
	public Account() {
		
	}

	public Account(String cli, String accid, String am, String st) {
		this.ClientID = cli;
		this.accountID = accid;
		this.amount = am;
		this.status = st;
	}
	
	public Account(String accid, String am, String st) {
		this.accountID = accid;
		this.amount = am;
		this.status = st;
	}

	public String getClientID() {
		return ClientID;
	}


	public void setClientID(String clientID) {
		ClientID = clientID;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public String _toString() {
		return "Client { ClientID: '" + this.ClientID + "\'\n" + 
						"Account ID: '" + this.accountID + "\'\n" + 
						"Amount: '" + this.amount + "\'\n" + 
						"Status: '" + this.status + "\'\n" + 
						"isUpdated: '" + String.valueOf(this.isUpdated) + "\'}\n"; 
						
	}
	
}
