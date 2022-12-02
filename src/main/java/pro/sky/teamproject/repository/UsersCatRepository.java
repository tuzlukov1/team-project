package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.UserCat;

import java.util.List;

@Repository
public interface UsersCatRepository extends JpaRepository<UserCat, Long> {

    UserCat findUserByChatId(final Long chatId);

    List<UserCat> findUsersByUserNameIgnoreCase(String name);

    List<UserCat> findUsersByFullNameIgnoreCase(String name);

    List<UserCat> findUsersByPhone(Long phone);
}
