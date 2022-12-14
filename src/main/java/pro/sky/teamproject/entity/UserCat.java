package pro.sky.teamproject.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "users_cat")
public class UserCat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_name")
    @Type(type = "org.hibernate.type.TextType")
    private String fullName;

    @Column(name = "phone")
    private Long phone;

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCat userCat = (UserCat) o;
        return Objects.equals(id, userCat.id) && Objects.equals(userId, userCat.userId) && Objects.equals(fullName, userCat.fullName) && Objects.equals(phone, userCat.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, fullName, phone);
    }

    @Override
    public String toString() {
        return "UserCat{" +
                "id=" + id +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", phone=" + phone +
                '}';
    }
}
