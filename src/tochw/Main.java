package tochw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;

public class Main {

	//traverse the entire table print the values 
	public static void printTable(ArrayList<Line> lines) {
		for (Line line : lines) {
			String lineName=line.getName();
			ArrayList<String> lineElements=line.getElements();
			System.out.print(lineName+"-");
			
			for (int i = 0; i < lineElements.size(); i++) {
				//array_type array_element = array[i];
				System.out.print(lineElements.get(i));
				if(i!=lineElements.size()-1) //||!lineElements.get(i+1).equals("")
					if(!lineElements.get(i+1).equals(""))
						System.out.print("|");
				
			}
			System.out.println();
		}
	}

	//traverse the entire table delete the given value 
	public static String deleteElement(ArrayList<Line> lines,String elementToDelete) {
		for (Line line : lines) {
			String lineName=line.getName();
			ArrayList<String> lineElements=line.getElements();
			
			for (String element : lineElements ) {
				
				if(element.equals(elementToDelete)) {
					line.deleteElement(elementToDelete);
					return lineName;
				}
			}
		}
		return null;
	}
	
	
	//traverse the entire table check if the searched value exists
	public static boolean isContainElement(ArrayList<Line> lines,String elementToCheck) {
		for (Line line : lines) {
			ArrayList<String> lineElements=line.getElements();
			
			for (String element : lineElements ) {
				
				if(element.equals(elementToCheck)) {
					
					return true;
				}
			}
			
		}
		return false;
	}
	
	//add epsilon line which can null, and any element contain nullable value then create all variation
	public static void addEpsilonAndCreateVariation(ArrayList<Line> lines,String elementToCheck) {
		for (Line line : lines) {
			ArrayList<String> lineElements=line.getElements();
			
			int size=lineElements.size();
			
			for (int k = 0; k < size; k++) {
				
				String element=lineElements.get(k);
				
				if(element.equals(elementToCheck)) {// if element  exactly equal to nullable value so push epsilon
					
					line.addElement("€");
		
				}
				
				if(element.contains(elementToCheck)) {// if element has nullable value inside create all variations
					
					int n=0;//number of nullable value inside element
					for (int i = 0; i < element.length(); i++) {
						
						if(Character.toString(element.charAt(i)).equals(elementToCheck)) {
							n++;
						}
					}
					boolean flag=true;// which check in how many steps the value to be added to an array will change
					int cnt=0;//count of  how many steps the value to be added to an array will change ex: last digit every step, second to last every two steps
					int numberOfFromTheEnd=0;// count of nullable value from the end
					String[] allCombinations=new String[(int) Math.pow(2,n)];// create array which length all variation
					
					for (int i = 0; i < allCombinations.length; i++) {//initialize the array
						allCombinations[i]="";
						
					}
					for (int i = 0; i < element.length(); i++) {
						cnt=0;
						for (int j = 0; j < allCombinations.length; j++) {
							
							if(flag && Character.toString(element.charAt(element.length()-i-1)).equals(elementToCheck)) {
								allCombinations[j]+=Character.toString(element.charAt(element.length()-i-1));
								cnt++;//increase count of nullable value
								if(cnt==(int) Math.pow(2,numberOfFromTheEnd)) {
									flag=!flag;
									cnt=0;
								}
								
							}else if(!flag && Character.toString(element.charAt(element.length()-i-1)).equals(elementToCheck)) {
								allCombinations[j]+="";
								cnt++;//increase count of nullable value
								if(cnt==(int) Math.pow(2,numberOfFromTheEnd)) {
									flag=!flag;
									cnt=0;
								}	
							}else {//if the next value cannot be null then definitely add
								allCombinations[j]+=Character.toString(element.charAt(element.length()-i-1));
							}	
						}
						if(Character.toString(element.charAt(element.length()-i-1)).equals(elementToCheck)) {
							numberOfFromTheEnd++;
						}
						
					}

					for (int i = 0; i < allCombinations.length; i++) {
						
						String str=allCombinations[i];
						
						String nstr = "";
						for (int j=0; j<str.length(); j++)//reverse string
					      {
					        char ch= str.charAt(j); //extracts each character
					        nstr= ch+nstr; //adds each character in front of the existing string
					      }
						
						
						if(i!=0)// add line variations not including first one because this is already in the line
							line.addElement(nstr);
						
					}
				}
			}
		}
	}
	
	

	public static void removeUnitProduction(ArrayList<Line> lines,ArrayList<String> alphabet) {

		for (Line line : lines) {
			
			ArrayList<String> lineElements=line.getElements();
			String lineName=line.getName();
			
			int size=lineElements.size();
			
			for (int k = 0; k < size; k++) {
				
				String element=lineElements.get(k);
				
				if(lineName.equals(element)) {//if the element shows itself, it is unnecessary, it should be deleted
					lineElements.remove(element);
				}
				else if(element.length()==1 && !alphabet.contains(element)) {// if a non-terminal exists by itself, it must be replaced by its own value
					
					lineElements.remove(element);
					
					int temp_k=k;
					
					int allLineSize=lines.size();
					for (int i = 0; i < allLineSize; i++) {
						
						String lineName2=lines.get(i).getName();
						if(lineName2.equals(element)) {
							int lineSize=lines.get(i).getElements().size();
							for (int j = 0; j < lineSize; j++) {
								lineElements.add(temp_k,lines.get(i).getElements().get(j) );
								temp_k++;
							}
						}
					}
				}
			}
		}
	}

