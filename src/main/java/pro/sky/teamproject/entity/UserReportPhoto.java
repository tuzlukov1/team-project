package pro.sky.teamproject.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "users_reports_photo")
public class UserReportPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "report_photo_filepath")
    @Type(type = "org.hibernate.type.TextType")
    private String reportPhotoFilepath;

    @Column(name = "report_photo_filesize")
    private Long reportPhotoFilesize;

    @Column(name = "report_photo_media_type")
    @Type(type = "org.hibernate.type.TextType")
    private String reportPhotoMediaType;

    @Column(name = "user_report_id")
    private Long userReportId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportPhotoMediaType() {
        return reportPhotoMediaType;
    }

    public void setReportPhotoMediaType(String reportPhotoMediaType) {
        this.reportPhotoMediaType = reportPhotoMediaType;
    }

    public Long getReportPhotoFilesize() {
        return reportPhotoFilesize;
    }

    public void setReportPhotoFilesize(Long reportPhotoFilesize) {
        this.reportPhotoFilesize = reportPhotoFilesize;
    }

    public String getReportPhotoFilepath() {
        return reportPhotoFilepath;
    }

    public void setReportPhotoFilepath(String reportPhotoFilepath) {
        this.reportPhotoFilepath = reportPhotoFilepath;
    }

    public Long getUserReportId() {
        return userReportId;
    }

    public void setUserReportId(Long userReportId) {
        this.userReportId = userReportId;
    }
}
