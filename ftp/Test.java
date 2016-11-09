package ftp;

import java.io.File;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

public class Test {

	public static void main(String args[]) throws Exception{
		
	    FtpUtil ftpUtil=new FtpUtil();
	    ftpUtil.connectServer("10.222.47.27", FTPClient.DEFAULT_PORT, "ricky", "ricky141028.", null);
	    //获得ftp服务器上某一目录名称下的所有文件名称
	    List<String> list=ftpUtil.getFileList("/");
	    System.out.println("文件名称列表为:"+list);
	    
	    //上传本地文件到服务器，第二个参数为服务器上名称
//	    ftpUtil.uploadFile("D:/mydownload/FTP_download/测试中文文件上传.txt", "/测试中文文夹的create123/upload中文文件名successs.txt");
//	    ftpUtil.uploadFile("D:/mydownload/FTP_download/测试中文文件上传.txt", "upload中文文件名successs.txt");
//	    ftpUtil.uploadFile("D:/mydownload/FTP_download/测试中文文件上传.txt","");
	   
	    //从服务器上下载文件到本地,第一个参数是要操作文件所在文件夹路径,第二个参数是要操作的文件，第三个是本地存放的路径
//	    ftpUtil.download("/","测试中文文件下载.txt", "D:/mydownload/FTP_download/EditPlus/downloadsuccess");
//	    ftpUtil.download("/","测试中文文件下载.txt", "D:/mydownload/FTP_download/EditPlus");
	    
	    //删除ftp服务器上文件
//	    ftpUtil.deleteFile("/测试remove/测试remove第二级目录/upload中文文件名successs.txt");
//	    ftpUtil.deleteFile("upload中文文件名successs.txt");
//	    ftpUtil.removeDirectory("/测试remove/测试remove第二级目录",false);   //测试remove 空文件夹
//	    ftpUtil.removeDirectory("测试remove",false);   
//	    ftpUtil.removeDirectory("/测试中文文夹的create123",true);                   //remove 非空文件夹
//	    ftpUtil.createDirectory("/测试remove/测试remove第二级目录");
	    
//	    ftpUtil.uploadDirFiles("D:/mydownload/FTP_download/second_test第二次测试文件夹下载","/");
//	    ftpUtil.uploadDirFiles("D:/mydownload/FTP_download/second_test第二次测试文件夹下载",null);
//	    ftpUtil.downloadDirFiles("/second_test第二次测试文件夹下载","D:/mydownload/FTP_download/second_test第二次测试文件夹下载");
	    ftpUtil.downloadDirFiles("/casperjs","D:/mydownload/FTP_download/second_test第二次测试文件夹下载");
	    	    
	    
	    
	  }
}
