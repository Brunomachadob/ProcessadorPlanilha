package br.com.app.view;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.google.gson.GsonBuilder;

import br.com.app.interseccao.ConfiguracaoInterseccao;
import br.com.app.interseccao.InterseccaoPlanilhas;
import br.com.app.interseccao.InterseccaoPlanilhas.InterseccaoListener;
import br.com.app.util.ResultadoProcessamento;
import br.com.app.view.componentes.PlanilhaErroDialog;
import br.com.app.view.componentes.SeletorArquivo;

public class AppInterseccaoPlanilhaView extends JPanel implements ActionListener {
	private static final String QUANTIDADE_LINHAS = "quantidadeLinhas";

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(AppInterseccaoPlanilhaView.class.getSimpleName());

	SeletorArquivo seletorCfg = new SeletorArquivo("configuração", "json");
	SeletorArquivo seletorPlanilha1 = new SeletorArquivo("planilha antiga", "xlsx");
	SeletorArquivo seletorPlanilha2 = new SeletorArquivo("planilha nova", "xlsx");

	JProgressBar progresso;
	JButton btnProcessar;

	public AppInterseccaoPlanilhaView() {
		GridLayout gridLayout = new GridLayout(5, 1);
		setLayout(gridLayout);
		
		add(seletorCfg);
		add(seletorPlanilha1);
		add(seletorPlanilha2);

		progresso = new JProgressBar();
		progresso.setMinimum(0);
		progresso.setStringPainted(true);
		add(progresso);

		btnProcessar = new JButton("Processar");
		btnProcessar.addActionListener(this);
		add(btnProcessar);
	}

	public void actionPerformed(ActionEvent evt) {
		File arquivoCfg = seletorCfg.getArquivoSelecionado();
		
		if (arquivoCfg == null) {
			JOptionPane.showMessageDialog(this, "Informe a configuração.", "Campos obrigatórios",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		File planilha1 = seletorPlanilha1.getArquivoSelecionado();
		File planilha2 = seletorPlanilha2.getArquivoSelecionado();

		if (planilha1 == null || planilha2 == null) {
			JOptionPane.showMessageDialog(this, "Informe as planilhas.", "Campos obrigatórios",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		ConfiguracaoInterseccao cfg;

		try {
			cfg = new GsonBuilder().create().fromJson(new FileReader(arquivoCfg), ConfiguracaoInterseccao.class);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Falha ao carregar configuração",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		progresso.setValue(0);
		progresso.setMaximum(0);

		ProcessarWorker worker = new ProcessarWorker(planilha1, planilha2, cfg);
		worker.addPropertyChangeListener(event -> {
			if ("progresso".equals(event.getPropertyName())) {
				progresso.setValue((Integer) event.getNewValue());
			} else if (QUANTIDADE_LINHAS.equals(event.getPropertyName())) {
				progresso.setValue(0);
				progresso.setMaximum((Integer) event.getNewValue());
			}
		});
		worker.execute();
	}

	private class ProcessarWorker extends SwingWorker<Boolean, Boolean> {

		File planilha1;
		File planilha2;
		ConfiguracaoInterseccao cfg;

		public ProcessarWorker(File planilha1, File planilha2, ConfiguracaoInterseccao cfg) {
			this.planilha1 = planilha1;
			this.planilha2 = planilha2;
			this.cfg = cfg;
		}

		@Override
		protected Boolean doInBackground() throws Exception {
			try {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InterseccaoPlanilhas processador = new InterseccaoPlanilhas(planilha1, planilha2, cfg, new InterseccaoListener() {
					@Override
					public void leuLinha(int linha) {
						firePropertyChange("progresso", 0, linha);
					}
					
					@Override
					public void iniciouLeitura(int quantidadeLinhas) {
						firePropertyChange(QUANTIDADE_LINHAS, 0, quantidadeLinhas);
					}
					
					@Override
					public void iniciouEscrita(int quantidadeLinhas) {
						firePropertyChange(QUANTIDADE_LINHAS, 0, quantidadeLinhas);
					}
				});

				ResultadoProcessamento resultado = processador.processar();

				if (resultado.erros.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Planilha processada com sucesso.", "Processamento de planilha",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					PlanilhaErroDialog erroDialog = new PlanilhaErroDialog(resultado.erros);
					erroDialog.setLocationRelativeTo(null);
					erroDialog.setVisible(true);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Falha ao processar planilha", e);
				JOptionPane.showMessageDialog(null, e.getMessage(), "Falha ao processar planilha",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			return true;
		}
	}
}
