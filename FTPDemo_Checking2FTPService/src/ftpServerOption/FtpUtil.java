package ftpServerOption;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
  
public class FtpUtil{  
    private FTPClient ftpClient=new FTPClient();  
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;  
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;  
    private String local_fileEncoding=System.getProperty("file.encoding");
    private String nameToCall;
    
//    public FtpUtil(String name) {
//		this.nameToCall=name;
//	}
      
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
     * 
     * @param server：服务器地址名称
     * @param port：端口号
     * @param user：用户名
     * @param password：用户密码
     * @param path：转移到FTP服务器目录 
     * @param isNew  true 表示连接到new server ,false 表示old server
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public FTPClient connectServer(String server, int port, String user,  
            String password, String path,boolean isNew) throws SocketException, IOException { 
        ftpClient = new FTPClient();  
        ftpClient.connect(server, port);  
        if(isNew){
            System.out.println("Connected to New Server : " + server + ".");  
        }else{
        	System.out.println("Connected to Old Server : " + server + ".");  
        }
        //连接成功后的回应码,开头的返回值就会为真  
//        System.out.println(ftpClient.getReplyCode());  
//        ftpClient.login(user, password);
        if(!ftpClient.login(user, password)){
        	System.err.println("FTP server refused login.");
        	System.exit(1);
        }
        
        
        if (path!=null&&path.length() != 0) {  
            ftpClient.changeWorkingDirectory(path);  
        } 
    	ftpClient.setBufferSize(1024);//设置上传缓存大小
//    	String encoding = System.getProperty("file.encoding");  //获取系统默认编码格式
    	ftpClient.setControlEncoding(local_fileEncoding);//设置编码
    	FTPClientConfig ftpClientConfig=new FTPClientConfig(FTPClientConfig.SYST_NT);
    	ftpClientConfig.setServerLanguageCode("zh");
    	ftpClient.setFileType(BINARY_FILE_TYPE);//设置文件类型,默认使用的是ASCII编码的,对于传输图片必须是二进制文件，否则上传图片会是一片彩色
    	
    	return ftpClient;
    }  
    
