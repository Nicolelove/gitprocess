package ftpServer_checking;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


import java.util.Map.Entry;

public class TestFTP_demo {

	static String FileDownloadTo = "D:/ftptest";
	static String testFile = "D:/ftptest/test_file.txt";
	static String testFileRenameTo = "test_file_rename.txt";
	static String userFile = "D:/ftptest/ftp_accountInfo.txt";
	static String olderServerIp = "10.222.48.49";
	static String newServer = "10.222.47.27";
	
	public static void main(String args[]) throws Exception{
	    
	    ReadAccountInfo readAccountInfo=new ReadAccountInfo();
		Map<String,String> accountMap=new HashMap<String,String>();
		accountMap=readAccountInfo.readAccountInfoFromTXT(userFile);
		Iterator iterator=accountMap.entrySet().iterator();
		int number=1;
		while (iterator.hasNext()) {
			Map.Entry entry=(Entry)iterator.next();
			FTPClient ftpClient1=new FTPClient();
			FTPClient ftpClient2=new FTPClient();
			FtpUtil3 ftpUtil1=new FtpUtil3();
			FtpUtil3 ftpUtil2=new FtpUtil3();
			System.out.println();
			System.out.println(number+" testing user : "+entry.getKey().toString());
			
			//test login 
			ftpClient1=ftpUtil1.connectServer(olderServerIp, FTPClient.DEFAULT_PORT, entry.getKey().toString(), entry.getValue().toString(), null ,false);
			ftpClient2=ftpUtil2.connectServer(newServer, FTPClient.DEFAULT_PORT, entry.getKey().toString(), entry.getValue().toString(), null ,true);
			
			String testFolder = "/";
			
			//test structure
			List<String> retList = new ArrayList<String>();
			retList=ftpUtil1.getFileList("/");
			List<String> retList2 = new ArrayList<String>();
			retList2=ftpUtil2.getFileList("/");
			
			if(retList.hashCode()==retList2.hashCode()){
				System.out.println("compare ftp folder structure: --passed");
				if (retList.size()>0) {
					testFolder = retList.get(retList.size()-1);
				}
			}else{
				System.err.println("compare ftp folder structure: --No pass");
			}
			System.out.println("use testing folder: "+testFolder);
			
			if(testFolder.endsWith("/") || testFolder.endsWith(File.separator)){
				//test upload and rename
		        ftpUtil2.uploadFile(testFile,testFolder+testFileRenameTo,true);   //rename pass
		        
		        //test download
			    ftpUtil2.download(testFolder,testFileRenameTo, FileDownloadTo);  //pass
			    
			    //test download content 
			    ftpUtil2.getFileMD5(testFile, FileDownloadTo+testFileRenameTo);
			    
			    //test delete
			    ftpUtil2.deleteFile(testFolder+testFileRenameTo);    //pass 
			    
			}else{
				//test upload and rename
				ftpUtil2.uploadFile(testFile,testFolder+File.separator+testFileRenameTo,true);
				//test download
			    ftpUtil2.download(testFolder,testFileRenameTo, FileDownloadTo);  //pass
			    
			    //test download content 
			    ftpUtil2.getFileMD5(testFile, FileDownloadTo + File.separator + testFileRenameTo);
			    
			    //test delete
			    ftpUtil2.deleteFile(testFolder+File.separator+testFileRenameTo);    //pass 
			}
			
			ftpUtil2.closeServer(ftpClient1,ftpClient2);
			
			number++;
						
		}   
	    
	    
	    
	  }
	
	
	
	
}
