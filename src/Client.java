import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
public class Client extends Thread {
    // parte que controla a recepção de mensagens do cliente
    private Socket conexao;
    // construtor que recebe o socket do cliente
    public Client(Socket socket) {
        this.conexao = socket;
    }
    public static void main(String args[])
    {
        try {
        	int porta = 1562;
			// conecta a IP do Servidor, Porta
            Socket socket = new Socket("127.0.0.1", porta);
            //Instancia do atributo prs, obtem os objetos para gerenciar comunicação 
            PrintStream prs = new PrintStream(socket.getOutputStream());
            BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome: ");
            String meuNome = bfr.readLine();
            //envia o nome digitado para o servidor
            prs.println(meuNome.toUpperCase());
            //instancia a thread para ip e porta conectados e depois inicia ela
            Thread thread = new Client(socket);
            thread.start();
            //Cria a variavel msg responsavel por enviar a mensagem para o servidor
            String msg;
            // cria linha para digitação da mensagem e a armazena na variavel msg
            System.out.println("Digite o nome que queira votar! ");
            System.out.println("1 - (Marco) para Presidente! ");
            System.out.println("1 - (Leonardo) para Presidente! ");
            System.out.println("1 - (Ricardo) para Presidente! ");
            msg = bfr.readLine();
            prs.println(msg);
            System.out.println("Obrigado pelo seu voto ");
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}