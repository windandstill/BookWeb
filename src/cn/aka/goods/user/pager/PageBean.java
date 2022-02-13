package cn.aka.goods.user.pager;

import java.util.List;

/**
 * 分页Bean，它会在各层之间传递！
 */
public class PageBean<T> {
	private int pageNow;//当前页码
	private int totalRecords;//总记录数
	private int pageSize;//每页记录数
	private String url;//请求路径和参数，例如：/BookServlet?method=findXXX&cid=1&bname=2
	private List<T> beanList;

	// 计算总页数
	public int getPageCount() {
		int pageCount = totalRecords / pageSize;
		return totalRecords % pageSize == 0 ? pageCount : pageCount + 1;
	}

	public int getPageNow() { return pageNow; }

	public void setPageNow(int pageNow) { this.pageNow = pageNow; }

	public int getTotalRecords() { return totalRecords; }

	public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }

	public int getPageSize() { return pageSize; }

	public void setPageSize(int pageSize) { this.pageSize = pageSize; }

	public String getUrl() { return url; }

	public void setUrl(String url) { this.url = url; }

	public List<T> getBeanList() { return beanList; }

	public void setBeanList(List<T> beanList) { this.beanList = beanList; }
}