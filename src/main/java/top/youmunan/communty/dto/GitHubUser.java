package top.youmunan.communty.dto;

/**
 * GitHub用户信息
 */
public class GitHubUser {
    private String login;
    private Long id;
    private String bio;

    @Override
    public String toString() {
        return "GitHubUser{" +
                "login='" + login + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String name) {
        this.login = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
