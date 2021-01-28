/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.dao;

/**
 *
 * @author mardio
 */
public class Teste {

 //   String servidor;
    String usuario;

    public static void main(String[] args) {
        Leitor leitor = new Leitor();
        String servidor = leitor.servidor;
        String usuario = leitor.usuario;
        System.out.println(usuario);
        System.out.println(servidor);
    }
}
