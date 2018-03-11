package br.com.app.util;

import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import br.com.app.util.ResultadoProcessamento.ErroImportacao;

public class PlanilhaUtil {
	public static Object getValorCelula(Cell celula) {
		switch (celula.getCellTypeEnum()) {
		case BLANK:
			return null;

		case BOOLEAN:
			return celula.getBooleanCellValue();

		case NUMERIC:
			return celula.getNumericCellValue();

		case STRING:
			return celula.getStringCellValue();

		default:
			return null;
		}
	}

	public static void setValorCelula(Cell celula, Object valor) {
		if (valor == null) {
			return;
		}

		if (String.class.isInstance(valor)) {
			celula.setCellValue((String) valor);
		} else if (Double.class.isInstance(valor)) {
			celula.setCellValue((Double) valor);
		} else if (Integer.class.isInstance(valor)) {
			celula.setCellValue((Integer) valor);
		} else if (Date.class.isInstance(valor)) {
			celula.setCellValue((Date) valor);
		} else if (Boolean.class.isInstance(valor)) {
			celula.setCellValue((Boolean) valor);
		}
	}
	
	public static void preencherPlanilha(Sheet abaDestino, ArrayList<LinhaPlanilha> listaPlanilha, PreenchimentoListener listener) {
		int rowIndex = 0;
		
		for (LinhaPlanilha linha : listaPlanilha) {
			Row linhaPlanilha = abaDestino.createRow(rowIndex);
			
			int cellIndex = 0;
			
			for (Object valor : linha.getValores()) {
				setValorCelula(linhaPlanilha.createCell(cellIndex++), valor);
			}
			
			if (listener != null) {
				listener.criouLinha(rowIndex);
			}
			
			rowIndex++;
		}
	}
	
	public static String getIdentificadorCelula(Cell celula) {
		return new CellAddress(celula).formatAsString();
	}
	
	public static void tratarErroCelula(String nomePlanilha, ResultadoProcessamento resultado, Cell celula, Throwable erro) {
		resultado.erros.add(new ErroImportacao("Planilha " + nomePlanilha + " | Célula: " + PlanilhaUtil.getIdentificadorCelula(celula),
				ExcecaoUtil.tratarMensagemErro(erro)));
	}

	public static void tratarErroLinha(String nomePlanilha, ResultadoProcessamento resultado, Row linha, Throwable erro) {
		resultado.erros.add(new ErroImportacao("Planilha: " + nomePlanilha + " | Linha: " + (linha.getRowNum() + 1),
				ExcecaoUtil.tratarMensagemErro(erro)));
	}
	
	public interface PreenchimentoListener {
		public void criouLinha(int linha);
	}
}
