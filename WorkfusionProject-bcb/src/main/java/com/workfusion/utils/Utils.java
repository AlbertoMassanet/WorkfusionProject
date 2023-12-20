package com.workfusion.utils;

import com.workfusion.rpa.driver.Driver;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.openqa.selenium.WebElement;

public class Utils extends baseClase {
	
	private int posicionElemento = 0;
	private Driver driver = null;
	
	Utils() {
		
	}
	
	Utils(Driver driver) {
		this.driver = driver;
	}
	
	
	// Tomamos de un conjunto de elementos web, el que contenga el texto en textABuscar.
	public WebElement getElementFromElements(List<WebElement> elementos, String textABuscar)
	{
		WebElement result = null;
		
		for(int i = 0; i < elementos.size();i++) {
			// Eliminamos espacios al inicio y al final.
			String texto=elementos.get(i).getText().trim();
			// Eliminamos espacios innecesarios en medio.
	        texto=texto.replaceAll("\\s{2,}", " ");
	        imprimirAdornado("texto : " + texto + " vs textoABuscar: " + textABuscar);
	        // Si el texto del elemento web es igual al texto que buscamos.
			if (texto.equals(textABuscar)) {
				// Tomamos el elemento para devolver.
				result = elementos.get(i);
				// Guardamos la posicion donde lo encontramos.
				posicionElemento = i;
				// Salimos echando leches.
				break;
			} 
			
		}
		
		return result;
	}

	public int getPosicionElemento() {
		// Actualizamos la posicion con respecto al array (0 - 1)
		return posicionElemento + 1;
	}
	
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static boolean isTimestamp(String s) {
	    try { 
	        Timestamp.valueOf(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static boolean fileExists(String path) {
		boolean result = false;
		
		try {
			File f = new File(path);
			result = (f.exists() && !f.isDirectory());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static boolean fileToDelete(String path) {
		boolean result = false;
		
		try {
			File f = new File(path);
			return f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}