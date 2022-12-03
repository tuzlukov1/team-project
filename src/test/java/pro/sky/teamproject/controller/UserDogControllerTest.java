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
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.repository.UsersCatRepository;
import pro.sky.teamproject.repository.UsersDogRepository;
import pro.sky.teamproject.services.UserCatService;
import pro.sky.teamproject.services.UserDogService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserDogController.class)
class UserDogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersDogRepository usersDogRepository;

    @SpyBean
    private UserDogService userDogService;

    @InjectMocks
    private UserDogController userDogController;

    @Test
    public void getUserInfoPositiveTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserDog user = new UserDog();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersDogRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value(fullName))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @Test
    public void getUserInfoNegativeTest() throws Exception {
        final long id = 10L;

        when(usersDogRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog/" + id)
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

        UserDog user = new UserDog();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersDogRepository.save(any(UserDog.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/userDog")
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
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserDog user = new UserDog();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersDogRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/userDog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserNegativeTest() throws Exception {
        final long id = 10L;

        when(usersDogRepository.findById(ArgumentMatchers.eq(id)))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/userDog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void findUserByUserNameTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserDog user = new UserDog();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersDogRepository.findUserDogByFullNameIgnoreCase(any(String.class)))
                .thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog?fullName=" + fullName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].fullName").value(fullName))
                .andExpect(jsonPath("$[0].phone").value(phone));
    }

    @Test
    public void findUserByFullNameTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserDog user = new UserDog();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersDogRepository.findUserDogByFullNameIgnoreCase(any(String.class)))
                .thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog?fullName=" + fullName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].fullName").value(fullName))
                .andExpect(jsonPath("$[0].phone").value(phone));
    }

    @Test
    public void findUserByByPhoneTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserDog user = new UserDog();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersDogRepository.findUserDogByPhone(any(Long.class)))
                .thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog?phone=" + phone)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].fullName").value(fullName))
                .andExpect(jsonPath("$[0].phone").value(phone));
    }

    @Test
    public void findUserFindAllUsersPositiveTest() throws Exception {
        final Long id = 1L;
        final String fullName = "User";
        final Long phone = 89994561122L;

        UserDog user = new UserDog();
        user.setId(id);
        user.setFullName(fullName);
        user.setPhone(phone);

        when(usersDogRepository.findAll())
                .thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].fullName").value(fullName))
                .andExpect(jsonPath("$[0].phone").value(phone));
    }

    @Test
    public void findUserFindAllUsersNegativeTest() throws Exception {
        when(usersDogRepository.findAll())
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .equals(Collections.EMPTY_LIST);
    }
}