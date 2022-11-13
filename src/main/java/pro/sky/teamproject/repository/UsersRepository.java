package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    User findUserByChatId(final Long chatId);
}
