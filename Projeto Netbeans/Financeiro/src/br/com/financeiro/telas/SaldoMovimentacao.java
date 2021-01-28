/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.telas;

import br.com.financeiro.dao.ModuloConexao;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author mardio
 */
public class SaldoMovimentacao extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    PreparedStatement pstR = null;
    PreparedStatement pstD = null;
    ResultSet rs = null;
    ResultSet rsR = null;
    ResultSet rsD = null;
    int ident;
    String fszDate = null;
    String szDate = null;
    String cdespesa = null;
    String crenda = null;
    Double vcdespesa = null;
    Double vcrenda = null;
    Double stotal = null;

    /**
     * Creates new form SaldoMovimentacao
     */
    public SaldoMovimentacao() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    public void pesquisarsoldomov() {
        String sql = "SELECT idMovimentacao,movimentacao.idCategoria,observacao,valor,data,descricao FROM movimentacao INNER JOIN categoria ON categoria.idCategoria = movimentacao.idCategoria WHERE `data` BETWEEN (?) AND (?)";
        if (datainicial.getDate() == null || datafinal.getDate() == null) {
            JOptionPane.showMessageDialog(null, "<html><center>Todos os campos são obrigatórios,<br>favor preencher campo vazio");
        } else {
            try {
                Date oDate = datainicial.getDate();
                Date foDate = datafinal.getDate();
                DateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                szDate = oDateFormat.format(oDate);
                fszDate = oDateFormat.format(foDate);
                pst = conexao.prepareStatement(sql);
                pst.setString(1, szDate);
                pst.setString(2, fszDate);
                rs = pst.executeQuery();
                tblmovimentacao.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    public void calculo_renda() {
        String sql = "SELECT SUM(valor)AS Renda FROM movimentacao INNER JOIN categoria ON categoria.idCategoria = movimentacao.idCategoria WHERE `data` BETWEEN (?) AND (?) AND categoria.tipo='renda'";
        try {
            Date oDate = datainicial.getDate();
            Date foDate = datafinal.getDate();
            DateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            szDate = oDateFormat.format(oDate);
            fszDate = oDateFormat.format(foDate);
            pst = conexao.prepareStatement(sql);
            pst.setString(1, szDate);
            pst.setString(2, fszDate);
            rs = pst.executeQuery();
            rs.next();
            crenda = rs.getString("Renda");
            txtrenda.setText(crenda);
            double vDouble = Double.parseDouble(crenda);
            vcrenda = vDouble;

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void calculo_despesa() {
        String sql = "SELECT SUM(valor)AS Despesa FROM movimentacao INNER JOIN categoria ON categoria.idCategoria = movimentacao.idCategoria WHERE `data` BETWEEN (?) AND (?) AND categoria.tipo='DESPESA'";
        try {
            Date oDate = datainicial.getDate();
            Date foDate = datafinal.getDate();
            DateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            szDate = oDateFormat.format(oDate);
            fszDate = oDateFormat.format(foDate);
            pst = conexao.prepareStatement(sql);
            pst.setString(1, szDate);
            pst.setString(2, fszDate);
            rs = pst.executeQuery();
            rs.next();
            cdespesa = rs.getString("Despesa");
            txtdespesa.setText(cdespesa);
            double aDouble = Double.parseDouble(cdespesa);
            vcdespesa = aDouble;
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void calculo_total() {
        if (vcdespesa == null) {
            vcdespesa = 0.00;
        }
        if (vcrenda == null) {
            vcrenda = 0.00;
        }
        stotal = vcrenda - vcdespesa;
        txtsaldo.setText(stotal.toString());
        if (stotal < 0) {
            txtsaldo.setBackground(new java.awt.Color(255, 9, 47));
        } else {
            txtsaldo.setBackground(Color.GREEN);

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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblmovimentacao = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        datainicial = new com.toedter.calendar.JDateChooser();
        datafinal = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnPesquisar = new javax.swing.JButton();
        txtsaldo = new javax.swing.JTextField();
        txtrenda = new javax.swing.JTextField();
        txtdespesa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblmovimentacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Título 5", "Título 6"
            }
        ));
        jScrollPane1.setViewportView(tblmovimentacao);

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(datainicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));
        jPanel1.add(datafinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, -1));

        jLabel1.setText("Data Inicial:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel2.setText("Data Final:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 5, 92, 30));

        btnPesquisar.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/financeiro/icones/pesquisar32.png"))); // NOI18N
        btnPesquisar.setText("Pesquisar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });
        jPanel1.add(btnPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(499, 0, 130, 40));

        txtsaldo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txtsaldo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtrenda.setBackground(new java.awt.Color(102, 242, 69));
        txtrenda.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txtrenda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtrenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtrendaActionPerformed(evt);
            }
        });

        txtdespesa.setBackground(new java.awt.Color(255, 9, 47));
        txtdespesa.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txtdespesa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setText("Renda:");

        jLabel4.setText("Despesa:");

        jLabel5.setText("Saldo:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtrenda, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtdespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtrenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(txtsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        pesquisarsoldomov();
        calculo_renda();
        calculo_despesa();
        calculo_total();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void txtrendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtrendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtrendaActionPerformed

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
            java.util.logging.Logger.getLogger(SaldoMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SaldoMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SaldoMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SaldoMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SaldoMovimentacao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPesquisar;
    private com.toedter.calendar.JDateChooser datafinal;
    private com.toedter.calendar.JDateChooser datainicial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblmovimentacao;
    private javax.swing.JTextField txtdespesa;
    private javax.swing.JTextField txtrenda;
    private javax.swing.JTextField txtsaldo;
    // End of variables declaration//GEN-END:variables
}
