import java.io.*;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Realizar a carga da base de dados");
            System.out.println("2. Ler um registro");
            System.out.println("3. Atualizar um registro");
            System.out.println("4. Deletar um registro");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int option;
            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer
            } catch (Exception e) {
                System.err.println("Entrada inválida. Por favor, insira um número válido.");
                scanner.nextLine(); // Limpar o buffer
                continue;
            }

            switch (option) {
                case 1:
                    System.out.println("Carregando base de dados:");
                    CSVtoBIN.carregar();
                    System.out.println("Base carregada com sucesso");
                    break;
                case 2:
                    CRUD.read();
                    break;
                case 3:
                    CRUD.update();
                    break;
                case 4:
                    CRUD.delete();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
