package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.repository.UsersCatRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserCatService {

    private final Logger logger = LoggerFactory.getLogger(UserCatService.class);

    private final UsersCatRepository usersCatRepository;

    public UserCatService(UsersCatRepository usersCatRepository) {
        this.usersCatRepository = usersCatRepository;
    }

    /**
     * Поиск пользователя в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id идентификатор пользователя, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public UserCat findUserById(Long id) {
        logger.debug("Was invoked method for get userDog info with id = {} ", id);
        UserCat userCat = usersCatRepository.findById(id).orElse(null);
        logger.warn("Response {} ", userCat);
        return userCat;
    }

    /**
     * Редактирование данных о пользователе в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param userCat данные о пользователе в формате JSON
     * @return измененная запись в базе данных в формате JSON
     */
    public UserCat updateUser(UserCat userCat) {
        logger.debug("Was invoked method for edit userCat");
        return usersCatRepository.save(userCat);
    }

    /**
     * Удаление записи о пользователе из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор пользователя, не может быть null
     */
    public void deleteUser(long id) {
        logger.debug("Was invoked method for delete userCat");
        usersCatRepository.deleteById(id);
    }

    /**
     * Поиск пользователя в БД по идентификатору чата.
     * Исползуется метод репозитория {@link UsersCatRepository#findUserByChatId(Long)}
     * @param id идентификатор чата пользователя, присваивается при регистрации
     *           через телеграм бота, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public Optional<UserCat> findUserByChatId(Long id) {
        logger.debug("Was invoked method for get userCat info with chatId = {} ", id);
        return Optional.ofNullable(usersCatRepository.findUserByChatId(id));
    }

    /**
     * Поиск пользователя в БД по UserName.
     * Исползуется метод репозитория {@link UsersCatRepository#findUsersByUserNameIgnoreCase(String)}
     * @param userName имя пользователя в мессенджере Telegram
     * @return список пользователей в формате JSON
     */
    public Collection<UserCat> findUserByUserName(String userName) {
        logger.debug("Was invoked method for get userCat info with user name");
        return usersCatRepository.findUsersByUserNameIgnoreCase(userName);
    }

    /**
     * Поиск пользователя в БД по FullName.
     * Исползуется метод репозитория {@link UsersCatRepository#findUsersByFullNameIgnoreCase(String)}
     * @param fullName имя введенное пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserCat> findUserByFullName(String fullName) {
        logger.debug("Was invoked method for get userCat info with full name");
        return usersCatRepository.findUsersByFullNameIgnoreCase(fullName);
    }

    /**
     * Поиск пользователя в БД по номеру телефона.
     * Исползуется метод репозитория {@link UsersCatRepository#findUsersByPhone(Long)}
     * @param phone номер телефона введенный пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserCat> findUserByPhone(Long phone) {
        logger.debug("Was invoked method for get userCat info with phone number");
        return usersCatRepository.findUsersByPhone(phone);
    }

    /**
     * Показывает все записи пользователей в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех пользователей из БД, в фомате JSON
     */
    public Collection<UserCat> findAllUsers() {
        logger.debug("Was invoked method for find all");
        return usersCatRepository.findAll();
    }
}
