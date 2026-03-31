package org.zzu.schoolimsystem.common;

/**
 * ClassName: PageResult
 * Package: org.zzu.schoolimsystem.common
 * Description:
 *
 * @Author gly
 * @Create 2026/3/22 19:46
 * @Version 1.0
 */
import java.util.List;

public class PageResult<T> {
    private List<T> list;
    private Pagination pagination;

    public PageResult() {
    }

    public PageResult(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
