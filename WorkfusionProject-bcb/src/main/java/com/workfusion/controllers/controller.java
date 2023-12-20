package com.workfusion.controllers;


import com.workfusion.utils.Constant;
import com.workfusion.utils.baseClase;

import com.workfusion.entities.Account;
import com.workfusion.entities.Client;
import com.workfusion.services.*;
import com.workfusion.repository.ClientRepository;
import com.workfusion.repository.AccountRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.workfusion.rpa.driver.Driver;

public class controller extends baseClase {
	
	private Driver driver;
	private serviceBrowser browser;
	private serviceApp app;
	private String user;
	private String password;
	private List<Client> listaClientes;
	private HashMap<String, List<Account>> listaCuentas;
	
	private ClientRepository client_db;
	private AccountRepository account_db;
	
	private serviceExcel output;
	
	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public controller(Driver driver, ClientRepository client, AccountRepository account) {
		this.driver = driver;
		this.client_db = client;
		this.account_db = account;
	}
	
	public controller(Driver driver, ClientRepository client, AccountRepository account, String user, String pass) {
		this.driver = driver;
		this.user = user;
		this.password = pass;
		this.client_db = client;
		this.account_db = account;
		
		//this.browser = new serviceBrowser(driver, client, user, password);
	}
	
	
	/**
	 * Ejecuta el browser hasta despues del login.
	 * 
	 * 
	 */
	public void executeBrowserTilHome() {
		
		try {
			
			if (Objects.isNull(browser)) browser = new serviceBrowser(driver, client_db, user, password);
			
			// Iniciamos el navegador y accedemos a la pagina de login
			browser.iniciarNavegador(driver, Constant.URLLOGINPATH);
			// Nos logeamos en el navegador
			browser.loginNavegador(driver);	
			
		} catch (Exception exc) {
			imprimirAdornado("controller<executeBrowserTilHome> Excepcion en controlador: " + exc.getLocalizedMessage());
		}
		
	}
	

	/**
	 * obtiene todos los clientes a investigar asi como la url de actualizacion de datos.
	 * 
	 * @return HashMap<String, Account> result Lista de ClientID y el modelo asociado.
	 */
	public List<Client> obtenerClientesBrowser() {
		
		if (Objects.isNull(browser)) executeBrowserTilHome();
		
		browser.HomeToWorkItemsNavegador(driver);
				
		return browser.obtenerClientes(driver);
	}
	
	/**
	 * Actualiza los datos de los clientes en la web.
	 * 
	 * @param clients Lista de ClientID y el modelo asociado.
	 */
	public void actualizarClientesBrowser(HashMap<String, List<Account>> clients) {
		
		browser.setAccountDB(account_db);
				
		browser.HomeToWorkItemsUpdateNavegador(driver, clients);
		
	}
	
	
	/**
	 * Inicia la aplicacion hasta el buscador por client Id.
	 * 
	 * @param driver
	 */
	public void iniciarAplicacion() {
		
		app = new serviceApp(driver, account_db, user, password);
		
		app.iniciarApp();
		
		app.loginApp();
				
	}
	

	
	/**
	 * obtiene las cuentas y sus datos desde la aplicacion.
	 * 
	 * @param clients Lista de ClientID y el modelo asociado.
	 * 
	 * @return  HashMap<String, Account>  Lista de ClientID y el modelo asociado.
	 */
	public HashMap<String, List<Account>> obtenerCuentasAplicacion(List<Client> clients) {
		
		if (Objects.isNull(app)) iniciarAplicacion();
		
		HashMap<String, List<Account>> result = null;
		
		clients = client_db.findAll();
		app.obtenerCuentasPorClienteBD(clients);
		
		return result;
	}
	
		
	/**
	 * Crea el archivo de salida.
	 * 
	 */
	public void crearOutput() {
		
		output = new serviceExcel();
		
	}
	
	/**
	 * Rellena el archivo de salida con los datos.
	 * 
	 * @param lista List<Account>
	 */
	public void rellenarOutput(HashMap<String, List<Account>> lista) {
		
		List<Account> temp = new ArrayList<Account>();
		
		temp = account_db.findAll();
					
		
		output.editarExcel(temp);
		
	}
	
	
	
	public HashMap<String, List<Account>> obtenerClientesCuentasBD() {
		
		HashMap<String, List<Account>> result = new HashMap<String, List<Account>>();
		
		List<String> c = account_db.findByUniqueClients();
		
		for (String el : c) {
			result.put(el,  account_db.findByClientId(el));
		}
		
		return result;
	}
	
}