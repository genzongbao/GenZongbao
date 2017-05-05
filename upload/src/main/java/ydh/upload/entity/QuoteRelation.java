package ydh.upload.entity;

import java.util.Date;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

/**
 * 引用关系表
 * @author tearslee
 *
 */
@Entity(name="QUOTE_RELATION")
public class QuoteRelation {
	/**
	 * 引用关系id
	 */
	@Id
	@Column(name="QUOTE_RELATION_ID")
	private String	quoteRelationId;
	/**
	 * 引用源id
	 */
	@Column(name="SOURCE_ID")
	private String  sourceId;
	/**
	 * 引用源类型
	 */
	@Column(name="SOURCE_TYPE")
	private SourceType  sourceType;
	/**
	 * 引用资源id
	 */
	@Column(name="QUOTE_SOURCE_ID")
	private String	quoteSourceId;
	/**
	 * 引用资源名称
	 */
	@Column(name="QUOTE_SOURCE_NAME")
	private String	quoteSourceName;
	/**
	 * 引用资源类型
	 */
	@Column(name="QUOTE_SOURCE_TYPE")
	private QuoteSourceType	quoteSourceType;
	/**
	 * 引用资源后缀
	 */
	@Column(name="QUOTE_SOURCE_SUFFIX")
	private	String	quoteSourceSuffix;
	/**
	 * 引用日期
	 */
	@Column(name="QUOTE_DATE")
	private Date	quoteDate;

	public String getQuoteRelationId() {
		return quoteRelationId;
	}

	public void setQuoteRelationId(String quoteRelationId) {
		this.quoteRelationId = quoteRelationId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getQuoteSourceId() {
		return quoteSourceId;
	}

	public void setQuoteSourceId(String quoteSourceId) {
		this.quoteSourceId = quoteSourceId;
	}

	

	public String getQuoteSourceName() {
		return quoteSourceName;
	}

	public void setQuoteSourceName(String quoteSourceName) {
		this.quoteSourceName = quoteSourceName;
	}

	public QuoteSourceType getQuoteSourceType() {
		return quoteSourceType;
	}

	public void setQuoteSourceType(QuoteSourceType quoteSourceType) {
		this.quoteSourceType = quoteSourceType;
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public String getQuoteSourceSuffix() {
		return quoteSourceSuffix;
	}

	public void setQuoteSourceSuffix(String quoteSourceSuffix) {
		this.quoteSourceSuffix = quoteSourceSuffix;
	}

	
	
	
}
