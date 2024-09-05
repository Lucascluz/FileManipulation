import java.io.*;
import java.util.Scanner;

public class CRUD {

    static Scanner scanner = new Scanner(System.in);
    static String binaryFile = "pets.bin";

    public static void read() {
        System.out.print("Informe o ID do registro: ");
        int searchId = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        try (DataInputStream dis = new DataInputStream(new FileInputStream(binaryFile))) {
            // Ignorar o cabeçalho
            int numberOfRecords = dis.readInt();
            @SuppressWarnings("unused")
            int lastId = dis.readInt();

            boolean found = false;
            for (int i = 0; i < numberOfRecords; i++) {
                boolean isDeleted = dis.readBoolean(); // Lê a lápide
                int recordSize = dis.readInt(); // Lê o tamanho do registro
                byte[] recordData = new byte[recordSize];
                dis.readFully(recordData); // Lê os dados do registro

                if (!isDeleted) {
                    Pet pet = Pet.fromByteArray(recordData);
                    if (pet.getId() == searchId) {
                        System.out.println(pet.toString()); // Retorna o registro se o ID corresponder
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                System.out.println("Registro com ID " + searchId + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    static void update() {
        System.out.print("Informe o ID do registro a ser atualizado: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        // Ler os novos dados do usuário
        System.out.print("Novo nome: ");
        String newName = scanner.nextLine();
        System.out.print("Nova data de nascimento: ");
        String newBirthday = scanner.nextLine();
        System.out.print("Nova raça: ");
        String newBreed = scanner.nextLine();
        System.out.print("Nova espécie: ");
        String newSpecies = scanner.nextLine();

        try (RandomAccessFile raf = new RandomAccessFile(binaryFile, "rw")) {
            // Cabeçalho
            int numberOfRecords = raf.readInt();
            int lastId = raf.readInt();

            long pointer = raf.getFilePointer();
            boolean found = false;

            for (int i = 0; i < numberOfRecords; i++) {
                boolean isDeleted = raf.readBoolean();
                int recordSize = raf.readInt();
                long recordStart = raf.getFilePointer();
                byte[] recordData = new byte[recordSize];
                raf.readFully(recordData);

                if (!isDeleted) {
                    Pet pet = Pet.fromByteArray(recordData);
                    if (pet.getId() == id) {
                        found = true;

                        // Criar um novo Pet com as informações atualizadas
                        Pet updatedPet = new Pet(id, newName, newBirthday, newBreed, newSpecies);
                        byte[] updatedData = updatedPet.toByteArray();

                        if (updatedData.length <= recordSize) {
                            // Sobrescrever o registro antigo com o novo se couber
                            raf.seek(recordStart);
                            raf.write(updatedData);
                        } else {
                            // Marcar o registro antigo como excluído
                            raf.seek(pointer);
                            raf.writeBoolean(true);

                            // Ir para o final do arquivo para escrever o novo registro
                            raf.seek(raf.length());
                            raf.writeBoolean(false); // Lápide do novo registro
                            raf.writeInt(updatedData.length);
                            raf.write(updatedData);
                            numberOfRecords++;
                            raf.seek(0); // Atualizar cabeçalho
                            raf.writeInt(numberOfRecords);
                            raf.writeInt(lastId);
                        }
                        System.out.println("Registro atualizado com sucesso.");
                        break;
                    }
                }
                pointer = raf.getFilePointer();
            }

            if (!found) {
                System.out.println("Registro não encontrado.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo: " + e.getMessage());
        }
    }

    static void delete() {
        System.out.print("Informe o ID do registro a ser deletado: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        try (RandomAccessFile raf = new RandomAccessFile(binaryFile, "rw")) {
            // Cabeçalho
            int numberOfRecords = raf.readInt();
            @SuppressWarnings("unused")
            int lastId = raf.readInt();

            long pointer = raf.getFilePointer();
            boolean found = false;

            for (int i = 0; i < numberOfRecords; i++) {
                boolean isDeleted = raf.readBoolean();
                int recordSize = raf.readInt();
                long recordStart = raf.getFilePointer();
                raf.skipBytes(recordSize);

                if (!isDeleted) {
                    // Lê o registro
                    raf.seek(recordStart);
                    byte[] recordData = new byte[recordSize];
                    raf.readFully(recordData);
                    Pet pet = Pet.fromByteArray(recordData);

                    // Verifica se o ID corresponde
                    if (pet.getId() == id) {
                        // Marcar como excluído
                        raf.seek(pointer);
                        raf.writeBoolean(true); // Marcar lápide como excluída
                        found = true;
                        System.out.println("Registro excluído com sucesso.");
                        break;
                    }
                }
                pointer = raf.getFilePointer();
            }

            if (!found) {
                System.out.println("Registro não encontrado.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao excluir o registro: " + e.getMessage());
        }
    }
}
