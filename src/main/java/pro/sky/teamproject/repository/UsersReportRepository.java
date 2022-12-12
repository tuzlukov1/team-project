package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.UserReport;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersReportRepository extends JpaRepository<UserReport, Long> {

    Optional<UserReport> findUserReportById(Long id);
    Optional<List<UserReport>> findUserReportsByUserId(Long id);

}
