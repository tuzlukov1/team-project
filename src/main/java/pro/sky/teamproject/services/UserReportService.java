package pro.sky.teamproject.services;

import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.UserReport;
import pro.sky.teamproject.repository.UsersReportRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserReportService {

    private final UsersReportRepository usersReportRepository;

    public UserReportService(UsersReportRepository usersReportRepository) {
        this.usersReportRepository = usersReportRepository;
    }

    public Optional<List<UserReport>> findUserReportsByUserId(Long userId) {
        return usersReportRepository.findUserReportsByUserId(userId);
    }

    public UserReport updateReport(UserReport userReport) {
        return usersReportRepository.save(userReport);
    }
}
