package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.Animal;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByNameIgnoreCase(String name);

    List<Animal> findByBreedIgnoreCase(String breed);

    List<Animal> findByAge(Integer age);

}
