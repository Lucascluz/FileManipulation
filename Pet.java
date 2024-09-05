import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pet {
    // Atributos privados
    int id;
    String name;
    String birthday;
    String breed;
    String species;

    // Construtor
    public Pet(int id, String name, String birthday2, String breed, String species) {
        this.id = id;
        this.name = name;
        this.birthday = birthday2;
        this.breed = breed;
        this.species = species;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Métodos get e set para name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Métodos get e set para date
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    // Métodos get e set para breed
    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    // Métodos get e set para species
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String toString() {
        return "\n ID: " + id +
                "\n Name: " + name +
                "\n Date of birth: " + birthday +
                "\n Breed: " + breed +
                "\n Species: " + species;
    }

    public byte[] toByteArray() {
        // Converte o objeto Pet para um vetor de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(id);
            dos.writeUTF(name);
            dos.writeUTF(birthday);
            dos.writeUTF(breed);
            dos.writeUTF(species);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static Pet fromByteArray(byte[] data) throws IOException {
        // Converte um vetor de bytes para um objeto Pet
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        int id = dis.readInt();
        String name = dis.readUTF();
        String birthday = dis.readUTF();
        String breed = dis.readUTF();
        String species = dis.readUTF();
        return new Pet(id, name, birthday, breed, species);
    }
}