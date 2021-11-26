package com.example.consumer.model.core;

public class PageQuery {

	private Integer offset = 0;

	private Integer limit = 20;

	private Integer pageNumber = 1;

	private Integer page;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPage() {
		if (limit == null || pageNumber == null) {
			return null;
		}
		return (pageNumber - 1) * limit;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

}