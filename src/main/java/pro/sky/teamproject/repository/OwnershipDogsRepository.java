package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.OwnershipDogs;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OwnershipDogsRepository extends JpaRepository<OwnershipDogs, Long> {

    List<OwnershipDogs> findOwnershipDogsByEndDateProbation(LocalDate dateProbation);

    List<OwnershipDogs> findOwnershipDogsByPassageProbationIgnoreCase(String passageProbation);
}
