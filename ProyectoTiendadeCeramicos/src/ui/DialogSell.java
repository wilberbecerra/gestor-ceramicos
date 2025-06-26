package ui;

import static ui.main.*;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.text.DecimalFormat;

public class DialogSell extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JButton btnClose;
	private JButton btnSell;
	private JComboBox cboModel;
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	private JTextField txtPrice;
	private JTextField txtQuantity;
	private JTextArea txtResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogSell dialog = new DialogSell();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DialogSell() {
		setBounds(100, 100, 480, 380);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		// Model
		JLabel lblModel = new JLabel("Modelo");
		lblModel.setBounds(10, 10, 90, 20);
		contentPanel.add(lblModel);
		
		cboModel = new JComboBox();
		cboModel.addItem(modelo0);
		cboModel.addItem(modelo1);
		cboModel.addItem(modelo2);
		cboModel.addItem(modelo3);
		cboModel.addItem(modelo4);
		cboModel.addActionListener(this);
		cboModel.setBounds(100, 10, 175, 20);
		contentPanel.add(cboModel);
		
		// Price
		JLabel lblPrice = new JLabel("Precio (S/.)");
		lblPrice.setBounds(10, 40, 90, 20);
		contentPanel.add(lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setBounds(100, 40, 175, 20);
		txtPrice.setEditable(false);
		contentPanel.add(txtPrice);
		txtPrice.setColumns(10);
		
		// Quantity sold
		JLabel lblQuantity = new JLabel("Cantidad");
		lblQuantity.setBounds(10, 70, 90, 20);
		contentPanel.add(lblQuantity);
		
		txtQuantity = new JTextField();
		txtQuantity.setBounds(100, 70, 175, 20);
		contentPanel.add(txtQuantity);
		txtQuantity.setColumns(10);
		
		txtResult = new JTextArea();
		txtResult.setBounds(0, 0, 465, 310);
		
		JScrollPane scrollPane = new JScrollPane(txtResult, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 100, 445, 235);
		contentPanel.add(scrollPane);
		
		// Buttons
		btnSell = new JButton("Vender");
		btnSell.addActionListener(this);
		btnSell.setBounds(380, 10, 75, 20);
		contentPanel.add(btnSell);
		
		btnClose = new JButton("Cerrar");
		btnClose.addActionListener(this);
		btnClose.setBounds(380, 40, 75, 20);
		contentPanel.add(btnClose);
		btnClose.setActionCommand("Cancel");
		
		getData(0);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cboModel) {
			int idx = cboModel.getSelectedIndex();
			getData(idx);
		}
		else if (e.getSource() == btnClose) {
			closeDialog();
		}
		else if (e.getSource() == btnSell) {
			int idx = cboModel.getSelectedIndex();
			sellProduct(idx);
		}
	}
	protected void closeDialog() {
		this.dispose();
	}
	
	protected void getData(int modelIndex) {
		switch (modelIndex) {
			case 0:
				txtPrice.setText(df.format(precio0));
				break;
			case 1:
				txtPrice.setText(df.format(precio1));
				break;
			case 2:
				txtPrice.setText(df.format(precio2));
				break;
			case 3:
				txtPrice.setText(df.format(precio3));
				break;
			case 4:
				txtPrice.setText(df.format(precio4));
				break;
			default:
				break;
		}
	}
	
	protected void sellProduct(int modelIndex) {
		double unitPrice = 0.0;
		int quantity = 0;
		
		try {
			unitPrice = Double.parseDouble(txtPrice.getText());
			quantity = Integer.parseInt(txtQuantity.getText());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error en el numero de productos a vender.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (quantity < 1) {
			JOptionPane.showMessageDialog(this, "El minimo de unidades a vender debe ser 1.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		double basePrice = unitPrice * quantity;
		double discount = 0.0;
		int gifts = 0;
		
		if (quantity <= 5) {
			discount = porcentaje1;
			gifts = obsequioCantidad1 * quantity;
		}
		else if (quantity <= 10) {
			discount = porcentaje2;
			gifts = obsequioCantidad2 * quantity;
		}
		else if (quantity <= 15) {
			discount = porcentaje3;
			gifts = obsequioCantidad3 * quantity;
		}
		else {
			discount = porcentaje4;
			gifts = obsequioCantidad3 * quantity;
		}
		discount *= basePrice / 100; // /100 to convert from number to actual percentage
		double finalPrice = basePrice - discount;
		
		// Display sale result
		txtResult.setText("BOLETA DE VENTA\n\n");
		txtResult.append("Modelo\t\t\t: " + cboModel.getSelectedItem().toString() + "\n");
		txtResult.append("Precio\t\t\t: S/. " + df.format(unitPrice) + "\n");
		txtResult.append("Cantidad adquirida\t: " + quantity + "\n");
		txtResult.append("Importe compra\t\t: S/. " + df.format(basePrice) + "\n");
		txtResult.append("Importe descuento\t: S/. " + df.format(discount) + "\n");
		txtResult.append("Importe pagar\t\t: S/. " + df.format(finalPrice) + "\n");
		txtResult.append("Tipo de obsequio\t: " + tipoObsequio + "\n");
		txtResult.append("Unidades obsequiadas\t: " + gifts);
		
		// Update counters
		switch (modelIndex) {
			case 0:
				totalNumVentas0 += 1;
				totalProdVend0 += quantity;
				totalMontVend0 += finalPrice;
				break;
			case 1:
				totalNumVentas1 += 1;
				totalProdVend1 += quantity;
				totalMontVend1 += finalPrice;
				break;
			case 2:
				totalNumVentas2 += 1;
				totalProdVend2 += quantity;
				totalMontVend2 += finalPrice;
				break;
			case 3:
				totalNumVentas3 += 1;
				totalProdVend3 += quantity;
				totalMontVend3 += finalPrice;
				break;
			case 4:
				totalNumVentas4 += 1;
				totalProdVend4 += quantity;
				totalMontVend4 += finalPrice;
				break;
			default:
				break;
		}
		
		// Info message shown every 5 sales
		int totalSales = totalNumVentas0 + totalNumVentas1 + totalNumVentas2 + totalNumVentas3 + totalNumVentas4;
		if (totalSales % 5 == 0) {
			double totalAmountSold = totalMontVend0 + totalMontVend1 + totalMontVend2 + totalMontVend3 + totalMontVend4;
					
			JOptionPane.showMessageDialog(this, 
				"Venta Nro. " + totalSales + "\n" +
				"Importe total general acumulado: S/. " + df.format(totalAmountSold) + "\n" + 
				"Porcentaje de la cuota diaria: " + df.format(totalAmountSold / cuotaDiaria * 100) + "%", "Avance de ventas",
				JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
