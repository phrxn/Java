package com.quazzom.utils.assinatura;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SwingUtils {
    
    private static final String DEFAULT_URL = "https://www.labsynth.com.br/assinatura/assinatura_email_fixa.jpg";

    /**
     * Usado para padronizar o título de todas as janelas que não sejam a principal.
     * Seguindo a seguinte estrutura: Informacoes.APLICATIVO_NOME - texto. Exemplo: 
     * Assinatura - Informação
     * 
     * @param texto
     *            o título da janela
     * 
     * @return uma {@link String} contendo um título devidamente formatado.
     */
    public static String criarTituloFrame(String texto) {
        return String.format("%s - %s", Informacoes.PROGRAMA_NOME, texto);
    }
    
    /**
     * Exibe uma mensagem em um {@link JOptionPane#showInputDialog} bloqueante, pedindo algum dado
     * para o usuário. 
     * 
     * @param mensagem
     *            a menssagem que será exibida para o usuário
     * @return uma {@link String} informando o dado que o usuário inseriu ou <code>null</code>
     *         se o usuário cancelar a operação
     */
    public static String dialog(String mensagem) {
        return (String) JOptionPane.showInputDialog(null,
                                                    mensagem,
                                                    criarTituloFrame("Input"),
                                                    JOptionPane.INFORMATION_MESSAGE,
                                                    null,
                                                    null,
                                                    DEFAULT_URL);
    }
    
    /**
     * Exibir um {@link JOptionPane#showMessageDialog} devidamente formatado e contendo uma
     * mensagem de erro para o usuário.
     * 
     * @param erro
     *            mensagem que deverá ser exibida para o usuário.
     */
    public static void showErro(String erro) {
        showMenssageDialog(erro, criarTituloFrame("Erro"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Cria e exibe uma mensagem mais detalhada sobre alguma exceção lançada. Contendo algum 
     * <b>delhalhe</b> e a pŕópria mensagem da exceção que está na exceção <b>e</b>
     *  
     * @param detalhes
     *            uma {@link String} contendo os detalhes para usuário da exceção que ocorreu,
     *            exemplo: Não foi possível abrir o arquivo a.txt.
     * @param e
     *            exceção onde irá se obter a mensagem, exemplo: File not found.
     */
    public static void showExcecao(String detalhes, Exception e) {
    	
        showMenssageDialog(String.format("%s.\nDetalhe: %s.", detalhes, e.getMessage()),
        		           criarTituloFrame("Erro"),
        		           JOptionPane.ERROR_MESSAGE);
        
    }    
    
    /**
     * Exibe um JOptioPanel.Dialog para o usuário.
     *  
     * @param mensagem
     *            a mensagem que deverá ser mostrada para o usuário
     * @param titulo
     *            o título da Dialog (veja: {@link SwingUtils#criarTituloFrame(String)}).
     * @param tipo
     *            o Tipo do Dialog, exemplos: JOptionPane.ERROR_MESSAGE, JOptionPane.INFORMATION_MESSAGE e etc...
     */
    private static void showMenssageDialog(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(null, mensagem, titulo, tipo); 
    }
    
    
    /**
     * Executa um {@link Runnable} dentro da Event Dispatch Thread.
     * 
     * @param r
     *         um {@link Runnable} que será executado dentro da Event Dispatch Thread.
     */
    public static void invokeLater(Runnable r) {
        
        if(SwingUtilities.isEventDispatchThread()) {
            r.run();
        }else {
            SwingUtilities.invokeLater(r);
        }
    }
}