    public FTPClient connectServer(String server, int port, String user,  
            String password, String path) throws SocketException, IOException { 
        ftpClient = new FTPClient();  
        ftpClient.connect(server, port);  
        System.out.println("Connected to " + server + ".");  

        //连接成功后的回应码,开头的返回值就会为真  
//        System.out.println(ftpClient.getReplyCode());  
//        ftpClient.login(user, password);
        if(!ftpClient.login(user, password)){
        	System.err.println("FTP server refused login.");
        	System.exit(1);
        }
        
        
        if (path!=null&&path.length() != 0) {  
            ftpClient.changeWorkingDirectory(path);  
        } 
    	ftpClient.setBufferSize(1024);//设置上传缓存大小
//    	String encoding = System.getProperty("file.encoding");  //获取系统默认编码格式
    	ftpClient.setControlEncoding(local_fileEncoding);//设置编码
    	FTPClientConfig ftpClientConfig=new FTPClientConfig(FTPClientConfig.SYST_NT);
    	ftpClientConfig.setServerLanguageCode("zh");
    	ftpClient.setFileType(BINARY_FILE_TYPE);//设置文件类型,默认使用的是ASCII编码的,对于传输图片必须是二进制文件，否则上传图片会是一片彩色
    	
    	return ftpClient;
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
     * @param path  指定工作目录
     * @return
     * @throws IOException
     */
    public boolean changeDirectory(String path) throws IOException {  
        return ftpClient.changeWorkingDirectory(path);  
    }  
    
    /**
     * 在服务器上创建目录
     * @param pathName 需要创建的目录名称
     * @return
     * @throws IOException
     */
    public boolean createDirectory(String pathName) throws IOException {  
    	if(ftpClient.makeDirectory(new String(pathName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING))){
    		System.out.println(pathName+"   :Directory create success!");
    	}else{
    		System.out.println(pathName+"   :Directory already exist!");
    	}
        return ftpClient.makeDirectory(pathName);  
    }  
    
    /**
     * 在服务器上删除目录
     * only can be remove success when the Directory is empty.
     * if not empty ,remove fail
     * @param path   指定删除目录
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path) throws IOException {
    	path=replace2Fileseparator(path);
    	String tempDirpath=path.substring(0,path.lastIndexOf(File.separator)+1);
    	//这里不转到相应目录下也是可以的，测试过没问题
    	System.out.println(ftpClient.changeWorkingDirectory(new String(tempDirpath.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING)));
//    	ftpClient.changeWorkingDirectory(new String(tempDirpath.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING));
    	//路径一定要经过编码处理，不然中文路径无法remove
    	if(ftpClient.removeDirectory(new String(path.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING))){
    		System.out.println("path remove success!");
    		return true;
    	}else{
    		System.out.println("path remove fail!");
    		return false;
    	}  
    }  
    
    /**
     * 删除服务器上的文件
     * @param pathName   指定删除文件目录
     * @return
     * @throws IOException
     */
//    public boolean deleteFile(String pathName) throws IOException {  
//    	String path="";
//    	pathName=replace2Fileseparator(pathName);
//    	if(pathName.indexOf(File.separator)>-1){
//    		path=pathName.substring(0, pathName.lastIndexOf(File.separator));
//    		if(path!=null && !path.equals("")){
//    			//对于删除非根止录下的文件，应该先转到相应的目录下才可以删除文件
//        		ftpClient.changeWorkingDirectory(new String(path.getBytes(),FTP.DEFAULT_CONTROL_ENCODING));
//        	}
//    	}
//    	if(ftpClient.deleteFile(new String(pathName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING))){
//    		System.out.println(pathName+"  :Delete success!");
//    		return true;
//    	}else{
//    		System.out.println(pathName+"  :Can not Delete!");
//    	}
//    	
//    	return false;
//         
//    }  
    public boolean deleteFile(String pathName) throws IOException {  
    	String path="";
    	pathName=replace2Fileseparator(pathName);
    	if(pathName.indexOf(File.separator)>-1){
    		path=pathName.substring(0, pathName.lastIndexOf(File.separator));
    		if(path!=null && !path.equals("")){
    			//对于删除非根止录下的文件，应该先转到相应的目录下才可以删除文件
        		ftpClient.changeWorkingDirectory(new String(path.getBytes(),FTP.DEFAULT_CONTROL_ENCODING));
        	}
    	}
    	if(ftpClient.deleteFile(new String(pathName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING))){
    		System.out.println("delete ftp file: "+pathName+"  -- passed");
    		return true;
    	}else{
    		System.err.println("delete ftp file: "+pathName+"  -- No pass");
    	}
    	
    	return false;
         
    }
      
    /**
     * 删除所有文件和目录
     * can remove when the direction is not empty 
     * remove both path and content
     * @param path   指定将要删除文件的目录
     * @param isAll true:删除所有文件和目录,当参数为false 表示被删除文件夹为空文件夹
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path, boolean isAll)  
            throws IOException {  
    	System.out.println(System.getProperty("file.encoding"));
        if (!isAll) {  
        	//removeDirectory()参数不能传入转码后的path ，否则会乱码
            return removeDirectory(path);  
        }  
        //定义一个iso_path是为了解决listFiles()不能处理中文路径的问题
        String iso_path=new String(path.getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING);
        FTPFile[] ftpFileArr = ftpClient.listFiles(iso_path);  
        if (ftpFileArr == null || ftpFileArr.length == 0) {  
            return removeDirectory(path);  
        }  
        //   
        for (FTPFile ftpFile : ftpFileArr) {  
            String name = ftpFile.getName();  
            if (ftpFile.isDirectory()) { 
//            	System.out.println("* [sD]Delete subPath ["+path + "/" + name+"]");    
            	System.out.println("* [sD]Delete subPath ["+path + File.separator + name+"]");
//                removeDirectory(new String((path + "/" + name).getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), true); 
            	removeDirectory(path + File.separator + name, true); 
            } else if (ftpFile.isFile()) {  
//            	System.out.println("* [sF]Delete file ["+path + "/" + name+"]");  
            	System.out.println("* [sF]Delete file ["+path + File.separator + name+"]");  
//                deleteFile(new String((path + "/" + name).getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING));  
                deleteFile(path + File.separator + name);  
            } else if (ftpFile.isSymbolicLink()) {  
  
            } else if (ftpFile.isUnknown()) {  
  
            }  
        }  
        return ftpClient.removeDirectory(new String(path.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING));  
    }   
    
  
    /**
     * 得到文件列表,listFiles返回包含目录和文件，它返回的是一个FTPFile数组
     * listNames()：只包含目录的字符串数组
     * String[] fileNameArr = ftpClient.listNames(path); 
     * @param path  指定扫描文件目录
     * @return
     * @throws IOException
     */
//    public List<String> getFileList(String path) throws IOException {  
//        FTPFile[] ftpFiles= ftpClient.listFiles(path);  
//        //通过FTPFileFilter遍历只获得文件
///*      FTPFile[] ftpFilesNew= ftpClient.listFiles(path,new FTPFileFilter() {
//      @Override
//      public boolean accept(FTPFile ftpFile) {
//        return ftpFile.isFile();
//      }
//    });  */
//        List<String> retList = new ArrayList<String>();  
//        if (ftpFiles == null || ftpFiles.length == 0) {  
//            return retList;  
//        }  
//        for (FTPFile ftpFile : ftpFiles) { 
//        	//only output file and ignore folder
////            if (ftpFile.isFile()) {  
////                retList.add(ftpFile.getName());  
////            }
//        	
//        	retList.add(ftpFile.getName());  
//        }  
//        return retList;  
//    }   
    
    
  
    /**
     * 上传文件到ftp服务器
     * 在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * 
     * @param localFilePath  本地文件路径和名称
     * @param remoteFileName   服务器文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String localFilePath, String remoteFileName)  
            throws IOException {  
        boolean flag = false;  
        InputStream iStream = null;  //建立一个文件输出流对象 
        localFilePath=replace2Fileseparator(localFilePath);   //预处理路径
        remoteFileName=replace2Fileseparator(remoteFileName);
        try { 
        	if(remoteFileName==null || remoteFileName.equals("")){
//        		ftpClient.changeWorkingDirectory("/");
        		ftpClient.changeWorkingDirectory(File.separator);
        		remoteFileName=localFilePath.substring(localFilePath.lastIndexOf(File.separator));
        	}else if(!remoteFileName.contains(File.separator)){
        		ftpClient.changeWorkingDirectory(File.separator);
        	}else{
            	System.out.println(ftpClient.changeWorkingDirectory(new String(remoteFileName.substring(0, remoteFileName.lastIndexOf(File.separator)).getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING)));
        	}
            iStream = new FileInputStream(localFilePath);  
            //我们可以使用BufferedInputStream进行封装
            BufferedInputStream bis=new BufferedInputStream(iStream);
            //utf-8,这个不是固定的值，是根据系统本身默认的编码来定的
            flag = ftpClient.storeFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bis); //将Bis输入流的东西上传到ftp服务器的fileName文件中，在这里我们对fileName文件名进行了转码,解决了中文文件名不能上传的问题
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
     * 上传文件到ftp服务器
     * 在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * 
     * @param localFilePath  本地文件路径和名称
     * @param remoteFileName   服务器文件名称
     * @param isRename  是否重命名
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String localFilePath, String remoteFileName,boolean isRename)  
            throws IOException {  
        boolean flag = false;  
        InputStream iStream = null;  //建立一个文件输出流对象 
        localFilePath=replace2Fileseparator(localFilePath);   //预处理路径
        remoteFileName=replace2Fileseparator(remoteFileName);
        try { 
        	if(remoteFileName==null || remoteFileName.equals("")){
//        		ftpClient.changeWorkingDirectory("/");
        		ftpClient.changeWorkingDirectory(File.separator);
        		remoteFileName=localFilePath.substring(localFilePath.lastIndexOf(File.separator));
        	}else if(!remoteFileName.contains(File.separator)){
        		ftpClient.changeWorkingDirectory(File.separator);
        	}else{
            	ftpClient.changeWorkingDirectory(new String(remoteFileName.substring(0, remoteFileName.lastIndexOf(File.separator)).getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING));
        	}
            iStream = new FileInputStream(localFilePath);  
            //我们可以使用BufferedInputStream进行封装
            BufferedInputStream bis=new BufferedInputStream(iStream);
            //utf-8,这个不是固定的值，是根据系统本身默认的编码来定的
            flag = ftpClient.storeFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bis); //将Bis输入流的东西上传到ftp服务器的fileName文件中，在这里我们对fileName文件名进行了转码,解决了中文文件名不能上传的问题
            if(flag){
            	if(!isRename){
                    System.out.println("upload file to new server: "+remoteFileName+"  -- passed");
            	}else{
            		System.out.println("rename ftp file name , from: "+localFilePath.substring(localFilePath.lastIndexOf(File.separator))+" to "+remoteFileName+"  -- passed");
            	}
            }else{
            	if(!isRename){
                   System.err.println("upload file to new server: "+remoteFileName+"  -- NO pass");
            	}else{
            		System.err.println("rename ftp file name, from : "+localFilePath.substring(localFilePath.lastIndexOf(File.separator))+" to "+remoteFileName+"  -- No pass");
            	}
            }
        } catch (IOException e) {  
        	System.err.println("localFilePath not exist!  --No pass");
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
     * 
     * 上传文件到ftp服务器
     * 在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * 
     * @param ftpClientOld   old server
     * @param ftpClientNew   new server
     * @param localFilePath   本地文件路径和名称
     * @param remoteFileName   服务器文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(FTPClient ftpClientOld,FTPClient ftpClientNew,String localFilePath, String remoteFileName)  
            throws IOException {  
        boolean flagOld = false;  
        boolean flagNew = false;  
        boolean isUploadSuccess = false;
        InputStream iStreamOld = null;  //建立一个文件输出流对象 ,一定要同时建两个不然其中一个文件内容将为空
        InputStream iStreamNew = null;
        localFilePath=replace2Fileseparator(localFilePath);   //预处理路径
        remoteFileName=replace2Fileseparator(remoteFileName);
        try { 
        	if(remoteFileName==null || remoteFileName.equals("")){
        		ftpClientOld.changeWorkingDirectory(File.separator);
        		ftpClientNew.changeWorkingDirectory(File.separator);
        		remoteFileName=localFilePath.substring(localFilePath.lastIndexOf(File.separator));
        	}else if(!remoteFileName.contains(File.separator)){
        		ftpClientOld.changeWorkingDirectory(File.separator);
        		ftpClientNew.changeWorkingDirectory(File.separator);
        	}else{
            	System.out.println(ftpClientOld.changeWorkingDirectory(new String(remoteFileName.substring(0, remoteFileName.lastIndexOf(File.separator)).getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING)));
            	System.out.println(ftpClientNew.changeWorkingDirectory(new String(remoteFileName.substring(0, remoteFileName.lastIndexOf(File.separator)).getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING)));
            	
        	}
        	iStreamOld = new FileInputStream(localFilePath);  
        	iStreamNew = new FileInputStream(localFilePath);  
            //我们可以使用BufferedInputStream进行封装
            BufferedInputStream bisOld=new BufferedInputStream(iStreamOld);
            BufferedInputStream bisNew=new BufferedInputStream(iStreamNew);
//            System.out.println("stream********"+bisOld);
//            System.out.println("stream2********"+bisNew);
            //utf-8,这个不是固定的值，是根据系统本身默认的编码来定的
            flagOld = ftpClientOld.storeFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bisOld); //将Bis输入流的东西上传到ftp服务器的fileName文件中，在这里我们对fileName文件名进行了转码,解决了中文文件名不能上传的问题
            flagNew = ftpClientNew.storeFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bisNew); //将Bis输入流的东西上传到ftp服务器的fileName文件中，在这里我们对fileName文件名进行了转码,解决了中文文件名不能上传的问题
            
            if(flagOld && flagNew){
            	isUploadSuccess=true;
                System.out.println(localFilePath+"  :upload success！");
            }else{
                System.out.println(localFilePath+"  :upload error！");
            }
        } catch (IOException e) {  
            return isUploadSuccess;  
        } finally {  
            if (iStreamOld != null) {  
            	iStreamOld.close();  
            } 
            if (iStreamNew != null) {  
            	iStreamNew.close();  
            }
        }  
        return isUploadSuccess;  
    } 
    
  
    /**
     * 上传文件到ftp服务器，上传新的文件名称和原名称一样
     * @param fileName： 指定文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String fileName) throws IOException {  
        return uploadFile(fileName, fileName);  
    }  
  
    /**
     * 上传文件到ftp服务器
     * @param iStream 指定输入流
     * @param newName 新文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(InputStream iStream, String newName)  
            throws IOException {  
        boolean flag = false;  
        try {  
            flag = ftpClient.storeFile(new String(newName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), iStream);  
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
     * 
     * @param ftpClientOld  old server
     * @param ftpClientNew  new server
     * @param fileDirPath    服务器文件所在路径
     * @param remoteFileName   下载文件名
     * @param localFileName     本地存放路径
     * @return
     * @throws IOException
     */
	public boolean download(FTPClient ftpClientOld,FTPClient ftpClientNew,String fileDirPath,String remoteFileName, String localFileName)  
            throws IOException {		
        boolean flagOld = false; 
        boolean flagNew = false;
        boolean isDownloadSuccesss = false;
        if(localFileName!=null && !localFileName.equals("")){
        fileDirPath=replace2Fileseparator(fileDirPath);
        localFileName=replace2Fileseparator(localFileName);
        //FileOutputStream fos1=new FileOutputStream(localFileName,false);  true表示如果文件已经存在，每执行程序一次便往文件追加一次内容，否则会每一次的执行结果会覆盖文件上一次执行结果
        File outfile = new File(localFileName); 
        File outfile2 = new File(localFileName); 
        if(outfile.isDirectory() && localFileName.endsWith(File.separator)){
        	localFileName=localFileName+remoteFileName;
        	outfile=new File(localFileName);
        	
        	
        	String splitLocalFileNameBeforePort=localFileName.substring(0, localFileName.indexOf("."));
        	String splitLocalFileNameAfterPort=localFileName.substring(localFileName.indexOf(splitLocalFileNameBeforePort));
        	outfile2=new File(splitLocalFileNameBeforePort+"2"+splitLocalFileNameAfterPort);
        }else if(outfile.isDirectory() && !localFileName.endsWith(File.separator)){
        	localFileName=localFileName+File.separator+remoteFileName;
        	outfile=new File(localFileName);
        	
        	String splitLocalFileNameBeforePort=localFileName.substring(0, localFileName.indexOf("."));
        	String splitLocalFileNameAfterPort=localFileName.substring(localFileName.indexOf(splitLocalFileNameBeforePort));
        	outfile2=new File(splitLocalFileNameBeforePort+"2"+splitLocalFileNameAfterPort);
        }        
        OutputStream oStreamOld = null;  
        OutputStream oStreamNew = null;  
        try {          	
        	//在对ftp服务器非根目录下的文件进行操作的时候一定要先跳转到相应的工作目录下，不然retrieveFile()返回的总是false，如果操作的文件是在根目下就可以省略这一步
        	ftpClient.changeWorkingDirectory(new String(fileDirPath.getBytes(local_fileEncoding),"ISO-8859-1"));  
        	oStreamOld = new FileOutputStream(outfile); 
        	oStreamNew = new FileOutputStream(outfile2); 
            //我们可以使用BufferedOutputStream进行封装,封装成具有缓冲功能的文件输出流
         	BufferedOutputStream bufferedOutputStreamOld=new BufferedOutputStream(oStreamOld); 
         	BufferedOutputStream bufferedOutputStreamNew=new BufferedOutputStream(oStreamNew); 
         	flagOld = ftpClient.retrieveFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bufferedOutputStreamOld); //从服务器检索命名文件并将其写入给定的OutputStream中,这里必须对remoteFileName进行转码，不然处理不了中文文件名
         	flagNew = ftpClient.retrieveFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bufferedOutputStreamNew);
         	bufferedOutputStreamOld.flush();    //当我们使用输出流发送数据时，当数据不能填满输出流的缓冲区时，这时，数据就会被存储在输出流的缓冲区中;如果，我们这个时候调用关闭(close)输出流，存储在输出流的缓冲区中的数据就会丢失。所以说，关闭(close)输出流时，应先刷新(flush)换冲的输出流
         	bufferedOutputStreamNew.flush();
         	if(flagOld && flagNew){
         		isDownloadSuccesss=true;
         		System.out.println(remoteFileName+"   :Download success!");
         	}else{
         		System.out.println(remoteFileName+"  :Download error!");
         	}
        } catch (IOException e) {  
            return isDownloadSuccesss;  
        } finally { 
        	oStreamOld.close();  //关闭文件输出流  
        	oStreamNew.close();
        }  
        }else{
        	System.out.println("localpath cannot is empty!");
        }
        return isDownloadSuccesss;  
    }
    
    
    
  
    /**
     * 从ftp服务器上下载文件到本地
     * @param fileDirPath   服务器文件所在路径
     * @param remoteFileName   下载文件名
     * @param localFileName   本地存放路径
     * @return
     * @throws IOException
     */
