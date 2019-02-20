/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.bean;

/**
 * @author Administrator
 *
 */
public class Page {
	private int thisPage = 1;
	private int startRow;
	private int pageRows = 11;
	private int totalRows;
	private int totalPages;
	private String namePart;

	

	public Page() {}
	public Page(int thisPage) {
		super();
		this.thisPage = thisPage;
	}
	
	
	/**
	 * @return the thisPage
	 */
	public int getThisPage() {
		return thisPage;
	}
	/**
	 * @param thisPage the thisPage to set
	 */
	public void setThisPage(int thisPage) {
		this.thisPage = thisPage;
	}
	/**
	 * @return the startRow
	 */
	public int getStartRow() {
		return (this.thisPage-1)*this.pageRows;
	}
	/**
	 * @param startRow the startRow to set
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	/**
	 * @return the pageRows
	 */
	public int getPageRows() {
		return pageRows;
	}
	/**
	 * @param pageRows the pageRows to set
	 */
	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}
	/**
	 * @return the totalRows
	 */
	public int getTotalRows() {
		return totalRows;
	}
	/**
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return (int)Math.ceil((totalRows*1.0/pageRows));
	}
	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}


	/**
	 * @return the namePart
	 */
	public String getNamePart() {
		return namePart;
	}

	/**
	 * @param namePart the namePart to set
	 */
	public void setNamePart(String namePart) {
		this.namePart = namePart;
	}

}
