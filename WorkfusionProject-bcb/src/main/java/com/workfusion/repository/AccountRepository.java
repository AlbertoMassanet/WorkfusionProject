package com.workfusion.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.workfusion.entities.Account;

import com.workfusion.utils.Constant;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.odf2.core.orm.OrmLiteRepository;


public class AccountRepository extends OrmLiteRepository<Account> {
	
	private static final String DATABASE_URL = "jdbc:sqlserver://;serverName=localhost;port=11433;databaseName=wfdb";
	private static final String DATABASE_USER = "ds";
	private static final String DATABASE_PWD = "almami9091";
	
	public AccountRepository(ConnectionSource connectionSource) throws SQLException {
		//super(connectionSource, Account.class);
		super(new JdbcConnectionSource(DATABASE_URL, DATABASE_USER, DATABASE_PWD), Account.class);
	}
	
	public void limpiarBD() throws SQLException 
	{

		String dao_delete = "DELETE FROM " + Constant.BDTOTABLE + Account.nameTable;
		
		super.getDao().executeRawNoArgs(dao_delete);
		
	}
	
	/**
	 * Obtiene una lista de objetos Account buscando por ID de cliente y su status.
	 * 
	 * @param clientID	String
	 * @param status	String
	 * @return List<Account>
	 */
    public List<Account> findByClientIdAndStatus(String clientID, String status) {
	    try {
	        QueryBuilder<Account, ?> queryBuilder = super.getDao().queryBuilder();
	        queryBuilder.where().eq(Account.id_client_COLUMN, clientID).and().eq(Account.status_COLUMN, status);
	        
	        return queryBuilder.query();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
    
    
    /**
     * Obtiene una lista de objetos Account buscando por ID de cliente.
     * 
     * @param clientID	String
     * @return List<Account>
     */
    public List<Account> findByClientId(String clientID) {
	    try {
	        QueryBuilder<Account, ?> queryBuilder = super.getDao().queryBuilder();
	        queryBuilder.where().eq(Account.id_client_COLUMN, clientID);
	        return queryBuilder.query();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
    
    
    /**
     * Obtiene todos los clientes unicos de Account.
     * 
     * @return List<String>
     */
    public List<String> findByUniqueClients() {
	    try {
	    	
	        QueryBuilder<Account, ?> queryBuilder = super.getDao().queryBuilder();
	        queryBuilder.selectColumns(Account.id_client_COLUMN).groupBy(Account.id_client_COLUMN);
	        
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
    
    
    /**
     * Actualiza la tabla de Cuentas como procesada
     * 
     * @param lista Lista de Cuentas ya procesadas.
     */
    public void updateUpdatedAccount(List<Account> lista) {
    	
	    try {
	    	for (Account acc : lista) {
	    		int bit = 1;
	    		
	    		String dao_update = "UPDATE " + Constant.BDTOTABLE + Account.nameTable + " SET " + Account.is_updated_COLUMN + " = " + bit + " WHERE " + Account.account_id_COLUMN + " = " + acc.getAccountID();
	    		
	    		
	    		super.getDao().executeRawNoArgs(dao_update);	    		
	    	}

	    	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        
	    } 	

    }
    
    
    /**
     * Comprueba si hay datos procesados.
     * 
     * 
     * @return boolean
     */
    public boolean checkIfIsUpdated() {
	    	
	    	List<Account> lista = this.findAll();
	    	
	    	for (Account acc : lista) {
	    		if (acc.isUpdated()) {
	    			return true;
	    		}
	    	}
	    	
	    	return false;
	    	

    }
    
    public boolean isClientUpdated(String cli) {
    	
    	boolean result = false;
    	
    	List<Account> accs = this.findByClientId(cli);
    	
    	for (Account acc : accs) {
    		if (acc.isUpdated()) {
    			result = true;
    			break;
    		}
    	}
    	
    	
    	return result;
    	
    }
    

	
	
}