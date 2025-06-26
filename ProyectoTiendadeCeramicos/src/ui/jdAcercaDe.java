package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class jdAcercaDe extends JDialog implements ActionListener {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            jdAcercaDe dialog = new jdAcercaDe();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public jdAcercaDe() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(null);
        
     // Título del proyecto
        JLabel lblTitulo = new JLabel("Tienda de Cerámico");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 26));
        // Aumentar el tamaño del espacio disponible para el título
        lblTitulo.setBounds(24, 20, 400, 40); // Cambié el ancho a 400 y la altura a 40 para más espacio
        getContentPane().add(lblTitulo);

        
        // Descripción del proyecto
        JLabel lblDescripcion = new JLabel("<html><center>Implementación de un programa para tiendas dedicadas a la venta de cerámicos en cajas para pisos.<br/>"
                + "Optimiza y automatiza la gestión de ventas, inventarios y clientes.</center></html>");
        lblDescripcion.setHorizontalAlignment(SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblDescripcion.setBounds(24, 70, 400, 50);
        getContentPane().add(lblDescripcion);
        
        // Información adicional
        JLabel lblInfo = new JLabel("<html><center>IDE: Eclipse IDE for Java Developers<br/>Lenguaje: Java</center></html>");
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblInfo.setBounds(24, 130, 400, 40);
        getContentPane().add(lblInfo);
        
        // Versión y desarrollador
        JLabel lblVersion = new JLabel("Versión: 1.0.0 - Desarrollado por: Wilber Becerra");
        lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
        lblVersion.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblVersion.setBounds(24, 180, 400, 25);
        getContentPane().add(lblVersion);
        
        // Botón Cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnCerrar) {
                    closed();
                }    
            }
        });
        btnCerrar.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnCerrar.setBounds(165, 215, 100, 30);
        getContentPane().add(btnCerrar);
    }
    
    private void closed(){
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Not implemented
    }
}
