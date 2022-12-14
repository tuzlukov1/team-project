package pro.sky.teamproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamproject.entity.UserReport;
import pro.sky.teamproject.repository.UsersReportRepository;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserReportServiceTest {

    @Mock
    private UsersReportRepository usersReportRepository;

    @InjectMocks
    private UserReportService userReportService;

//    @Test
//    public void getUsersMoreThanTwoDaysReportsTest() {
//        final Long id = 1L;
//        final LocalDate reportDate = LocalDate.now().minusDays(2);
//        final String reportText = "text";
//
//
//        UserReport userReport = new UserReport();
//        userReport.setId(id);
//        userReport.setReportDate(reportDate);
//        userReport.setReportText(reportText);
//        when(userReportService.findUsersMoreThanTwoDaysReports())
//                .thenReturn(List.of(userReport));
//
//
//    }
}
