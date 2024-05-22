
package interfaces;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;


/**
 *
 * @author Ruper __
 */
public class IG_EmpleadosEmpresa2 extends javax.swing.JFrame {
    private EmpleadosEmpresa empresa;
    /**
     * Creates new form IG_EmpleadosEmpresa
     */
    public IG_EmpleadosEmpresa2() {
        empresa = new EmpleadosEmpresa();
        empresa.cargarEmpleadosDB();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bt_Añadir = new javax.swing.JButton();
        bt_Eliminar = new javax.swing.JButton();
        bt_Actualizar = new javax.swing.JButton();
        bt_Buscar = new javax.swing.JButton();
        bt_Ordenados = new javax.swing.JButton();
        bt_GastoTotal = new javax.swing.JButton();
        bt_MostrarAntiguos = new javax.swing.JButton();
        bt_Salir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_Display = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bt_Añadir.setText("Añadir Empleado");
        bt_Añadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_AñadirActionPerformed(evt);
            }
        });

        bt_Eliminar.setText("Eliminar Empleado");
        bt_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_EliminarActionPerformed(evt);
            }
        });

        bt_Actualizar.setText("Actualizar Empleado");
        bt_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_ActualizarActionPerformed(evt);
            }
        });

        bt_Buscar.setText("Buscar Empleado");
        bt_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_BuscarActionPerformed(evt);
            }
        });

        bt_Ordenados.setText("Empleados Ordenados");
        bt_Ordenados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_OrdenadosActionPerformed(evt);
            }
        });

        bt_GastoTotal.setText("Calcular Gasto Total");
        bt_GastoTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_GastoTotalActionPerformed(evt);
            }
        });

        bt_MostrarAntiguos.setText("Mostrar Empleados Antiguos");
        bt_MostrarAntiguos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_MostrarAntiguosActionPerformed(evt);
            }
        });

        bt_Salir.setText("Salir");
        bt_Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_SalirActionPerformed(evt);
            }
        });

        ta_Display.setColumns(20);
        ta_Display.setRows(5);
        jScrollPane1.setViewportView(ta_Display);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(bt_Actualizar, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(bt_Eliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bt_Añadir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bt_Buscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bt_Ordenados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bt_GastoTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bt_MostrarAntiguos, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(bt_Salir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_Añadir)
                    .addComponent(bt_Ordenados))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_Eliminar)
                    .addComponent(bt_GastoTotal))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_Actualizar)
                    .addComponent(bt_MostrarAntiguos))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_Buscar)
                    .addComponent(bt_Salir))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_AñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_AñadirActionPerformed
        try {
            String codigo = JOptionPane.showInputDialog(this, "Introduce el código del empleado:");
            String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre del empleado:");
            String apellidos = JOptionPane.showInputDialog(this, "Introduce los apellidos del empleado:");
            String fechaNacimiento = JOptionPane.showInputDialog(this, "Introduce la fecha de nacimiento del empleado (DD/MM/YYYY):");
            String fechaIngreso = JOptionPane.showInputDialog(this, "Introduce la fecha de ingreso del empleado (DD/MM/YYYY):");
            String puesto = JOptionPane.showInputDialog(this, "Introduce el puesto del empleado:");
            String salarioStr = JOptionPane.showInputDialog(this, "Introduce el salario del empleado:");
            double salario = Double.parseDouble(salarioStr);
    
            Empleado nuevoEmpleado = new Empleado(codigo, nombre, apellidos, LocalDate.parse(fechaNacimiento, DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(fechaIngreso, DateTimeFormatter.ofPattern("dd/MM/yyyy")), puesto, salario);
            // Capturar la salida estándar
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);

            // Llamar al método que guarda el empleado
            empresa.guardarEmpleadoDB(nuevoEmpleado);

            // Restaurar la salida estándar
            System.out.flush();
            System.setOut(old);

            // Mostrar el resultado en el JTextArea
            ta_Display.setText(baos.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al añadir empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_bt_AñadirActionPerformed

    private void bt_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_EliminarActionPerformed
        try {
            String codigo = JOptionPane.showInputDialog(this, "Introduce el código del empleado a eliminar:");

            if (codigo != null && !codigo.trim().isEmpty()) {
                // Capturar la salida estándar
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                // Llamar al método que elimina el empleado
                empresa.eliminarEmpleado(codigo);

                // Restaurar la salida estándar
                System.out.flush();
                System.setOut(old);

                // Mostrar el resultado en el JTextArea
                ta_Display.setText(baos.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Código del empleado no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bt_EliminarActionPerformed

    private void bt_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_ActualizarActionPerformed
        try {
            String codigo = JOptionPane.showInputDialog(this, "Introduce el código del empleado a actualizar:");
            String nuevoPuesto = JOptionPane.showInputDialog(this, "Introduce el nuevo puesto del empleado:");
            String nuevoSalarioStr = JOptionPane.showInputDialog(this, "Introduce el nuevo salario del empleado:");
            double nuevoSalario = Double.parseDouble(nuevoSalarioStr);
    
            if (codigo != null && !codigo.trim().isEmpty() && nuevoPuesto != null && !nuevoPuesto.trim().isEmpty() && nuevoSalarioStr != null && !nuevoSalarioStr.trim().isEmpty()) {
                // Capturar la salida estándar
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                // Llamar al método que actualiza el empleado
                empresa.actualizarEmpleadoDB(codigo, nuevoPuesto, nuevoSalario);

                // Restaurar la salida estándar
                System.out.flush();
                System.setOut(old);

                // Obtener la salida del método actualizarEmpleadoDB
                String resultado = baos.toString();

                // Mostrar el resultado en el JTextArea
                if (resultado.trim().isEmpty()) {
                    ta_Display.setText("No se encontró ningún empleado con ese código para actualizar.");
                } else {
                    ta_Display.setText(resultado);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Todos los campos deben ser completados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_bt_ActualizarActionPerformed

    private void bt_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_BuscarActionPerformed
        try {
            String codigo = JOptionPane.showInputDialog(this, "Introduce el código del empleado a buscar:");

            if (codigo != null && !codigo.trim().isEmpty()) {
                // Capturar la salida estándar
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                // Llamar al método que busca el empleado
                empresa.buscarEmpleado(codigo);

                // Restaurar la salida estándar
                System.out.flush();
                System.setOut(old);

                // Obtener la salida del método buscarEmpleado
                String resultado = baos.toString();

                // Mostrar el resultado en el JTextArea
                if (resultado.trim().isEmpty()) {
                    ta_Display.setText("No se encontró ningún empleado con ese código.");
                } else {
                    ta_Display.setText(resultado);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Código del empleado no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bt_BuscarActionPerformed
    
    
    private void bt_OrdenadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_OrdenadosActionPerformed
        try {
            ta_Display.setText(""); // Limpiar el área de texto
            // Capturar la salida estándar
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);

            // Llamar al método que imprime en consola
            empresa.ordenadosPorAntigüedad();

            // Restaurar la salida estándar
            System.out.flush();
            System.setOut(old);

            // Mostrar el resultado en el JTextArea
            ta_Display.setText(baos.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar empleados ordenados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bt_OrdenadosActionPerformed

    private void bt_GastoTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_GastoTotalActionPerformed
        try {
            ta_Display.setText(""); // Limpiar el área de texto
            // Capturar la salida estándar
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);

            // Llamar al método que imprime en consola
            empresa.calcularGastoTotal();

            // Restaurar la salida estándar
            System.out.flush();
            System.setOut(old);

            // Mostrar el resultado en el JTextArea
            ta_Display.setText(baos.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al calcular gasto total: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bt_GastoTotalActionPerformed

    private void bt_MostrarAntiguosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_MostrarAntiguosActionPerformed
        try {
            ta_Display.setText(""); // Limpiar el área de texto
            // Capturar la salida estándar
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);

            // Llamar al método que imprime en consola
            empresa.mostrarEmpleadosAntiguos();

            // Restaurar la salida estándar
            System.out.flush();
            System.setOut(old);

            // Mostrar el resultado en el JTextArea
            ta_Display.setText(baos.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar empleados antiguos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bt_MostrarAntiguosActionPerformed

    private void bt_SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_SalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_bt_SalirActionPerformed
    
    private void mostrarDatos() {
        ta_Display.setText(""); // Limpiar el área de texto
        for (Empleado empleado : EmpleadosEmpresa.empleados) {
            ta_Display.append(empleado.toString() + "\n");
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IG_EmpleadosEmpresa2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IG_EmpleadosEmpresa2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IG_EmpleadosEmpresa2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IG_EmpleadosEmpresa2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IG_EmpleadosEmpresa2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_Actualizar;
    private javax.swing.JButton bt_Añadir;
    private javax.swing.JButton bt_Buscar;
    private javax.swing.JButton bt_Eliminar;
    private javax.swing.JButton bt_GastoTotal;
    private javax.swing.JButton bt_MostrarAntiguos;
    private javax.swing.JButton bt_Ordenados;
    private javax.swing.JButton bt_Salir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea ta_Display;
    // End of variables declaration//GEN-END:variables
}
