package pl.jarosyjarosy.yougetin.rating.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createDate;
    private Long givingId;
    private Long driverId;
    private Long passengerId;
    private Boolean approval;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getGivingId() {
        return givingId;
    }

    public void setGivingId(Long givingId) {
        this.givingId = givingId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Boolean getApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equal(id, rating.id) &&
                Objects.equal(createDate, rating.createDate) &&
                Objects.equal(givingId, rating.givingId) &&
                Objects.equal(driverId, rating.driverId) &&
                Objects.equal(passengerId, rating.passengerId) &&
                Objects.equal(approval, rating.approval) &&
                Objects.equal(comment, rating.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, createDate, givingId, driverId, passengerId, approval, comment);
    }
}
