package br.com.app.view;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.google.gson.GsonBuilder;

import br.com.app.processador.ConfiguracaoProcessamento;
import br.com.app.processador.ProcessadorPlanilha;
import br.com.app.processador.ProcessadorPlanilha.ProcessamentoListener;
import br.com.app.util.ResultadoProcessamento;
import br.com.app.view.componentes.PlanilhaErroDialog;
import br.com.app.view.componentes.SeletorArquivo;

public class AppProcessaPlanilhaView extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	JProgressBar progresso;
	JButton btnProcessar;

	public AppProcessaPlanilhaView() {
		GridLayout gridLayout = new GridLayout(2, 1);
		setLayout(gridLayout);

		progresso = new JProgressBar();
		progresso.setMinimum(0);
		progresso.setStringPainted(true);
		add(progresso);

		btnProcessar = new JButton("Processar");
		btnProcessar.addActionListener(this);
		add(btnProcessar);
	}

	public void actionPerformed(ActionEvent evt) {
		File planilha = SeletorArquivo.openChooser("planilha", "xlsx");

		if (planilha == null) {
			JOptionPane.showMessageDialog(this, "Informe a planilha.", "Campos obrigatórios",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		ConfiguracaoProcessamento cfg;

		try {
			File cfgFile = new File(getClass().getClassLoader().getResource("configPlanilha.json").getFile());
			cfg = new GsonBuilder().create().fromJson(new FileReader(cfgFile), ConfiguracaoProcessamento.class);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Falha ao carregar configuração",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		progresso.setValue(0);
		progresso.setMaximum(0);

		ProcessarWorker worker = new ProcessarWorker(planilha, cfg);
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progresso".equals(evt.getPropertyName())) {
					progresso.setValue((Integer) evt.getNewValue());
				} else if ("quantidadeLinhas".equals(evt.getPropertyName())) {
					progresso.setMaximum((Integer) evt.getNewValue());
				}
			}
		});
		worker.execute();
	}

	private class ProcessarWorker extends SwingWorker<Boolean, Boolean> {

		File planilha;
		ConfiguracaoProcessamento cfg;

		public ProcessarWorker(File planilha, ConfiguracaoProcessamento cfg) {
			this.planilha = planilha;
			this.cfg = cfg;
		}

		@Override
		protected Boolean doInBackground() throws Exception {
			try {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				ProcessadorPlanilha processador = new ProcessadorPlanilha(planilha, cfg, new ProcessamentoListener() {
					public void leuLinha(int linha) {
						firePropertyChange("progresso", 0, linha);
					}

					@Override
					public void iniciou(int quantidadeLinhas) {
						firePropertyChange("quantidadeLinhas", 0, quantidadeLinhas);
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
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "Falha ao processar planilha",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			return true;
		}
	}
}
