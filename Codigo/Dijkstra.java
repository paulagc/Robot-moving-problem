import java.util.ArrayList;

public class Dijkstra {
	//Arrays especial, predecesor y C
	private ArrayList<Nodo> predecesor;
	private ArrayList<Integer> especial;
	private ArrayList<Nodo> c;
	//Matriz de adyacencia
	private int[][] mAdy;
	//Camino solucion
	private ArrayList<Nodo> caminoMin;
	//Nodos R y S
	private Nodo r;
	private Nodo s;
	//Energia total acumulada
	private int energia;
	//Traza
	private String traza;
	private boolean hayTraza;
	//String que contiene la solucion al problema
	private String solucion;
	
	public Dijkstra(ArrayList<Nodo> c, int [][] mAdy, boolean hayTraza){
		this.predecesor = new ArrayList<Nodo>();
		this.especial = new ArrayList<Integer>();
		this.caminoMin = new ArrayList<Nodo>();
		this.c = new ArrayList<Nodo>(c);
		//Comprueba que haya nodos R y S y los inicializa si existen
		datos();
		//Energia consumida 0 al principio
		this.energia = 0;
		//Copia matriz de adyacencia vacia con tamaño
		this.mAdy = mAdy;
		//Inicializa la traza
		this.traza = "TRAZA: \n";
		this.hayTraza = hayTraza;
		//Inicializa la solucion dijkstra vacia
		this.solucion = "\n";
		
	}
	//Inicializa datos de nodos
	void datos(){
		boolean hayR = false;
		boolean hayS = false;
		//Recorre C buscando R y S
		for(Nodo n:c){
			if(n.isR()){
				this.r = new Nodo(n);
				hayR = true;
			}else if(n.isS()){
				this.s = new Nodo (n);
				hayS = true;
			}
		}
		//Finaliza el programa si R o S no estan
		if(!hayR){
			System.out.println("No hay R");
			System.exit(0);
		}else if(!hayS){
			System.out.println("No hay S");
			System.exit(0);
		}
	}
	
//-------------------------------------------------------------------------------------------------------------------------	    
//		FUNCIONES AUXILIARES
//-------------------------------------------------------------------------------------------------------------------------		
	//Funcion que dice si "a" es adyacente a un nodo "n"
	boolean adyacente (Nodo n, Nodo a){
		
		//Coordenadas nodo referencia
		int nF = n.getN();
		int nC = n.getM();
		//Coordenadas nodo posible adyacente
		int aF = a.getN();
		int aC = a.getM();
		
		//Si es el mismo nodo, no son adyacentes
		if(aF==nF && aC == nC){
			return false;
		
		//Si es un obstaculo lo toma como no adyacente
		}else if(a.isO()){
			return false;
		
		//Si las coordenadas fila y columna son del mismo número ó +1, -1 son adyacentes
		}else if(((aF==nF)||(aF==nF+1)||(aF==nF-1))&&((aC==nC)||(aC==nC+1)||(aC==nC-1))){
			return true;
		}
		return false;	
	}
	
	//Funcion que calcula la distancia o energia entre dos nodos, de v a w
	int distancia (Nodo v, Nodo w){
		
		int dist = -1;
		//Si no son adyacentes no hay distancia, ponemos -1 (infinito)
		if(!adyacente(v,w)){
			return dist;
		//Ademas si alguno es obstaculo tambien distancia -1
		}else if(v.isO() || w.isO()){
			return dist;
		//Si son adyacentes y casillas normales la distancia (energia consumida) es el valor de la casilla destino
		}else{
			dist = w.getValor();
		}
		return dist;
	}
	
	//Funcion que calcula el nodo mas economico de los disponibles en especial y devuelve su posicion
	int minimo (){
		int minimo = Integer.MAX_VALUE;
		int solucion = 0;
		int pos = 0;
		//Busca el minimo en el array especial
		for(int nodo:especial){
			//Que no sea -1 (infinito), que esté en C todavia y que minimice lo que llevamos recorrido
			if ((nodo != -1) && (c.get(pos).isV() == false) && (minimo > nodo)){
				minimo = nodo;
				solucion = pos+1;	
			}
			pos++;
		}
		//Pone visitado == true siempre que la solucion sea accesible
		if(solucion != 0){
			c.get(solucion-1).setV(true);
		}else{
			//No tiene solucion, inaccesible
			System.out.println("No tiene solucion, nodo inaccesible");
			System.exit(0);
		}
		return solucion;
	}
	
