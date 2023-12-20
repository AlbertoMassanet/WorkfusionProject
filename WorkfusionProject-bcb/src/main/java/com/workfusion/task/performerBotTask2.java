package com.workfusion.task;


import com.workfusion.utils.*;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Injector;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.AdHocTask;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;

import com.workfusion.odf2.service.vault.SecretsVaultService;
import com.workfusion.odf2.service.ControlTowerServicesModule;

import com.workfusion.controllers.controller;
import com.workfusion.repository.*;
import com.workfusion.entities.*;
import com.workfusion.module.RepositoryModule;

@BotTask
@Requires({ControlTowerServicesModule.class, RepositoryModule.class})
public class performerBotTask2 extends baseClase implements AdHocTask {
	
	final RpaRunner rpaRunner;
	private final SecretsVaultService secretsVaultService;
	private final Logger logger;
	private final ClientRepository clientRepository;
	private final AccountRepository accountRepository;
	private String user;
	private String password;
	
    @Inject
    public performerBotTask2(Injector injector, Logger logger, ClientRepository cr, AccountRepository ar){
    	 RpaFactory rpaFactory = injector.instance(RpaFactory.class);
         this.rpaRunner = rpaFactory
                 .builder(RpaDriver.UNIVERSAL)
                 .closeOnCompletion(true)
                 .build();
         this.secretsVaultService = injector.instance(SecretsVaultService.class);
         this.logger = logger;
         
         this.clientRepository = cr;
         this.accountRepository = ar;
         
         this.user = secretsVaultService.getEntry(Constant.VAULTNAME).getKey();
         this.password = secretsVaultService.getEntry(Constant.VAULTNAME).getValue();
    }
   
 
    @Override
    public TaskRunnerOutput run(TaskInput taskInput) {
    	
        rpaRunner.execute(d->{

        	if (! accountRepository.checkIfIsUpdated()) {
	        	// Se inicia el controlador.
	        	controller oController = new controller(d, clientRepository, accountRepository, user, password);
	        	
	        	// Se inicia la aplicacion de escritorio.
	        	oController.iniciarAplicacion();
	        	
	        	// Se obtiene la lista de cuentas de los clientes.
	        	oController.obtenerCuentasAplicacion(null);
        	        		
        	} else {
        		imprimirAdornado("Bot terminando sin datos que procesar.");
        	}

        	
        	
        });
    	
        return taskInput.asResult()
                .withColumn("example_bot_task_output", "completed_successfully");
    }

}
