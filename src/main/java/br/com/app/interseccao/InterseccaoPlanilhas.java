package br.com.app.interseccao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.app.util.LinhaPlanilha;
import br.com.app.util.PlanilhaUtil;
import br.com.app.util.ResultadoProcessamento;
import br.com.app.util.ResultadoProcessamento.ErroImportacao;

public class InterseccaoPlanilhas {

	private File planilhaAntiga;
	private File planilhaNova;

	private ConfiguracaoInterseccao cfg;
	private InterseccaoListener listener;

	public InterseccaoPlanilhas(File planilhaAntiga, File planilhaNova, ConfiguracaoInterseccao cfg,
			InterseccaoListener listener) {
		this.planilhaAntiga = planilhaAntiga;
		this.planilhaNova = planilhaNova;
		this.cfg = cfg;
		this.listener = listener;
	}

	public ResultadoProcessamento processar() {
		ResultadoProcessamento resultado = new ResultadoProcessamento();

		try (XSSFWorkbook wbPlanilhaAntiga = new XSSFWorkbook(new FileInputStream(this.planilhaAntiga));
				XSSFWorkbook wbPlanilhaNova = new XSSFWorkbook(new FileInputStream(this.planilhaNova));
				XSSFWorkbook wbDestino = new XSSFWorkbook()) {

			XSSFSheet abaPlanilaAntiga = wbPlanilhaAntiga.getSheetAt(0);
			XSSFSheet abaPlanilaNova = wbPlanilhaNova.getSheetAt(0);

			XSSFSheet abaDestino = wbDestino.createSheet(abaPlanilaAntiga.getSheetName());

			if (listener != null) {
				listener.iniciouLeitura(
						abaPlanilaAntiga.getPhysicalNumberOfRows() + abaPlanilaNova.getPhysicalNumberOfRows());
			}

			List<Integer> colunasChave = cfg.colunas.stream().mapToInt(CellReference::convertColStringToIndex).boxed()
					.collect(Collectors.toList());

			ArrayList<LinhaPlanilha> listaPlanilhaAntiga = processarAbaPlanilha("antiga", colunasChave, resultado,
					abaPlanilaAntiga);
			ArrayList<LinhaPlanilha> listaPlanilhaNova = processarAbaPlanilha("nova", colunasChave, resultado,
					abaPlanilaNova);

			if (resultado.erros.isEmpty()) {
				removerDuplicados(listaPlanilhaAntiga, listaPlanilhaNova);

				if (listener != null) {
					listener.iniciouEscrita(listaPlanilhaNova.size());
				}

				PlanilhaUtil.preencherPlanilha(abaDestino, listaPlanilhaNova, linha -> {
					if (listener != null) {
						listener.leuLinha(linha + 1);
					}
				});

				if (abaDestino.getPhysicalNumberOfRows() > 0) {
					Row linha = abaDestino.getRow(0);

					Iterator<Cell> celulaIterator = linha.cellIterator();
					while (celulaIterator.hasNext()) {
						Cell celula = celulaIterator.next();
						abaDestino.autoSizeColumn(celula.getColumnIndex());
					}
				}

				String nomeDestino = planilhaNova.getName().replaceAll(".xlsx", "") + "Intersecção" + ".xlsx";
				resultado.planilhaProcessada = new File(planilhaAntiga.getParent(), nomeDestino);

				wbDestino.write(new FileOutputStream(resultado.planilhaProcessada));
			}
		} catch (Exception e) {
			resultado.erros.add(new ErroImportacao("geral", "Falha ao processar planilha.\n" + e.getMessage()));
		}

		return resultado;
	}

	private void removerDuplicados(ArrayList<LinhaPlanilha> listaPlanilhaAntiga,
			ArrayList<LinhaPlanilha> listaPlanilhaNova) {
		for (LinhaPlanilha linhaAntiga : listaPlanilhaAntiga) {
			if (!(linhaAntiga.getIndice() == 0 && cfg.temCabecalho)) {
				listaPlanilhaNova.remove(linhaAntiga);
			}
		}
	}

	private ArrayList<LinhaPlanilha> processarAbaPlanilha(String nomePlanilha, List<Integer> colunasChave,
			ResultadoProcessamento resultado, XSSFSheet abaPlanila1) {
		ArrayList<LinhaPlanilha> listaPlanilha = new ArrayList<>();

		abaPlanila1.rowIterator().forEachRemaining(linha -> {
			if (!PlanilhaUtil.linhaEhVazia(linha)) {
				listaPlanilha.add(processarLinha(nomePlanilha, colunasChave, resultado, linha));

				if (listener != null) {
					listener.leuLinha(linha.getRowNum() + 1);
				}
			}
		});

		return listaPlanilha;
	}

	public LinhaPlanilha processarLinha(String nomePlanilha, List<Integer> colunasChave,
			ResultadoProcessamento resultado, Row linha) {
		ArrayList<Object> linhaProcessada = new ArrayList<>();

		try {
			for (int colIndex = 0; colIndex < linha.getLastCellNum(); colIndex++) {
				Cell celulaOrigem = linha.getCell(colIndex);
				linhaProcessada.add(PlanilhaUtil.getValorCelula(celulaOrigem));
			}
		} catch (Exception e) {
			PlanilhaUtil.tratarErroLinha(nomePlanilha, resultado, linha, e);
		}

		return new LinhaPlanilhaInterseccao(linha.getRowNum(), colunasChave, linhaProcessada);
	}

	public interface InterseccaoListener {
		public void iniciouLeitura(int quantidadeLinhas);

		public void iniciouEscrita(int quantidadeLinhas);

		public void leuLinha(int linha);
	}
}
