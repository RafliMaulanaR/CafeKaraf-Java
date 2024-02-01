/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sun.awt.DesktopBrowse;
import sun.net.util.URLUtil;

/**
 *
 * @author Shine
 */
public class view_transaksi extends javax.swing.JFrame {
    public Statement st;
      public ResultSet rs;
      public DefaultTableModel tabmodel;
      public Statement st1;
      public ResultSet rs2;
      public DefaultTableModel tabmodel1;
      
      
        PreparedStatement pr;
        String sql;
      
      Connection con = koneksi.koneksi.Conn();
    /**
     * Creates new form view_transaksi
     */
    public static void openWebpage(String url){
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public view_transaksi() {
        initComponents();
        kd_trans.setEditable(false);
        kd_trans1.setEditable(false);
        kd_trans2.setEditable(false);
        harga_menu.setEditable(false);
        kd_menu.setEditable(false);
        nm_menu.setEditable(false);
        stok.setEditable(false);
        total.setEditable(false);
        tb.setEditable(false);
        kembali.setEditable(false);
        batal_beli.setVisible(false);
        judul();
        tampil();
        
        judul2();
        tampil_keranjang();
        autokode();
        ngas();
    }
 
    public void ngas(){
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM tb_kasir where keterangan='Masuk'");
            while (rs.next()) {
                kd_trans2.setText(rs.getString("id_kasir"));
                kd_trans1.setText(rs.getString("nama_kasir"));
           }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
    public void reset(){
        kd_menu.setText("");
        nm_menu.setText("");
        harga_menu.setText("");
        stok.setText("");
        total.setText("");
   }
    public void judul(){
        Object[] judul = {"Id Menu","Menu","Nama Menu","Harga Menu","Stok"};
        tabmodel = new DefaultTableModel(null,judul){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tmenu2.setModel(tabmodel);
    }
    public void judul2(){
        Object[] judul = {"ID Transaksi","ID Menu","Nama Menu","Harga Menu","Jumlah Beli","Total"};
        tabmodel1 = new DefaultTableModel(null,judul);
        tkeranjang.setModel(tabmodel1);
    }
    public void tampil(){
        try {
            st = con.createStatement();
            tabmodel.getDataVector().removeAllElements();
            tabmodel.fireTableDataChanged();
            rs = st.executeQuery("SELECT * FROM tb_menu");
            while (rs.next()) {
                Object[] data = {
                    rs.getString("id_menu"),
                    rs.getString("menu"),
                    rs.getString("nama_menu"),
                    rs.getString("harga_menu"),
                    rs.getString("stok"),
                };
                tabmodel.addRow(data);
                    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetall(){
        kd_menu.setText("");
        nm_menu.setText("");
        harga_menu.setText("");
        stok.setText("");
        jumlah.setText("");
        total.setText("");
        tb.setText("");
        bayar.setText("");
        kembali.setText("");
    }
    public void autokode(){
        try {
            rs = con.createStatement().executeQuery("Select * from tb_transaksi order by id_transaksi desc");
            if (rs.next()) {
                String kode = rs.getString("id_transaksi").substring(3);
                String an = "" + (Integer.parseInt(kode)+1);
                String nol = "";
                
                if (an.length()== 1) {
                    nol = "000";
                }
                else if(an.length()== 2){
                    nol = "00";
                }
                else if(an.length()==3){
                    nol = "0";
                }
                else if(an.length() == 4){
                    nol = "";
                }
                kd_trans.setText("PR" + nol + an);
            }
            else{
                kd_trans.setText("PR0001");
            }
            kd_trans.enable(false);
            rs.close();
        } catch (Exception e) 
        {
        }

    }
    public void bayar(){
       int a = Integer.parseInt(tb.getText());
       int b = Integer.parseInt(bayar.getText());
       int c;
       
       c = b - a;
       kembali.setText(String.valueOf(c));
    }
    public void tampil_keranjang(){
        try {
            st = con.createStatement();
            tabmodel1.getDataVector().removeAllElements();
            tabmodel1.fireTableDataChanged();
            rs = st.executeQuery("select * from tb_keranjang WHERE status='0'");
            while(rs.next()){
                Object[] data = {
                    rs.getString("id_transaksi"),
                    rs.getString("id_menu"),
                    rs.getString("nama_menu"),
                    rs.getString("harga_menu"),
                    rs.getString("jumlah_beli"),
                    rs.getString("total"),
                };
                tabmodel1.addRow(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void tampil_keranjang2(){
        try {
            st = con.createStatement();
            tabmodel1.getDataVector().removeAllElements();
            tabmodel1.fireTableDataChanged();
            rs = st.executeQuery("select * from tb_keranjang WHERE satus='2'");
            while(rs.next()){
                Object[] data = {
                    rs.getString("id_transaksi"),
                    rs.getString("id_menu"),
                    rs.getString("nama_menu"),
                    rs.getString("harga_menu"),
                    rs.getString("jumlah_beli"),
                    rs.getString("total"),
                };
                tabmodel1.addRow(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void total(){
    try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT SUM(total) FROM tb_keranjang WHERE status='0'");
            while (rs.next()) {
                tb.setText(rs.getString("SUM(total)"));     
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public void kosong(){
        kd_menu.setText("");
        nm_menu.setText("");
        harga_menu.setText("");
        stok.setText("");
        jumlah.setText("");
        total.setText("");
    }
     public void hitung(){
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT id_penjualan,SUM(total) as total_semua FROM tb_penampung group by id_penjualan");
            while (rs.next()) {
                kembali.setText(rs.getString("total_semua"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        kd_trans1 = new javax.swing.JTextField();
        kd_trans2 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tkeranjang = new javax.swing.JTable();
        label2 = new java.awt.Label();
        jPanel11 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jSeparator27 = new javax.swing.JSeparator();
        jLabel39 = new javax.swing.JLabel();
        jSeparator28 = new javax.swing.JSeparator();
        TransaksiTOTAL = new javax.swing.JTextField();
        jumlah = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        kd_trans = new javax.swing.JTextField();
        jSeparator31 = new javax.swing.JSeparator();
        jLabel36 = new javax.swing.JLabel();
        harga_menu = new javax.swing.JTextField();
        jSeparator33 = new javax.swing.JSeparator();
        jLabel37 = new javax.swing.JLabel();
        jSeparator26 = new javax.swing.JSeparator();
        nm_menu = new javax.swing.JTextField();
        kd_menu = new javax.swing.JTextField();
        jSeparator32 = new javax.swing.JSeparator();
        jLabel35 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tb = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        bayar = new javax.swing.JTextField();
        batal_beli = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        kembali = new javax.swing.JTextField();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel40 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        jSeparator29 = new javax.swing.JSeparator();
        jLabel38 = new javax.swing.JLabel();
        stok = new javax.swing.JTextField();
        jSeparator34 = new javax.swing.JSeparator();
        addBTNTR3 = new javax.swing.JButton();
        btnHitung1 = new javax.swing.JButton();
        addBTNTR4 = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tmenu2 = new javax.swing.JTable();
        tcari2 = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        addBTN9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID Kasir :");

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nama Kasir :");

        kd_trans1.setBackground(new java.awt.Color(0, 102, 153));
        kd_trans1.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        kd_trans1.setForeground(new java.awt.Color(255, 255, 255));
        kd_trans1.setBorder(null);
        kd_trans1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_trans1ActionPerformed(evt);
            }
        });

        kd_trans2.setBackground(new java.awt.Color(0, 102, 153));
        kd_trans2.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        kd_trans2.setForeground(new java.awt.Color(255, 255, 255));
        kd_trans2.setBorder(null);
        kd_trans2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_trans2ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Gabriola", 1, 30)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("CAFEKARAF");

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transaksi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(153, 153, 153)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kd_trans2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kd_trans1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 11, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(kd_trans1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kd_trans2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tkeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tkeranjang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tkeranjangFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tkeranjangFocusLost(evt);
            }
        });
        tkeranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tkeranjangMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tkeranjangMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tkeranjang);

        label2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label2.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        label2.setText("Cari");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1084, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setMaximumSize(new java.awt.Dimension(233, 430));
        jPanel11.setMinimumSize(new java.awt.Dimension(233, 430));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel34.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel34.setText("ID TRANSAKSI");
        jPanel11.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setPreferredSize(new java.awt.Dimension(1, 305));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        jPanel11.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 11, -1, 380));
        jPanel11.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 209, -1));

        jLabel39.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel39.setText("Jumlah Beli");
        jPanel11.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));
        jPanel11.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(651, 90, 1, -1));

        TransaksiTOTAL.setFont(new java.awt.Font("Bauhaus", 1, 12)); // NOI18N
        TransaksiTOTAL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TransaksiTOTALKeyReleased(evt);
            }
        });
        jPanel11.add(TransaksiTOTAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 360, 238, 0));

        jumlah.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jumlah.setBorder(null);
        jumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumlahActionPerformed(evt);
            }
        });
        jumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jumlahKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jumlahKeyTyped(evt);
            }
        });
        jPanel11.add(jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 200, 31));
        jPanel11.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 390, 338, 20));
        jPanel11.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 220, 19));

        kd_trans.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        kd_trans.setBorder(null);
        kd_trans.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_trans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_transActionPerformed(evt);
            }
        });
        jPanel11.add(kd_trans, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 27, 80, 36));
        jPanel11.add(jSeparator31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 69, 80, -1));

        jLabel36.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel36.setText("Nama Menu");
        jPanel11.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        harga_menu.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        harga_menu.setBorder(null);
        jPanel11.add(harga_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 177, 188, 26));
        jPanel11.add(jSeparator33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 209, 205, -1));

        jLabel37.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel37.setText("Harga ");
        jPanel11.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 155, -1, -1));
        jPanel11.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 147, 205, -1));

        nm_menu.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        nm_menu.setBorder(null);
        jPanel11.add(nm_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 117, 205, 24));

        kd_menu.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        kd_menu.setBorder(null);
        kd_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_menuActionPerformed(evt);
            }
        });
        jPanel11.add(kd_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 27, 80, 36));
        jPanel11.add(jSeparator32, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 69, 80, -1));

        jLabel35.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel35.setText("ID MENU");
        jPanel11.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 11, 80, -1));

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel6.setText("Total Beli");
        jPanel11.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        tb.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        tb.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbActionPerformed(evt);
            }
        });
        tb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbKeyReleased(evt);
            }
        });
        jPanel11.add(tb, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 60, 144, 37));

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel7.setText("Bayar");
        jPanel11.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, -1, -1));

        bayar.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        bayar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bayarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bayarKeyTyped(evt);
            }
        });
        jPanel11.add(bayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 144, 37));

        batal_beli.setBackground(new java.awt.Color(0, 204, 153));
        batal_beli.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        batal_beli.setForeground(new java.awt.Color(255, 255, 255));
        batal_beli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete-button.png"))); // NOI18N
        batal_beli.setText("Batal Beli");
        batal_beli.setBorderPainted(false);
        batal_beli.setContentAreaFilled(false);
        batal_beli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        batal_beli.setIconTextGap(2);
        batal_beli.setOpaque(true);
        batal_beli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_beliActionPerformed(evt);
            }
        });
        jPanel11.add(batal_beli, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 150, 30));

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel10.setText("Kembalian");
        jPanel11.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 200, -1, -1));
        jPanel11.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 180, 144, 10));
        jPanel11.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 140, 10));

        kembali.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        kembali.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kembali.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kembaliKeyReleased(evt);
            }
        });
        jPanel11.add(kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 230, 144, 37));
        jPanel11.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 270, 144, -1));

        jLabel40.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel40.setText("Total");
        jPanel11.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, -1, -1));

        total.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        total.setBorder(null);
        total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalActionPerformed(evt);
            }
        });
        total.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                totalKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                totalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                totalKeyTyped(evt);
            }
        });
        jPanel11.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 150, 31));
        jPanel11.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 150, -1));

        jLabel38.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel38.setText("Stok");
        jPanel11.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        stok.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        stok.setBorder(null);
        stok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                stokKeyReleased(evt);
            }
        });
        jPanel11.add(stok, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 188, 26));
        jPanel11.add(jSeparator34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 205, -1));

        addBTNTR3.setBackground(new java.awt.Color(0, 153, 255));
        addBTNTR3.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        addBTNTR3.setForeground(new java.awt.Color(255, 255, 255));
        addBTNTR3.setText("DONE");
        addBTNTR3.setBorderPainted(false);
        addBTNTR3.setContentAreaFilled(false);
        addBTNTR3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addBTNTR3.setIconTextGap(2);
        addBTNTR3.setOpaque(true);
        addBTNTR3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBTNTR3ActionPerformed(evt);
            }
        });
        jPanel11.add(addBTNTR3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 290, 150, 40));

        btnHitung1.setBackground(new java.awt.Color(0, 51, 51));
        btnHitung1.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        btnHitung1.setForeground(new java.awt.Color(255, 255, 255));
        btnHitung1.setText("Beli");
        btnHitung1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitung1ActionPerformed(evt);
            }
        });
        jPanel11.add(btnHitung1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 90, 30));

        addBTNTR4.setBackground(new java.awt.Color(23, 32, 41));
        addBTNTR4.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        addBTNTR4.setForeground(new java.awt.Color(204, 204, 204));
        addBTNTR4.setText("DONE");
        addBTNTR4.setBorderPainted(false);
        addBTNTR4.setContentAreaFilled(false);
        addBTNTR4.setIconTextGap(2);
        addBTNTR4.setOpaque(true);
        addBTNTR4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBTNTR4ActionPerformed(evt);
            }
        });
        jPanel11.add(addBTNTR4, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 290, 150, 40));

        clear.setBackground(new java.awt.Color(255, 0, 102));
        clear.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        clear.setForeground(new java.awt.Color(255, 255, 255));
        clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/settings.png"))); // NOI18N
        clear.setText("Reset");
        clear.setBorderPainted(false);
        clear.setContentAreaFilled(false);
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.setIconTextGap(2);
        clear.setOpaque(true);
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        jPanel11.add(clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 150, 30));

        tmenu2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tmenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tmenu2MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tmenu2);

        label1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label1.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        label1.setText("Cari Menu");

        addBTN9.setBackground(new java.awt.Color(0, 51, 255));
        addBTN9.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        addBTN9.setForeground(new java.awt.Color(255, 255, 255));
        addBTN9.setText("BACK");
        addBTN9.setBorder(null);
        addBTN9.setBorderPainted(false);
        addBTN9.setContentAreaFilled(false);
        addBTN9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addBTN9.setIconTextGap(2);
        addBTN9.setOpaque(true);
        addBTN9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBTN9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tcari2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addBTN9, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tcari2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addBTN9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tkeranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tkeranjangMouseClicked
        // TODO add your handling code here:
        kd_trans.setText(tabmodel1.getValueAt(tkeranjang.getSelectedRow(), 0) + "");
        kd_menu.setText(tabmodel1.getValueAt(tkeranjang.getSelectedRow(), 1) + "");                                   
        nm_menu.setText(tabmodel1.getValueAt(tkeranjang.getSelectedRow(), 2) + "");                                   
        harga_menu.setText(tabmodel1.getValueAt(tkeranjang.getSelectedRow(), 3) + "");
        jumlah.setText(tabmodel1.getValueAt(tkeranjang.getSelectedRow(), 4) + "");
        total.setText(tabmodel1.getValueAt(tkeranjang.getSelectedRow(), 5) + "");
        
        batal_beli.setVisible(true);
        
        stok.setText("-");
        bayar.setText("");
        kembali.setText("");
        
        btnHitung1.setEnabled(false);
        
    }//GEN-LAST:event_tkeranjangMouseClicked

    private void kd_transActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_transActionPerformed
        // TODO add your handling code here:
        autokode();
        
    }//GEN-LAST:event_kd_transActionPerformed

    private void jumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahKeyTyped
   if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_jumlahKeyTyped

    private void jumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahKeyReleased
        
        int a,b,jum,jml_bel,ts;
        String d;
        if(jumlah.getText().length() !=0){
            a = Integer.valueOf(harga_menu.getText());
            b = Integer.valueOf(jumlah.getText());
            int baris = tmenu2.getSelectedRow();
            jml_bel = Integer.valueOf(jumlah.getText());
            String stok = tmenu2.getValueAt(baris, 4).toString();
            ts = Integer.valueOf(stok);
            if(jml_bel > ts)
            {
               JOptionPane.showMessageDialog(null, "Stok Menu Melebihi","Informasi", JOptionPane.INFORMATION_MESSAGE);
               jumlah.setText(null);
            }else{
                jum = a * b;
                d = String.valueOf(jum);
                total.setText(d);
            }
            
        }else{
            total.setText("0");
        }
       
        
    }//GEN-LAST:event_jumlahKeyReleased

    private void jumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahKeyPressed

    }//GEN-LAST:event_jumlahKeyPressed

    private void TransaksiTOTALKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TransaksiTOTALKeyReleased

    }//GEN-LAST:event_TransaksiTOTALKeyReleased

    private void tkeranjangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tkeranjangMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tkeranjangMouseReleased

    private void kd_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_menuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_menuActionPerformed

    private void totalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalKeyPressed

    private void totalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_totalKeyReleased

    private void totalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_totalKeyTyped

    private void addBTNTR3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBTNTR3ActionPerformed
        // TODO add your handling code here:
        try {
            int b = Integer.valueOf(bayar.getText());
            int a = Integer.valueOf(tb.getText());
            if (a > b) {
                JOptionPane.showMessageDialog(null, "Maaf Uang Kurang !!", "Informasi", JOptionPane.ERROR_MESSAGE);
            }else if (bayar.getText().equals("") || tb.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Masukan Uang Terlebih Dahulu !", "Informasi", JOptionPane.ERROR_MESSAGE);
            }else if (tb.getText().equals("") || tb.getText().equals(" ")) {
                JOptionPane.showMessageDialog(null, "Maaf Harus Memesan Menu T  erlebih Dahulu !", "Informasi", JOptionPane.ERROR_MESSAGE);
            }else{
                st = con.createStatement();
                st.executeUpdate("INSERT INTO tb_fix SET " + "id_transaksi='"+ kd_trans.getText() +"', total_beli='"+ tb.getText() +"', bayar='"+ bayar.getText() +"', kembalian='"+ kembali.getText() +"'");
                st.executeUpdate("TRUNCATE tb_keranjang");
                st.executeUpdate("UPDATE tb_keranjang SET status='2'");
                tampil_keranjang2();
                JOptionPane.showMessageDialog(null, "Pembelian Berhasil !", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                openWebpage("http://localhost:81/CafeKaraf/struk.php?id="+kd_trans2.getText()+"&id_trans="+ kd_trans.getText());
                //Desktop.getDesktop().browse(new URL().toURI());
                st.executeUpdate("UPDATE tb_keranjang SET status='1'");
                autokode();
                resetall();
                tkeranjang.removeAll();
                
            }
         } catch (Exception e) {
        }
    }//GEN-LAST:event_addBTNTR3ActionPerformed

    private void batal_beliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_beliActionPerformed
        // TODO add your handling code here:
        try{
            st = con.createStatement();
            st.executeUpdate("DELETE FROM `tb_keranjang` WHERE `tb_keranjang`.`id_menu` ='"+ kd_menu.getText() +"' AND status ='0' ");
            JOptionPane.showMessageDialog(null,"Data Berhasil Di Hapus");
            st.executeUpdate("DELETE FROM `tb_transaksi` WHERE `tb_transaksi`.`id_menu` ='" + kd_menu.getText() + "'");
            tampil_keranjang();
            total();
            tampil();
            jumlah.setText("");
            
        batal_beli.setVisible(false);
            
            reset();
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_batal_beliActionPerformed

    private void btnHitung1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitung1ActionPerformed
        // TODO add your handling code here:
        try {
            st = con.createStatement();
                rs = st.executeQuery("SELECT COUNT(id_menu) AS cek FROM tb_keranjang WHERE id_menu = '"+ kd_menu.getText() +"' AND id_transaksi = '"+ kd_trans.getText() +"' ");
                if (rs.next()) {
                    String cekdata = rs.getString("cek");
                    if (Integer.parseInt(cekdata) > 0) {
                        st = con.createStatement();
                        rs = st.executeQuery("SELECT * FROM tb_keranjang WHERE id_menu = '"+ kd_menu.getText() +"' AND id_transaksi = '"+ kd_trans.getText() +"' ");
                        if (rs.next()) {
                           String jumlah_lama = rs.getString("jumlah_beli");
                           String sub_total_lama = rs.getString("total");
                           int total_baru = Integer.parseInt(jumlah_lama) + Integer.parseInt(jumlah.getText());
                           int sub_baru = Integer.parseInt(total.getText() )+ Integer.parseInt(sub_total_lama);
                           st = con.createStatement();
                           st.executeUpdate("UPDATE tb_keranjang SET jumlah_beli = '"+ String.valueOf(total_baru) +"', total = '"+ String.valueOf(sub_baru) +"' WHERE id_menu= '"+ kd_menu.getText() +"'");
                           JOptionPane.showMessageDialog(null, "Ditambahkan di keranjang");
                           tampil();
                           reset();
                           total();
                           tampil_keranjang();
                           jumlah.setText("");
                           
                        }
                    }else{
                        sql = "INSERT into tb_keranjang values('"+ kd_trans.getText() +"','"+ kd_menu.getText() +"','"  + nm_menu.getText() +"','"+ harga_menu.getText() +"','"+ jumlah.getText() +"','"+ total.getText() +"',status)";
                        st = con.createStatement();
                        st.executeUpdate(sql);
                        st.executeUpdate("INSERT INTO tb_transaksi SET " + " id_kasir='"+ kd_trans2.getText() +"', id_transaksi='"+ kd_trans.getText() +"',id_menu='"+ kd_menu.getText() +"',nama_menu='"+ nm_menu.getText() +"',harga_menu='"+ harga_menu.getText() +"',jumlah_beli='"+ jumlah.getText() +"',total='"+ total.getText()+"'");
                        JOptionPane.showMessageDialog(null, "Berhasil di masukkan ke dalam keranjang","Informasi", JOptionPane.INFORMATION_MESSAGE);
                        tampil();
                        jumlah.setText("");
                        tampil_keranjang();
                        kosong();
                        total();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            sql = "INSERT into tb_keranjang values('"+ kd_trans.getText() +"','"+ kd_menu.getText() +"','"  + nm_menu.getText() +"','"+ harga_menu.getText() +"','"+ jumlah.getText() +"','"+ total.getText() +"',status)";
//            st = con.createStatement();
//            st.executeUpdate(sql);
//            st.executeUpdate("INSERT INTO tb_transaksi SET " + " id_kasir='"+ kd_trans2.getText() +"', id_transaksi='"+ kd_trans.getText() +"',id_menu='"+ kd_menu.getText() +"',nama_menu='"+ nm_menu.getText() +"',harga_menu='"+ harga_menu.getText() +"',jumlah_beli='"+ jumlah.getText() +"',total='"+ total.getText() +"'");
//            JOptionPane.showMessageDialog(null, "Berhasil di masukkan ke dalam keranjang","Informasi", JOptionPane.INFORMATION_MESSAGE);
//            tampil();
//            tampil_keranjang();
//            kosong();
//            total();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
    }//GEN-LAST:event_btnHitung1ActionPerformed

    private void addBTNTR4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBTNTR4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addBTNTR4ActionPerformed

    private void tbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKeyReleased
        // TODO add your handling code here:
        total();
        
    }//GEN-LAST:event_tbKeyReleased

    private void jumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahActionPerformed

    private void totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalActionPerformed

    private void tbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbActionPerformed

    private void bayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayarKeyTyped
   if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }        // TODO add your handling code here:
        
    }//GEN-LAST:event_bayarKeyTyped

    private void kembaliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kembaliKeyReleased
        // TODO add your handling code here:
        bayar();
    }//GEN-LAST:event_kembaliKeyReleased

    private void bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayarKeyReleased
        // TODO add your handling code here:
        bayar();
    }//GEN-LAST:event_bayarKeyReleased

    private void kd_trans1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_trans1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_trans1ActionPerformed

    private void kd_trans2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_trans2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_trans2ActionPerformed

    private void tkeranjangFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tkeranjangFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tkeranjangFocusLost

    private void tkeranjangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tkeranjangFocusGained
        // TODO add your handling co
    }//GEN-LAST:event_tkeranjangFocusGained

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        // TODO add your handling code here:
        reset();
        jumlah.setText("");
    }//GEN-LAST:event_clearActionPerformed

    private void tmenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tmenu2MouseClicked
         kd_menu.setText(tabmodel.getValueAt(tmenu2.getSelectedRow(), 0) + "");                                   
         nm_menu.setText(tabmodel.getValueAt(tmenu2.getSelectedRow(), 2) + "");                                   
         harga_menu.setText(tabmodel.getValueAt(tmenu2.getSelectedRow(), 3) + "");
         stok.setText(tabmodel.getValueAt(tmenu2.getSelectedRow(),4) + "");
        batal_beli.setVisible(false);
         
         jumlah.setText("");
         btnHitung1.setEnabled(true);
         total.setText("");
    }//GEN-LAST:event_tmenu2MouseClicked

    private void stokKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stokKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_stokKeyReleased

    private void addBTN9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBTN9ActionPerformed
        view_hompage vh = new view_hompage();
        vh.show();
        this.dispose();
    }//GEN-LAST:event_addBTN9ActionPerformed

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
            java.util.logging.Logger.getLogger(view_transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(view_transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(view_transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(view_transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new view_transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TransaksiTOTAL;
    private javax.swing.JButton addBTN9;
    private javax.swing.JButton addBTNTR3;
    private javax.swing.JButton addBTNTR4;
    private javax.swing.JButton batal_beli;
    private javax.swing.JTextField bayar;
    private javax.swing.JButton btnHitung1;
    private javax.swing.JButton clear;
    private javax.swing.JTextField harga_menu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator32;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator34;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jumlah;
    private javax.swing.JTextField kd_menu;
    private javax.swing.JTextField kd_trans;
    private javax.swing.JTextField kd_trans1;
    private javax.swing.JTextField kd_trans2;
    private javax.swing.JTextField kembali;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private javax.swing.JTextField nm_menu;
    private javax.swing.JTextField stok;
    private javax.swing.JTextField tb;
    private javax.swing.JTextField tcari2;
    private javax.swing.JTable tkeranjang;
    private javax.swing.JTable tmenu2;
    private javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables
}
