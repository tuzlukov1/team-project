package pro.sky.teamproject.entity;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animals_dog")
public class AnimalDog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    @Type(type = "org.hibernate.type.TextType")
    private String name;

    @Column(name = "breed")
    @Type(type = "org.hibernate.type.TextType")
    private String breed;

    @Column(name = "age")
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return Objects.equals(id, animalDog.id) && Objects.equals(name, animalDog.name) && Objects.equals(breed, animalDog.breed) && Objects.equals(age, animalDog.age);
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
