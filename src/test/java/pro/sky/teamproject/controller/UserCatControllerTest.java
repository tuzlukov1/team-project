package pro.sky.teamproject.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.repository.UsersCatRepository;
import pro.sky.teamproject.services.UserCatService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserCatController.class)
class UserCatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersCatRepository usersCatRepository;

    @SpyBean
    private UserCatService userCatService;

    @InjectMocks
    private UserCatController userCatController;

    @Test
    public void getUserInfoPositiveTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserCat user = new UserCat();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersCatRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userCat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value(fullName))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @Test
    public void getUserInfoNegativeTest() throws Exception {
        final long id = 10L;

        when(usersCatRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userCat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editUserTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        JSONObject userCatObject = new JSONObject();
        userCatObject.put("id", id);
        userCatObject.put("fullName", fullName);
        userCatObject.put("phone", phone);

        UserCat user = new UserCat();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);


        when(usersCatRepository.save(any(UserCat.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/userCat")
                        .content(userCatObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value(fullName))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @Test
    public void deleteUserPositiveTest() throws Exception {
        final Long id = 1L;
        final String userName = "UserCat";
        final Long chatId = 123456L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserCat user = new UserCat();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersCatRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/userCat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserNegativeTest() throws Exception {
        final long id = 10L;

        when(usersCatRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/userCat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserByUserNameTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserCat user = new UserCat();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersCatRepository.findUserCatByFullNameIgnoreCase(any(String.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userCat?fullName=" + fullName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value(fullName))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @Test
    public void findUserByFullNameTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserCat user = new UserCat();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersCatRepository.findUserCatByFullNameIgnoreCase(any(String.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userCat?fullName=" + fullName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value(fullName))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @Test
    public void findUserByByPhoneTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserCat user = new UserCat();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersCatRepository.findUserCatByPhone(any(Long.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userCat?phone=" + phone)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value(fullName))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @Test
    public void findUserFindAllUsersPositiveTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserCat user = new UserCat();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersCatRepository.findAll())
                .thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userCat")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value(fullName))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @Test
    public void findUserFindAllUsersNegativeTest() throws Exception {
        when(usersCatRepository.findAll())
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userCat")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .equals(Collections.EMPTY_LIST);
    }
}