package br.com.app.processador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.app.util.PlanilhaUtil;
import br.com.app.util.ResultadoProcessamento;
import br.com.app.util.ResultadoProcessamento.ErroImportacao;
import br.com.app.util.StringUtil;

public class ProcessadorPlanilha {

	private File planilha;
	private ConfiguracaoProcessamento cfg;
	private ProcessamentoListener listener;

	private static final Map<String, Transformador> transformadoresCache;

	static {
		transformadoresCache = new HashMap<>();
	}

	public ProcessadorPlanilha(File planilha, ConfiguracaoProcessamento cfg, ProcessamentoListener listener) {
		this.planilha = planilha;
		this.cfg = cfg;
		this.listener = listener;
	}

	public ResultadoProcessamento processar() {
		if (this.planilha == null) {
			throw new IllegalStateException("Arquivo a ser processado não foi informado.");
		}

		if (this.cfg == null) {
			throw new IllegalStateException("Configuração de processamento não foi informado.");
		}

		ResultadoProcessamento resultado = new ResultadoProcessamento();

		try (XSSFWorkbook origem = new XSSFWorkbook(new FileInputStream(this.planilha));
				XSSFWorkbook destino = new XSSFWorkbook()) {

			XSSFSheet abaOrigem = origem.getSheetAt(0);

			XSSFSheet abaDestino = destino.createSheet(abaOrigem.getSheetName());
			
			if (listener != null) {
				listener.iniciou(abaOrigem.getPhysicalNumberOfRows());
			}

			abaOrigem.rowIterator().forEachRemaining(linha -> {
				processarLinha(resultado, linha, abaDestino);
				
				if (listener != null) {
					listener.leuLinha(linha.getRowNum() + 1);
				}
			});

			if (resultado.erros.isEmpty()) {
				if (abaDestino.getPhysicalNumberOfRows() > 0) {
					Row linha = abaDestino.getRow(0);
					
					Iterator<Cell> celulaIterator = linha.cellIterator();
					while (celulaIterator.hasNext()) {
						Cell celula = celulaIterator.next();
						abaDestino.autoSizeColumn(celula.getColumnIndex());
					}
				}

				String nomeDestino = planilha.getName().replaceAll(".xlsx", "") + "Processada" + ".xlsx";
				resultado.planilhaProcessada = new File(planilha.getParent(), nomeDestino);

				destino.write(new FileOutputStream(resultado.planilhaProcessada));
			}
		} catch (Exception e) {
			resultado.erros.add(new ErroImportacao("geral", "Falha ao processar planilha.\n" + e.getMessage()));
		}

		return resultado;
	}

	public void processarLinha(ResultadoProcessamento resultado, Row linha, XSSFSheet abaDestino) {
		Row linhaDestino = abaDestino.createRow(linha.getRowNum());

		try {
			cfg.colunas.forEach(cfgColuna -> {
				int colIndex = CellReference.convertColStringToIndex(cfgColuna.nome);

				Cell celulaOrigem = linha.getCell(colIndex);
				Cell celulaDestino = linhaDestino.createCell(cfg.colunas.indexOf(cfgColuna),
						celulaOrigem.getCellTypeEnum());

				if (cfg.temCabecalho && linha.getRowNum() == 0) {
					if (StringUtil.isNotEmpty(cfgColuna.descricao)) {
						celulaDestino.setCellValue(cfgColuna.descricao);
					} else {
						celulaDestino.setCellValue(celulaOrigem.getStringCellValue());
					}
				} else {
					try {
						Object valor = processarCelula(celulaOrigem, cfgColuna.processadores);

						PlanilhaUtil.setValorCelula(celulaDestino, valor);
					} catch (Exception e) {
						PlanilhaUtil.tratarErroCelula("origem", resultado, celulaOrigem, e);
					}
				}
			});
		} catch (Exception e) {
			PlanilhaUtil.tratarErroLinha("origem", resultado, linhaDestino, e);
		}
	}

	private Object processarCelula(Cell celulaOrigem, List<String> processadores) {
		Object valor = PlanilhaUtil.getValorCelula(celulaOrigem);

		if (valor != null && processadores != null) {
			for (Iterator<String> ite = processadores.iterator(); ite.hasNext();) {
				Transformador transformador = getTransformador(ite.next());

				valor = transformador.transformar(valor);
			}
		}

		return valor;
	}

	public static Transformador getTransformador(String nome) {
		Transformador transformador = transformadoresCache.get(nome);

		if (transformador == null) {
			try {
				transformador = (Transformador) Class.forName("br.com.app.processador.transformadores." + nome)
						.newInstance();
				transformadoresCache.put(nome, transformador);
			} catch (Exception e) {
				throw new IllegalStateException(
						"Não foi possível carregar o transformador '" + nome + "', motivo: " + e.getMessage(), e);
			}
		}

		return transformador;
	}
	
	public interface ProcessamentoListener {
		public void iniciou(int quantidadeLinhas);
		public void leuLinha(int linha);
	}
}
