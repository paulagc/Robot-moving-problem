//Clase Nodo que contendra la energia, posicion lineal y de coordenadas y si es casilla especial

public class Nodo {
	//Valor de la casilla (Energia)
	private int valor;
	//Posicion lineal en la matriz
	private int pos;
	//Coordenadas fila x columna
	private int n;
	private int m;
	//Booleans de obstaculo, inicio y fin
	private boolean o;
	private boolean r;
	private boolean s;
	//Boolean para saber si fue visitado en el algoritmo
	private boolean v;
	
	//Constructor para casillas normales, guarda su peso y posicion
	public Nodo(int num, int pos, int n, int m){
		
		this.valor = num;
		this.pos = pos;
		this.n = n;
		this.m = m;
		this.o = false;
		this.r = false;
		this.s = false;
		this.v = false;
	}
	
	//Constructor para casillas obstaculo, no tienen valor y obs es verdadero
	public Nodo(int pos, int n, int m, boolean o, boolean r, boolean s){
		
		this.pos = pos;
		this.n = n;
		this.m = m;
		this.o = o;	
		this.r = r;	
		this.s = s;	
		this.v = false;
	}
	
	//Constructor por copia
	public Nodo(Nodo n){
		this.valor = n.getValor();
		this.pos = n.getPos();
		this.n = n.getN();
		this.m = n.getM();
		this.o = n.isO();
		this.r = n.isR();
		this.s = n.isS();
		this.v = n.isV();
	}
	
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public boolean isO() {
		return o;
	}
	public void setO(boolean o) {
		this.o = o;
	}
	public boolean isR() {
		return r;
	}
	public void setR(boolean r) {
		this.r = r;
	}
	public boolean isS() {
		return s;
	}
	public void setS(boolean s) {
		this.s = s;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public boolean isV() {
		return v;
	}
	public void setV(boolean v) {
		this.v = v;
	}
	
}
