package br.com.financeiro.dao;

import java.sql.*;

public class ModuloConexao {

    String User;
    String senha;
    String servidor;

    //método responsál por estabelecer a conexão com o banco
    public static Connection conector() {
        java.sql.Connection conexao = null;
        Leitor leitor = new Leitor();
        String user = leitor.usuario;
        String senha = leitor.senha;
        String servidor = "localhost";
        //a linha abaixo chama o driver que eu importei aqui para a bibliotec
        String driver = "com.mysql.jdbc.Driver";
        String url = String.format("jdbc:mysql://" + servidor + "/financeiro", null);
             //String url = servidor;
        //String user = "infox";
        //String password = "infox2020";
        //estabelecendo a conexao com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, senha);
            return conexao;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
