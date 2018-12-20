package com.yinsin.simter.modal.base;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;

public class PageImpl {

	private boolean first = false;
	private boolean last = false;
	private int number = 0;
	private int numberOfElements = 0;
	private int size = 10;
	private long totalElements = 0;
	private int totalPages = 0;
	private List<?> content = new ArrayList<Object>();
	
	public PageImpl(){
		
	}
	
	public PageImpl(Page<?> value){
		this.totalPages = value.getPages();
		this.totalElements = value.getTotal();
		this.size = value.getPageSize();
		if(this.totalElements > 0 && this.totalPages == 0){
			this.totalPages = (int) ((this.totalElements/this.size) + ((this.totalElements%this.size) > 0 ? 1 : 0));
		}
		this.number = value.getPageNum() > 0 ? value.getPageNum() - 1 : 0;
		this.numberOfElements = value.getResult() != null ? value.getResult().size() : 0;
		this.first = number == 0;
		this.last = totalPages == 0 || number == totalPages - 1;
		this.content = value.getResult();
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<?> getContent() {
		return content;
	}

	public void setContent(List<?> content) {
		this.content = content;
	}
}
