package pro.sky.teamproject.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animals_dog")
public class AnimalDog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String breed;
    private int age;

    public AnimalDog() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnimalDog animalDog = (AnimalDog) o;
        return id == animalDog.id && age == animalDog.age && Objects.equals(name, animalDog.name) && Objects.equals(breed, animalDog.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, breed, age);
    }

    @Override
    public String toString() {
        return "AnimalDog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                '}';
    }
}
