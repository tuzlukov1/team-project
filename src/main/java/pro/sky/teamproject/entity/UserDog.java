package pro.sky.teamproject.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users_dog")
public class UserDog extends User{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "user_id")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDog userDog = (UserDog) o;
        return Objects.equals(id, userDog.id) && Objects.equals(fullName, userDog.fullName) && Objects.equals(phone, userDog.phone) && Objects.equals(userId, userDog.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, phone, userId);
    }

    @Override
    public String toString() {
        return "UserDog{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone=" + phone +
                ", userId=" + userId +
                '}';
    }
}
