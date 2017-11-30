package Chat;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;

public class Client extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	private JTextArea texto;
	private JTextField txtMsg;
	private JButton btnSend;
	private JButton btnSair;
	private JLabel labelHistorico;
	private JLabel labelMsg;
	private JPanel pannelContent;
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private BufferedWriter bfW;
	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtNome;

	public Client() throws IOException{
		JLabel labelMessage = new JLabel("Verificar!");
		txtIP = new JTextField("127.0.0.1");
		txtPorta = new JTextField("12345");
		txtNome = new JTextField("Cliente");
		Object[] texts = {labelMessage, txtIP, txtPorta, txtNome};
		JOptionPane.showMessageDialog(null, texts);
		pannelContent = new JPanel();
		texto = new JTextArea(10,20);
		texto.setEditable(false);
		texto.setBackground(new Color(230,230,230));
		txtMsg = new JTextField(20);
		labelHistorico = new JLabel("Histórico");
		labelMsg = new JLabel("Mensagem");
		btnSend = new JButton("Enviar");
		btnSend.setToolTipText("Enviar mensagem");
		btnSair = new JButton("Sair");
		btnSair.setToolTipText("Sair do chat");
		btnSend.addActionListener(this);
		btnSair.addActionListener(this);
		btnSend.addKeyListener(this);
		txtMsg.addKeyListener(this);
		JScrollPane scroll = new JScrollPane(texto);
		texto.setLineWrap(true);
		pannelContent.add(labelHistorico);
		pannelContent.add(scroll);
		pannelContent.add(labelMsg);
		pannelContent.add(txtMsg);
		pannelContent.add(btnSair);
		pannelContent.add(btnSend);
		pannelContent.setBackground(Color.BLACK);
		texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
		txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
		setTitle(txtNome.getText());
		setContentPane(pannelContent);
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(250, 300);
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void conectar() throws IOException{
		socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfW = new BufferedWriter(ouw);
		bfW.write(txtNome.getText()+ "\r\n");
		bfW.flush();
	}

	public void enviarMensagem(String mensagem) throws IOException{

		if(mensagem.equals("Sair")){
			bfW.write("Desconectado\r\n");
			texto.append("Desconectado \r\n);");
		}else{
			bfW.write(mensagem + "\r\n");
			texto.append(txtNome.getText() + " diz: " + txtMsg.getText()+"\r\n");
		}
		bfW.flush();
		txtMsg.setText("");
	}

	public void escutar() throws IOException{

		InputStream in = socket.getInputStream();
		InputStreamReader inR = new InputStreamReader(in);
		BufferedReader bfR = new BufferedReader(inR);
		String mensagem = "";

		while(!"Sair".equalsIgnoreCase(mensagem)){
			if(bfR.ready()){
				mensagem = bfR.readLine();
			}
			if(mensagem.equals("Sair")){
				texto.append("Servidor caiu! \r\n");
			}else{
				texto.append(mensagem + "\r\n");
			}
		}
	}

	public void sair() throws IOException{
		//evita que os arquivos fiquem abertos
		enviarMensagem("Sair");
		bfW.close();
		ouw.close();
		ou.close();
		socket.close();
	}
	@Override
	public void actionPerformed(ActionEvent e){
		try {
		     if(e.getActionCommand().equals(btnSend.getActionCommand())){
		        enviarMensagem(txtMsg.getText());
		     }else{
		    	 if(e.getActionCommand().equals(btnSair.getActionCommand())){
		    		 sair();
		    	 }
		     }
		}catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			try{
				enviarMensagem(txtMsg.getText());
			}catch(IOException f){
				f.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0){

	}
	@Override
	public void keyTyped(KeyEvent arg0){

	}

	public static void main(String[]args) throws IOException{

		Client c = new Client();
		c.conectar();
		c.escutar();
	}
}
