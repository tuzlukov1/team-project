package pro.sky.teamproject.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamproject.entity.UserReport;
import pro.sky.teamproject.repository.UsersReportRepository;
import pro.sky.teamproject.services.UserReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserReportController.class)
public class UserReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersReportRepository usersReportRepository;

    @SpyBean
    private UserReportService userReportService;

    @InjectMocks
    private UserReportController userReportController;


//    @Test
//    public void getUsersMoreThanTwoDaysReportsTest() throws Exception {
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
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/userReports")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(id))
//                .andExpect(jsonPath("$[0].reportDate").value(reportDate))
//                .andExpect(jsonPath("$[0].reportText").value(reportText));
//
//    }
//    @Test
//    public void findAllReportsTest() throws Exception {
//        final Long id = 1L;
//        LocalDate reportDate = LocalDate.now();
//        String reportText = "text";
//
//
//        UserReport userReport = new UserReport();
//        userReport.setId(id);
//        userReport.setReportDate(reportDate);
//        userReport.setReportText(reportText);
//
//        when(usersReportRepository.findUserReportsByUserId(any(Long.class)))
//                .thenReturn(Optional.of(List.of(userReport)));
//
//
//        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
//                .get("/userReports?id=/" + id)
//                .accept(MediaType.APPLICATION_JSON));
//        perform
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(id))
//                .andExpect(jsonPath("$[0].reportDate").value(reportDate))
//                .andExpect(jsonPath("$[0].reportText").value(reportText));
//
//    }


}
