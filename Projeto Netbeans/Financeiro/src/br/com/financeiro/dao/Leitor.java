/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author mardio
 */
public class Leitor {

public String usuario;
public String senha;
public String servidor;

    public Leitor() {
        try {
            FileInputStream arquivo = new FileInputStream("arquivo.txt");
            InputStreamReader input = new InputStreamReader(arquivo);
            BufferedReader br = new BufferedReader(input);
            String linha;
            do {
                linha = br.readLine();
                if (linha != null) {
                    String[] palavras = linha.split(";");
                    usuario = palavras[0];
                    senha = palavras[1];
                    servidor = palavras[2];

                }
            } while (linha != null);

        } catch (Exception e) {
            System.out.println("Erro ao Ler Arquivo:" + e);
        }
    }
}
