package Chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Server extends Thread{
	private static ArrayList<BufferedWriter>clientes;           
	  private static ServerSocket server; 
	  private String nome;
	  private Socket skt;
	  private InputStream inS;  
	  private InputStreamReader inR;  
	  private BufferedReader bfR;  
	  
	  public Server(Socket skt){
		   this.skt = skt;
		   try {
		         inS  = skt.getInputStream();
		         inR = new InputStreamReader(inS);
		          bfR = new BufferedReader(inR);
		   } catch (IOException e) {
		          e.printStackTrace();
		   }                          
		} 
	  
	  public void run(){
          
		  try{
		                                     
		    String mensagem;
		    OutputStream ou =  this.skt.getOutputStream();
		    Writer ouw = new OutputStreamWriter(ou);
		    BufferedWriter bfw = new BufferedWriter(ouw); 
		    clientes.add(bfw);
		    nome = mensagem = bfR.readLine();
		              
		    while(!"Sair".equalsIgnoreCase(mensagem) && mensagem != null)
		      {           
		       mensagem = bfR.readLine();
		       sendToAll(bfw, mensagem);
		       System.out.println(mensagem);                                              
		       }
		                                     
		   }catch (Exception e) {
		     e.printStackTrace();
		   
		   }                       
		}  
	  
	  public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException 
	  {
	    BufferedWriter bwS;
	     
	    for(BufferedWriter bw : clientes){
	     bwS = (BufferedWriter)bw;
	     if(!(bwSaida == bwS)){
	       bw.write(nome + ": " + msg+"\n");
	       bw.flush(); 
	     }
	    }          
	  }
	  
	  public static void main(String []args) {
		   
		  try{
		    JLabel lblMessage = new JLabel("Porta: ");
		    JTextField txtPorta = new JTextField("123");
		    Object[] texts = {lblMessage, txtPorta };  
		    JOptionPane.showMessageDialog(null, texts);
		    server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
		    clientes = new ArrayList<BufferedWriter>();
		    JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+         
		    txtPorta.getText());
		   
		     while(true){
		       System.out.println("Aguardando conexão...");
		       Socket con = server.accept();
		       System.out.println("Cliente conectado...");
		       Thread t = new Server(con);
		        t.start();   
		    }
		                             
		  }catch (Exception e) {
		   
		    e.printStackTrace();
		}                       
	}                      
}

