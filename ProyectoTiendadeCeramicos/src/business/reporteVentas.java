package business;

import java.awt.Dialog.ModalityType;

import static ui.main.*;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class reporteVentas extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JComboBox<?> comboBox;
	private JButton btnNewButton;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					reporteVentas frame = new reporteVentas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	String[] mStrings = {"Ventas por modelo", "Comparación de precios con el precio promedio", "Comparación de cajas vendidas con la cantidad óptima", "Estadística sobre el precio"};
	private JScrollPane scrollPane;
	private JTextArea reporte;
	public reporteVentas() {
		setTitle("Generar reportes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		{
			
			comboBox = new JComboBox<Object>();
			comboBox.setModel(new DefaultComboBoxModel(mStrings));
			comboBox.setBounds(61, 27, 539, 35);
			contentPane.add(comboBox);
		}
		{
			btnNewButton = new JButton("Borrar");
			btnNewButton.addActionListener(this);
			btnNewButton.setBounds(644, 27, 153, 35);
			contentPane.add(btnNewButton);
		}
		
		comboBox.setSelectedIndex(0);
		{
			scrollPane = new JScrollPane();
			scrollPane.setBounds(61, 104, 734, 333);
			contentPane.add(scrollPane);
			{
				reporte = new JTextArea();
				scrollPane.setViewportView(reporte);
			}
		}
		comboBox.addActionListener(this);
		
		
	}
		
