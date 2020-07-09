package top.youmunan.communty.dto;

import lombok.Data;

/**
 * GitHub用户信息
 */
@Data
public class GitHubUser {
    private String login;
    private Long id;
    private String bio;
    private String avatarUrl;
}
