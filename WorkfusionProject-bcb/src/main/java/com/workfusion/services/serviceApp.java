package com.workfusion.services;

import static com.workfusion.rpa.helpers.RPA.$;
import static com.workfusion.rpa.helpers.RPA.$$;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import com.workfusion.utils.Constant;
import com.workfusion.utils.baseClase;
import com.workfusion.entities.Account;
import com.workfusion.entities.Client;

import com.workfusion.rpa.driver.Driver;
import com.workfusion.rpa.helpers.RPA;
import com.workfusion.rpa.helpers.UiElement;
import com.workfusion.rpa.helpers.UiElementCollection;
import com.workfusion.rpa.helpers.UiConditions;
import com.workfusion.repository.AccountRepository;

public class serviceApp extends baseClase {
	
	private int reintentos = 0;
	private String user;
	private String password;
	private Driver driver;
	private AccountRepository account_db;
	
	public serviceApp(Driver driver, AccountRepository account) {
		this.driver = driver;
		this.account_db = account;
	}
	
	public serviceApp(Driver driver, AccountRepository account, String user, String pwd) {
		this.driver = driver;
		this.user = user;
		this.password = pwd;
		this.account_db = account;
	}

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
	
	/**
	 * Inicia la aplicación.
	 * 
	 * 
	 */
	public void iniciarApp() {
		
		try {
			 RPA.open(Constant.APPPATH);
		     
			 RPA.sleep(1000);
			 
    		} catch (NoSuchElementException nse) {
    		
    			imprimirAdornado("serviceApp<iniciarApp> =================> Ha ocurrido una exception element: " + nse.getLocalizedMessage());
    		
    		} catch (NoSuchWindowException nsw) {
    		
    			imprimirAdornado("serviceApp<iniciarApp> =================> Ha ocurrido una exception window: " + nsw.getLocalizedMessage());
    		}
		
	}
	
	/**
	 * Logueo en la aplicacion.
	 * 
	 */
	
	public void loginApp() {
		
		
		try {
			
			RPA.switchTo().window("[CLASS:WindowsForms10.Window.8.app.0.141b42a_r8_ad1;TITLE:workfusion System3]");
	        
	        // Se obtienen los elementos del objeto.
	        UiElement boxUser = $("[class=\"WindowsForms10.EDIT.app.0.141b42a_r8_ad1\"][name=\"textBox1\"]");
	        UiElement boxPassword = $("[class=\"WindowsForms10.EDIT.app.0.141b42a_r8_ad1\"][name=\"textBox2\"]");
	        UiElement buttonAccess = $("[class=\"WindowsForms10.BUTTON.app.0.141b42a_r8_ad1\"][name=\"button1\"]");
	        
	        // Se introduce el usuario.
	        boxUser.clear();
	        boxUser.setText(user);
	        
	        // Se introduce la contraseña.
	        boxPassword.clear();
	        boxPassword.setText(password);

	        buttonAccess.click();
	        
	        RPA.sleep(8000);
			 
    		} catch (NoSuchElementException nse) {
    		
    			imprimirAdornado("serviceApp<loginApp> =================> Ha ocurrido una exception element: " + nse.getLocalizedMessage());
    		
    		} catch (NoSuchWindowException nsw) {
    		
    			imprimirAdornado("serviceApp<loginApp> =================> Ha ocurrido una exception window: " + nsw.getLocalizedMessage());
    		}
		
		
		
	}
	
	
	
	
	/**
	 * Selecciona la opción Buscar por ID del Cliente en el menu de la aplicacion.
	 * 
	 *    (Obsoleto)
	 */
	public void selSearchMenuApp() {
		
		try {
			
	        RPA.switchTo().window("[CSS:[class=\"WindowsForms10.Window.8.app.0.141b42a_r8_ad1\"][title=\"workfusion System 3\"]]");
            
            // Foco sobre el menu de la aplicacion.
	        UiElement menu = $("[class=\"WindowsForms10.Window.8.app.0.141b42a_r8_ad1\"][name=\"menuStrip1\"]");
	        UiElement submenu = menu.find(".MenuItem[name=\"Clients\"]");
	        
	        submenu.click();
	        
	        RPA.sleep(100);
	        
	        // Pasamos por pulsaciones de teclado a la busqueda por ID.
	        submenu.sendKeys(Keys.TAB); // Primer submenu de Clients.
	        submenu.sendKeys(Keys.TAB); // Segundo submenu de Clients.
	        submenu.sendKeys(Keys.ENTER); // Tercer submenu de Clients con subsubmenu donde pulsamos ENTER.
	        submenu.sendKeys(Keys.TAB); // Primer subsubmenu.
	        submenu.sendKeys(Keys.ENTER); // Segundo subsubmenu y pulsamos ENTER.     	

	        RPA.sleep(600);
	        
			 
   		} catch (NoSuchElementException nse) {
   		
   			imprimirAdornado("serviceApp<selSearchMenuApp> =================> Ha ocurrido una exception element: " + nse.getLocalizedMessage());
   		
   		} catch (NoSuchWindowException nsw) {
   		
   			imprimirAdornado("serviceApp<selSearchMenuApp> =================> Ha ocurrido una exception window: " + nsw.getLocalizedMessage());
   		}	
		
	}
	
	
	
