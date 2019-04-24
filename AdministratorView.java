import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.Date;


public class AdministratorView extends JFrame {

    Connection c = Login.JDBC();
    int o_id;
    Statement s = c.createStatement();
    Statement s1 = c.createStatement(); //need to reduce redundancy in creating statements
    Statement s2 = c.createStatement();
    Statement s3 = c.createStatement();
    Statement s4 = c.createStatement();
    Statement s5 = c.createStatement();
    ResultSet rs = s.executeQuery("select * from customers_cv");
    ResultSet rs1 = s2.executeQuery("select products.pname as Product_name, sum(qty) as Total_Sold, price from products, odetails where products.p_id=odetails.p_id group by pname, price order by sum(qty) desc limit 3;");
    ResultSet rs2 = s1.executeQuery("select * from developers");
    ResultSet rs4 = s3.executeQuery("select * from orders");
    ResultSet rs5 = s4.executeQuery("select name, orders.o_id, orders.received from customers join orders on customers.c_id = orders.c_id where shipped is null;");
    ResultSet rs6 = s5.executeQuery("select o_id, shipped  from orders where year(received) < year(curdate()) and shipped is not null");
    JButton deleteCustomer = new JButton("Delete Customer");
    JButton deleteDeveloper = new JButton("Delete Developer");
    JButton sendOrder = new JButton("Send Order");
    JButton viewOdetails = new JButton("View Details");
    JButton refresh = new JButton("Refresh");
    
    JButton addQty = new JButton("Add");
    JTextField pid = new JTextField("ProductID");
    JTextField qty = new JTextField("Add Qty");
    
    
    
