package pro.sky.teamproject.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users_cat")
public class UserCat extends User{

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
        UserCat userCat = (UserCat) o;
        return Objects.equals(id, userCat.id) && Objects.equals(fullName, userCat.fullName) && Objects.equals(phone, userCat.phone) && Objects.equals(userId, userCat.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, phone, userId);
    }

    @Override
    public String toString() {
        return "UserCat{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone=" + phone +
                ", userId=" + userId +
                '}';
    }
}
