package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.OwnershipCats;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OwnershipCatsRepository extends JpaRepository<OwnershipCats, Long> {

    List<OwnershipCats> findOwnershipCatsByEndDateProbation(LocalDate dateProbation);

    List<OwnershipCats> findOwnershipCatsByPassageProbationIgnoreCase(String passageProbation);
}
