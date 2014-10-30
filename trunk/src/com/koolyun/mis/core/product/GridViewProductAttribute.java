package com.koolyun.mis.core.product;

public class GridViewProductAttribute {
	// public static enum AttributeSelectType

	private int type; // 0 父,1子
	private String name;
	private int id;
	private int pid;// -1,表示没有
	private int selectType;// 0，单选。1，多选
	private int statue;// 0，未选中，1，选中
	private ProductSubAttribute attchSubAttribute;

	public GridViewProductAttribute(int type, String name, int id, int pid, int selectType, int statue) {
		super();
		this.type = type;
		this.name = name;
		this.pid = pid;
		this.id = id;
		this.selectType = selectType;
		this.statue = statue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSelectType() {
		return selectType;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

	public int getStatue() {
		return statue;
	}

	public void setStatue(int statue) {
		this.statue = statue;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public ProductSubAttribute getAttchSubAttribute() {
		return attchSubAttribute;
	}

	public void setAttchSubAttribute(ProductSubAttribute attchSubAttribute) {
		this.attchSubAttribute = attchSubAttribute;
	}

}
