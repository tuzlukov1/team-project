package pro.sky.teamproject.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animals_cat")
public class AnimalCat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String breed;
    private int age;

    public AnimalCat() {

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
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnimalCat animalCat = (AnimalCat) o;
        return id == animalCat.id && age == animalCat.age && Objects.equals(name, animalCat.name) && Objects.equals(breed, animalCat.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, breed, age);
    }

    @Override
    public String toString() {
        return "AnimalCat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                '}';
    }
}

