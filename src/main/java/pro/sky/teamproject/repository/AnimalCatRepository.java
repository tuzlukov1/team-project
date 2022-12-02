package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.AnimalCat;

import java.util.List;

@Repository
public interface AnimalCatRepository extends JpaRepository<AnimalCat, Long> {

    List<AnimalCat> findByNameIgnoreCase(String name);

    List<AnimalCat> findByBreedIgnoreCase(String breed);

    List<AnimalCat> findByAge(Integer age);

}
