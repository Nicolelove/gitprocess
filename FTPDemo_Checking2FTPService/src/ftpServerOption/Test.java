package ftpServerOption;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import ftpServer_checking.ReadAccountInfo;

import java.util.Map.Entry;

public class Test {

	public static void main(String args[]) throws Exception{
		
//		FTPClient ftpClient1=new FTPClient();
//		FTPClient ftpClient2=new FTPClient();
//	    FtpUtil ftpUtil1=new FtpUtil();
//	    FtpUtil ftpUtil2=new FtpUtil();
//	    ftpClient1=ftpUtil1.connectServer("10.222.48.49", FTPClient.DEFAULT_PORT, "nicole", "nicole141028.", null);
//	    ftpClient2=ftpUtil2.connectServer("10.222.47.27", FTPClient.DEFAULT_PORT, "nicole", "nicole141028.", null);
	    
	    ReadAccountInfo readAccountInfo=new ReadAccountInfo();
		Map<String,String> accountMap=new HashMap<String,String>();
		accountMap=readAccountInfo.readAccountInfoFromTXT("C:/Users/ZHENGNI2/Desktop/ftp_source/ftp_accountInfo.txt");
		Iterator iterator=accountMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry=(Entry)iterator.next();
			FTPClient ftpClient1=new FTPClient();
			FTPClient ftpClient2=new FTPClient();
			FtpUtil ftpUtil1=new FtpUtil();
			FtpUtil ftpUtil2=new FtpUtil();
			System.out.println("testing user : "+entry.getKey().toString());
			
			//test login 
			ftpClient1=ftpUtil1.connectServer("10.222.48.49", FTPClient.DEFAULT_PORT, entry.getKey().toString(), entry.getValue().toString(), null ,false);
			ftpClient2=ftpUtil2.connectServer("10.222.47.27", FTPClient.DEFAULT_PORT, entry.getKey().toString(), entry.getValue().toString(), null ,true);
			
		    //get alll file below one content
//		    List<String> list1=ftpUtil1.getFileList("/");
//		    System.out.println("文件名称列表为:"+list1);
//		    List<String> list2=ftpUtil2.getFileList("/");
//		    System.out.println("文件名称列表为:"+list2);
			
			
//			List pathList1=new ArrayList<String>();
//			List pathList2=new ArrayList<String>();
			//compare structure
//			pathList1=ftpUtil2.traverseContents(ftpClient1, ftpClient2);   //test structure
			
		    //test upload
//		    ftpUtil1.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx","hihihihihihi.docx");
//		    ftpUtil2.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx","hihihihihihi.docx");
//		    ftpUtil1.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/EDI2016081908132409-68_1.in.csv","");
		    ftpUtil2.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/EDI2016081908132409-68_1.in.csv","",false);
		    ftpUtil2.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx","hihihihihihi.docx",true);
//		    ftpUtil2.uploadFile(ftpClient1, ftpClient2, "C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/EDI2016081908132409-68_1.in.csv", "hahahahahahahaha哈哈哈.in.csv");
		    
		    
		    //test download
//			ftpUtil1.download("/","hahahahahahahaha哈哈哈.in.csv", "C:/Users/ZHENGNI2/Desktop/ftp_source/testremotedownload");
		    ftpUtil2.download("/","hahahahahahahaha哈.in.csv", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload");
		    ftpUtil2.download("/","hahahahahahahaha哈哈哈.in.csv", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload");
//		    ftpUtil2.download(ftpClient1, ftpClient2, "/","hahahahahahahaha哈哈哈.in.csv", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload");
		    
		    //test download content 
		    ftpUtil2.getFileMD5("C:/Users/ZHENGNI2/Desktop/ftp_source/testremotedownload/hahahahahahahaha哈哈哈.in.csv", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload/hahahahahahahaha哈哈哈.in.csv");   //compare ownload file if is same completed 

			
			
//			ftpUtil1.deleteFile("/hahahahahahahaha哈哈哈.in.csv");
			ftpUtil2.deleteFile("/hahahahahahahaha哈哈哈.in.csv");
			System.out.println();
			
		}
	    
	    
	    
	    
	    //获得ftp服务器上某一目录名称下的所有文件名称
//	    List<String> list1=ftpUtil1.getFileList("/");
//	    System.out.println("文件名称列表为:"+list1);
//	    List<String> list2=ftpUtil2.getFileList("/");
//	    System.out.println("文件名称列表为2:"+list2);
//	    
//	    System.out.println("dir-------->"+ftpClient2.printWorkingDirectory());
//	    String[] listname=ftpClient2.listNames();  
//	    for(int i=0;i<listname.length;i++){
//	    	System.out.println("list============"+listname[i]);
//	    }
	    
//	    FTPFile[] ftpDir= ftpClient2.listDirectories();  
//	    for(int i=0;i<ftpDir.length;i++){
//	    	System.out.println("listDir============"+ftpDir[i].getName());
//	    }
	    
//	    System.out.println(ftpUtil2.compareContextStructure(ftpClient1, ftpClient2));   //test structure
	    
	   
	    
	    
//	    if(ftpClient1.changeWorkingDirectory("/casperjs")){
//	    	System.out.println("access success===============");
//	    }else{
//	    	System.out.println("access false===============");
//	    }
//	    if(ftpClient2.changeWorkingDirectory("/casperjs")){
//	    	System.out.println("access success===============");
//	    }else{
//	    	System.out.println("access false===============");
//	    }
		   
	    
	    
	    //上传本地文件到服务器，第二个参数为服务器上名称
//	    ftpUtil.uploadFile("D:/mydownload/FTP_download/测试中文文件上传.txt", "/测试中文文夹的create123/upload中文文件名successs.txt");
//	    ftpUtil.uploadFile("D:/mydownload/FTP_download/测试中文文件上传.txt", "upload中文文件名successs.txt");
//	    ftpUtil.uploadFile("D:/mydownload/FTP_download/测试中文文件上传.txt","");
//	    ftpUtil2.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx","");
//	    ftpUtil1.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx","");
//	    ftpUtil2.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx","hihihihihihi.docx");
//	    ftpUtil2.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testupload/PRT.txt","");
//	    ftpUtil2.uploadFile("C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload/B2BCalcium_userSQL.sql","");
	   
	    //从服务器上下载文件到本地,第一个参数是要操作文件所在文件夹路径,第二个参数是要操作的文件，第三个是本地存放的路径
//	    ftpUtil.download("/","测试中文文件下载.txt", "D:/mydownload/FTP_download/EditPlus/downloadsuccess");
//	    ftpUtil.download("/","26016_DISNEY_OLIVER_PREADV.xml", "D:/mydownload/FTP_download/EditPlus");
//	    ftpUtil.download("/","guideline of BC pattern after class1_20160902.docx", "D:/mydownload/FTP_download/EditPlus");
//	    ftpUtil2.download("/","B2BCalcium_userSQL.sql", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload");
//	    ftpUtil2.download("/","测试中文文件下载.txt", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload");
//	    ftpUtil1.download("/","测试中文文件下载.txt", "C:/Users/ZHENGNI2/Desktop/ftp_source/testremotedownload");
//	    ftpUtil2.download("/","测试中文文件下载.txt", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload");
//	    System.out.println("the content is same:  "+ftpUtil2.getFileMD5("C:/Users/ZHENGNI2/Desktop/ftp_source/testremotedownload/测试中文文件下载.txt", "C:/Users/ZHENGNI2/Desktop/ftp_source/testdownload/测试中文文件下载.txt"));   //compare ownload file if is same completed 
//	    System.out.println("the content is same:  "+ftpUtil2.getFileMD5("/1.html", "/1.html"));
	    
	    
	    
	    
	    
	    //删除ftp服务器上文件
//	    ftpUtil.deleteFile("/测试remove/测试remove第二级目录/upload中文文件名successs.txt");
//	    ftpUtil.deleteFile("upload中文文件名successs.txt");
//	    ftpUtil.deleteFile("/EDI2016110706044973-27.in");
//	    ftpUtil2.deleteFile("/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx");
//	    ftpUtil1.deleteFile("/CS-SYSINT-CUS-CASSOCEANFRT-IR.docx");
//	    ftpUtil2.deleteFile("/PRT.txt");
	    
	    
	    
//	    ftpUtil.removeDirectory("/测试remove/测试remove第二级目录",false);   //测试remove 空文件夹
//	    ftpUtil.removeDirectory("测试remove",false);   
//	    ftpUtil.removeDirectory("/测试中文文夹的create123",true);                   //remove 非空文件夹
//	    ftpUtil.createDirectory("/测试remove/测试remove第二级目录");
	    
//	    ftpUtil.uploadDirFiles("D:/mydownload/FTP_download/second_test第二次测试文件夹下载","/");
//	    ftpUtil.uploadDirFiles("D:/mydownload/FTP_download/second_test第二次测试文件夹下载",null);
//	    ftpUtil.downloadDirFiles("/second_test第二次测试文件夹下载","D:/mydownload/FTP_download/second_test第二次测试文件夹下载");
//	    ftpUtil.downloadDirFiles("/abc","D:/localhost_direction/abc");
	    	     
	    
	    
	    
	    
	    
	  }
	
	
	
	
}
