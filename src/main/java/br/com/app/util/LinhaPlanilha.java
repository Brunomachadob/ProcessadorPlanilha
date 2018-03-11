package br.com.app.util;

import java.util.ArrayList;

public class LinhaPlanilha {
	private int hash = -1;
	private int indice;
	
	private ArrayList<Object> valores;

	public LinhaPlanilha(int indice, ArrayList<Object> valores) {
		this.indice = indice;
		this.valores = valores;
	}
	
	public int getIndice() {
		return indice;
	}
	
	public ArrayList<Object> getValores() {
		return valores;
	}

	@Override
	public int hashCode() {
		if (hash == -1) {
			hash = valores.hashCode();
		}

		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;

		return this.hashCode() == ((LinhaPlanilha) obj).hashCode();
	}
}
