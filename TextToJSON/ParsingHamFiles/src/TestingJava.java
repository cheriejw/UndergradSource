import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class TestingJava {

	public static void main(String[]args) throws IOException{
		//-----------------------------------------------------
		//Testing Paths
		System.out.println("\nStart of Paths Test: ");
		
		Path pathDir = Paths.get("D:\\Workspaces\\CECS429\\JSONPARSE\\ParsingHamFiles\\src\\HAMILTON AND MADISON\\paper_18.txt");
		System.out.println(pathDir.getParent().getFileName().toString());
	
		//-----------------------------------------------------
		//Testing Gson
		System.out.println("\nStart of Gson Test: ");
		
		Gson gson = new Gson();
		JSONobject jobj = new JSONobject();
		jobj.author = "AUTHOR";
		jobj.body = "BODY";
		jobj.title = "TITLE";
		String json = gson.toJson(jobj);
		System.out.println(json);
		
		//-----------------------------------------------------
		//Testing Body Reader
		System.out.println("\nStart of Body Reader Test: ");
		
		BufferedReader br = new BufferedReader(new FileReader("D:\\Workspaces\\CECS429\\JSONPARSE\\ParsingHamFiles\\src\\HAMILTON AND MADISON\\paper_18.txt"));
		String line, body;
		int count = 0;
		try {
		    StringBuilder sb = new StringBuilder();
		    line = br.readLine();
		    
		    while (line != null) {
		    	if (line.length() < 4)
		    		count++;
		    	else if (count == 2) { //usually starts after two newlines.
			        sb.append(line);
			        sb.append(System.lineSeparator());
		    	}
		        line = br.readLine();
		    }
		    
		    body = sb.toString();
		} finally {
		    br.close();
		}
		System.out.println(body);
		
		//-----------------------------------------------------
		//Testing Title Reader
		System.out.println("Start of Title Reader Test: ");
		
		BufferedReader br2 = new BufferedReader(new FileReader("D:\\Workspaces\\CECS429\\JSONPARSE\\ParsingHamFiles\\src\\MADISON\\paper_10.txt"));
		String title;
		try {
		    StringBuilder sb = new StringBuilder();
		    line = br2.readLine();

		    while (line.length() > 2) { //once you read the empty line stop.
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br2.readLine();
		    }
		    
		    title = sb.toString();
		} finally {
		    br2.close();
		}
		System.out.println(title);
	}
}
