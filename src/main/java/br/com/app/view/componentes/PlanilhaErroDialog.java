package br.com.app.view.componentes;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import br.com.app.util.ResultadoProcessamento.ErroImportacao;

public class PlanilhaErroDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public PlanilhaErroDialog(List<ErroImportacao> erros) {
		super((JFrame) null, "Erros no processamento", true);

		setSize(600, 300);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JTable table = new JTable(new TableModel(erros));

		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private class TableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		List<ErroImportacao> erros;
		
		public TableModel(List<ErroImportacao> erros) {
			this.erros = erros;
		}

		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int col) {
			if (col == 0) {
				return "Identificador";
			} else if (col == 1) {
				return "Mensagem";
			} else {
				return super.getColumnName(col);
			}
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}

		public int getRowCount() {
			return erros.size();
		}

		public Object getValueAt(int row, int col) {
			ErroImportacao erro = erros.get(row);
			return col == 0 ? erro.identificador : erro.mensagem;
		}
	}
}
