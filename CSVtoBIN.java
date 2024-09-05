import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVtoBIN {
    public static void carregar() {
        String csvFile = "pets.csv";
        String binaryFile = "pets.bin";
        List<Pet> pets = new ArrayList<>();
        int lastId = 0;

        // Ler o arquivo CSV
        try (BufferedReader br = Files.newBufferedReader(Paths.get(csvFile))) {
            String line;
            br.readLine(); // Pular o cabeçalho
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String name = values[1];
                String birthday = values[2];
                String breed = values[3];
                String species = values[4];

                pets.add(new Pet(id, name, birthday, breed, species));
                lastId = id;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Escrever no arquivo binário
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(binaryFile))) {
            // Cabeçalho: quantidade de registros e ID do último registro
            dos.writeInt(pets.size());
            dos.writeInt(lastId);

            for (Pet pet : pets) {
                byte[] petData = pet.toByteArray();
                // Lápide (indicador de exclusão) - 'false' indica que não foi excluído
                dos.writeBoolean(false);
                // Tamanho do registro
                dos.writeInt(petData.length);
                // Dados do registro
                dos.write(petData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Arquivo binário criado com sucesso!");
    }
}