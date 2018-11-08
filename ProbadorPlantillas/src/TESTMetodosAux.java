import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TESTMetodosAux {
	static TESTAvisos  avisos = new TESTAvisos();
	TESTLectorPasos lectorPasos =  new TESTLectorPasos();


	public boolean checkLiteralesPARDB2(String param) {
		// TODO Auto-generated method stub
		if (!param.startsWith("&")) {
			return true;
		}
		for(int i = 1; i < param.length(); i++) {
			if(param.charAt(i) == '-') {
				if(!(param.charAt(i+1) == '&')) {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<String> buscaInfoProc(int pasoE, String letraPaso, String nombre) throws IOException{
		boolean seguir = true, buscar = false;	
		String linea;
		ArrayList<String> infoFichero = new ArrayList<String>();
		//----------------Fichero de plantilla JPROC--------------------------
	    FileReader ficheroPROC = new FileReader("C:\\Cortex\\PROC\\" + TESTmainApp.programa.substring(0,6) + ".txt");
	    BufferedReader lectorPROC = new BufferedReader(ficheroPROC);
		//-----------------------------------------------------------------------
	    
	    String numeroPaso;    
	    numeroPaso = (pasoE < 10) ? "0" + String.valueOf(pasoE) : String.valueOf(pasoE) ;
	    
	    while((linea = lectorPROC.readLine()) != null && seguir) {
	    	if(linea.startsWith("//" + letraPaso + numeroPaso)) {
	    		buscar = true;
	    	}
	    	if(buscar) {
	    		if(linea.startsWith("//" + nombre + " ")) {
	    			infoFichero.add(linea);
	    			linea = lectorPROC.readLine();
	    			while (linea.startsWith("//  ")) {
						infoFichero.add(linea);
						linea = lectorPROC.readLine();
					}
	    			buscar = false;
	    			seguir = false;
	    		}
	    	}	    	
	    }
	    lectorPROC.close();	
	    
	    return infoFichero;
	}
	
	public Map<String, String> infoFichero(int pasoE, String letraPaso, String nombre) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> infoFichero = new ArrayList<String>();
		Map<String, String> infoFich = new HashMap<String, String>();
		
		infoFichero = buscaInfoProc(pasoE, letraPaso, nombre);
	    
	    String clave, valor;
    	int primario = 0, secundario = 0, tama�o;
	    for(int j = 0; j < infoFichero.size(); j++) {
	    	int index = 1;
		    while (index != -1) {
				index = infoFichero.get(j).indexOf('=', index);
				if (index != -1) {
					clave = lectorPasos.leerClave(infoFichero.get(j), index);
					valor = lectorPasos.leerValor(infoFichero.get(j), index);
					valor = valor.replace("(","");
					if (!clave.equals("") && !valor.equals("")) {
						infoFich.put(clave, valor);
					}
				}
				if (index != - 1) {
					index ++;
				}
			}
		    if(infoFichero.get(j).contains("SPACE") && !infoFich.get("SPACE").equals("CYL") && !infoFich.get("SPACE").equals("TRK")) {
		    	if (infoFich.containsKey("LRECL")) {
		    		int ini = 1, fin = 2;
			    	ini = infoFichero.get(j).lastIndexOf("(");
			    	for(int i = ini; i < infoFichero.get(j).length(); i++) {
			    		if(infoFichero.get(j).charAt(i) == ',') {
			    			fin = i;
			    			i = 1000;
			    		}
			    	}
			    	tama�o = Integer.valueOf(infoFichero.get(j).substring(ini + 1, fin));
			    	primario = Integer.parseInt(infoFich.get("SPACE")) * tama�o / Integer.parseInt(infoFich.get("LRECL")) / 1000;
			    	primario = primario < 5 ? 10 : primario;
			    	
			    	ini = fin;
			    	for(int i = ini; i < infoFichero.get(j).length(); i++) {
			    		if(infoFichero.get(j).charAt(i) == ')') {
			    			fin = i;
			    			i = 1000;
			    		}
			    	}
			    	tama�o = Integer.valueOf(infoFichero.get(j).substring(ini + 1, fin));
			    	secundario = Integer.parseInt(infoFich.get("SPACE")) * tama�o / Integer.parseInt(infoFich.get("LRECL")) / 1000;; 
			    	secundario = secundario < 3 ? 3 : secundario;
		    	}else {
		    		TESTAvisos.LOGGER.log(Level.INFO, letraPaso + String.valueOf(pasoE) + " //Fichero no contiene LRCL: " + nombre);
					infoFich.put("LRECL","LRECL");
		    	}
		    }else {
		    	if(infoFichero.get(j).contains("SPACE") && infoFich.get("SPACE").equals("CYL")) {
		    		primario = 15;
		    		secundario = 1;
		    	}
		    }
	    }
	    
	    clave = "Definicion";
	    valor = "(" + infoFich.get("LRECL") + ",(" + String.valueOf(primario) + "," + String.valueOf(secundario) + "))";
		infoFich.put(clave, valor);
		
		if(!infoFich.containsKey("DSN")) {
			clave = "DUMMY";
			valor = infoFichero.get(0);
			infoFich.put(clave, valor);
		}
		else {
			if(infoFich.get("DSN").endsWith("XP")) {
				infoFich.replace("DISP", "TEMP");
			}
		}
		System.out.println("------- Datos sacados del Fichero:  -------");
	    infoFich.forEach((k,v) -> System.out.println(k + "-" + v));
	    System.out.println("----------------------------------------");
		
		return infoFich;
	}

	public Map<String, String> infoReportes(String nombre, int pasoE, String letraPaso) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> infoFichero = new ArrayList<String>();
		Map<String, String> infoRep = new HashMap<String, String>();
		String clave, valor;
		
		infoFichero = buscaInfoProc(pasoE, letraPaso, nombre);
		
		if (infoFichero.size() == 1) {
			clave = "ReportKey";
			valor = infoFichero.get(0);
			
		}else {
			clave = "ReportKey";
			valor = "* Error al leer l�nea de Reporte - Nombre reporte: " + nombre; 
			TESTAvisos.LOGGER.log(Level.INFO, letraPaso + String.valueOf(pasoE) + " // Error al leer el reporte - Nombre reporte: " + nombre);
		}
			
		infoRep.put(clave, valor);
		return infoRep;
	}

	public String infoFTP(int pasoE, String letraPaso, String fhost) throws IOException {
		// TODO Auto-generated method stub
		boolean seguir = true, buscar = false;	
		@SuppressWarnings("unused")
		String linea, clave, valor = "";
		int index = 0;
		//----------------Fichero de plantilla JPROC--------------------------
	    FileReader ficheroPROC = new FileReader("C:\\Cortex\\PROC\\" + TESTmainApp.programa.substring(0,6) + ".txt");
	    BufferedReader lectorPROC = new BufferedReader(ficheroPROC);
		//-----------------------------------------------------------------------
	    
	    String numeroPaso;    
	    numeroPaso = (pasoE < 10) ? "0" + String.valueOf(pasoE) : String.valueOf(pasoE) ;
	    
	    while((linea = lectorPROC.readLine()) != null && seguir) {
	    	if(linea.startsWith("//" + letraPaso + numeroPaso)) {
	    		buscar = true;
	    	}
	    	if(buscar) {
	    		if(linea.contains(fhost + ".") && linea.contains("DSN=")){
	    			index = linea.indexOf('=', index);
	    			clave = lectorPasos.leerClave(linea, index);
					valor = lectorPasos.leerValor(linea, index);
					buscar = false;
					seguir = false;
	    		}
	    	}	    	
	    }
	    lectorPROC.close();
		return valor;
	}

	public Map<String, String> infoSort(int paso, String letraPaso) throws IOException {
		// TODO Auto-generated method stub
	    Map<String, String> infoFichIn = new HashMap<String, String>();
	    Map<String, String> infoFich   = new HashMap<String, String>();
	    String clave, valor;
	    
	    infoFichIn = infoFichero(paso, letraPaso, "SORTIN");
	    infoFich   = infoFichero(paso, letraPaso, "SORTOUT");    
	    clave = "SORTIN";
	    valor = infoFichIn.get("DSN");
	    infoFich.put(clave, valor);
	    
		return infoFich;
	}
	
	public Map<String, String> infoFtpReb(int pasoE, String letraPaso) throws IOException {
		// TODO Auto-generated method stub
		Map<String, String> infoFich   = new HashMap<String, String>();
		
		infoFich = infoFichero(pasoE, letraPaso, "SORTI1");

		return infoFich;
	}
	
	public String infoDSN(int pasoE, String letraPaso, String name) throws IOException {
		// TODO Auto-generated method stub
		boolean seguir = true, buscar = false;	
		@SuppressWarnings("unused")
		String linea, clave, valor = "";
		int index = 0;
		//----------------Fichero de plantilla JPROC--------------------------
	    FileReader ficheroPROC = new FileReader("C:\\Cortex\\PROC\\" + TESTmainApp.programa.substring(0,6) + ".txt");
	    BufferedReader lectorPROC = new BufferedReader(ficheroPROC);
		//-----------------------------------------------------------------------
	    
	    String numeroPaso;    
	    numeroPaso = (pasoE < 10) ? "0" + String.valueOf(pasoE) : String.valueOf(pasoE) ;
	    
	    while((linea = lectorPROC.readLine()) != null && seguir) {
	    	if(linea.startsWith("//" + letraPaso + numeroPaso)) {
	    		buscar = true;
	    	}
	    	if(buscar) {
	    		if(linea.startsWith("//" + name + "  ")){
	    			index = linea.indexOf('=', index);
	    			clave = lectorPasos.leerClave(linea, index);
					valor = lectorPasos.leerValor(linea, index);
					buscar = false;
					seguir = false;
	    		}
	    	}	    	
	    }
	    lectorPROC.close();
		return valor;
	}
	
	public void infoJFUSION(Map<String, String> datos, int pasoE, String letraPaso) throws IOException {
		// TODO Auto-generated method stub
		Map<String, String> infoFich   = new HashMap<String, String>();
		String[] ficheros;
		int contadorFicheros = 0;
		String clave = "", valor = "";
		
		pasoE -= 2;
		for(int i = 1; datos.containsKey("FICHA" + String.valueOf(i)); i++) {
			ficheros = datos.get("FICHA" + String.valueOf(i)).split(",");
			ficheros[0] = ficheros[0].replace("ENTRADA=", "");
			
			if(ficheros[0].contains("SORTIDA=")) {
				infoFich = infoFichero(pasoE, letraPaso, ficheros[0].replace("SORTIDA=", ""));
				if (infoFich.containsKey("MGMTCLAS")){
					datos.put("MGMTCLAS", infoFich.get("MGMTCLAS"));
				}
				datos.put("Definicion", infoFich.get("Definicion"));
				datos.put("DSN", infoFich.get("DSN"));
				datos.put("SALIDA", ficheros[0].replace("SORTIDA=", ""));
				
			}else {
				for (int j = 0; j < ficheros.length; j++) {
					contadorFicheros++;
					clave = "DSN" + contadorFicheros;
					valor = infoDSN(pasoE, letraPaso, ficheros[j]);
					datos.put(clave, valor);
					datos.put("FICH" + contadorFicheros, ficheros[j]);
				}
			}	
		}
		
	}
	
	public static ArrayList<String> ComprobarTama�oLinea(String cabecera, String linea, String fi, Map<String, String> datos) {
		// TODO Auto-generated method stub
		ArrayList<String> salida = new ArrayList<String>();
		if((linea.trim() + fi + datos.get(cabecera)).length() < 72) {
			if(datos.get(cabecera) == null) {
				salida.add(0 ,linea.trim() + fi.trim());
			}else {
				if (fi.isEmpty()) {
					salida.add(0 ,linea.trim() + fi.trim() + datos.get(cabecera));
				}else {
					salida.add(0 ,linea.trim() + fi.trim() + " " + datos.get(cabecera));
				}
			}
			salida.add(1, "");			 
		}
		else{
			fi = linea.trim()+ fi.trim() + " " + datos.get(cabecera);
			for(int i = 72; i > 0; i--) {
				if(fi.lastIndexOf(" ", i) != -1) {
					salida.add(0, fi.substring(0, fi.lastIndexOf(" ", i)));
					salida.add(1, fi.substring(fi.lastIndexOf(" ", i)) + " ");
					i = -1;
				}
			}			
		}		
		return salida;
	}

}