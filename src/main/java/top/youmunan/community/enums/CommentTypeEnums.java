package top.youmunan.community.enums;

public enum CommentTypeEnums {
    QUESTION(1),
    COMMENT(2);

    CommentTypeEnums(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    private Integer type;
}
