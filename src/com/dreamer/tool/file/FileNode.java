package com.dreamer.tool.file;

import java.io.File;
public class FileNode{

	
	private int id;
	private String name;
	private boolean isSelected;
	private int parentId;
	private File file;
	
	public FileNode(int id,String name,boolean isSelected,int parentId,File file){
		
		this.id = id;
		this.name = name;
		this.isSelected = isSelected;
		this.parentId = parentId;
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public int getParentId() {
		return parentId;
	}
	
	public String toString(){
		return "Name is : "+name;
	}
}
