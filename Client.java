import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "127.0.0.1"; // IP do servidor
    private static final int SERVER_PORT = 5000;          // Porta do servidor

    public static void main(String[] args) {
        System.out.println("Conectando ao servidor..."); // [cite: 26]
        
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT); // Conecta ao servidor [cite: 16]
             // Stream para ler do console (entrada do usuário)
             Scanner consoleInput = new Scanner(System.in);
             // Stream para enviar ao servidor
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             // Stream para receber do servidor
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            System.out.println("Conectado. Digite comandos (TIME, STATUS, ECHO <texto>, EXIT):");

            String userInput;
            
            // Loop para enviar múltiplos comandos
            while (true) {
                System.out.print("Digite um comando: "); // [cite: 27, 29, 31]
                userInput = consoleInput.nextLine();

                // Envia a mensagem (comando) para o servidor [cite: 17]
                out.println(userInput); 

                // Aguarda e recebe a resposta do servidor [cite: 19]
                String response = in.readLine();
                System.out.println("Resposta do servidor: " + response); // [cite: 28, 30, 32]

                // Encerra a conexão se o comando for EXIT
                if (userInput.trim().equalsIgnoreCase("EXIT")) {
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + SERVER_HOST);
        } catch (IOException e) {
            System.err.println("Erro de I/O ao conectar/comunicar com o servidor: " + e.getMessage());
        }
        System.out.println("Client encerrado.");
    }
}