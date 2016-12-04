package compareContents;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MethodToCompareFileContent {

	
	List allPathList=new ArrayList<String>();
	//traverse filepath,and can check contents structure 
	 public List traverseContents(String path,boolean isRecursion){
		 if (!isRecursion) {
			allPathList.clear();
		}
	    	File file=new File(path);
	    	List pathList=new ArrayList<String>();
	    	if(file.exists()){
	    		File[] files=file.listFiles();
	    		if(files.length==0){
	    			System.err.println("the folder is empty!");
	    			return null;
	    		}else{
	    			for(File file2 : files){
	    				if(file2.isDirectory()){
	    					traverseContents(file2.getAbsolutePath(),true);
	    				}else{
//	    					System.out.println(file2.getAbsolutePath());
	    					pathList.add(file2.getAbsolutePath());
	    				}
	    				
	    			}
	    			for(int i=0;i<pathList.size();i++){
//	    				System.out.println("path===="+pathList.get(i));
	    				allPathList.add(pathList.get(i));
	    			}
	    			return allPathList;
	    		}
	    	}else{
	    		System.err.println("the path not exist!");
	    		return null;
	    	}
	    	
	    }
}
