package ftp;

import java.io.File;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

public class Test {

	public static void main(String args[]) throws Exception{
		
	    FtpUtil2 ftpUtil=new FtpUtil2(); 
		//第一个参数是服务器主机ip,第三个是用户名，第四个是password
	    ftpUtil.connectServer("", FTPClient.DEFAULT_PORT, "", "", null);
	    //获得ftp服务器上某一目录名称下的所有文件名称
	    List<String> list=ftpUtil.getFileList("/abc");
	    System.out.println("文件名称列表为:"+list);
	    //上传本地文件到服务器，第二个参数为服务器上名称
//	    ftpUtil.uploadFile("d:" + File.separator + "1.txt", "/abc/remove"+"/"+"name.txt");
	    //从服务器上下载文件到本地
//	    ftpUtil.download("text.txt", "d:" + File.separator + "textsuccess.txt");
	    //删除ftp服务器上文件
//	    ftpUtil.deleteFile("code.txt");
//	    ftpUtil.removeDirectory("/remove",true);
//	    ftpUtil.createDirectory("/remove");
	    
//	    ftpUtil.uploadDirFiles("D:/copyAll/testcopyfolder","/abc");
	    ftpUtil.downloadDirFiles("/abc","D:/copyAll");
	    
	    
	    
	  }
}
