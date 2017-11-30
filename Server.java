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
	private Socket cons;
	private InputStream inS;
	private InputStreamReader inR;
	private BufferedReader bfR;

	public Server(Socket cons){
		this.cons = cons;

		try{
			inS = cons.getInputStream();
			inR = new InputStreamReader(inS);
			bfR = new BufferedReader(inR);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void run(){
		try{
			String mensagem;
			OutputStream ou = this.cons.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfW = new BufferedWriter(ouw);
			clientes.add(bfW);
			nome = mensagem = bfR.readLine();
			while(!"Sair".equalsIgnoreCase(mensagem) && mensagem != null){
				mensagem = bfR.readLine();
				mandaMensagem(bfW, mensagem);
				System.out.println(mensagem);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void mandaMensagem(BufferedWriter bwSaida, String mensagem) throws IOException{
		BufferedWriter bwS;

		for(BufferedWriter bw: clientes){
			bwS = (BufferedWriter)bw;
			if(!(bwSaida == bwS)){
				bw.write(nome + ": " + mensagem + "\r\n");
				bw.flush(); //flush força o dado a ser "gravado" no disco
			}
		}

	}


	public static void main(String[]args){
		try{
			JLabel labelMessage = new JLabel("Porta: ");
			JTextField txtPorta = new JTextField("12345");
			Object[] textos = {labelMessage, txtPorta};
			JOptionPane.showMessageDialog(null, textos);
			server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
			clientes = new ArrayList<BufferedWriter>();
			JOptionPane.showMessageDialog(null,"Servidor ativo na porta " + txtPorta.getText());

			while(true){
				System.out.println("Aguardando conexão...");
				Socket cons = server.accept();
				System.out.println("Cliente conectando...");
				Thread t = new Server(cons);
				t.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
