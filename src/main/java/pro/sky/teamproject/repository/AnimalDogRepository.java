package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.AnimalDog;

import java.util.List;

@Repository
public interface AnimalDogRepository extends JpaRepository<AnimalDog, Long> {

    List<AnimalDog> findByNameIgnoreCase(String name);

    List<AnimalDog> findByBreedIgnoreCase(String breed);

    List<AnimalDog> findByAge(Integer age);
}
