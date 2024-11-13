package blog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Article {
    @Id
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long createTime;
    private int readCount;
    private int likeCount;
}