//	public boolean download(String fileDirPath,String remoteFileName, String localFileName)  
//            throws IOException {		
//        boolean flag = false; 
//        if(localFileName!=null && !localFileName.equals("")){
//        fileDirPath=replace2Fileseparator(fileDirPath);
//        localFileName=replace2Fileseparator(localFileName);
//        //FileOutputStream fos1=new FileOutputStream(localFileName,false);  true表示如果文件已经存在，每执行程序一次便往文件追加一次内容，否则会每一次的执行结果会覆盖文件上一次执行结果
//        File outfile = new File(localFileName); 
//        if(outfile.isDirectory() && localFileName.endsWith(File.separator)){
//        	localFileName=localFileName+remoteFileName;
//        	outfile=new File(localFileName);
//        }else if(outfile.isDirectory() && !localFileName.endsWith(File.separator)){
//        	localFileName=localFileName+File.separator+remoteFileName;
//        	outfile=new File(localFileName);
//        }        
//        OutputStream oStream = null;  
//        try {          	
//        	//在对ftp服务器非根目录下的文件进行操作的时候一定要先跳转到相应的工作目录下，不然retrieveFile()返回的总是false，如果操作的文件是在根目下就可以省略这一步
//        	ftpClient.changeWorkingDirectory(new String(fileDirPath.getBytes(local_fileEncoding),"ISO-8859-1"));  
//        	oStream = new FileOutputStream(outfile); 
//            //我们可以使用BufferedOutputStream进行封装,封装成具有缓冲功能的文件输出流
//         	BufferedOutputStream bos=new BufferedOutputStream(oStream); 	
//         	flag = ftpClient.retrieveFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bos); //从服务器检索命名文件并将其写入给定的OutputStream中,这里必须对remoteFileName进行转码，不然处理不了中文文件名
//         	bos.flush();    //当我们使用输出流发送数据时，当数据不能填满输出流的缓冲区时，这时，数据就会被存储在输出流的缓冲区中;如果，我们这个时候调用关闭(close)输出流，存储在输出流的缓冲区中的数据就会丢失。所以说，关闭(close)输出流时，应先刷新(flush)换冲的输出流
//         	if(flag){
//         		System.out.println(remoteFileName+"   :Download success!");
//         	}else{
//         		System.out.println(remoteFileName+"  :Download error!");
//         	}
//        } catch (IOException e) {  
//            flag = false; 
//            return flag;  
//        } finally { 
//            oStream.close();  //关闭文件输出流  
//        }  
//        }else{
//        	System.out.println("localpath cannot is empty!");
//        }
//        return flag;  
//    }  
	
	
	/**
	 * 
	 * 从ftp服务器上下载文件到本地
	 * 
     * @param fileDirPath   服务器文件所在路径
     * @param remoteFileName   下载文件名
     * @param localFileName   本地存放路径
	 * @return
	 * @throws IOException
	 */
	public boolean download(String fileDirPath,String remoteFileName, String localFileName)  
            throws IOException {		
        boolean flag = false; 
        if(localFileName!=null && !localFileName.equals("")){
        fileDirPath=replace2Fileseparator(fileDirPath);
        localFileName=replace2Fileseparator(localFileName);
        //FileOutputStream fos1=new FileOutputStream(localFileName,false);  true表示如果文件已经存在，每执行程序一次便往文件追加一次内容，否则会每一次的执行结果会覆盖文件上一次执行结果
        File outfile = new File(localFileName); 
        if(outfile.isDirectory() && localFileName.endsWith(File.separator)){
        	localFileName=localFileName+remoteFileName;
        	outfile=new File(localFileName);
        }else if(outfile.isDirectory() && !localFileName.endsWith(File.separator)){
        	localFileName=localFileName+File.separator+remoteFileName;
        	outfile=new File(localFileName);
        }        
        OutputStream oStream = null;  
        try {          	
        	//在对ftp服务器非根目录下的文件进行操作的时候一定要先跳转到相应的工作目录下，不然retrieveFile()返回的总是false，如果操作的文件是在根目下就可以省略这一步
        	ftpClient.changeWorkingDirectory(new String(fileDirPath.getBytes(local_fileEncoding),"ISO-8859-1"));  
        	oStream = new FileOutputStream(outfile); 
            //我们可以使用BufferedOutputStream进行封装,封装成具有缓冲功能的文件输出流
         	BufferedOutputStream bos=new BufferedOutputStream(oStream); 	
         	flag = ftpClient.retrieveFile(new String(remoteFileName.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING), bos); //从服务器检索命名文件并将其写入给定的OutputStream中,这里必须对remoteFileName进行转码，不然处理不了中文文件名
         	bos.flush();    //当我们使用输出流发送数据时，当数据不能填满输出流的缓冲区时，这时，数据就会被存储在输出流的缓冲区中;如果，我们这个时候调用关闭(close)输出流，存储在输出流的缓冲区中的数据就会丢失。所以说，关闭(close)输出流时，应先刷新(flush)换冲的输出流
         	if(flag){
         		System.out.println("download ftp file: "+fileDirPath+remoteFileName+"   -- passed");
         	}else{
         		System.err.println("download ftp file: "+fileDirPath+remoteFileName+"  -- No pass");
         	}
        } catch (IOException e) {  
            flag = false; 
            return flag;  
        } finally { 
            oStream.close();  //关闭文件输出流  
        }  
        }else{
        	System.out.println("localpath cannot is empty!");
        }
        return flag;  
    }
	
      
	/**
	 * 从ftp服务器上下载文件到本地
	 * @param sourceFileName   服务器资源文件名称
	 * @return
	 * @throws IOException
	 */
    public InputStream downFile(String sourceFileName) throws IOException {  
        return ftpClient.retrieveFileStream(sourceFileName);  
    }  
     
    /**
     * 上传整个目录到FTP的指定目录中 
     * @param dirPath  上传文件路径
     * @param ftpPath   指定上传到ftp的目录
     * @throws IOException
     */
	public void uploadDirFiles(String dirPath, String ftpPath) throws IOException {
		String path = "";
		dirPath = replace2Fileseparator(dirPath);
		ftpPath = replace2Fileseparator(ftpPath);
		if (dirPath != null && !dirPath.equals("")) {
			if (ftpPath != null && !ftpPath.equals("")) {
				if (ftpClient.changeWorkingDirectory(
						new String(ftpPath.getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING))) {
					// 建立上传目录的File对象
					File dirFile = new File(dirPath);
					// 判断File对象是否为目录类型
					if (dirFile.isDirectory()) {
						// 如果是目录类型;在FTP上创建一个和File对象文件相同名称的文件夹
						if (!ftpPath.endsWith(File.separator)) {
							// ftpClient.makeDirectory(ftpPath+"/"+dirFile.getName());
							ftpClient.makeDirectory(new String(
									(ftpPath + File.separator + dirFile.getName()).getBytes(local_fileEncoding),
									FTP.DEFAULT_CONTROL_ENCODING)); // 当文件路径中包含中文件时，在调用makeDirectory（）方法创建路径
																	// 的时候一定要对中文进行处理，否则不能创建成功
						} else {
							ftpClient.makeDirectory(
									new String((ftpPath + dirFile.getName()).getBytes(local_fileEncoding),
											FTP.DEFAULT_CONTROL_ENCODING));
						}
						// 获得File对象中包含的子目录数组
						File[] subFiles = dirFile.listFiles();
						// 路径
						if (!ftpPath.endsWith(File.separator)) {
							// path = ftpPath+"/"+dirFile.getName();
							path = ftpPath + File.separator + dirFile.getName();
						} else {
							path = ftpPath + dirFile.getName();
						}
						// 判断数组是否为空
						if (subFiles != null && subFiles.length > 0) {
							// 遍历整个File数组
							for (int i = 0; i < subFiles.length; i++) {
								// 判断是否为目录类型
								if (subFiles[i].isDirectory()) {
									// 如果为目录类型 ;跳转到FTP的根目录层级
									ftpClient.changeWorkingDirectory(ftpPath);
									// 在FTP上建立相同的目录名称
									ftpClient.makeDirectory(path + "/" + subFiles[i].getName());
									// 递归调用自身方法，进行到下一层级的目录循环
									uploadDirFiles(subFiles[i].getAbsolutePath(), path);
								} else {
									// 如果为文件类型 ,调用文件上传方法，将文件上传到FTP上
									uploadFile(subFiles[i].getPath(), path + subFiles[i].getPath()
											.substring(subFiles[i].getPath().lastIndexOf(File.separator)));
								}
							}
						}
					} else {
						// 如果为文件类型
						// 调用文件上传方法，将文件上传到FTP上
						uploadFile(dirFile.getPath());
					}
				} else {
					System.out.println("this path is not invalid in server!");
				}
			} else {
				System.out.println("the path in service cannot is empty!");
			}
		} else {
			System.out.println("target path not exist!");
		}
	}
    
    
	/**
	 * 下载FTP上的目录结构到本地中 
	 * @param dirPath   服务器目标文件路径
	 * @param localPath   本地存入路径
	 * @throws IOException
	 */
    public void downloadDirFiles(String dirPath,String localPath) throws IOException{  
        if (dirPath!=null && !dirPath.equals("")) {
        	localPath=replace2Fileseparator(localPath);
        	dirPath=replace2Fileseparator(dirPath);
        	//在本地建立一个相同的文件目录  
        	File localFile = new File(localPath+dirPath.substring(dirPath.lastIndexOf(File.separator))); 
            localFile.mkdirs();  
            //获得目录在本地的绝对路径  
            localPath = localFile.getAbsolutePath();              
            //获得FTPFile对象数组  ,这里用FTP.DEFAULT_CONTROL_ENCODING是因为ftp文件名的编码方式就是ISO-8859-1
            FTPFile[] ftpFiles = ftpClient.listFiles(new String(dirPath.getBytes(local_fileEncoding),FTP.DEFAULT_CONTROL_ENCODING));  
            if (ftpFiles!=null && ftpFiles.length>0) {  
                for (FTPFile subFile: ftpFiles) { 
                    //判断是否为目录结构  
                    if (subFile.isDirectory()) {  
                        //如果为目录结构  ，调用自身方法，进行下一层级目录循环       
                    	downloadDirFiles(dirPath+File.separator+subFile.getName(), localPath);  
                    } else {  
                        //如果不为目录结构,为文件类型  ，调用下载方法对文件进行下载  
                    	download(dirPath,subFile.getName(),localPath+File.separator+subFile.getName());  
                    }  
                }  
            }  
        }  
    }  
    
    /**
     * 
     * @param path 需要进行replace的路径
     * @return
     */
    public String replace2Fileseparator(String path){
		if (path != null && !path.equals("")) {
			if (path.contains("/")) {
				path = path.replace("/", File.separator);
			} else if (path.contains("\\\\")) {
				path = path.replace("\\\\", File.separator);
			}
		}
    	return path;
    }
    
    /**
     * compare the context structure if is same completed in old and new
     * @param ftpClientOld
     * @param ftpClientNew
     * @return
     * @throws Exception
     */
    public boolean compareContextStructure(FTPClient ftpClientOld,FTPClient ftpClientNew) throws Exception{
    	boolean isSame=false;
    	FTPFile[] ftpFilesOld= ftpClientOld.listFiles();  
	    FTPFile[] ftpFilesNew= ftpClientNew.listFiles();  
	    List<String> retListOld = new ArrayList<String>();
	    List<String> retListNew = new ArrayList<String>();
	    for(FTPFile ftpFile : ftpFilesOld){
	    	retListOld.add(ftpFile.getName());
	    }
	    for(FTPFile ftpFile : ftpFilesNew){
	    	retListNew.add(ftpFile.getName());
	    }
//	    for(String string : retListOld){
//	    	System.out.println(string);
//	    }
	    isSame=retListOld.hashCode()==retListNew.hashCode();
	    if(retListOld.hashCode()==retListNew.hashCode()){
	    	System.out.println("结构一致");
	    }else{
	    	System.out.println("结构不一致");
	    }
	    return isSame;
    }
    
    public List<String> compareContextStructure(FTPClient ftpClientOld) throws Exception{
    	boolean isSame=false;
    	FTPFile[] ftpFilesOld= ftpClientOld.listFiles();  
	    List<String> retList = new ArrayList<String>();
	    for(FTPFile ftpFile : ftpFilesOld){
	    	if(ftpFile.isDirectory()){
	    		System.out.println(File.separator+ftpFile.getName());
	    		retList=getFileList(File.separator+ftpFile.getName());
//	    	   retListOld.add(ftpFile.getName());
	    	}
	    }
	    
	    return retList;
    }
    
    List<String> retList = new ArrayList<String>(); 
    public List<String> getFileList(String path) throws IOException {  
//    	System.out.println("in--------->"+path);
    	retList.add(path);
        FTPFile[] ftpFiles= ftpClient.listFiles(path);  
        if (ftpFiles == null || ftpFiles.length == 0) {  
            return retList;  
        }  
        for (FTPFile ftpFile : ftpFiles) { 
        	if(ftpFile.isDirectory()){
        		if (path.endsWith(File.separator) || path.equals("/")) {
        			getFileList(path+ftpFile.getName());
				}else{
        		    getFileList(path+File.separator+ftpFile.getName());
				}
        	} 
        }  
        return retList;  
    }
    
    
    
