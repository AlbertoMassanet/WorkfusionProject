package com.workfusion.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.workfusion.entities.Client;
import com.workfusion.utils.Constant;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.odf2.core.orm.OrmLiteRepository;


public class ClientRepository extends OrmLiteRepository<Client> {
	
	private static final String DATABASE_URL = "jdbc:sqlserver://;serverName=localhost;port=11433;databaseName=wfdb";
	private static final String DATABASE_USER = "ds";
	private static final String DATABASE_PWD = "almami9091";
	
	public ClientRepository(ConnectionSource connectionSource) throws SQLException {
		//super(connectionSource, Client.class);
		
		super(new JdbcConnectionSource(DATABASE_URL, DATABASE_USER, DATABASE_PWD), Client.class);
	}
	
	public void limpiarBD() throws SQLException {
		
		String dao_delete = "DELETE FROM " + Constant.BDTOTABLE + Client.nameTable;
		
		super.getDao().executeRawNoArgs(dao_delete);

	}
	
	public List<String> findAllClients() {
		
	    try {
	    	
	        QueryBuilder<Client, ?> queryBuilder = super.getDao().queryBuilder();
	        queryBuilder.selectColumns(Client.id_client_COLUMN);
	        
	        List<String> result = new ArrayList<String>();
	        
	        queryBuilder.query().forEach(acc -> {
	        	result.add(acc.getClientID());
	        });
	        
	        return result;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
		
	}
	
	
}