public void actionPerformed(ActionEvent e) {
	
	if (e.getSource() == btnNewButton) {
		actionPerformedBorrar(e);
	}
	
	
	if (e.getSource()== comboBox) {
		
		try {
			seleccion(e);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
		
	}
	private void seleccion(ActionEvent e) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
	// TODO Auto-generated method stub
		JComboBox<?> cb = (JComboBox<?>)e.getSource();
		int msg = cb.getSelectedIndex();
		
		switch(msg) 
		
		{
		
		case 0: 
			ventasModelo();
			break;
		
		case 1: 
			comparacion_precio();
			
			break;
		
		case 2: 
			comparacion_cajas();
			
			break;
		
		default: 
			estadistica_precio();
			
			break;
		
		}
}

	private void actionPerformedBorrar(ActionEvent e) {
		reporte.setText("Seleccione un tipo de reporte.");	
	}



	private void ventasModelo() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String i;
		int v;
		int c;
		double m;
		Double aporte = 0.0, nOpt = cuotaDiaria;

		reporte.setText("CANTIDAD VENDIDA VS CANTIDAD ÓPTIMA"+"\n"+"\n");
		
		i = modelo0;
		v = totalNumVentas0;
		c = totalProdVend0;
		m = totalMontVend0;
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Cantidad de ventas					: "+ v + "\n");
		reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
		aporte = m  / (nOpt * 100);
		reporte.append("Aporte a la cuota diaria				: "+ decimalFormat.format(aporte) + "%" + "\n" + "\n");
		
		i = modelo1;
		v = totalNumVentas1;
		c = totalProdVend1;
		m = totalMontVend1;
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Cantidad de ventas					: "+ v + "\n");
		reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
		aporte = m  / (nOpt * 100);
		reporte.append("Aporte a la cuota diaria				: "+ decimalFormat.format(aporte) + "%" + "\n" + "\n");
		
		i = modelo2;
		v = totalNumVentas2;
		c = totalProdVend2;
		m = totalMontVend2;
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Cantidad de ventas					: "+ v + "\n");
		reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
		aporte = m  / (nOpt * 100);
		reporte.append("Aporte a la cuota diaria				: "+ decimalFormat.format(aporte) + "%" + "\n" + "\n");
		
		i = modelo3;
		v = totalNumVentas3;
		c = totalProdVend3;
		m = totalMontVend3;
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Cantidad de ventas					: "+ v + "\n");
		reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
		aporte = m  / (nOpt * 100);
		reporte.append("Aporte a la cuota diaria				: "+ decimalFormat.format(aporte) + "%" + "\n" + "\n");
		}

	
	private void comparacion_precio() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		reporte.setText("PRECIOS VS PRECIO PROMEDIO"+"\n"+"\n");

		String i;
		double p = 0.0, prmPrecios=0.0, sumPrecios = 0.0;;
		sumPrecios = precio0 + precio1 + precio2 + precio3 + precio4;
        prmPrecios = sumPrecios/5;
		
        i = modelo0;
		p = precio0;
		
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Precio							: "+ "S/." + decimalFormat.format(p) + "\n");	
		reporte.append("Precio promedio						: "+  "S/." + decimalFormat.format(prmPrecios) + "\n");
		if (p < prmPrecios) 			
			reporte.append("Comparación						: "+ "S/." + decimalFormat.format(prmPrecios - p) + " menos que el promedio." + "\n"+ "\n");
		else reporte.append("Comparación						: "+ "S/." + decimalFormat.format(p - prmPrecios) + " más que el promedio." + "\n"+ "\n");
		
		reporte.append("\n");
		
		i = modelo1;
		p = precio1;
		
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Precio							: "+ "S/." + decimalFormat.format(p) + "\n");	
		reporte.append("Precio promedio						: "+  "S/." + decimalFormat.format(prmPrecios) + "\n");
		if (p < prmPrecios) 			
			reporte.append("Comparación						: "+ "S/." + decimalFormat.format(prmPrecios - p) + " menos que el promedio." + "\n"+ "\n");
		else reporte.append("Comparación						: "+ "S/." + decimalFormat.format(p - prmPrecios) + " más que el promedio." + "\n"+ "\n");
		
		reporte.append("\n");
		
		i = modelo2;
		p = precio2;
		
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Precio							: "+ "S/." + decimalFormat.format(p) + "\n");	
		reporte.append("Precio promedio						: "+  "S/." + decimalFormat.format(prmPrecios) + "\n");
		if (p < prmPrecios) 			
			reporte.append("Comparación						: "+ "S/." + decimalFormat.format(prmPrecios - p) + " menos que el promedio." + "\n"+ "\n");
		else reporte.append("Comparación						: "+ "S/." + decimalFormat.format(p - prmPrecios) + " más que el promedio." + "\n"+ "\n");
		
		reporte.append("\n");
		
		i = modelo3;
		p = precio3;
		
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Precio							: "+ "S/." + decimalFormat.format(p) + "\n");	
		reporte.append("Precio promedio						: "+  "S/." + decimalFormat.format(prmPrecios) + "\n");
		if (p < prmPrecios) 			
			reporte.append("Comparación						: "+ "S/." + decimalFormat.format(prmPrecios - p) + " menos que el promedio." + "\n"+ "\n");
		else reporte.append("Comparación						: "+ "S/." + decimalFormat.format(p - prmPrecios) + " más que el promedio." + "\n"+ "\n");
		
		reporte.append("\n");
		
		i = modelo4;
		p = precio4;
		
		reporte.append("Modelo							: "+ i +"\n");
		reporte.append("Precio							: "+ "S/." + decimalFormat.format(p) + "\n");	
		reporte.append("Precio promedio						: "+  "S/." + decimalFormat.format(prmPrecios) + "\n");
		if (p < prmPrecios) 			
			reporte.append("Comparación						: "+ "S/." + decimalFormat.format(prmPrecios - p) + " menos que el promedio." + "\n"+ "\n");
		else reporte.append("Comparación						: "+ "S/." + decimalFormat.format(p - prmPrecios) + " más que el promedio." + "\n"+ "\n");
		
		reporte.append("\n");}
				

	private void comparacion_cajas() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		
		reporte.setText("CANTIDAD VENDIDA VS CANTIDAD ÓPTIMA"+"\n"+"\n");
		
		int nOptima = cantidadOptima;
	
		String i = modelo0;
		int c = totalProdVend0;
			
			reporte.append("Modelo							: "+ i +"\n");
			reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
			reporte.append("Cantidad óptima						: "+ nOptima + "\n");

			if (c < nOptima) 
				reporte.append("Comparación	vendidas-óptimas			: "+ (nOptima-c) + " menos que la cantidad óptima." + "\n");
			else 
				reporte.append("Comparación	vendidas-óptimas			: "+ (c-nOptima) + " menos que la cantidad óptima." + "\n");
			reporte.append("\n");
			
			i = modelo1;
			c = totalProdVend1;
			
			reporte.append("Modelo							: "+ i +"\n");
			reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
			reporte.append("Cantidad óptima						: "+ nOptima + "\n");
			
			if (c < nOptima) 
				reporte.append("Comparación	vendidas-óptimas			: "+ (nOptima-c) + " menos que la cantidad óptima." + "\n");
			else 
				reporte.append("Comparación	vendidas-óptimas			: "+ (c-nOptima) + " menos que la cantidad óptima." + "\n");
			reporte.append("\n");
			
			i = modelo2;
			c = totalProdVend2;
			
			reporte.append("Modelo							: "+ i +"\n");
			reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
			reporte.append("Cantidad óptima						: "+ nOptima + "\n");
			
			if (c < nOptima) 
				reporte.append("Comparación	vendidas-óptimas			: "+ (nOptima-c) + " menos que la cantidad óptima." + "\n");
			else 
				reporte.append("Comparación	vendidas-óptimas			: "+ (c-nOptima) + " menos que la cantidad óptima." + "\n");
			reporte.append("\n");
			
			i = modelo3;
			c = totalProdVend3;
			
			reporte.append("Modelo							: "+ i +"\n");
			reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
			reporte.append("Cantidad óptima						: "+ nOptima + "\n");
			
			if (c < nOptima) 
				reporte.append("Comparación	vendidas-óptimas			: "+ (nOptima-c) + " menos que la cantidad óptima." + "\n");
			else 
				reporte.append("Comparación	vendidas-óptimas			: "+ (c-nOptima) + " menos que la cantidad óptima." + "\n");
			reporte.append("\n");

			i = modelo4;
			c = totalProdVend4;
			
			reporte.append("Modelo							: "+ i +"\n");
			reporte.append("Cantidad de cajas vendidas				: "+ c +"\n");
			reporte.append("Cantidad óptima						: "+ nOptima + "\n");
			
			if (c < nOptima) 
				reporte.append("Comparación	vendidas-óptimas			: "+ (nOptima-c) + " menos que la cantidad óptima." + "\n");
			else 
				reporte.append("Comparación	vendidas-óptimas			: "+ (c-nOptima) + " menos que la cantidad óptima." + "\n");
			reporte.append("\n");
			
			
	}

	private void estadistica_precio() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		reporte.setText("ESTADÍSTICA SOBRE EL PRECIO"+"\n"+"\n");
		double mean, sum = 0, min=precio0, max=precio0;
		
		if (precio1 > max) max = precio1;
		if (precio1 < min) min = precio1;
		
		if (precio2 > max) max = precio2;
		if (precio2 < min) min = precio2;
		
		if (precio3 > max) max = precio3;
		if (precio3 < min) min = precio3;
		
		if (precio4 > max) max = precio4;
		if (precio4 < min) min = precio4;
		
		sum = precio0 + precio1 + precio2 + precio3 + precio4;
        mean = sum / 5;
        
        reporte.append("Precio promedio				: "+ mean + "\n");
        reporte.append("Precio máximo				: "+ max + "\n");
        reporte.append("Precio mínimo				: "+ min + "\n");

}
		
	
	public void setModalityType(ModalityType applicationModal) {
		// TODO Auto-generated method stub
		
	}
}




//while (modelo !="") {
//reporte.getText("Modelo				: "+modelo"\n");
//reporte.getText("Modelo				: "+modelo"\n");
//reporte.getText("Modelo				: "+modelo"\n");
//reporte.getText("Modelo				: "+modelo"\n");
//reporte.getText("Modelo				: "+modelo"\n");{}


	
	
