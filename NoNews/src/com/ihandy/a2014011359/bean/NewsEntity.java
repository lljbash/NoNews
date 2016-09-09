package com.ihandy.a2014011359.bean;

import java.io.Serializable;
import java.util.List;

public class NewsEntity implements Serializable, Comparable {
	/** 新闻类别 ID */
	private Integer newsCategoryId;
	/** 新闻类型 */
	private String newsCategory;
	/** 新闻ID */
	private Long newsId;
	/** 标题 */
	private String title;
	/** 新闻源 */
	private String source;
	/** 新闻源地址 URL */
	private String source_url;
	/** 图片1 URL */
	private String picOne;
	/** 图片2 URL */
	private String picTwo;
	/** 图片3 URL */
	private String picThr;
	/** 图片 列表 */
	private List<String> picList;
	/** 图片类型是否为大图 */
	private Boolean isLarge;
	/** 收藏状态 */
	private Boolean collectStatus;

	public Integer getNewsCategoryId() {
		return newsCategoryId;
	}

	public void setNewsCategoryId(Integer newsCategoryId) {
		this.newsCategoryId = newsCategoryId;
	}

	public String getNewsCategory() {
		return newsCategory;
	}

	public void setNewsCategory(String newsCategory) {
		this.newsCategory = newsCategory;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPicOne() {
		return picOne;
	}

	public void setPicOne(String picOne) {
		this.picOne = picOne;
	}

	public String getPicTwo() {
		return picTwo;
	}

	public void setPicTwo(String picTwo) {
		this.picTwo = picTwo;
	}

	public String getPicThr() {
		return picThr;
	}

	public void setPicThr(String picThr) {
		this.picThr = picThr;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public Boolean getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(Boolean collectStatus) {
		this.collectStatus = collectStatus;
	}

	public Boolean getIsLarge() {
		return isLarge;
	}

	public void setIsLarge(Boolean isLarge) {
		this.isLarge = isLarge;
	}

	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	@Override
	public int compareTo(Object o) {
		NewsEntity n = (NewsEntity) o;
		if (newsId < n.newsId) {
			return 1;
		} else if (newsId == n.newsId) {
			return 0;
		} else {
			return -1;
		}
	}
}
