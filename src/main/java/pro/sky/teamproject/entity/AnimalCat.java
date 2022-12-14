package pro.sky.teamproject.entity;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animals_cat")
public class AnimalCat {

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
        AnimalCat animalCat = (AnimalCat) o;
        return Objects.equals(id, animalCat.id) && Objects.equals(name, animalCat.name) && Objects.equals(breed, animalCat.breed) && Objects.equals(age, animalCat.age);
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

