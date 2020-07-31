package life.java.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


//页面数的信息
@Data
public class PaginationDto {
    private List<QuestionDTO> questions;
    private boolean showPrevious;//上一页按钮
    private boolean showFirstPage;//首页按钮
    private boolean showNext;//下一页
    private boolean showEndPage;//尾页
    private Integer page;//当前页面数
    private List<Integer> pages = new ArrayList<>();//页面数列
    private Integer totalpage;//总页数

    public void setPagination(Integer totalcount, Integer page, Integer size) {
        this.page=page;

        if (totalcount % size == 0) {
            totalpage = totalcount / size;
        } else {
            totalpage = (totalcount / size) + 1;
        }

        if(page<1){
            page=1;
        }
        if(page>totalpage){
            page=totalpage;
        }

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            //若当前是第4页 判断页面1234567的123页是否加到列
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            //若当前是第4页 判断页面1234567的567页是否加到列
            if (page + i <= totalpage) {
                pages.add(page + i);
            }
        }

        //上下页按钮
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        if (page == totalpage) {
            showNext = false;
        } else {
            showNext = true;
        }

        //首尾页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        if (pages.contains(totalpage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
