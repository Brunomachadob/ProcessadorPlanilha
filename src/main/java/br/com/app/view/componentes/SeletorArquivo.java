package br.com.app.view.componentes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SeletorArquivo extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final String NAO_ESCOLHIDO = "Não selecionada";

	private JLabel planilhaLabel;
	private File arquivoSelecionado;

	public SeletorArquivo(String nome, String formato) {
		setLayout(new FlowLayout());

		JButton btnArquivo = new JButton("Selecionar " + nome);
		add(btnArquivo);

		planilhaLabel = new JLabel(NAO_ESCOLHIDO);
		add(planilhaLabel);

		btnArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				arquivoSelecionado = openChooser(nome, formato);
				planilhaLabel.setText(arquivoSelecionado != null ? arquivoSelecionado.getName() : NAO_ESCOLHIDO);
			}
		});
	}
	
	public File getArquivoSelecionado() {
		return arquivoSelecionado;
	}
	
	public static File openChooser(String nome, String formato) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Selecione uma " + nome);
		fileChooser.setFileFilter(new FileNameExtensionFilter(nome, formato));
		fileChooser.setAcceptAllFileFilterUsed(false);

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		
		return null;
	}
}