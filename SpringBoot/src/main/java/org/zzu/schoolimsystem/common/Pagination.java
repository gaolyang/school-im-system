package org.zzu.schoolimsystem.common;

/**
 * ClassName: pagination
 * Package: org.zzu.schoolimsystem.common
 * Description:
 *
 * @Author gly
 * @Create 2026/3/22 19:45
 * @Version 1.0
 */
public class Pagination {
    private Integer page;
    private Integer page_size;
    private Long total;
    private Integer total_pages;

    public Pagination() {
    }

    public Pagination(Integer page, Integer pageSize, Long total, Integer totalPages) {
        this.page = page;
        this.page_size = pageSize;
        this.total = total;
        this.total_pages = totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }
}
