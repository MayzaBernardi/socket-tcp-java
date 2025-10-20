import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT + "."); // [cite: 34]
            
            // Loop principal para aceitar múltiplos clientes
            while (true) {
                // Bloqueia até que um cliente se conecte, retornando um novo Socket
                Socket clientSocket = serverSocket.accept(); 
                
                // Cria e inicia uma nova thread para lidar com o cliente
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}

// Classe Thread para lidar com cada cliente separadamente
class ClientHandler extends Thread {
    private Socket clientSocket;
    
    // Streams de comunicação
    private BufferedReader in;  // Para receber do cliente
    private PrintWriter out;    // Para enviar ao cliente

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Inicializa as streams de entrada e saída
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true); // 'true' para auto-flush
            
            String clientAddress = clientSocket.getInetAddress().getHostAddress();
            System.out.println("Cliente conectado (" + clientAddress + ")"); // [cite: 35]

            String clientCommand;
            
            // Loop de comunicação com o cliente
            while ((clientCommand = in.readLine()) != null) {
                System.out.println("Comando recebido: " + clientCommand); // [cite: 36, 37, 38]
                String response = processCommand(clientCommand);
                out.println(response); // Envia a resposta de volta ao cliente

                if (clientCommand.trim().equalsIgnoreCase("EXIT")) { // [cite: 23]
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro de comunicação com o cliente: " + e.getMessage());
        } finally {
            try {
                // Encerramento adequado da conexão e dos recursos
                if (clientSocket != null) {
                    clientSocket.close();
                    System.out.println("Conexão encerrada."); // [cite: 39]
                }
            } catch (IOException e) {
                System.err.println("Erro ao fechar o socket: " + e.getMessage());
            }
        }
    }

    // Método para interpretar o comando e gerar a resposta [cite: 13, 22]
    private String processCommand(String command) {
        String upperCommand = command.trim().toUpperCase();

        if (upperCommand.equals("TIME")) { // [cite: 23, 48]
            // Retorna o horário atual do servidor
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            return "Hora atual: " + dtf.format(LocalDateTime.now()); // [cite: 23]

        } else if (upperCommand.equals("STATUS")) { // [cite: 23, 48]
            // Retorna o estado do servidor
            return "Servidor ativo e aguardando conexões"; // [cite: 23]

        } else if (upperCommand.startsWith("ECHO ")) { // [cite: 23, 48]
            // Retorna o texto enviado após 'ECHO '
            return command.substring("ECHO ".length()).trim(); // [cite: 23]

        } else if (upperCommand.equals("EXIT")) { // [cite: 23, 48]
            // Encerra a conexão
            return "Conexão encerrada"; // [cite: 23]
        } else {
            // Validação: Retorna mensagem de erro para comandos desconhecidos [cite: 45]
            return "ERRO: Comando desconhecido ou formato inválido."; 
        }
    }
}