package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.entity.UserDog;

import java.util.List;

@Repository
public interface UsersCatRepository extends JpaRepository<UserCat, Long> {

    UserCat findUserCatByFullNameIgnoreCase(String name);

    UserCat findUserCatByPhone(Long phone);

    UserCat findUserCatByUserId(Long id);
}
