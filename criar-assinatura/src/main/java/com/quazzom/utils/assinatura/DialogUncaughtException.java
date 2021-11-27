package com.quazzom.utils.assinatura;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


/**
 * Dialog para exibir ao usuário uma exceção não tratada. Essa tela exibe a pilha de erro, o nome 
 * do aplicativo e a versão. Além de um aviso pedindo para ele enviar o erro exibido por e-mail 
 * para o desenvolvedor.
 * 
 * @author david
 *
 */
public class DialogUncaughtException extends JDialog implements UncaughtExceptionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int MIN_WIDTH_SIZE = 600;
    private static final int MIN_HEIGHT_SIZE = 400;

    private static final String MENSAGEM_ERRO_PADRAO = "<html>%s experimentou um erro não tratado, "
                                + "e pode estar funcionando de forma inconsistente. É aconselhavel "
                                + "reinicar o programa para garantir que tudo funcione como o "
                                + "esperado.<br><br>Esse erro pode ser reportado para o "
                                + "desenvolvedor.<br>" + "Faça o seguinte:<br><i><font "
                                + "color=\"red\">copie</font></i> e <i><font color=\"red\">cole"
                                + "</font></i> o texto abaixo em um novo e-mail, escreva uma "
                                + "pequena explicação do que você estava fazendo no momento do "
                                + "erro; e envie o e-mail para bug@quazzom.com.<br>";

    private JTextArea jTextAreaErro;

    /**
     * Construtor padrão
     */
    public DialogUncaughtException() {
        
        JPanel jPanelConteudo = new JPanel();
        jPanelConteudo.setLayout(new BorderLayout());
        
        JPanel jPanelInfoParaOUsuario = new JPanel();
        jPanelConteudo.add(jPanelInfoParaOUsuario, BorderLayout.NORTH);
        jPanelInfoParaOUsuario.setLayout(new BorderLayout(0, 0));

        JLabel jLabelErroIcon = new JLabel();
        jLabelErroIcon.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelErroIcon.setPreferredSize(new Dimension(100, 0));
        
        ImageIcon imgIconBug = new ImageIcon(DialogUncaughtException.
                                             class.getResource("/images/icons/bug.png"));
        //Evita uma exceção nessa tela se a imagem for nula
        if(imgIconBug != null) {
            jLabelErroIcon.setIcon(imgIconBug);
        }
        jPanelInfoParaOUsuario.add(jLabelErroIcon, BorderLayout.WEST);

        JLabel jLabelErroTexto = new JLabel();
        jLabelErroTexto.setText(String.format(MENSAGEM_ERRO_PADRAO, Informacoes.PROGRAMA_NOME));
        jPanelInfoParaOUsuario.add(jLabelErroTexto, BorderLayout.CENTER);

        JScrollPane jScrollPaneErro = new JScrollPane();
        jPanelConteudo.add(jScrollPaneErro, BorderLayout.CENTER);

        jTextAreaErro = new JTextArea();
        jTextAreaErro.setEditable(false);
        jScrollPaneErro.setViewportView(jTextAreaErro);

        JPanel jPanelBotao = new JPanel();
        jPanelBotao.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanelConteudo.add(jPanelBotao, BorderLayout.SOUTH);

        JButton jButtonOk = new JButton("OK");
        jButtonOk.addActionListener((e)->{
            dispose();
        });
        jPanelBotao.add(jButtonOk);
        
        
        getRootPane().setDefaultButton(jButtonOk);
        setContentPane(jPanelConteudo);
        setBounds(100, 100, MIN_WIDTH_SIZE, MIN_HEIGHT_SIZE);
        setMinimumSize(new Dimension(MIN_WIDTH_SIZE, MIN_HEIGHT_SIZE));
        setTitle(SwingUtils.criarTituloFrame("Exceção não tratada"));

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        SwingUtils.invokeLater(() -> {

            final StringWriter strWExcecaoInfoBuffer = new StringWriter();

            strWExcecaoInfoBuffer.append(String.format("Aplicativo: %s, Versão: %s.",
                                                       Informacoes.PROGRAMA_NOME,
                                                       Informacoes.PROGRAMA_VERSAO));
            strWExcecaoInfoBuffer.append("\n");

            final PrintWriter printWriter = new PrintWriter(strWExcecaoInfoBuffer);
            e.printStackTrace(printWriter);
            printWriter.close();

            if (jTextAreaErro.getText().length() > 0) {
                strWExcecaoInfoBuffer.append("\n");
                strWExcecaoInfoBuffer.append(jTextAreaErro.getText());
            }

            jTextAreaErro.setText(strWExcecaoInfoBuffer.toString());

            setLocationRelativeTo(getParent());
            setVisible(true);

        });

    }

}
