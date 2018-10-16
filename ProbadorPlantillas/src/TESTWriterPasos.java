import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class TESTWriterPasos {

	
	
	//------- INSERTAR NUESTRO CÓDIGO DE ESCRIBIR LA PLANTILLA
	
	
	
	
	
	
	
	//------------------------------------------------------------
	

	public void writeJFICHSAL(Map<String, String> datos, String numeroPaso, int i, String letraPaso,
			BufferedWriter writerCortex) throws IOException {
		// TODO Auto-generated method stub
		//----------------Fichero de plantilla JFICHENT--------------------------
	    FileReader ficheroJFICHSAL = new FileReader("C:\\Users\\0014879\\Desktop\\Cortex\\Plantillas\\JFICHSAL.txt");
	    BufferedReader lectorJFICHSAL = new BufferedReader(ficheroJFICHSAL);	
	    //----------------Variables------------------------------------------
	    String linea, nombre;
	    int contadorLinea = 0;
	    
	    //----------------Método---------------------------------------------
	    nombre = datos.get("Salida" + String.valueOf(i));
	    for(int j = nombre.length(); j < 8; j++) {
			nombre += " ";
		}
	    while((linea = lectorJFICHSAL.readLine()) != null) {
	    	contadorLinea ++;
	    	switch (contadorLinea) {
	    	case 8:
	    		linea = linea.replace("DDNAME--", nombre);
	    		break;
	    	case 14:
	    		linea = linea.replace("DDNAME--", nombre);
	    		break;
	    	case 19:
	    		linea = linea.replace("DDNAME--", nombre);
	    		break;
	    	default:
				break;
	    	}
	    	System.out.println("Escribimos: " + linea);
	    	writerCortex.write(linea);
	    	writerCortex.newLine();
	    }
	    lectorJFICHSAL.close();	 
	}

	public void writeJFICHENT(Map<String, String> datos, String numeroPaso, int i, String letraPaso, BufferedWriter writerCortex) throws IOException {
		// TODO Auto-generated method stub
		//----------------Fichero de plantilla JFICHENT--------------------------
	    FileReader ficheroJFICHENT = new FileReader("C:\\Users\\0014879\\Desktop\\Cortex\\Plantillas\\JFICHENT.txt");
	    BufferedReader lectorJFICHENT = new BufferedReader(ficheroJFICHENT);	
	    //----------------Variables------------------------------------------
	    String linea, nombre;
	    int contadorLinea = 0;
	    
	    //----------------Método---------------------------------------------
	    while((linea = lectorJFICHENT.readLine()) != null) {
	    	contadorLinea ++;
	    	switch (contadorLinea) {
	    	case 2:
	    		nombre = datos.get("Entrada" + String.valueOf(i));
	    		for(i = nombre.length(); i < 8; i++) {
	    			nombre += " ";
	    		}
	    		linea = linea.replace("DDNAME--", nombre);
	    		break;
	    	default:
				break;
	    	}
	    	System.out.println("Escribimos: " + linea);
	    	writerCortex.write(linea);
	    	writerCortex.newLine();
	    }
	    lectorJFICHENT.close();	 
	}

	public void writeJBORRAF(String numeroPaso, int i, String letraPaso, BufferedWriter writerCortex) throws IOException {
		// TODO Auto-generated method stub
		//----------------Fichero de plantilla JBORRAF--------------------------
	    FileReader ficheroJBORRAF = new FileReader("C:\\Users\\0014879\\Desktop\\Cortex\\Plantillas\\JBORRAF.txt");
	    BufferedReader lectorJBORRAF = new BufferedReader(ficheroJBORRAF);	
	    //----------------Variables------------------------------------------
	    String linea;
	    int contadorLinea = 0;
	    
	    //----------------Método---------------------------------------------
	    while((linea = lectorJBORRAF.readLine()) != null) {
	    	contadorLinea ++;
	    	switch (contadorLinea) {
	    	case 2:
	    		linea = linea.replace("//---D-", "//" + letraPaso + numeroPaso + "D" + String.valueOf(i));
	    		break;
	    	default:
				break;
	    	}
	    	System.out.println("Escribimos: " + linea);
	    	writerCortex.write(linea);
	    	writerCortex.newLine();
	    }
	    lectorJBORRAF.close();	 
	}
}
