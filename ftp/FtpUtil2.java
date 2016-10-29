package ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import com.sun.xml.internal.ws.developer.StreamingAttachment;
  
public class FtpUtil2 {  
    private FTPClient ftpClient;  
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;  
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;  
      
    /**
     * 利用FtpConfig进行服务器连接
     * @param ftpConfig 参数配置Bean类
     * @throws SocketException
     * @throws IOException
     */
    public void connectServer(FtpConfig ftpConfig) throws SocketException,  
            IOException {  
        String server = ftpConfig.getServer();  
        int port = ftpConfig.getPort();  
        String user = ftpConfig.getUsername();  
        String password = ftpConfig.getPassword();  
        String location = ftpConfig.getLocation();  
        connectServer(server, port, user, password, location);  
    }   
      
    /**
     * 使用详细信息进行服务器连接
     * @param server：服务器地址名称
     * @param port：端口号
     * @param user：用户名
     * @param password：用户密码
     * @param path：转移到FTP服务器目录 
     * @throws SocketException
     * @throws IOException
     */
    public void connectServer(String server, int port, String user,  
            String password, String path) throws SocketException, IOException {  
        ftpClient = new FTPClient();  
        ftpClient.connect(server, port);  
        System.out.println("Connected to " + server + ".");  
        //连接成功后的回应码,开头的返回值就会为真  
        System.out.println(ftpClient.getReplyCode());  
        ftpClient.login(user, password);  
        if (path!=null&&path.length() != 0) {  
            ftpClient.changeWorkingDirectory(path);  
        }  
    	ftpClient.setBufferSize(1024);//设置上传缓存大小
//    	String encoding = System.getProperty("file.encoding");  //获取系统默认编码格式
    	ftpClient.setControlEncoding("UTF-8");//设置编码
    	FTPClientConfig ftpClientConfig=new FTPClientConfig(FTPClientConfig.SYST_NT);
    	ftpClientConfig.setServerLanguageCode("zh");
    	ftpClient.setFileType(BINARY_FILE_TYPE);//设置文件类型,默认使用的是ASCII编码的,对于传输图片必须是二进制文件，否则上传图片会是一片彩色
    }  
    
    /**
     * 设置传输文件类型:FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE  
     * 二进制文件或文本文件
     * @param fileType
     * @throws IOException
     */
    public void setFileType(int fileType) throws IOException {  
        ftpClient.setFileType(fileType);  
    }  
  
    /**
     * 关闭连接
     * @throws IOException
     */
    public void closeServer() throws IOException {  
        if (ftpClient!=null&&ftpClient.isConnected()) {  
        	ftpClient.logout();//退出FTP服务器 
            ftpClient.disconnect();//关闭FTP连接 
        }  
    }
    
    /**
     * 转移到FTP服务器工作目录
     * @param path
     * @return
     * @throws IOException
     */
    public boolean changeDirectory(String path) throws IOException {  
        return ftpClient.changeWorkingDirectory(path);  
    }  
    
