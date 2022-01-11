import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	public static ArrayList<String> fileLines=new ArrayList<String>();
	public static void main(String[] args) {
		File dataFolder = new File("data");
		File seeds = new File("tripadvisor.seed");
		String seedWords[][][]=readSeeds(seeds);
		fileLines.add("City,Hotel Name,Value,Cleanliness,Room,Location,Service,Overall");
		readFolderFiles(dataFolder,seedWords);
		try {
			createCSV(fileLines);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	 
       }
	
	
	public static void createCSV(ArrayList<String> fList) throws FileNotFoundException {
		File csvOutputFile = new File("hotels.csv");
	    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
	        System.out.println("Writing the file..............");
	    	for(String str:fList) {
	        	pw.println(str);
	        }
	        pw.close();
	        System.out.println("File written successfully");
	    }
	}
	
	
	public static String[][][] readSeeds(File seedFile) {
		String seeds[][][]=new String[2][5][];
		int i=0,counter=0,flag=0;
		try {
			
		      Scanner myReader = new Scanner(seedFile);
		      while (myReader.hasNextLine()) {
		    	  counter++;
		    	  if(counter==6) {
		    		  flag=1;
		    	  }
		        String data[] = myReader.nextLine().split("\t");
		        String s[]=data[1].split(",");
		        seeds[flag][i]=new String[s.length];
		        for(int j=0;j<s.length;j++) {
		        	seeds[flag][i][j]=s[j].trim();
		        }
		        i++;
		        if(i%5==0) {
		        	i=0;
		        }
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return seeds;
	}
	
	public static String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
	
	public static String makeRow(String city,String name,double ratings[],double overall) {
		return city+","+
				String.join(" ",name.split("_"))+","+
				ratings[0]+","+
				ratings[1]+","+
				ratings[2]+","+
				ratings[3]+","+
				ratings[4]+","+
				overall;
	}
	
	public static void readFolderFiles(File folder,String[][][] seedWords) {
		for(final File fileEntry: folder.listFiles()) {
			if(fileEntry.isDirectory()) {
				readFolderFiles(fileEntry,seedWords);
			}
			else {
				if (fileEntry.isFile()) {
					try {
						
					      Scanner myReader = new Scanner(fileEntry);
					      double ratings[]= {0,0,0,0,0};
					      double sum=0;
					      double counters[]= {0,0,0,0,0};
					      while (myReader.hasNextLine()) {
					        String data[] = myReader.nextLine().split("\t",2);
					       if(data.length>1) {
					    	   for(int i=0;i<2;i++) {
						        	for(int j=0;j<seedWords[i].length;j++) {
//						        		double counter=0;
						        		for(int p=0;p<seedWords[i][j].length;p++) {
						        			int occurence=data[1].indexOf(seedWords[i][j][p]);
						        			if(occurence!=-1) {
						        				counters[j]++;
						        				if(i==0) {
						        					ratings[j]+=1;
						        				}
						        				else {
						        					ratings[j]-=1;
						        				}
						        			}
						        		}
						        		
						        	}
						        }
					       }
					         
					      }
					      for(int i=0;i<5;i++) {
					    	  if(ratings[i]!=0) {
					    		  double new_rating=Math.round(((ratings[i]/counters[i])*5)*Math.pow(10, 2))/Math.pow(10, 2);
					    		  if(new_rating<0) {
					    			  ratings[i]=0;
					    		  }
					    		  else {
					    			  ratings[i]=new_rating;
					    		  }
					    	  }
					    	  sum+=ratings[i];
					      }
					  
					      myReader.close();
					      fileLines.add(makeRow(fileEntry.getParentFile().getName(),fileEntry.getName(),ratings,sum/5.0));
					    } catch (FileNotFoundException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
			         }
			}
		}
	}

}