//    List<String> retListOld = new ArrayList<String>(); 
//    List<String> retListNew = new ArrayList<String>(); 
//    List<String> retListNewtemp = new ArrayList<String>(); 
//    
//    public boolean getFileList(FTPClient ftpClientOld,String pathOld,FTPClient ftpClientNew,String pathNew) throws IOException {  
//    	int index=0;
//    	retListOld.add(pathOld);
//    	retListNew.add(pathNew);
//        FTPFile[] ftpFilesOld= ftpClient.listFiles(pathOld);  
//        FTPFile[] ftpFilesNew= ftpClient.listFiles(pathNew);  
//        if (ftpFilesOld == null || ftpFilesOld.length == 0 || ftpClientNew==null || ftpFilesNew.length == 0) {  
//            return true;  
//        }
//        for(FTPFile ftpFileNew : ftpFilesNew){
//        	if (ftpFileNew.isDirectory()) {
//				retListNewtemp.add(ftpFileNew.getName());
//			}
//        }
//        for (FTPFile ftpFile : ftpFilesOld) { 
//        	if(ftpFile.isDirectory()){
//        		if (pathOld.endsWith(File.separator) || pathOld.equals("/")) {
//        			getFileList(ftpClientOld, pathOld+ftpFile.getName(), ftpClientNew, pathNew+retListNewtemp.get(index));
//				}else{
//        		    getFileList(ftpClientOld, pathOld+File.separator+ftpFile.getName(), ftpClientNew, pathNew+File.separator+retListNewtemp.get(index));
//				}
//        	} 
//        	index++;
//        }  
////        for (FTPFile ftpFileNew : ftpFilesNew) { 
////        	if(ftpFileNew.isDirectory()){
////        		if (pathNew.endsWith(File.separator) || pathNew.equals("/")) {
////        			getFileList(pathNew+ftpFileNew.getName());
////				}else{
////        		    getFileList(pathNew+File.separator+ftpFileNew.getName());
////				}
////        	} 
////        } 
//        if(retListOld.hashCode() == retListNew.hashCode()){
//        	System.out.println("--passed");
//        	return true;
//        }else{
//        	System.err.println("--No pass");
//        	return false;
//        }  
//    }
    
    
    
    
    
    
    public void traverseContents(String path){
    	File file=new File(path);
    	if(file.exists()){
    		File[] files=file.listFiles();
    		if(files.length==0){
    			System.out.println("the folder is empty!");
    		}else{
    			for(File file2 : files){
    				if(file2.isDirectory()){
    					traverseContents(file2.getAbsolutePath());
    				}else{
    					System.out.println(file2.getAbsolutePath());
    				}
    			}
    		}
    	}else{
    		System.out.println("the path not exist!");
    	}
    }
    
    
    /**
     * compare the content if is same between two file
     * @param filePath
     * @return
     * @throws Exception
     */
    public boolean getFileMD5 (String filePathOld,String filePathNew) throws Exception{
    	
		File fileOld=new File(filePathOld);
		File fileNew=new File(filePathNew);
		InputStream inputStreamOld=new FileInputStream(fileOld);
		InputStream inputStreamNew=new FileInputStream(fileNew);
		MessageDigest messageDigestOld=MessageDigest.getInstance("MD5");
		MessageDigest messageDigestNew=MessageDigest.getInstance("MD5");
		byte bufferOld[]=new byte[1024];
		int len;
		while ((len=inputStreamOld.read(bufferOld))!=-1) {
			messageDigestOld.update(bufferOld,0,len);
			
		}
		while ((len=inputStreamNew.read(bufferOld))!=-1) {
			messageDigestNew.update(bufferOld,0,len);
			
		}
		BigInteger bigIntegerOld=new BigInteger(1,messageDigestOld.digest());
		BigInteger bigIntegerNew=new BigInteger(1,messageDigestNew.digest());
		 if(bigIntegerOld.toString().equals(bigIntegerNew.toString())){
			 System.out.println("compare file context : -- passed");
			 return true;
		 }
		 System.err.println("compare file context : -- No pass");
		 return false;
		
	}
    
    
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
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 public void traverseContents(FTPClient ftpClientOld,FTPClient ftpClientNew) throws Exception{
		 
		    FTPFile[] ftpFilesOld= ftpClientOld.listFiles();  
		    FTPFile[] ftpFilesNew= ftpClientNew.listFiles();  
		    List<String> retListOld = new ArrayList<String>();
		    List<String> retListNew = new ArrayList<String>();
		    for(FTPFile ftpFile : ftpFilesOld){
		    	retListOld.add(ftpFile.getName());
		    	if(ftpFile.isDirectory()){
		    		traverseContents(ftpFile.getName(),false);
		    	}
		    }
		    for(FTPFile ftpFile : ftpFilesNew){
		    	retListNew.add(ftpFile.getName());
		    }
		    
	 }
    
    
    
}