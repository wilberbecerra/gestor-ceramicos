package utils;

import static ui.main.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class ConfigurarDescuentos extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtMas15;
	private JTextField txtDe11a15;
	private JTextField txtDe6a10;
	private JTextField txtDe1a5;
	private JButton btnAceptar;
	private JButton btnCancel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConfigurarDescuentos dialog = new ConfigurarDescuentos();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigurarDescuentos() {
		setTitle("Configurar porcentajes de descuento");
		setBounds(100, 100, 458, 218);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("1 a 5 unidades");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(45, 43, 110, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("6 a 10 unidades");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(45, 70, 110, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("11 a 15 unidades");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(45, 97, 110, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("M\u00E1s de 15 unidades");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(45, 124, 136, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("%");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(257, 43, 110, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("%");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(257, 70, 110, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("%");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(257, 97, 110, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("%");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel.setBounds(257, 124, 110, 13);
			contentPanel.add(lblNewLabel);
		}
		{
			txtMas15 = new JTextField();
			txtMas15.setBounds(172, 123, 79, 19);
			contentPanel.add(txtMas15);
			txtMas15.setColumns(10);
		}
		{
			txtDe11a15 = new JTextField();
			txtDe11a15.setBounds(172, 93, 77, 19);
			contentPanel.add(txtDe11a15);
			txtDe11a15.setColumns(10);
		}
		{
			txtDe6a10 = new JTextField();
			txtDe6a10.setBounds(172, 69, 77, 19);
			contentPanel.add(txtDe6a10);
			txtDe6a10.setColumns(10);
		}
		
		txtDe1a5 = new JTextField();
		txtDe1a5.setBounds(171, 42, 79, 19);
		contentPanel.add(txtDe1a5);
		txtDe1a5.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(this);
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAceptar.setBounds(316, 41, 85, 21);
		contentPanel.add(btnAceptar);
		
		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this);
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(316, 68, 85, 21);
		contentPanel.add(btnCancel);
		
		mostrarDescuentos();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnCancel) {
			cancelar();
		}
		if (e.getSource() == btnAceptar) {
			configurarDescuentos();
		}

	}
	
	private void cancelar() {
		this.dispose();
	}
	private void mostrarDescuentos() {
		
		txtDe1a5.setText(Double.toString(porcentaje1));
		txtDe6a10.setText(Double.toString(porcentaje2));
		txtDe11a15.setText(Double.toString(porcentaje3));
		txtMas15.setText(Double.toString(porcentaje4));	
	}
	
	private void configurarDescuentos() {
		double n1= Double.parseDouble(txtDe1a5.getText());
		double n2= Double.parseDouble(txtDe6a10.getText());
		double n3= Double.parseDouble(txtDe11a15.getText());
		double n4= Double.parseDouble(txtMas15.getText());
		
		porcentaje1=n1;
		porcentaje2=n2;
		porcentaje3=n3;
		porcentaje4=n4;
		
		cancelar();
	}
}
