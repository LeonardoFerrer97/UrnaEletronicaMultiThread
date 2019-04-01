import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
public class Server extends Thread {
	private String eleitores[] = {"Marco","Leonardo","Ricardo"};
	public static int votos[];
    // Parte que controla as conexões por meio de threads.
    private static Vector ELEITORES;
    // socket deste cliente
    private Socket conexao;
    // nome deste cliente
    private String nomeCliente;
    // lista que armazena nome de ELEITORES
    private static List LISTA_DE_NOMES = new ArrayList();
    // construtor que recebe o socket deste cliente
    public Server(Socket socket) {
        this.conexao = socket;
    }
    //testa se nomes são iguais, se for retorna true
    public boolean armazena(String newName){
    //   System.out.println(LISTA_DE_NOMES);
       for (int i=0; i < LISTA_DE_NOMES.size(); i++){
         if(LISTA_DE_NOMES.get(i).equals(newName))
           return true;
       }
       //adiciona na lista apenas se não existir
       LISTA_DE_NOMES.add(newName);
       return false;
    }
    //remove da lista os ELEITORES que já deixaram o chat
    public void remove(String oldName) {
       for (int i=0; i< LISTA_DE_NOMES.size(); i++){
         if(LISTA_DE_NOMES.get(i).equals(oldName))
           LISTA_DE_NOMES.remove(oldName);
       }
    }
    public static void main(String args[]) {
        // Vetor com os ELEITORES, eleitores conectados
        ELEITORES = new Vector();
        votos = new int[3];
        votos[0]= 0;
        votos[1]= 0;
        votos[2]= 0;
        try {
        	int porta = 1562;
            // cria um socket que fica escutando a porta 5555.
            ServerSocket server = new ServerSocket(porta);
            System.out.println("ServidorSocket rodando na porta 5555");
            // Loop principal.
            while (true) {
               // Aguardadno conexao
                Socket conexao = server.accept();
                // cria uma nova thread para essa conexão
                Thread t = new Server(conexao);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
    // execução da thread
    public void run()
    {
        try {
            // objetos que permitem controlar a comunicação
            BufferedReader entrada = 
				new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            
			PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            // recebe o nome do cliente
            this.nomeCliente = entrada.readLine();
            //chamada ao metodo que testa nomes iguais
            if (armazena(this.nomeCliente)){
              saida.println("Nome ja existente");
              ELEITORES.add(saida);
              //fecha a conexao com este cliente
              this.conexao.close();
              return;
            } else {
               //mostra o nome do cliente conectado no servidor
               System.out.println(this.nomeCliente + " : Conectado ao Servidor!");
            }
            //igual a null encerra a execução
            if (this.nomeCliente == null) {
                return;
            }
            //adiciona os dados de saida do cliente no objeto ELEITORES
            ELEITORES.add(saida);
            //recebe a mensagem do cliente
            String msg = entrada.readLine();
            
            if (msg.equals("Marco"))votos[0]++;
            else if (msg.equals("Leonardo")) votos[1]++;
            else if (msg.equals("Ricardo")) votos[2]++;
            System.out.println("Marco tem :"+votos[0]);
            System.out.println("Leonardo tem :"+votos[1]);
            System.out.println("Ricardo tem :"+votos[2]);
            
            //se cliente enviar linha em branco, mostra a saida no servidor
            System.out.println(this.nomeCliente + " saiu da Urna!");
            //remove nome da lista
            remove(this.nomeCliente);
            ELEITORES.remove(saida);
            //fecha a conexao com o cliente
            this.conexao.close();
        } catch (IOException e) {
            // Caso ocorra alguma excessão de E/S, mostre qual foi.
            System.out.println("Falha na Conexao... .. ."+" IOException: " + e);
        }
    }
    // enviar uma mensagem para todos, menos para o próprio
    public void sendToAll(PrintStream saida, String acao, String msg) throws IOException {
        Enumeration e = ELEITORES.elements();
        while (e.hasMoreElements()) {
            // obtém o fluxo de saída de um dos ELEITORES
            PrintStream chat = (PrintStream) e.nextElement();
            // envia para todos, menos para o próprio usuário
            if (chat != saida) {
                chat.println(this.nomeCliente + acao + msg);
            }
        }
      }
    public static void leitor(String path) throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        String linha = "";
        while (true) {
            if (linha != null) {
                System.out.println(linha);
            } else
                break;
            linha = buffRead.readLine();
        }
        buffRead.close();
    }
 
    public static void escritor(String path,String voto) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        buffWrite.append(voto + "\n");
        buffWrite.close();
    }
 
}