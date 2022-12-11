package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.UserReportPhoto;

@Repository
public interface UsersReportPhotoRepository extends JpaRepository<UserReportPhoto, Long> {

    UserReportPhoto findUserReportPhotosByUserReportId(Long userReportId);
}
