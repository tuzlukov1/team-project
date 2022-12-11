package pro.sky.teamproject.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name")
    @Type(type = "org.hibernate.type.TextType")
    private String userName;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "start_registration")
    private Boolean startRegistration;

    @Column(name = "start_report")
    private Boolean startReport;

    @Column(name = "have_warning")
    private Boolean haveWarning;

    public Boolean getHaveWarning() {
        return haveWarning;
    }

    public void setHaveWarning(Boolean haveWarning) {
        this.haveWarning = haveWarning;
    }

    public Boolean getStartReport() {
        return startReport;
    }

    public void setStartReport(Boolean startReport) {
        this.startReport = startReport;
    }

    public Boolean getStartRegistration() {
        return startRegistration;
    }

    public void setStartRegistration(Boolean startRegistration) {
        this.startRegistration = startRegistration;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