	/**
	 * Obtiene las cuentas por cada cliente. Necesita estar en el Home de la aplicación.
	 * 
	 * @param cuentaClientes List<Client>
	 * @return HashMap<String, Account>
	 */
	
	
	
	public void obtenerCuentasPorClienteBD (List<Client> listaClients) {
		
		selSearchMenuApp();
		
        boolean isClicked = false;

        WebDriver workfusionSearch = null;
        
        // Bucle sobre la lista de clientes.
        for (Client client : listaClients) {
        	workfusionSearch = RPA.switchTo().window("[CLASS:WindowsForms10.Window.8.app.0.141b42a_r8_ad1;TITLE:workfusion System 3]");

        	UiElement txtSearch = $("[CLASS:WindowsForms10.EDIT.app.0.141b42a_r8_ad1]");
            
        	imprimirAdornado("Cliente: " + client.getClientID());
        	imprimirAdornado("Ventana: " + workfusionSearch.getWindowHandle());
        	
        	// Se introduce el cliente.
            txtSearch.clear();
            txtSearch.setText(client.getClientID());        
            
            // Se activa checkbox.
            if (isClicked == false) {
            	
            	$("[CSS:[class=\"WindowsForms10.BUTTON.app.0.141b42a_r8_ad1\"][name=\"checkBox1\"]]").click();
            	isClicked = true;
            }
            
            
            // Pulsarmos Buscar
            $("[CSS:[class=\"WindowsForms10.BUTTON.app.0.141b42a_r8_ad1\"][name=\"button1\"]]").click();
            
            // Espera para que RPA focusee los cambios.
            RPA.sleep(1500);
            
            // Se obtiene la lista de registros del cliente.
            UiElementCollection table = $$("[CLASS:ListItem]");
            
            table.get(0).doubleClick();
            
            RPA.sleep(3000);
            
    		// Se hace foco sobre el nuevo modal.
    		WebDriver workfusionBoton = RPA.switchTo().window("[CLASS:WindowsForms10.Window.8.app.0.141b42a_r8_ad1;TITLE:workfusion System 3]");
    		
    		// Boton de Client Accounts
    		$("[CLASS:WindowsForms10.BUTTON.app.0.141b42a_r8_ad1;NAME:button1]").click();
    		
    		// Espera larga sobre nuevo modal.
    		RPA.sleep(3000);
    		
    		// Se hace foco sobre el nuevo modal.
    		buscarCuentasBD(client.getWIID());
    		
    		RPA.sleep(300);
    		
    		workfusionBoton.close();
            
         }    
       
        if (workfusionSearch != null) workfusionSearch.close();
        
        RPA.sleep(500);
        
        RPA.close();

	}
	
	
	
	
	public void buscarCuentasBD (String wiid) {
		
		// Se hace foco sobre el nuevo modal.
		WebDriver workfusionAccounts = RPA.switchTo().window("[CLASS:WindowsForms10.Window.8.app.0.141b42a_r8_ad1;TITLE:workfusion System 3]");

		// Se obtiene la lista de cuentas.
		UiElementCollection tableAccounts = $$("[CLASS:ListItem]");
		
		int cont = 1;
		
		// Bucle sobre los elementos de la tabla de cuentas.
		for (UiElement acc : tableAccounts) {
			int field1 = cont;
			int field2 = cont + 1;
			int field3 = cont + 2;
			int field4 = cont + 3;
			String account_id = acc.$("[CLASS:TextBlock;INSTANCE:" + field1 + "]").getText();
			String amount_str = acc.$("[CLASS:TextBlock;INSTANCE:" + field3 + "]").getText();
			String status_str = acc.$("[CLASS:TextBlock;INSTANCE:" + field4 + "]").getText();
			
			if (!status_str.isEmpty()) {
				// Instanciamos Account.
				Account account = new Account(wiid, account_id, amount_str, status_str);
				
    			// Guardar en BD
    			account_db.create(account);
				
//				imprimirAdornado("*********************** ACCOUNT: " + account_id);
//				imprimirAdornado("*********************** AMOUNT: " + amount_str);
//				imprimirAdornado("*********************** STATUS: " + status_str);
			}
			cont = field4 + 1;
		}
		
		workfusionAccounts.close();
		
	}
	
}