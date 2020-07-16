package top.youmunan.community.dto;

import lombok.Data;
import top.youmunan.community.model.User;

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String detail;
    private String tags;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
