package com.quazzom.utils.assinatura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Essa classe tem alguns metódos úteis para obter as informações que serão necessárias para a 
 * criação da assinatura. Informações essas que estão dentro de um arquivo de texto que estará 
 * no JAR que será criado.
 * 
 * @author david
 *
 */
public class IOUtils {

	
	/**
	 * Retorna as informações da parte de cima da assinatura.
	 * 
	 * @return uma {@link String} contendo a parte de cima da assinatura.
	 * 
	 * @throws IOException
     *             se houver algum erro durante a leitura do arquivo de texto.
	 */
	public static String getAssinaturaParteCima() throws IOException {
		return lerArquivoDeTexto("/assinatura/assinatura_cima.txt");
	}
	
	
	/**
	 * Retorna as informações da parte de baixo da assinatura.
	 * 
	 * @return uma {@link String} contendo a parte de baixo da assinatura.
	 * 
	 * @throws IOException
	 *             se houver algum erro durante a leitura do arquivo de texto.
	 */
	public static String getAssinaturaParteBaixo() throws IOException {
		return lerArquivoDeTexto("/assinatura/assinatura_baixo.txt");
	}
		
	
	/**
	 * Lê os dados que estão em um arquivo de texto que está dentro do JAR e retorna esses dados 
	 * em uma {@link String}.
	 * 
	 * @param arquivo
	 *            caminho para o arquivo de texto que está dentro do JAR examplo: <b>/a/texto.txt</b>
	 * 
	 * @return uma {@link String} contendo os dados dentro do arquivo de texto.
	 * 
	 * @throws IOException
	 *             se ocorrer algum erro durante a tentativa de obter os dados dentro do arquivo de texto.
	 */
	private static String lerArquivoDeTexto(String arquivo) throws IOException {

		InputStream is = IOUtils.class.getResourceAsStream(arquivo);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader buffer = new BufferedReader(isr);
		
		String line= buffer.readLine();
		
		StringBuffer sb = new StringBuffer();
		
		while (line!=null) {
			
            sb.append(line);
            sb.append("\n");
	        line= buffer.readLine();
	        
	    }
		
		return sb.toString();
	}
	
}