    JLabel label = new JLabel("Top three products sold");
    JLabel label2 = new JLabel("Orders not yet sent");
    JLabel label3 = new JLabel("Old finished orders");
    JPanel panel = new JPanel();
    JTable buildView = new JTable(Login.buildTableModel(rs));
    JTable developerTAble = new JTable(Login.buildTableModel(rs2));
    JTable buildOrders = new JTable(Login.buildTableModel(rs4));
    JTable topThree = new JTable(Login.buildTableModel(rs1));
    JTable outstandOrder = new JTable(Login.buildTableModel(rs5));
    JTable oldOrders = new JTable(Login.buildTableModel(rs6));
    JScrollPane viewTable =  new JScrollPane(buildView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //Tables for 'admin' views
    JScrollPane devTable =  new JScrollPane(developerTAble, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollPane ordersTable =  new JScrollPane(buildOrders, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollPane topThreeTable =  new JScrollPane(topThree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollPane outOrder =  new JScrollPane(outstandOrder, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollPane yearOldOrders =  new JScrollPane(oldOrders, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    AdministratorView() throws SQLException {
        super("Admin View");
        setSize(900,900);
        setLocation(0,0);
        panel.setLayout(null);
        viewTable.setBounds(350, 50, 500, 100);
        devTable.setBounds(350, 155, 500, 100);
        ordersTable.setBounds(350, 260, 500, 100);
        topThreeTable.setBounds(75, 600, 200, 200);
        outOrder.setBounds(300, 600, 300, 200);
        yearOldOrders.setBounds(625, 600, 200, 200);
        deleteCustomer.setBounds(100, 75, 200, 25);
        deleteDeveloper.setBounds(100, 180, 200, 25);
        sendOrder.setBounds(100, 325, 200, 25);
        viewOdetails.setBounds(100,285,200,25);
        label.setBounds(110, 525, 200, 100);
        label2.setBounds(390, 525, 300, 100);
        label3.setBounds(670, 525, 300, 100);
        refresh.setBounds(15, 15, 100, 25);
       
        panel.add(viewOdetails);
        panel.add(viewTable);
        panel.add(devTable);
        panel.add(ordersTable);
        panel.add(deleteCustomer);
        panel.add(deleteDeveloper);
        panel.add(sendOrder);
        panel.add(outOrder);
        panel.add(topThreeTable);
        panel.add(yearOldOrders);
        panel.add(label);
        panel.add(label2);
        panel.add(label3);
        panel.add(refresh);
        
        addQty.setBounds(15, 45, 50, 25);
        qty.setBounds(100, 45, 100, 25);
        pid.setBounds(220, 45, 100, 25);
        
        panel.add(addQty);
        panel.add(qty);
        panel.add(pid);
        
        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        actionAdmin();
    }

    public void actionAdmin() {
        deleteCustomer.addActionListener(new ActionListener() { //button uses selected cell to delete from SQL table at that selected value
            public void actionPerformed(ActionEvent ae) {
                int row = buildView.getSelectedRow();
                int column = buildView.getSelectedColumn();
                String name = (String) buildView.getValueAt(row, 0);
                try {
                    PreparedStatement deleteCustomer = c.prepareStatement("delete from customers where email = ?");  //Prepared Statement for adding to customer table
                    deleteCustomer.setString(1, name);
                    deleteCustomer.executeUpdate();
                    dispose();
                    new AdministratorView();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,"Select Email in Table");
                }
            }
        });
        deleteDeveloper.addActionListener(new ActionListener() { //button uses selected cell to delete from SQL table at that selected value
            public void actionPerformed(ActionEvent ae) {
                int row = developerTAble.getSelectedRow();
                int column = developerTAble.getSelectedColumn();
                String name = (String) developerTAble.getValueAt(row, 1);
                try {
                    PreparedStatement deleteDeveloper = c.prepareStatement("delete from developers where name = ?");  //Prepared Statement for adding to customer table
                    deleteDeveloper.setString(1, name);
                    deleteDeveloper.executeUpdate();
                    dispose();
                    new AdministratorView();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,"Select Email in Table");
                }
            }
        });
        sendOrder.addActionListener(new ActionListener() { //button uses selected cell to delete from SQL table at that selected value
            public void actionPerformed(ActionEvent ae) {
                int row = buildOrders.getSelectedRow();
                int name = (int) buildOrders.getValueAt(row, 0);
                o_id = name;
                System.out.println(o_id);
                try {
                    PreparedStatement sendOrder = c.prepareStatement("{call sendOrder(?)}");  //Prepared Statement for adding to customer table
                    sendOrder.setInt(1, o_id);
                    sendOrder.executeUpdate();
                    dispose();
                    new AdministratorView();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,"Select Email in Table");
                }
            }
        });
        
        
        
        addQty.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
        

                
                try {

                    PreparedStatement updateQTY = c.prepareStatement("update products set qoh = qoh + ? where p_id = ? ");
                    updateQTY.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(qty.getText())));
                    updateQTY.setBigDecimal(2, BigDecimal.valueOf(Long.parseLong(pid.getText())));
                    updateQTY.execute();
                	
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                }
           });
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        viewOdetails.addActionListener(new ActionListener() { //button uses selected cell to delete from SQL table at that selected value
            public void actionPerformed(ActionEvent ae) {

                int row = buildOrders.getSelectedRow();
                int name = (int) buildOrders.getValueAt(row, 0);
                o_id = name;
                System.out.println(name);
                try {
                    PreparedStatement viewOrder = c.prepareStatement("{call viewOrder(?)}");
                    viewOrder.setInt(1,o_id);
                    viewOrder.execute();
                    ResultSet rs5 = viewOrder.getResultSet();
                    JTable buildOdetails = new JTable(Login.buildTableModel(rs5));
                    JScrollPane odetailsTable =  new JScrollPane(buildOdetails, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    odetailsTable.setBounds(150, 365, 700, 100);
                    panel.add(odetailsTable);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        refresh.addActionListener(new ActionListener() { //button uses selected cell to delete from SQL table at that selected value
            public void actionPerformed(ActionEvent ae) {
                dispose();
                try {
                    new AdministratorView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}