package com.workfusion.services;

import static com.workfusion.rpa.helpers.RPA.$;
import static com.workfusion.rpa.helpers.RPA.$$;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.workfusion.utils.Constant;
import com.workfusion.utils.baseClase;
import com.workfusion.entities.Account;
import com.workfusion.entities.Client;
import com.workfusion.repository.*;

import com.workfusion.rpa.driver.Driver;
import com.workfusion.rpa.helpers.RPA;
import com.workfusion.rpa.helpers.UiElement;
import com.workfusion.rpa.helpers.UiElementCollection;

public class serviceBrowser extends baseClase {
	
	private int reintentos = 0;
	private String user;
	private String password;
	private Driver driver;
	private ClientRepository client;
	private AccountRepository accountDB;
	

	public serviceBrowser(Driver driver, ClientRepository client) {
		this.driver = driver;
		this.client = client;
	}
	
	public serviceBrowser(Driver driver, ClientRepository client, String user, String pass) {
		this.driver = driver;
		this.user = user;
		this.password = pass;
		this.client = client;
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
	
	public AccountRepository getAccountDB() {
		return accountDB;
	}

	public void setAccountDB(AccountRepository accountDB) {
		this.accountDB = accountDB;
	}

	
	
	/**
	 * Inicio del navegador. Requiere Driver y url donde iniciar.
	 * 
	 * 
	 * @param driver	Driver
	 * @param url		String
	 */
	public void iniciarNavegador(Driver driver, String url) {
		
		try {
			
    		this.driver = Objects.requireNonNull(driver);
            
            RPA.openChrome(url);
            
            imprimirAdornado("Navegador ejecutando...");
            
		} catch (Exception exc) {
			 
			 if (exc instanceof WebDriverException || exc instanceof NoSuchWindowException) {
				 
				 reintentos++;
				 imprimirAdornado("<serviceBrowser><iniciarNavegador> Ha habido un error al abrir la web: " + exc.getLocalizedMessage() + " con reintentos " + reintentos);
				 if (reintentos < Constant.MAX_REINTENTOS) {
					 iniciarNavegador(driver, url);
				 } else {
					 imprimirAdornado("serviceBrowser<loginNavegador> Excedido reintentos maximos. Cerramos aplicacion.");
				 }

			 } else {
				    throw new RuntimeException("serviceBrowser<loginNavegador>", exc);
			  }

		 }
		

	}
	
	
	/**
	 * Realiza el logeo desde la pagina de login.
	 * 
	 * @param driver	Driver.
	 */
	public void loginNavegador(Driver driver) {
		try { 

			if (!user.isEmpty() && !password.isEmpty()) {
				
				
				 imprimirAdornado("Pagina de login accediendo...");
					
		            WebDriver nav = RPA.switchTo().window("[CLASS:Chrome_WidgetWin_1]");
		            
		            
		            
		            RPA.sleep(1000);
		            WebElement btnWebLogin = nav.findElement(By.cssSelector(".Button[name=\"Login\"]"));
		            WebElement boxWebUser = nav.findElement(By.cssSelector(".Edit[name=\"email\"]"));
		            WebElement boxWebPswd = nav.findElement(By.cssSelector(".Edit[name=\"password\"]"));
		           
		            boxWebUser.clear();
		            boxWebUser.setText(user);
		            boxWebPswd.clear();
		            boxWebPswd.setText(password);
		            
		            btnWebLogin.click();
		            
		            imprimirAdornado("Pagina de login accedido correctamente...");
		            
			} else {
				throw new RuntimeException("serviceBrowser<loginNavegador> No existen datos de acceso.");
			}
			 
			 
			 
		} catch (Exception exc) {
			 if (exc instanceof WebDriverException || exc instanceof NoSuchWindowException) {
				 
				 reintentos++;
				 imprimirAdornado("<serviceBrowser><login> Ha habido un error al logearse en la web: " + exc.getLocalizedMessage() + " con reintentos " + reintentos);
				 if (reintentos < Constant.MAX_REINTENTOS) {
					 loginNavegador(driver);
				 } else {
					 imprimirAdornado("serviceBrowser<loginNavegador>  Excedido reintentos maximos. Cerramos aplicacion.");
				 }

			 } else {
				    throw new RuntimeException("serviceBrowser<loginNavegador> Runtime Exception: ", exc);
			  }

		}
	}
	
	/**
	 * Navega desde el menu principal hasta la pagina de Work Items
	 * 
	 * @param driver	Driver
	 */
	public void HomeToWorkItemsNavegador(Driver driver) {
		
		try {
			
			RPA.switchToExistingWindow("[CLASS:Chrome_WidgetWin_1]");
			
            RPA.sleep(600);
            
            // Pulsacion sobre el menu principal.
            $(By.xpath("/html/body/div/div[2]/div/div[2]/a")).click();
            
            RPA.sleep(600);
			
    	} catch (NoSuchElementException nse) {
    		
    		imprimirAdornado("serviceBrowser<HomeToWorkItemsNavegador> =================> Ha ocurrido una exception element: " + nse.getLocalizedMessage());
    		
    	} catch (NoSuchWindowException nsw) {
    		
    		imprimirAdornado("serviceBrowser<HomeToWorkItemsNavegador> =================> Ha ocurrido una exception window: " + nsw.getLocalizedMessage());
    	}
		
	}
	
	/**
	 * Acceso a la actualizacion de datos desde el navegador. (2a. parte)
	 * 
	 * @param driver
	 * @param listaAccounts
	 */
	public void HomeToWorkItemsUpdateNavegador(Driver driver, HashMap<String, List<Account>> listaAccounts) {
		
		try {
			
	        RPA.switchToExistingWindow("[CLASS:Chrome_WidgetWin_1]");
	        
	    	$(By.xpath("/html/body/div/div[2]/h1")).click();
	    	WebDriver nav = RPA.switchTo().defaultContent();
	    	
            RPA.sleep(500);
            
            // Por cada cliente (en WIID) en la lista.
            listaAccounts.forEach((wiid, listAcc) -> {
            	
            	boolean saltar = false;
            	
            	saltar = accountDB.isClientUpdated(wiid);
            	
            	if (!saltar) {
                	// Navegacion hasta pagina del cliente.
                	nav.navigate().to(Constant.URLCLIENTID + wiid);
                	
                	RPA.sleep(500);
                	
                	// Pulsacion sobre boton Update Work item del cliente.
                	$(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/p[2]/a")).click();
                	
                	RPA.sleep(2500);
                	
                	WebDriver popup = RPA.switchTo().window("[CLASS:Chrome_WidgetWin_1;TITLE:workfusion System 1 - Google Chrome]");
                	
                	RPA.sleep(500);
                	
                	// Se obtienen los elementos del Popup.
                	UiElement comment = $("[CLASS:Edit;NAME:newComment]");
                	UiElement statusButtonOpen = $("[CLASS:ComboBox]");
                	UiElement buttonUpdate = $("[CLASS:Button;NAME:buttonUpdate]");
                	
                	//imprimirAdornado("Para WIID: " + wiid + " List size: " + listAcc.size());
                	// Se introduce el comentario.
            		String strComment = getDataToComment(listAcc);
            		
            		//imprimirAdornado("Para WIID: " + wiid + " Comentario: " + strComment);
            		// Logica de seleccion de estado en el ComboBox.
            		int optionCombo = Constant.OPEN;
                	
                	if (strComment != Constant.CUENTASACTIVASNO) {
                		optionCombo = Constant.COMPLETED;            		
                	} else {
                		optionCombo = Constant.REJECTED;
                	}
                	
                	comment.clear();
                	comment.setText(strComment);
                	
            		statusButtonOpen.click();
            		
            		accountDB.updateUpdatedAccount(listAcc);
            		
            		RPA.sleep(500);
            		// Se selecciona el ComboBox.
            		List<UiElement> listaOpciones = $$("[CLASS:ListItem]");
            		
            		// Se selecciona el elemento del ComboBox correcto.
            		listaOpciones.get(optionCombo).click();
            		
            		RPA.sleep(500);
            		
            		// Se pulsa sobre el boton actualizar.
            		buttonUpdate.click();
            		
            		popup.close();
            	}
            	            	
            	
            });
            
      
			
    	} catch (NoSuchElementException nse) {
    		
    		imprimirAdornado("serviceBrowser<HomeToWorkItemsUpdateNavegador> =================> Ha ocurrido una exception element: " + nse.getLocalizedMessage());
    		
    	} catch (NoSuchWindowException nsw) {
    		
    		imprimirAdornado("serviceBrowser<HomeToWorkItemsUpdateNavegador> =================> Ha ocurrido una exception window: " + nsw.getLocalizedMessage());
    	}
		
	}
	
	/**
	 * Acceso a la obtencion de datos de cliente. (1a. parte)
	 * 
	 * 
	 * @return HashMap<String, String> Lista de clientes y sus url de actualizacion posterior
	 */
	public List<Client> obtenerClientes(Driver driver) {
		
		List<Client> result = new ArrayList<Client>();
		
		try {
			// Se obtiene el foco sobre la pagina.
			RPA.switchToExistingWindow("[CLASS:Chrome_WidgetWin_1]");
			// Se carga en una lista todos los elementos de la paginacion.
	        UiElementCollection pages = $$(By.xpath("/html/body/div/div[2]/div/nav/ul/li"));
	        
	        List<String> urlPages = new ArrayList<String>();

	        for(UiElement url : pages)  {

	        	// url de la pagina.
	        	urlPages.add(url.findElement(By.xpath("child::*")).getAttribute("href"));

	        }
	        // Se elimina la primera pagina que no contiene url.
	        urlPages.remove(0);
	        // Y la ultima que no contiene url canonica.
	        urlPages.remove(urlPages.size() - 1);
	        
	        imprimirAdornado("Tamaño del array de paginacion: " + urlPages.size());
	       
	        
	        
	        int page = 1;
	        int posTable = 1;

	        for(String pg : urlPages) {
	        	// Se obtienen todos los tr de la tabla.
	        	UiElementCollection table = $$(By.xpath("/html/body/div/div[2]/div/table/tbody/tr"));
	        	// Saltar cabecera
	        	posTable = 1;
	        	for(UiElement tr: table) {
	        		// Se toman los tds de la fila.
	        		List<WebElement> tds = tr.findElements(By.xpath("child::td"));
	        	
	        		if (tds.size() > 0) {
	        			// Si pasa el filtro...
	        			if (tds.get(2).getText().contains("Verify Account Position") && tds.get(4).getText().contains("Open")) {
	        				
	        				result.add(new Client(tds.get(1).getText(), 
	        									page, 
	        									posTable));
	        				        			
	        			}
	        		
	        		}
	        		posTable++;
	        	}
	        	
	        	// Filtro para DEBUG
	        	if (Constant.MAXNUMPAGS_PAGINATION != 0 && page >= Constant.MAXNUMPAGS_PAGINATION) break;
	        	imprimirAdornado("Procesando url: " + pg);  
	        	// Navegamos hasta la siguiente pagina.
	        	driver.navigate().to(pg);
	        	RPA.sleep(200);            		
	      
	        	page++;
	        }

	        for (int p = 0; p < result.size(); p++) {
	        	driver.navigate().to(Constant.URLCLIENTID + result.get(p).getWIID());
	        	
	        	RPA.sleep(500);
	        	
	        	String clientid = $(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[1]/p")).getText().split("\\n")[0].split(" ")[2].trim();
	        	
	        	imprimirAdornado("Cliente ID ==> " + clientid);
	        	result.get(p).setClientID(clientid);
	        	
	        	// Se inserta en la BD.
	        	client.create(result.get(p));
	        	
	        }
	        
	        // Volvemos a la pagina Home
	        driver.navigate().to(Constant.URLHOMEPATH);
			
			
    	} catch (NoSuchElementException nse) {
    		
    		imprimirAdornado("serviceBrowser<obtenerClientes> =================> Ha ocurrido una exception element: " + nse.getLocalizedMessage());
    		
    	} catch (NoSuchWindowException nsw) {
    		
    		imprimirAdornado("serviceBrowser<obtenerClientes> =================> Ha ocurrido una exception window: " + nsw.getLocalizedMessage());
    	}
		
		return result;
	}
	
	/**
	 * (privada) Construccion del comentario en base al estado de la cuenta a insertar en popup.
	 * 
	 * 
	 * @param lista	List<Account>
	 * @return	String
	 */
    private String getDataToComment(List<Account> lista) {
    	
    	String result = Constant.CUENTASACTIVASNO;
    	List<String> cuentas = new ArrayList<String>();
    	
    	for (Account acc : lista) {
    		
    		if (acc.getStatus().trim().contains(Constant.CUANTAACTIVE)) {
    			
    			cuentas.add(acc.getAccountID());
    		}
    	}
    	
    	if (cuentas.size() > 0 ) {
    		result = Constant.CUENTASACTIVASSI + cuentas.toString().substring(1, cuentas.toString().length() - 1);
    	}
    	
    	
    	return result;
    }
	
	
}