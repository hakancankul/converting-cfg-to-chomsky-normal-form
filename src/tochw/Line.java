package tochw;

import java.util.ArrayList;

public class Line {
	
	private String name;
	private ArrayList<String> elements;
	
	
	public Line(String name, ArrayList<String> elements) {
		
		this.name = name;
		this.elements = elements;
	}
	
	
	public void deleteElement(String element) {
		elements.remove(element);
	}
	
	public void addElement(String element) {
		elements.add(element);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<String> getElements() {
		return elements;
	}


	public void setElements(ArrayList<String> elements) {
		this.elements = elements;
	}
	
	
	

}
