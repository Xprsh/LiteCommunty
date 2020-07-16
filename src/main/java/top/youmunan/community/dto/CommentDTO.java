package top.youmunan.community.dto;

import lombok.Data;
import top.youmunan.community.model.User;

@Data
public class CommentDTO {
    private Long id;
    private Long parentID;
    private Long type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
}
