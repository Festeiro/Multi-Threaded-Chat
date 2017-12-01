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

public class Client extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private JTextArea texto;
	private JTextField txtMsg;
	private JButton btnEnviar;
	private JButton btnSair;
	private JLabel labelHist;
	private JLabel labelMensagem;
	private JPanel pannelContent;
	private Socket socket;
	private OutputStream out;
	private Writer ouw; 
	private BufferedWriter bfw;
	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtNome; 
	
	public Client() throws IOException{                  
	    JLabel lblMensagem = new JLabel("Login");
	    txtIP = new JTextField("127.0.0.1");
	    txtPorta = new JTextField("123");
	    txtNome = new JTextField("Marcelo");                
	    Object[] texts = {lblMensagem, txtIP, txtPorta, txtNome};  
	    JOptionPane.showMessageDialog(null, texts);              
	     pannelContent = new JPanel();
	     texto = new JTextArea(10,25);
	     texto.setEditable(false);
	     texto.setBackground(Color.white);
	     txtMsg = new JTextField(25);
	     labelHist = new JLabel("Chat");
	     labelMensagem = new JLabel("Mensagem");
	     btnEnviar = new JButton("Enviar");
	     btnEnviar.setToolTipText("Enviar Mensagem");
	     btnSair = new JButton("Sair");
	     btnSair.setToolTipText("Sair do Chat");
	     btnEnviar.addActionListener(this);
	     btnSair.addActionListener(this);
	     btnEnviar.addKeyListener(this);
	     txtMsg.addKeyListener(this);
	     JScrollPane scroll = new JScrollPane(texto);
	     texto.setLineWrap(true);  
	     pannelContent.add(labelHist);
	     pannelContent.add(scroll);
	     pannelContent.add(labelMensagem);
	     pannelContent.add(txtMsg);
	     pannelContent.add(btnSair);
	     pannelContent.add(btnEnviar);
	     //Color col = new Color(DARK_GRAY);
	     pannelContent.setBackground(Color.gray);                                                  
	     setTitle(txtNome.getText());
	     setContentPane(pannelContent);
	     setLocationRelativeTo(null);
	     setResizable(true);
	     setSize(300,320);
	     setVisible(true);
	     setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void conectar() throws IOException{
        
		  socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
		  out = socket.getOutputStream();
		  ouw = new OutputStreamWriter(out);
		  bfw = new BufferedWriter(ouw);
		  bfw.write(txtNome.getText()+"\n");
		  bfw.flush();
	}
	
	public void enviarMensagem(String mensagem) throws IOException{
        
	    if(mensagem.equals("Sair")){
	      bfw.write("Desconectado \n");
	      texto.append("Desconectado \n");
	    }else{
	      bfw.write(mensagem +"\r\n");
	      texto.append( txtNome.getText() + " diz: " + txtMsg.getText()+"\n");
	    }
	     bfw.flush();
	     txtMsg.setText("");        
	}
	
	public void escutar() throws IOException{
        
		   InputStream in = socket.getInputStream();
		   InputStreamReader inr = new InputStreamReader(in);
		   BufferedReader bfr = new BufferedReader(inr);
		   String mensagem = "";
		                          
		    while(!"Sair".equalsIgnoreCase(mensagem))
		                                     
		       if(bfr.ready()){
		         mensagem = bfr.readLine();
		       if(mensagem.equals("Sair"))
		         texto.append("Servidor caiu! \n");
		        else
		         texto.append(mensagem +"\n");         
		        }
	}
	
	public void sair() throws IOException{
        
	    enviarMensagem("Sair");
	    bfw.close();
	    ouw.close();
	    out.close();
	    socket.close();
	 }
	
	@Override
	public void actionPerformed(ActionEvent e) {
	         
	  try {
	     if(e.getActionCommand().equals(btnEnviar.getActionCommand()))
	        enviarMensagem(txtMsg.getText());
	     else
	        if(e.getActionCommand().equals(btnSair.getActionCommand()))
	        sair();
	     } catch (IOException f) {
	          f.printStackTrace();
	     }                       
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	               
	    if(e.getKeyCode() == KeyEvent.VK_ENTER){
	       try {
	          enviarMensagem(txtMsg.getText());
	       } catch (IOException f) {
	           f.printStackTrace();
	       }                                                          
	   }                       
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {              
	}
	   
	@Override
	public void keyTyped(KeyEvent arg0) {              
	}           
	
	public static void main(String []args) throws IOException{
        
		   Client c = new Client();
		   c.conectar();
		   c.escutar();
	}
	
	
}
