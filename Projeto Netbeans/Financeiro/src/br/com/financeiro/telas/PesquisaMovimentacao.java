/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.telas;

import br.com.Util.Utils;
import br.com.financeiro.dao.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mardio
 */
public class PesquisaMovimentacao extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int ident;
    String ndata = null;
    String scombo = null;
    String idcombo = null;
    String selectcombo = null;
    String itensCombo = null;
    String descricao = null;

    /**
     * Creates new form PesquisaMovimentacao
     */
    public PesquisaMovimentacao() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    public void pesquisar_mov() {
        String sql = "SELECT idMovimentacao,movimentacao.idCategoria,observacao,valor,data,descricao FROM movimentacao INNER JOIN categoria ON categoria.idCategoria = movimentacao.idCategoria WHERE idMovimentacao > 0";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblmovimentacao.getModel();
            // ocultando coluna idMovimentacao
            tblmovimentacao.getColumnModel().getColumn(0).setMinWidth(0);
            tblmovimentacao.getColumnModel().getColumn(0).setMaxWidth(0);
            tblmovimentacao.getColumnModel().getColumn(1).setMinWidth(0);
            tblmovimentacao.getColumnModel().getColumn(1).setMaxWidth(0);
            model.setNumRows(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    //retorna os dados da tabela do BD, cada campo e um coluna.
                    rs.getString("idMovimentacao"),
                    rs.getString("idCategoria"),
                    rs.getString("descricao"),
                    rs.getString("observacao"),
                    rs.getString("valor").replace(".", ","),
                    Utils.convertData(rs.getDate("data"))

                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void alterar_mov() {
        String sql = "UPDATE movimentacao SET idCategoria =?, observacao =?, valor =?, data =? WHERE idMovimentacao =?";
        // String sql="update tbclientes set nomecli=?,endcli=?,fonecli=?,emailcli=? where idcli=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cbocategoria.getSelectedItem().toString().replaceAll("[\\D]", ""));
            pst.setString(2, txtobservacao.getText());
            pst.setString(3, txtvalor.getText().replace(",", "."));
            Date oDate = jDatamovimentacao.getDate();
            DateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String szDate = oDateFormat.format(oDate);
            pst.setString(4, szDate);
            pst.setString(5, txtidmovimentacao.getText());
            if (txtobservacao.getText().isEmpty() || txtvalor.getText().isEmpty() || cbocategoria.getSelectedItem().toString() == "<Selecionar>") {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Movimentação Atualizada com Sucesso!!");
                    txtobservacao.setText(null);
                    cbocategoria.setSelectedItem(null);
                    txtvalor.setText(null);
                    jDatamovimentacao.setDate(null);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void excluir_mov() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM `movimentacao` WHERE `movimentacao`.`idMovimentacao` =?";
            try {
                if (txtidmovimentacao.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Favor Selecionar a movimentação a ser excluida!!!!");
                } else {

                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtidmovimentacao.getText());
                    int apagado = pst.executeUpdate();
                    if (apagado > 0) {
                        JOptionPane.showMessageDialog(null, "Movimentação excluída com sucesso");

                        txtidmovimentacao.setText(null);
                        txtobservacao.setText(null);
                        txtvalor.setText(null);
                        jDatamovimentacao.setDate(null);
                        cbocategoria.addItem(null);
                        cbocategoria.setSelectedItem(null);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void addcombo() {
        String sql = "SELECT * FROM categoria";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                ident = rs.getInt("idCategoria");
                descricao = rs.getString("descricao");
                itensCombo = descricao + " - " + ident;
                cbocategoria.addItem(itensCombo);
            }
        } catch (Exception e) {
        }
    }

    private void setar_campos() throws ParseException {
        int setar = tblmovimentacao.getSelectedRow();
        txtidmovimentacao.setText(tblmovimentacao.getModel().getValueAt(setar, 0).toString());
        txtobservacao.setText(tblmovimentacao.getModel().getValueAt(setar, 3).toString());
        txtvalor.setText(tblmovimentacao.getModel().getValueAt(setar, 4).toString());
        ndata = tblmovimentacao.getModel().getValueAt(setar, 5).toString();
        jDatamovimentacao.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(ndata));
        scombo = tblmovimentacao.getModel().getValueAt(setar, 2).toString();
        idcombo = tblmovimentacao.getModel().getValueAt(setar, 1).toString();
        selectcombo = scombo + " - " + idcombo;
        cbocategoria.setSelectedItem(selectcombo);

        //txtdata.setDate((Date) tblmovimentacao.getModel().getValueAt(setar, 5));
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
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblmovimentacao = new javax.swing.JTable();
        lbdescricao = new javax.swing.JLabel();
        lbobservacao = new javax.swing.JLabel();
        lbvalor = new javax.swing.JLabel();
        txtobservacao = new javax.swing.JTextField();
        txtvalor = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbocategoria = new javax.swing.JComboBox<>();
        txtidmovimentacao = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jDatamovimentacao = new com.toedter.calendar.JDateChooser();
        btnAtualizar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Título 4", "Título 5", "Título 6"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        tblmovimentacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "idMovimentacao", "idCategoria", "descricao", "Observacao", "Valor", "Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblmovimentacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblmovimentacaoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblmovimentacao);

        lbdescricao.setText("Descricao:");

        lbobservacao.setText("Observação:");

        lbvalor.setText("valor:");

        jLabel1.setText("Data:");

        cbocategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Selecionar>" }));

        txtidmovimentacao.setEditable(false);

        jLabel2.setText("ID:");

        btnAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/financeiro/icones/update.png"))); // NOI18N
        btnAtualizar.setToolTipText("Gravar Edição");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/financeiro/icones/deletecor.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir Registro selecionado");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbobservacao)
                    .addComponent(lbvalor)
                    .addComponent(lbdescricao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtobservacao, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtvalor, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbocategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtidmovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDatamovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(btnAtualizar)
                .addGap(51, 51, 51)
                .addComponent(btnExcluir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDatamovimentacao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbdescricao)
                        .addComponent(jLabel1)
                        .addComponent(cbocategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtidmovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbobservacao)
                    .addComponent(txtobservacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbvalor)
                    .addComponent(txtvalor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAtualizar)
                    .addComponent(btnExcluir))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        addcombo();
        pesquisar_mov();
    }//GEN-LAST:event_formWindowActivated

    private void tblmovimentacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblmovimentacaoMouseClicked
        try {
            setar_campos();        // TODO add your handling code here:
        } catch (ParseException ex) {
            Logger.getLogger(PesquisaMovimentacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblmovimentacaoMouseClicked

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir_mov();        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        alterar_mov();        // TODO add your handling code here:
    }//GEN-LAST:event_btnAtualizarActionPerformed

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
            java.util.logging.Logger.getLogger(PesquisaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PesquisaMovimentacao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JComboBox<String> cbocategoria;
    private com.toedter.calendar.JDateChooser jDatamovimentacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbdescricao;
    private javax.swing.JLabel lbobservacao;
    private javax.swing.JLabel lbvalor;
    private javax.swing.JTable tblmovimentacao;
    private javax.swing.JTextField txtidmovimentacao;
    private javax.swing.JTextField txtobservacao;
    private javax.swing.JTextField txtvalor;
    // End of variables declaration//GEN-END:variables
}
