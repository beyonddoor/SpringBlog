package codefun.blog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy= jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Long articleId;
    private Date createTime;
    private String content;
}
