import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Main {

	
	public static void main(String [] args) throws IOException{
		long time_start, time_end;
		//Variable que contara tiempo de ejecucion
		time_start = System.currentTimeMillis();
//-------------------------------------------------------------------------------------------------------------------------	    
//		PREPARACION DE DATOS DE ENTRADA
//-------------------------------------------------------------------------------------------------------------------------		
		//Variable para indicar si se pide traza
		boolean traza = false;
		//Ruta relativa de los ficheros de prueba
		String entrada = null;
		String salida = null;
		//String que contiene la ayuda a mostrar
		String ayuda = "SINTAXIS:  \n"
				+ "robot [-t][-h][fichero_entrada] [fichero_salida] \n "
				+ "-t  Traza la aplicacion del algoritmo a los datos \n"
				+ "-h  Muestra esta ayuda  \n"
				+ "fichero_entrada  Nombre del fichero de entrada \n"
				+ "fichero_salida  Nombre del fichero de salida \n";
		
		//Controla varios errores en la entrada por consola
		if ((args.length > 4)|| (args.length == 0)){
			System.out.println("Numero de argumentos incorrecto");
			System.exit(0);
		}
		if(args.length > 1){
			if(args[1].equals("-t")){
				System.out.println("Argumentos incorrectos");
				System.out.println(ayuda);
				System.exit(0);
			}
		}else if(args.length > 3){
			if(args[1].equals("-t")||args[2].equals("-t")||args[3].equals("-t")){
				System.out.println("Argumentos incorrectos");
				System.out.println(ayuda);
				System.exit(0);	
			}else if(args[2].equals("-h")||args[3].equals("-h")){
				System.out.println("Argumentos incorrectos");
				System.out.println(ayuda);
				System.exit(0);
			}
		}
		
		//Indica si hay fichero de entrada y salida
		boolean hayEntrada = false;
		boolean haySalida = false;
		
		//Bucle para recorrer los argumentos
		for (int i = 0; i< args.length; i++){
			if(args[i].equals("-h")){
				//Si la ayuda es el segundo argumento tiene que haber un -t delante o estará erroneo
				if(i==1){
					if(traza){
						System.out.println(ayuda);
					}else{
						System.out.println("Argumentos incorrectos!");
						System.out.println(ayuda);
						System.exit(0);	
					}
				}else{
					System.out.println(ayuda);
					
				}
			//Si hay -t activa la traza
			}else if(args[i].equals("-t")){
				traza = true;
			//Guarda el nombre del archivo de entrada
			}else if (!hayEntrada){
				entrada = args[i];
				hayEntrada = true;
			//Guarda el nombre del archivo de salida
			}else{
				salida = args[i];
				haySalida = true;
			}
		}
		
		//Si no hay fichero de entrada es un error
		if(!hayEntrada){
			System.out.println("No hay fichero de entrada");
			System.exit(0);
		}
		
		BufferedReader in = null;
		//Lectura del fichero de operaciones
		try{
		    in = new BufferedReader(new InputStreamReader(new FileInputStream(entrada), "UTF-8"));
		}catch(FileNotFoundException ex){
			System.out.println("El fichero de entrada no existe");
			System.exit(0);
		}
//-------------------------------------------------------------------------------------------------------------------------	    
//						PREPARACION DE LOS NODOS
//-------------------------------------------------------------------------------------------------------------------------			
	    int filas = 0;
	    int colum = 0;
	    //Extrae numero de filas y columnas de los dos primeros valores
	    for(int j = 0; j < 2; j++){
	    	if (j == 0) {filas = Integer.parseInt(in.readLine());}
	    	else if(j == 1){colum = Integer.parseInt(in.readLine());}
	    }
	   
	    //Extrae el numero total de casillas
	    int casillas = filas*colum;
	    
	    //Crea la matriz de adyacencia vacia
	    int[][] mAdy = new int [casillas][casillas];
	    
	    //Crea array de nodos C
	    ArrayList<Nodo> c = new ArrayList<Nodo>();
	    int n = 1;
	    int m = 1;
	    
	    //Posicion lineal en la matriz de casillas
	    int i = 1;
	    
	    //Variables para contar que solo haya una S y una R y al menos un obstaculo
	    int hayS = 0;
	    int hayR = 0;
	    int hayO = 0;
	    
	    String num;
	    //Bucle para guardar los nodos
	    while ((num = in.readLine())!=null) {
	    	if(num.equals("O")){
	    		//Si es un obstáculo
	    		//Constructor con posicion lineal en la matriz, coordenadas y boolean si es obstaculo
	    		Nodo nodo = new Nodo ( i, n, m, true, false, false);
	    		c.add(nodo);
	    		hayO ++;
	    	}else if(num.equals("S")){
	    		//Si es el final
	    		//Constructor con posicion lineal en la matriz, coordenadas y boolean si es S
	    		Nodo nodo = new Nodo ( i, n, m, false, false, true);
	    		if((nodo.getN() == filas)||(nodo.getN()==1)||(nodo.getM()==colum)||(nodo.getM()==1)){
	    			c.add(nodo);
	    			hayS ++;
	    			//Comprueba que solo haya una S
	    			if(hayS > 1){
	    				System.out.println("Hay mas de un S");
	    				System.exit(0);
	    			}
	    		//Comprueba que S sea periferico
	    		}else{
	    			System.out.println("S no es periferico");
	    			System.exit(0);
	    		}	
	    	}else if(num.equals("R")){
	    		//Si es el inicio
	    		//Constructor con posicion lineal en la matriz, coordenadas y boolean si es R
	    		Nodo nodo = new Nodo ( i, n, m, false, true, false);
	    		c.add(nodo);
	    		hayR ++;
	    		//Comprueba que no haya mas de un R
    			if(hayR > 1){
    				System.out.println("Hay mas de un R");
    				System.exit(0);
    			}
	    	}else{
	    		//Try-catch que salta si hay algun caracter no valido
	    		try{
	    			//Casillas normales, con valor de casilla, posicion lineal y coordenadas
		    		if(Long.parseLong(num) >= Integer.MAX_VALUE){
		    			System.out.println("Valor que excede del rango entero");
		    			System.exit(0);
		    		//Controla que no haya numeros negativos en la energia
		    		}else if(Integer.parseInt(num) < 0){
		    			System.out.println("Hay numeros negativos");
		    			System.exit(0);
		    		}else{
		    			Nodo nodo = new Nodo (Integer.parseInt(num), i, n, m);
			    		c.add(nodo);
		    		}
	    		}catch(NumberFormatException nfe){
	    			System.out.println("Caracteres no validos");
	    			System.exit(0);
	    		}
	    	}
	    	i++;
	    	m++;
	    	//Si llega al final de la fila, cambia de fila
	    	if (m > colum){
	    		m = 1;
	    		n++;
	    	}
	    }
	    
	    //No funciona si no hay al menos un obstaculo
    	if(hayO == 0){
    		System.out.println("No hay ningun obstaculo");
    		System.exit(0);
    	}
    	
    	//Cierra la lectura de archivo
	    in.close();
//-------------------------------------------------------------------------------------------------------------------------	    
//					DIJKSTRA
//-------------------------------------------------------------------------------------------------------------------------	    
	    //Nuevo dijkstra con los datos C, la matriz de adyacencia vacia y el booleano de traza
	    Dijkstra dijkstra = new Dijkstra(c, mAdy, traza);
	    String solucion = dijkstra.dijkstra();
	    
	    //Tiempo que tardo la ejecucion
	    time_end = System.currentTimeMillis();
	    solucion += "\nTiempo: " +(time_end -time_start) +" milisegundos.";
	    
	    //Si hay salida escribe el archivo, si no por pantalla
	    if(haySalida){
	    	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(salida), "UTF-8"));
	    	System.out.println("Se escribió el archivo");
	    	out.write(solucion);
	    	out.flush();
	    	out.close();
	    }else{
	    	System.out.println(solucion);
	    }    
	}
}

