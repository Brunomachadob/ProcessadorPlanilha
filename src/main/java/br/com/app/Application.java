package br.com.app;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import br.com.app.view.AppInterseccaoPlanilhaView;
import br.com.app.view.AppProcessaPlanilhaView;
import br.com.app.view.popup.PopupCfgProcessador;

@ApplicationScoped
public class Application implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

	@Override
	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			LOGGER.log(Level.INFO, e1.getMessage(), e1);
		}

		MainAppFrame app = new MainAppFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(450, 300);
		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}

	private static class MainAppFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		public MainAppFrame() {
			super("Processamento de planilhas");
			
			setJMenuBar(new ApplicationMenu(this));

			JTabbedPane abas = new JTabbedPane();

			abas.addTab("Processar planilha", null, new AppProcessaPlanilhaView());
			abas.addTab("Intersecção planilhas", null, new AppInterseccaoPlanilhaView());

			add(abas);
		}
	}

	private static class ApplicationMenu extends JMenuBar {
		private static final long serialVersionUID = 1L;
		
		private JFrame frame;
		
		public ApplicationMenu(JFrame frame) {
			this.frame = frame;
			
			buildMenuArquivo();
			buildMenuConfiguracoes();
		}
		
		private void buildMenuArquivo() {
			JMenu menu = new JMenu("Arquivo");
			menu.setMnemonic(KeyEvent.VK_A);
			add(menu);
			
			
			JMenuItem ItemSair = new JMenuItem("Sair", KeyEvent.VK_S);
			ItemSair.addActionListener((e) -> {
				System.exit(0);
			});
			menu.add(ItemSair);
		}
		
		private void buildMenuConfiguracoes() {
			JMenu menu = new JMenu("Configurações");
			menu.setMnemonic(KeyEvent.VK_C);
			add(menu);
			
			JMenuItem ItemCfgProcessador = new JMenuItem("Processador", KeyEvent.VK_P);
			ItemCfgProcessador.addActionListener((e) -> {
				PopupCfgProcessador.open(frame);
			});
			menu.add(ItemCfgProcessador);
			
			JMenuItem ItemCfgInterseccao = new JMenuItem("Intersecção", KeyEvent.VK_I);
			ItemCfgInterseccao.addActionListener((e) -> {
			});
			menu.add(ItemCfgInterseccao);
		}
	}
}
