package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmpleadosFrame1 extends JFrame {
    private EmpleadosEmpresa empleadosEmpresa;
    private JTextArea textArea;

    public EmpleadosFrame1() {
        empleadosEmpresa = new EmpleadosEmpresa();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión de Empleados");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 5, 5));

        JButton btnAdd = new JButton("Añadir Empleado");
        JButton btnDelete = new JButton("Eliminar Empleado");
        JButton btnUpdate = new JButton("Actualizar Empleado");
        JButton btnSearch = new JButton("Buscar Empleado");
        JButton btnSortByAntiguedad = new JButton("Imprimir empleados ordenados por antigüedad");
        JButton btnCalcTotalSalary = new JButton("Calcular gasto total de los empleados");
        JButton btnShowOldEmployees = new JButton("Mostrar empleados antiguos");
        JButton btnExit = new JButton("Salir");

        panel.add(btnAdd);
        panel.add(btnDelete);
        panel.add(btnUpdate);
        panel.add(btnSearch);
        panel.add(btnSortByAntiguedad);
        panel.add(btnCalcTotalSalary);
        panel.add(btnShowOldEmployees);
        panel.add(btnExit);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(panel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmpleado();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmpleado();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmpleado();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmpleado();
            }
        });

        btnSortByAntiguedad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortByAntiguedad();
            }
        });

        btnCalcTotalSalary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotalSalary();
            }
        });

        btnShowOldEmployees.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOldEmployees();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void addEmpleado() {
        JTextField codigo = new JTextField();
        JTextField nombre = new JTextField();
        JTextField apellidos = new JTextField();
        JTextField fechaNacimiento = new JTextField();
        JTextField fechaIngreso = new JTextField();
        JTextField puesto = new JTextField();
        JTextField salario = new JTextField();

        Object[] message = {
            "Código:", codigo,
            "Nombre:", nombre,
            "Apellidos:", apellidos,
            "Fecha de Nacimiento (DD/MM/YYYY):", fechaNacimiento,
            "Fecha de Ingreso (DD/MM/YYYY):", fechaIngreso,
            "Puesto:", puesto,
            "Salario:", salario,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Añadir Empleado", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Empleado nuevoEmpleado = new Empleado(
                    codigo.getText(),
                    nombre.getText(),
                    apellidos.getText(),
                    LocalDate.parse(fechaNacimiento.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    LocalDate.parse(fechaIngreso.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    puesto.getText(),
                    Double.parseDouble(salario.getText())
                );
                empleadosEmpresa.guardarEmpleadoDB(nuevoEmpleado);
                textArea.setText("Empleado añadido: " + nuevoEmpleado.toString());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al añadir empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteEmpleado() {
        String codigo = JOptionPane.showInputDialog(this, "Introduce el código del empleado a eliminar:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            PrintStream originalOut = System.out;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream newOut = new PrintStream(baos);
            System.setOut(newOut);

            empleadosEmpresa.eliminarEmpleado();

            System.out.flush();
            System.setOut(originalOut);
            textArea.setText(baos.toString());
        }
    }

    private void updateEmpleado() {
        String codigo = JOptionPane.showInputDialog(this, "Introduce el código del empleado a actualizar:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            JTextField puesto = new JTextField();
            JTextField salario = new JTextField();
            Object[] message = {
                "Nuevo Puesto:", puesto,
                "Nuevo Salario:", salario
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Actualizar Empleado", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    PrintStream originalOut = System.out;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream newOut = new PrintStream(baos);
                    System.setOut(newOut);

                    empleadosEmpresa.actualizarEmpleadoDB(codigo, puesto.getText(), Double.parseDouble(salario.getText()));

                    System.out.flush();
                    System.setOut(originalOut);
                    textArea.setText(baos.toString());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void searchEmpleado() {
        String codigo = JOptionPane.showInputDialog(this, "Introduce el código del empleado a buscar:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            PrintStream originalOut = System.out;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream newOut = new PrintStream(baos);
            System.setOut(newOut);

            empleadosEmpresa.buscarEmpleado();

            System.out.flush();
            System.setOut(originalOut);
            textArea.setText(baos.toString());
        }
    }

    private void sortByAntiguedad() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);

        empleadosEmpresa.ordenadosPorAntigüedad();

        System.out.flush();
        System.setOut(originalOut);
        textArea.setText(baos.toString());
    }

    private void calculateTotalSalary() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);

        empleadosEmpresa.calcularGastoTotal();

        System.out.flush();
        System.setOut(originalOut);
        textArea.setText(baos.toString());
    }

    private void showOldEmployees() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);

        empleadosEmpresa.mostrarEmpleadosAntiguos();

        System.out.flush();
        System.setOut(originalOut);
        textArea.setText(baos.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmpleadosFrame1().setVisible(true);
            }
        });
    }
}
