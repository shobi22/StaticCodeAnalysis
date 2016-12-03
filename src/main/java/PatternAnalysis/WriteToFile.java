package PatternAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {

	public String fileString;
	private static FileWriter fw;
	private static BufferedWriter bw;
	private static File file;
	
	WriteToFile(String fileS){
		this.fileString = fileS;	
		file = new File(fileString);
		if(file.exists()){
			file.delete();
		}else{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void write(String content){
		try {

			fw = new FileWriter(file.getAbsoluteFile(),true);
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.close();

//			System.out.println("Report created!!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void append(String content){
		try {
			bw.append(content);
			bw.close();
			System.out.println("Report created!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