	public static void removeUselessProduction(ArrayList<Line> lines,ArrayList<String> alphabet) {
		
		for (int i = 0; i < lines.size(); i++) {
			Line line=lines.get(i);
			String lineName=line.getName();
			boolean isReachable=false;
			if (!lineName.equals("S0")) {
				for (Line line2 : lines) {
					ArrayList<String> lineElements=line2.getElements();
					for (String element : lineElements ) {
						
						if(element.contains(lineName)) {
							isReachable=true;
							break;
						}
					}
					if(isReachable)
						break;
				}
				if (!isReachable&&!lineName.equals("S")) {
					lines.remove(line);
				}
			}
			
		}
	}

	public static void eliminateMoreThanTwoNonTerminal(ArrayList<Line> lines,ArrayList<String> alphabet,ArrayList<String> newLineNames) {

		for (int i = 0; i < lines.size(); i++) {

			ArrayList<String> lineElements=lines.get(i).getElements();
			int lineSize=lineElements.size();
			for (int j = 0; j < lineSize; j++) {
				
				String element=lineElements.get(j);
				int countNonTerminalValues=0;
				
				String toChange="";
				
				for (int k = 0; k < element.length(); k++) {// traverse of each element and count how many non terminal its have
					if(!alphabet.contains(Character.toString(element.charAt(k)))) {
						countNonTerminalValues++;
					}
					if(countNonTerminalValues>=2) {// keep values from the 2nd non terminal in the toChange
						toChange+=Character.toString(element.charAt(k));
					}
				}
				
				if(toChange.length()>=2) {//if lenght of toChange greater or equal to 2 it have least 3 non-terminal
					int lineSize2=lines.size();
					for (int k = 0; k < lineSize2; k++) {//traverse all table again to determine which element contain toChange
						ArrayList<String> lineElements2=lines.get(k).getElements();
						for (int l = 0; l < lineElements2.size(); l++) {
							String element2=lineElements2.get(l);
							if(element2.endsWith(toChange)&&!element2.equals(toChange)) {//update element which contain toChange
								lineElements2.remove(element2);
								lineElements2.add(l, Character.toString(element2.charAt(0))+newLineNames.get(0));
							}
						}
					}
					lines.add(new Line(newLineNames.get(0),new ArrayList<>( Arrays.asList(toChange))));// update table to new non-terminal indicates toChange
					newLineNames.remove(0);
				}
			}
		}
	}
	
	public static void eliminateTerminals(ArrayList<Line> lines,ArrayList<String> alphabet,ArrayList<String> newLineNames) {
		
		for (String terminal : alphabet) {//traverse all alpahbet
			
			boolean isTerminalNearNonTerminal=false;
			
			for (Line line : lines) {
				ArrayList<String> lineElements=line.getElements();
				
				for (int i = 0; i < lineElements.size(); i++) {//
					String element=lineElements.get(i);
					if(element.contains(terminal)&&!element.equals(terminal)) {//if an element contains a terminal and has more than one length then this terminal should be changed
						String newElement=element.replace(terminal.charAt(0),newLineNames.get(0).charAt(0));
						lineElements.remove(i);
						lineElements.add(i, newElement);
						isTerminalNearNonTerminal=true;
					}
				}
				
			}
			if(isTerminalNearNonTerminal) {
				
				lines.add(new Line(newLineNames.get(0),new ArrayList<>( Arrays.asList(terminal))));// update table to new non-terminal indicates toChange
				newLineNames.remove(0);
			}
		}
	}
	
	public static void createNewStartSymbol(ArrayList<Line> lines) {
		
		for (int i = 0; i < lines.size(); i++) {
			Line line=lines.get(i);
			ArrayList<String> lineElements=line.getElements();
			
			
			for (int j = 0; j < lineElements.size(); j++) {
				
				if(lineElements.get(j).equals("S")) {
					lines.add(0,new Line("S0",  new ArrayList<>( Arrays.asList("S"))));
					return;
				}
					
				
			}
		}
		
			
			
			
		
	}

	public static void main(String[] args) {	
		ArrayList<Line> lines=new ArrayList<>();
		ArrayList<String> alphabet = null;
		ArrayList<String> newLineNames = new ArrayList<>( Arrays.asList("X", "Y", "Z", "Q", "W", "R", "T", "P", "F", "G", "H"));
		boolean isFirstLine=true;
		
		try {
		      File myObj = new File("CFG.txt");
		      Scanner myReader = new Scanner(myObj, "UTF-8");
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        
		        if(isFirstLine) {
		        	String[] firstLine = data.split("=");
		        	alphabet=new ArrayList<>(Arrays.asList(firstLine[1].split(",")));
		        	isFirstLine=false;
		        }else {
		        	String[] result = data.split("-");
		        	lines.add(new Line(result[0], new ArrayList<>(Arrays.asList(result[1].split("\\|")))));   	
		        }
		        
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		System.out.println("\nCFG Form");
		
		printTable(lines);
		
		System.out.println("\nEliminate €");
		createNewStartSymbol(lines);
		while(isContainElement(lines, "€")) {
			
			String lineName=deleteElement(lines,"€");
			addEpsilonAndCreateVariation(lines,lineName);
		}
		
		printTable(lines);
		
		System.out.println("\nEliminate unit production");
		
		removeUnitProduction(lines, alphabet);
		printTable(lines);
		
		System.out.println("\nEliminate useless production");
		
		removeUselessProduction(lines, alphabet);
		printTable(lines);
		
		
		System.out.println("\nEliminate terminals");
		eliminateTerminals(lines, alphabet, newLineNames);
		printTable(lines);
		
		System.out.println("\nBreak variable strings longer than 2 (eliminate More Than Two Non Terminal)");
		
		eliminateMoreThanTwoNonTerminal(lines,alphabet,newLineNames);
			
		printTable(lines);
		
		System.out.println("\nCNF");
		printTable(lines);
		
		System.out.println();
		
	}

}
