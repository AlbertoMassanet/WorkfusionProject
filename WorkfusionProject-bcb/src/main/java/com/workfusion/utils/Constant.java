package com.workfusion.utils;


public class Constant {
	

	
	// DB
	public final static String BDTOTABLE = "wfdb.ds.";
	
	// Browser
	public final static String URLLOGINPATH = "https://workfusion-test.uipath.com/login";
	public final static String URLHOMEPATH = "https://workfusion-test.uipath.com/home";
	
	public final static String URLCLIENTID = "https://workfusion-test.uipath.com/work-items/";
	
	// Reintentos
	public final static int MAX_REINTENTOS = 3;
	
	// Output	
	public static final String[] CABEXCEL = {"Cuenta", "Saldo", "Estatus"};
	
    public static final int OPEN = 1;
    public static final int REJECTED = 2;
    public static final int COMPLETED = 3;
    
    public static final String CUENTASACTIVASSI = "Existe/n cuenta/s activa/s: ";
    public static final String CUENTASACTIVASNO = "No existen cuentas activas para este cliente";
    
    public static final String CUENTAOPEN = "Open"; 		
    public static final String CUENTAVERIFY = "Verify Account Position";
    public static final String CUANTAACTIVE = "Active";
    public static final String CUENTACOMPLETED = "Completed";

     
    
    // Editables
    public static final int MAXNUMPAGS_PAGINATION = 0; // 0 - leera toda la paginacion.
	
	public final static String VAULTNAME = "vaultworkfusion";
    
	public final static String PATHOUTPUT = "C:\\workfusionSystem3\\ListAccounts.xlsx";
	
	public final static String APPPATH = "C:\\workfusionSystem3\\workfusion-System3.exe";
    
} 