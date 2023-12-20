package com.workfusion.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "ds_uc_uc_wf_clients_queue_v1_0")
public class Client {
	
	public static final String nameTable = "ds_uc_uc_wf_clients_queue_v1_0";
	public static final String wiid_COLUMN = "wiid";
	public static final String id_client_COLUMN = "id_client";
	public static final String page_num_COLUMN = "page_num";
	public static final String line_num_COLUMN = "line_num";
	
	@DatabaseField(columnName = id_client_COLUMN, dataType = DataType.STRING)
	private String ClientID;
	@DatabaseField(columnName = wiid_COLUMN, dataType = DataType.STRING)
	private String WIID;
	@DatabaseField(columnName = page_num_COLUMN, dataType = DataType.INTEGER)
	private int page;
	@DatabaseField(columnName = line_num_COLUMN, dataType = DataType.INTEGER)
	private int positionTable;
	

	
	public Client() {
		
	}
	

	
	public Client(String wiid) {
		this.WIID = wiid;
	}
	
	public Client(String wiid, int pg, int pos) {
		this.WIID = wiid;
		this.page = pg;
		this.positionTable = pos;
	}
	
	public Client(String id, String wiid, int pg, int pos) {
		this.WIID = wiid;
		this.ClientID = id;
		this.page = pg;
		this.positionTable = pos;
	}

	public String getClientID() {
		return ClientID;
	}



	public void setClientID(String clientID) {
		ClientID = clientID;
	}



	public int getPage() {
		return page;
	}



	public void setPage(int page) {
		this.page = page;
	}



	public int getPositionTable() {
		return positionTable;
	}



	public void setPositionTable(int positionTable) {
		this.positionTable = positionTable;
	}

	public String getWIID() {
		return WIID;
	}

	public void setWIID(String wIID) {
		WIID = wIID;
	}
	
	
	public String _toString() {
		return "Client { ClientID: '" + this.ClientID + "\'\n" + 
						"WIID: '" + this.WIID + "\'\n" + 
						"Page: '" + this.page + "\'\n" + 
						"positionTable: '" + String.valueOf(this.positionTable) + "\'}\n"; 
						
	}
	
}