    /**
     * 在服务器上创建目录
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean createDirectory(String pathName) throws IOException {  
    	if(ftpClient.makeDirectory(pathName)){
    		System.out.println(pathName+"   :Directory create success!");
    	}else{
    		System.out.println(pathName+"   :Directory create fail!");
    	}
        return ftpClient.makeDirectory(pathName);  
    }  
    
    /**
     * 在服务器上删除目录
     * only can be remove success when the Directory is empty.
     * if not empty ,remove fail
     * @param path
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path) throws IOException {  
    	if(ftpClient.removeDirectory(path)){
    		System.out.println("path remove success!");
    	}else{
    		System.out.println("path remove fail!");
    	}
        return ftpClient.removeDirectory(path);  
    }  
    
    /**
     * 删除服务器上的文件
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String pathName) throws IOException {  
        return ftpClient.deleteFile(pathName);  
    }  
      
    /**
     * 删除所有文件和目录
     * can remove when the direction is not empty 
     * remove both path and content
     * @param path
     * @param isAll true:删除所有文件和目录
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path, boolean isAll)  
            throws IOException {  
          
        if (!isAll) {  
            return removeDirectory(path);  
        }  
  
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        if (ftpFileArr == null || ftpFileArr.length == 0) {  
            return removeDirectory(path);  
        }  
        //   
        for (FTPFile ftpFile : ftpFileArr) {  
            String name = ftpFile.getName();  
            if (ftpFile.isDirectory()) {  
            	System.out.println("* [sD]Delete subPath ["+path + "/" + name+"]");               
                removeDirectory(path + "/" + name, true);  
            } else if (ftpFile.isFile()) {  
            	System.out.println("* [sF]Delete file ["+path + "/" + name+"]");                          
                deleteFile(path + "/" + name);  
            } else if (ftpFile.isSymbolicLink()) {  
  
            } else if (ftpFile.isUnknown()) {  
  
            }  
        }  
        return ftpClient.removeDirectory(path);  
    }  
    
    /**
     * 检查目录在服务器上是否存在 true：存在  false：不存在
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existDirectory(String path) throws IOException {  
        boolean flag = false;  
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        for (FTPFile ftpFile : ftpFileArr) {  
            if (ftpFile.isDirectory()  
                    && ftpFile.getName().equalsIgnoreCase(path)) {  
                flag = true;  
                break;  
            }  
        }  
        System.out.println("The path not exist!");
        return flag;  
    }  
  
    /**
     * 得到文件列表,listFiles返回包含目录和文件，它返回的是一个FTPFile数组
     * listNames()：只包含目录的字符串数组
     * String[] fileNameArr = ftpClient.listNames(path); 
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> getFileList(String path) throws IOException {  
        FTPFile[] ftpFiles= ftpClient.listFiles(path);  
        //通过FTPFileFilter遍历只获得文件
/*      FTPFile[] ftpFiles2= ftpClient.listFiles(path,new FTPFileFilter() {
      @Override
      public boolean accept(FTPFile ftpFile) {
        return ftpFile.isFile();
      }
    });  */
        List<String> retList = new ArrayList<String>();  
        if (ftpFiles == null || ftpFiles.length == 0) {  
            return retList;  
        }  
        for (FTPFile ftpFile : ftpFiles) {  
            if (ftpFile.isFile()) {  
                retList.add(ftpFile.getName());  
            }  
        }  
        return retList;  
    }  
  
  
    /**
     * 上传文件到ftp服务器
     * 在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * localFilePath:本地文件路径和名称
     * remoteFileName:服务器文件名称
     */
    public boolean uploadFile(String localFilePath, String remoteFileName)  
            throws IOException {  
        boolean flag = false;  
        InputStream iStream = null;  //建立一个文件输出流对象 
        try { 
        	System.out.println("url--------->"+remoteFileName.substring(0, remoteFileName.lastIndexOf("/")));
        	System.out.println(ftpClient.changeWorkingDirectory("/"));
//        	ftpClient.changeWorkingDirectory(remoteFileName.substring(0, remoteFileName.lastIndexOf("/")));
            iStream = new FileInputStream(localFilePath);  
            //我们可以使用BufferedInputStream进行封装
            //BufferedInputStream bis=new BufferedInputStream(iStream);
            //flag = ftpClient.storeFile(remoteFileName, bis); 
            flag = ftpClient.storeFile(remoteFileName.substring(remoteFileName.lastIndexOf("/")), iStream);  
            if(flag){
                System.out.println(localFilePath+"  :upload success！");
            }else{
                System.out.println(localFilePath+"  :upload error！");
            }
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            if (iStream != null) {  
                iStream.close();  
            }  
        }  
        return flag;  
    }  
  
    /**
     * 上传文件到ftp服务器，上传新的文件名称和原名称一样
     * @param fileName：文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String fileName) throws IOException {  
        return uploadFile(fileName, fileName);  
    }  
  
    /**
     * 上传文件到ftp服务器
     * @param iStream 输入流
     * @param newName 新文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(InputStream iStream, String newName)  
            throws IOException {  
        boolean flag = false;  
        try {  
            flag = ftpClient.storeFile(newName, iStream);  
        } catch (IOException e) {  
            flag = false;  
            System.out.println("upload error！");
            return flag;  
        } finally {  
            if (iStream != null) {  
                iStream.close();  
            }  
        }  
        return flag;  
    }  
  
    /**
     * 从ftp服务器上下载文件到本地
     * @param remoteFileName：ftp服务器上文件名称
     * @param localFileName：本地文件名称
     * @return
     * @throws IOException
     */
    public boolean download(String ftpWorkUrl,String remoteFileName, String localFileName)  
            throws IOException {  
        boolean flag = false;  
        File outfile = new File(localFileName);  
        OutputStream oStream = null;  
        try {  
        	//在对ftp服务器文件进行操作的时候一定要先跳转到其工作目录下，不然retrieveFile()返回的总是false
        	ftpClient.changeWorkingDirectory(ftpWorkUrl);  
            oStream = new FileOutputStream(outfile);  
            //我们可以使用BufferedOutputStream进行封装
         	BufferedOutputStream bos=new BufferedOutputStream(oStream);
         	System.out.println("**********");
         	bos.flush();
         	System.out.println("**************************");
         	flag = ftpClient.retrieveFile(remoteFileName, bos); 
//            flag = ftpClient.retrieveFile(remoteFileName, oStream);  
         	if(flag){
         		System.out.println("Download success!");
         	}else{
         		System.out.println(remoteFileName+"Download error!");
         	}
        } catch (IOException e) {  
            flag = false; 
            return flag;  
        } finally { 
            oStream.close();  //关闭文件输出流  
        }  
        return flag;  
    }  
      
    /**
     * 从ftp服务器上下载文件到本地
     * @param sourceFileName：服务器资源文件名称
     * @return InputStream 输入流
     * @throws IOException
     */
    public InputStream downFile(String sourceFileName) throws IOException {  
        return ftpClient.retrieveFileStream(sourceFileName);  
    }  
    
    
    
    
    
    
    //上传整个目录到FTP的指定目录中  
    public void uploadDirFiles(String dirPath,String ftpPath) throws IOException{  
        if (dirPath!=null && !dirPath.equals("")) {  
            //建立上传目录的File对象  
            File dirFile = new File(dirPath);  
            //判断File对象是否为目录类型  
            if (dirFile.isDirectory()) {  
                //如果是目录类型。  
                //在FTP上创建一个和File对象文件相同名称的文件夹  
                ftpClient.makeDirectory(ftpPath+"//"+dirFile.getName());  
                //获得File对象中包含的子目录数组  
                File[] subFiles = dirFile.listFiles();  
                //路径  
                String path = ftpPath+"//"+dirFile.getName();  
                //判断数组是否为空  
                if (subFiles!=null && subFiles.length>0) {  
                    //遍历整个File数组  
                    for (int i = 0; i < subFiles.length; i++) {  
                        //判断是否为目录类型  
                        if (subFiles[i].isDirectory()) {  
                            //如果为目录类型  
                            //跳转到FTP的根目录层级  
                            ftpClient.changeWorkingDirectory(ftpPath);  
                            //在FTP上建立相同的目录名称  
                            ftpClient.makeDirectory(path+"//"+subFiles[i].getName());  
                            //递归调用自身方法，进行到下一层级的目录循环  
                            uploadDirFiles(subFiles[i].getAbsolutePath(), path);  
                        } else {  
                            //如果为文件类型  
                            //调用文件上传方法，将文件上传到FTP上  
                            uploadFile(subFiles[i].getPath());                            
                        }  
                    }  
                }  
            } else {  
                //如果为文件类型  
                //调用文件上传方法，将文件上传到FTP上  
                uploadFile(dirFile.getPath());  
            }  
        }  
    }
    
    
    
  //下载FTP上的目录结构到本地中  
    public void downloadDirFiles(String dirPath,String localPath) throws IOException{  
        if (dirPath!=null && !dirPath.equals("")) {  
        	//在本地建立一个相同的文件目录  
        	File localFile = new File(localPath+dirPath.substring(dirPath.lastIndexOf("/"))); 
            localFile.mkdirs();  
            //获得目录在本地的绝对路径  
            localPath = localFile.getAbsolutePath();              
//            FTPFile[] ftpFiles= ftpClient.listFiles(dirPath);  
            //获得FTPFile对象数组  ,这里用ISO-8859-1是因为ftp文件名的编码方式就是ISO-8859-1
            FTPFile[] ftpFiles = ftpClient.listFiles(new String(dirPath.getBytes("utf-8"),"ISO-8859-1"));  
            if (ftpFiles!=null && ftpFiles.length>0) {  
                for (FTPFile subFile: ftpFiles) {  
                    //判断是否为目录结构  
                    if (subFile.isDirectory()) {  
                        //如果为目录结构  ，调用自身方法，进行下一层级目录循环         	
                        downloadDirFiles(dirPath+"/"+subFile.getName(), localPath);  
                    } else {  
                        //如果不为目录结构,为文件类型  ，调用下载方法对文件进行下载  
                    	download(dirPath,subFile.getName(),localPath+File.separator+subFile.getName());  
                    }  
                }  
            }  
        }  
    }  
    
}