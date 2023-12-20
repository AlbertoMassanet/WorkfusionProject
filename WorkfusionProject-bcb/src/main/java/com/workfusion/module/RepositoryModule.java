package com.workfusion.module;


import com.workfusion.repository.ClientRepository;
import com.workfusion.repository.AccountRepository;
import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.odf2.core.cdi.OdfModule;
import org.codejargon.feather.Provides;

import javax.inject.Singleton;
import java.sql.SQLException;

public class RepositoryModule implements OdfModule {
	
    @Provides
    @Singleton
    public ClientRepository ClientRepository(ConnectionSource connectionSource) throws SQLException {
        return new ClientRepository(connectionSource);
    }
    
    @Provides
    @Singleton
    public AccountRepository AccountRepository(ConnectionSource connectionSource) throws SQLException {
        return new AccountRepository(connectionSource);
    }
	
	
}