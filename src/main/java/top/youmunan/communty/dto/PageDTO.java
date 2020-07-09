package top.youmunan.communty.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    private List<QuestionDTO> questions;

    private Integer currentPage;
    private Integer totalPage;

}
