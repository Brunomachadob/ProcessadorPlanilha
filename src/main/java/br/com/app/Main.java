package br.com.app;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.com.app.view.AppInterseccaoPlanilhaView;
import br.com.app.view.AppProcessaPlanilhaView;

public class Main {
	
	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainApp app = new MainApp();
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				app.setSize(450, 300);
				app.setLocationRelativeTo(null);
				app.setVisible(true);
			}
		});

	}
	
	private static class MainApp extends JFrame {
		private static final long serialVersionUID = 1L;

		public MainApp() {
			super("Processamento de planilhas");
			
			JTabbedPane abas = new JTabbedPane();
			
			abas.addTab("Processar planilha", null, new AppProcessaPlanilhaView());
			abas.addTab("Intersecção planilhas", null, new AppInterseccaoPlanilhaView());
			
			add(abas);
		}
	}
}
