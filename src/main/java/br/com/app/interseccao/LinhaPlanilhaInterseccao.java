package br.com.app.interseccao;

import java.util.ArrayList;
import java.util.List;

import br.com.app.util.LinhaPlanilha;

public class LinhaPlanilhaInterseccao extends LinhaPlanilha {
	private final List<Integer> indicesChave;

	public LinhaPlanilhaInterseccao(int indice, List<Integer> indicesChave, List<Object> valores) {
		super(indice, valores);
		this.indicesChave = indicesChave;
	}

	@Override
	public int hashCode() {
		if (hash == -1) {
			List<Object> chaves = new ArrayList<>();
			indicesChave.forEach(indice -> {
				if (indice > valores.size()) {
					throw new IllegalStateException("Linha com menos colunas do que exigido pela configuração.");
				}
				
				chaves.add(valores.get(indice));
			});
			
			hash = chaves.hashCode();
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

		return this.hashCode() == ((LinhaPlanilhaInterseccao) obj).hashCode();
	}
}
