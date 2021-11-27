package com.quazzom.utils.assinatura;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FrameAssinatura extends JFrame{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JPanel jPanelRoot;
    private JTextArea jTextAreaAssinatura;

    /** buffer usado para armazenar os dados da criação da assinatura */
    private StringBuilder strAssinaturaBuffer;
    
    private String strAssinaturaParteDeCima,
                   strAssinaturaParteDeBaixo;

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new DialogUncaughtException());
        
        SwingUtils.invokeLater(()->{
            new FrameAssinatura();
        });
        
    }

    
    /**
     * Construtor padrão
     */
    public FrameAssinatura() {
        
        //pede a URL da imagem que será usada para criar a assinatura
        String strURL = SwingUtils.dialog("<html>Insira uma URL onde está a imagem que será "
                                         + "usada na assinatura, exemplo: <br><font color=\"red\">"
                                         + "https://www.dominio.com/imagem_assinatura.jpg</font>");

        //ser for null, o usuário clicou no cancelar, logo, o programa deve ser encerrado.
        if(strURL == null) {
            System.exit(0);
        }

        //lê o html que será usado para criar a assinatura.
        try {
        	
            strAssinaturaParteDeCima = IOUtils.getAssinaturaParteCima();
            strAssinaturaParteDeBaixo = IOUtils.getAssinaturaParteBaixo();
            
        } catch (IOException e) {
            SwingUtils.showExcecao("Não foi possível ler os dados dentro do JAR, os mesmo são "
                                   + "necessários para a criação da assinatura",
                                   e);
            System.exit(0);
        }

        //formata a parte de cima, substituindo o %s pela URL ou caminho da imagem inserida pelo usuário no Input.
        strAssinaturaParteDeCima = String.format(strAssinaturaParteDeCima, strURL);

        //cria a janela e seus componentes
        criarFrame();
        
    }

    /**
     * Cria a tela.
     */
    public void criarFrame() {
        
        jPanelRoot = new JPanel();
        jPanelRoot.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        jPanelRoot.setLayout(new BorderLayout(0, 0));

        jTextAreaAssinatura = new JTextArea();
        jTextAreaAssinatura.addKeyListener(new SomenteCopiarEColar());
        
        AbstractDocument jTextAreaAssinaturaAD = (AbstractDocument) jTextAreaAssinatura.getDocument();
        jTextAreaAssinaturaAD.setDocumentFilter(new AssinaturaDocumentFilter());
        
        JScrollPane jScrollPaneAssinatura = new JScrollPane();
        jScrollPaneAssinatura.setViewportView(jTextAreaAssinatura);
        
        jPanelRoot.add(jScrollPaneAssinatura, BorderLayout.CENTER);
        
        this.strAssinaturaBuffer = new StringBuilder();        
        
        setContentPane(jPanelRoot);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setTitle(String.format("%s - %s", Informacoes.PROGRAMA_NOME, Informacoes.PROGRAMA_VERSAO));
        setVisible(true);
        
    }

    /**
     * Cria a assinatura
     */
    public void criarAssinatura() {        
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);
        
        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            
            String dado = null;
            
            try {
            	
                dado = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                
            }catch (UnsupportedFlavorException | IOException e) {
            	
                SwingUtils.showExcecao(
                        "Algum erro ocorreu durante a tentativa de manipular o clipboard", e);
                
            }
            
            if(dado == null)
                return;
            
            //limpa o atual texto do buffer
            strAssinaturaBuffer.setLength(0);
            
            strAssinaturaBuffer.append(strAssinaturaParteDeCima);
            
            final String[] linhas = dado.split("\n");
            
            for (int a = 0; a < linhas.length; ++a) {
            	
                strAssinaturaBuffer.append("\t\t\t\t\t\t" +
                                           linhas[a].replaceAll("\\s+"," ") +
                                           "<br>\n"); 
                
            }

            strAssinaturaBuffer.append(strAssinaturaParteDeBaixo);
            
            StringSelection stringSelection = new StringSelection(strAssinaturaBuffer.toString());

            clipboard.setContents(stringSelection, null);
            
        }
    }

    /**
     * Filtro usado para interceptar o texto inserido no {@link FrameAssinatura#jTextAreaAssinatura},
     * criar a assinatura e substituir o texto inserido pela assinatura
     *  
     * @author david
     */
    private class AssinaturaDocumentFilter extends DocumentFilter{
        
        @Override
        public void insertString(FilterBypass fb,
                                 int offset,
                                 String string,
                                 AttributeSet attr)
                                     throws BadLocationException {
            
            replace(fb, offset, 0, string, attr);
        
        }
        
        @Override
        public void replace(FilterBypass fb, 
                            int offset, 
                            int length, 
                            String text, 
                            AttributeSet attrs) 
                                throws BadLocationException {

            criarAssinatura();
            
            fb.replace(0, fb.getDocument().getLength(), strAssinaturaBuffer.toString(), null);
            
        }
        
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            //ignorar...
        }
        
    }
    
    /**
     * Esse {@link KeyAdapter} é usado para que somente CTRL+V seja processado.
     * 
     * @author david
     */
    private class SomenteCopiarEColar extends KeyAdapter{
        
        @Override
        public void keyTyped(KeyEvent e) {
            keyPressed(e);
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            
            boolean isControlPressionado = ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0);
            boolean isTeclaVPressionada  = e.getKeyCode() == KeyEvent.VK_V;
      
            //se não for CTRL+V não processar
            if(!(isControlPressionado && isTeclaVPressionada)) {
                e.consume();
            }
      
        }

    }

}