	// Metodo que calcula el camino solucion a partir de Predecesor y la energia consumida
	void caminoMin(){
		//desde la posicion de S (en indice de array uno menos)
		int i = s.getPos()-1;
		//primero agrega nodo S a la solucion
		caminoMin.add(s);
		//mientras que no llegue a la posicion de R ir recorriendo en predecesor de S a R
		while(i != r.getPos()-1){
			Nodo n = predecesor.get(i);
			 caminoMin.add(n);
			 i = n.getPos()-1;
		}
		//Calcula la energia total consumida
		for(int x = caminoMin.size()-1; x>=0; x--){
			energia += caminoMin.get(x).getValor();
		}
		//Imprime las coordenadas del camino minimo
		for(int x = caminoMin.size()-1; x>=0; x--){
			if(x == caminoMin.size()-1){
				solucion += "R["+caminoMin.get(x).getN() +", " +caminoMin.get(x).getM() +"],";
			}else if(x == 0){
				solucion += "S["+caminoMin.get(x).getN() +", " +caminoMin.get(x).getM() +"]";
			}else{
				solucion += "["+caminoMin.get(x).getN() +", " +caminoMin.get(x).getM() +"],";
			}
			
		}
		//Imprime la energia consumida
		solucion += "\nEnergia consumida: " +energia +"\n";
	}
	
	//Rellena la matriz de adyacencia con sus valores
	void rellenarMatriz(){
		for(int x = 0; x < mAdy.length; x++){
			for(int y = 0; y< mAdy.length; y++){
				//Introduce en la casilla el valor distancia de los de cada fila a las columnas
				mAdy[x][y] = distancia(c.get(x), c.get(y));	
			}
		}	
	}
	
	//Recorre la matriz de adyacencia cuando Dijkstra la necesita
	int recorrerMatriz(Nodo v, Nodo w){
		int energia = -1;
		for(int x = 0; x < mAdy.length; x++){
			for(int y = 0; y< mAdy.length; y++){
				//Si encuentro esa casilla, recupero la energia o valor distancia
				if(x == (v.getPos()-1) && y== (w.getPos()-1)){
					energia = mAdy[x][y];
				}
			}
		}
		return energia;	
	}
//-------------------------------------------------------------------------------------------------------------------------	    
//	   		FUNCION DIJKSTRA
//-------------------------------------------------------------------------------------------------------------------------	
	String dijkstra (){
		//Rellena la matriz de adyacencia
		rellenarMatriz();
		
		//Contador pasos dijkstra
		int pasos = 0;
		
		//Inicializar
		for(Nodo nodo : c){
			//si no tiene arista con R (no son adyacentes) se mete -1 (infinito), sino el valor distancia con R
			especial.add(recorrerMatriz(r,nodo));
			predecesor.add(r);
		}
		//Quito R de la lista, visitado = true
		c.get(r.getPos()-1).setV(true);
		
		//Bucle que va cogiendo nodos de C que no esten visitados
		while (c.get(s.getPos()-1).isV() == false){
			//v guarda el numero de nodo que aun esta en C y es minimo de especial 
			int min = minimo();
			Nodo v = new Nodo(c.get(min-1));
			
			//Si v es distinto de la salida
			if (v.getPos() != s.getPos()){
				
				for(int m = 0; m < c.size(); m++){
					Nodo w = new Nodo(c.get(m));
					
					//Si el nodo aun no fue visitado y es adyacente al actual
					if((w.isV() == false) && adyacente(v, w)){
						//Si especial de w es mayor que especial de v más su distancia
						if ((especial.get(w.getPos()-1) == -1) ||(especial.get(w.getPos()-1)) > (especial.get(v.getPos()-1) + recorrerMatriz(v,w))){
							//En la posicion de w pone especial de v + su distancia
							especial.set( w.getPos()-1, (especial.get(v.getPos()-1) + recorrerMatriz(v,w)) );
							//En predecesor de w pone v
							predecesor.set(w.getPos()-1, v);
							//Aumenta 1 paso dikjstra
							pasos++;
							//Guarda la traza
							traza +="\nPaso " +pasos +"\n"+"C: [";
							for(Nodo n: c){
								if(!n.isV()){
									traza += n.getPos() +", ";
								}
							}
							traza+= "]\nEspecial: " +especial +"\nPredecesor: [";
							for(Nodo n: predecesor){
								traza+= n.getPos() +", ";
							}
							traza +="] \n";
						}					
						
					}
				}
			}
		}		
		
		//Calcula el camino solucion y su energia
		caminoMin();
		//Si la traza esta activa la agrega a la solucion
		if(hayTraza){
			solucion += "\n" +traza;
		}
		return solucion;
	}
	
	
	
	public ArrayList<Nodo> getPredecesor() {
		return predecesor;
	}

	public void setPredecesor(ArrayList<Nodo> predecesor) {
		this.predecesor = predecesor;
	}

	public ArrayList<Integer> getEspecial() {
		return especial;
	}

	public void setEspecial(ArrayList<Integer> especial) {
		this.especial = especial;
	}

	public ArrayList<Nodo> getC() {
		return c;
	}

	public void setC(ArrayList<Nodo> c) {
		this.c = c;
	}
	public int getEnergia() {
		return energia;
	}
	public void setEnergia(int energia) {
		this.energia = energia;
	}
	public int[][] getmAdy() {
		return mAdy;
	}
	public void setmAdy(int[][] mAdy) {
		this.mAdy = mAdy;
	}
}
