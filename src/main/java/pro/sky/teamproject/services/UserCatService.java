package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.repository.UsersCatRepository;

import java.util.Collection;

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
    public UserCat findUserCatById(Long id) {
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
    public UserCat updateUserCat(UserCat userCat) {
        logger.debug("Was invoked method for edit userCat");
        return usersCatRepository.save(userCat);
    }

    /**
     * Удаление записи о пользователе из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор пользователя, не может быть null
     */
    public void deleteUserCat(long id) {
        logger.debug("Was invoked method for delete userCat");
        usersCatRepository.deleteById(id);
    }

    /**
     * Поиск пользователя в БД по FullName.
     * Исползуется метод репозитория {@link UsersCatRepository#findUserCatByFullNameIgnoreCase(String)}
     * @param fullName имя введенное пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserCat> findUserCatByFullName(String fullName) {
        logger.debug("Was invoked method for get userCat info with full name");
        return usersCatRepository.findUserCatByFullNameIgnoreCase(fullName);
    }

    /**
     * Поиск пользователя в БД по номеру телефона.
     * Исползуется метод репозитория {@link UsersCatRepository#findUserCatByPhone(Long)}
     * @param phone номер телефона введенный пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserCat> findUserCatByPhone(Long phone) {
        logger.debug("Was invoked method for get userCat info with phone number");
        return usersCatRepository.findUserCatByPhone(phone);
    }

    /**
     * Поиск пользователя в БД по айди юзера.
     * Исползуется метод репозитория {@link UsersCatRepository#findUserCatByUserId(Long)}
     * @param userId ид пользователя из таблицы users
     * @return список пользователей в формате JSON
     */
    public Collection<UserCat> findUserCatByUserId(Long userId) {
        logger.debug("Was invoked method for get userCat info with phone number");
        return usersCatRepository.findUserCatByUserId(userId);
    }

    /**
     * Показывает все записи пользователей в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех пользователей из БД, в фомате JSON
     */
    public Collection<UserCat> findAllUsersCat() {
        logger.debug("Was invoked method for find all");
        return usersCatRepository.findAll();
    }
}
