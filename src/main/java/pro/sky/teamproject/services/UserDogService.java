package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.repository.UsersDogRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserDogService {

    private final Logger logger = LoggerFactory.getLogger(UserDogService.class);

    private final UsersDogRepository usersDogRepository;

    public UserDogService(UsersDogRepository usersDogRepository) {
        this.usersDogRepository = usersDogRepository;
    }

    /**
     * Поиск пользователя в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id идентификатор пользователя, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public UserDog findUserById(Long id) {
        logger.debug("Was invoked method for get userDog info with id = {} ", id);
        UserDog userDog = usersDogRepository.findById(id).orElse(null);
        logger.warn("Response {} ", userDog);
        return userDog;
    }

    /**
     * Редактирование данных о пользователе в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param userDog данные о пользователе в формате JSON
     * @return измененная запись в базе данных в формате JSON
     */
    public UserDog updateUser(UserDog userDog) {
        logger.debug("Was invoked method for edit userDog");
        return usersDogRepository.save(userDog);
    }

    /**
     * Удаление записи о пользователе из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор пользователя, не может быть null
     */
    public void deleteUser(long id) {
        logger.debug("Was invoked method for delete userDog");
        usersDogRepository.deleteById(id);
    }

    /**
     * Поиск пользователя в БД по идентификатору чата.
     * Исползуется метод репозитория {@link UsersDogRepository#findUserByChatId(Long)}
     * @param id идентификатор чата пользователя, присваивается при регистрации
     *           через телеграм бота, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public Optional<UserDog> findUserByChatId(Long id) {
        logger.debug("Was invoked method for get userDog info with chatId = {} ", id);
        return Optional.ofNullable(usersDogRepository.findUserByChatId(id));
    }

    /**
     * Поиск пользователя в БД по UserName.
     * Исползуется метод репозитория {@link UsersDogRepository#findUsersByUserNameIgnoreCase(String)}
     * @param userName имя пользователя в мессенджере Telegram
     * @return список пользователей в формате JSON
     */
    public Collection<UserDog> findUserByUserName(String userName) {
        logger.debug("Was invoked method for get userDog info with user name");
        return usersDogRepository.findUsersByUserNameIgnoreCase(userName);
    }

    /**
     * Поиск пользователя в БД по FullName.
     * Исползуется метод репозитория {@link UsersDogRepository#findUsersByFullNameIgnoreCase(String)}
     * @param fullName имя введенное пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserDog> findUserByFullName(String fullName) {
        logger.debug("Was invoked method for get userDog info with full name");
        return usersDogRepository.findUsersByFullNameIgnoreCase(fullName);
    }

    /**
     * Поиск пользователя в БД по номеру телефона.
     * Исползуется метод репозитория {@link UsersDogRepository#findUsersByPhone(Long)}
     * @param phone номер телефона введенный пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserDog> findUserByPhone(Long phone) {
        logger.debug("Was invoked method for get userDog info with phone number");
        return usersDogRepository.findUsersByPhone(phone);
    }

    /**
     * Показывает все записи пользователей в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех пользователей из БД, в фомате JSON
     */
    public Collection<UserDog> findAllUsers() {
        logger.debug("Was invoked method for find all");
        return usersDogRepository.findAll();
    }
}
