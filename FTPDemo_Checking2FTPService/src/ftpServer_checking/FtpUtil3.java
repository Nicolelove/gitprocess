package ftpServer_checking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class FtpUtil3 {

	private FTPClient ftpClient = new FTPClient();
	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;
	private String local_fileEncoding = System.getProperty("file.encoding");

	/**
	 * 使用详细信�?�进行�?务器连接
	 * 
	 * @param server：�?务器地�?��??称
	 * @param port：端�?��?�
	 * @param user：用户�??
	 * @param password：用户密�?
	 * @param path：转移到FTP�?务器目录
	 * @param isNew
	 *            true 表示连接到new server ,false 表示old server
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public FTPClient connectServer(String server, int port, String user, String password, String path, boolean isNew)
			throws SocketException, IOException {
		ftpClient = new FTPClient();
		ftpClient.connect(server, port);
		if (ftpClient.login(user, password)) {
//			System.err.println("FTP server refused login.");
//			System.exit(1);
			if (isNew) {
				System.out.println("Login New Server : " + server + "." + " --passed");
			} else {
				System.out.println("Login Old Server : " + server + "." + " --passed");
			}
		}else{
			if (isNew) {
				System.err.println("Login New Server : " + server + "." + " --No pass");
				System.exit(1);
			} else {
				System.err.println("Login Old Server : " + server + "." + " --No pass");
				System.exit(1);
			}
		}

		if (path != null && path.length() != 0) {
			ftpClient.changeWorkingDirectory(path);
		}
		ftpClient.setBufferSize(1024);// 设置上传缓存大�?
		// String encoding = System.getProperty("file.encoding"); //获�?�系统默认编�?格�?
		ftpClient.setControlEncoding(local_fileEncoding);// 设置编�?
		FTPClientConfig ftpClientConfig = new FTPClientConfig(FTPClientConfig.SYST_NT);
		ftpClientConfig.setServerLanguageCode("zh");
		ftpClient.setFileType(BINARY_FILE_TYPE);// 设置文件类型,默认使用的是ASCII编�?的,对于传输图片必须是二进制文件，�?�则上传图片会是一片彩色

		return ftpClient;
	}
	
	

	/**
	 * 设置传输文件类型:FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE 二进制文件或文本文件
	 * 
	 * @param fileType
	 * @throws IOException
	 */
	public void setFileType(int fileType) throws IOException {
		ftpClient.setFileType(fileType);
	}

	/**
	 * 关闭连接
	 * 
	 * @throws IOException
	 */
	public void closeServer() throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.logout();// 退出FTP�?务器
			ftpClient.disconnect();// 关闭FTP连接
		}
	}
	
	public void closeServer(FTPClient ftpClientOld,FTPClient ftpClientNew) throws IOException {
		if (ftpClientOld != null && ftpClientOld.isConnected()) {
			ftpClientOld.logout();// 退出FTP�?务器
			ftpClientOld.disconnect();// 关闭FTP连接
		}
		if (ftpClientNew != null && ftpClientNew.isConnected()) {
			ftpClientNew.logout();// 退出FTP�?务器
			ftpClientNew.disconnect();// 关闭FTP连接
		}
		System.out.println("logout----");
	}

	/**
	 * 转移到FTP�?务器工作目录
	 * 
	 * @param path
	 *            指定工作目录
	 * @return
	 * @throws IOException
	 */
	public boolean changeDirectory(String path) throws IOException {
		return ftpClient.changeWorkingDirectory(path);
	}

	/**
	 * 删除�?务器上的文件
	 * 
	 * @param pathName
	 *            指定删除文件目录
	 * @return
	 * @throws IOException
	 */
	public void deleteFile(String pathName) throws IOException {
		String path = "";
		pathName = replace2Fileseparator(pathName);
		if (pathName.indexOf(File.separator) > -1) {
			path = pathName.substring(0, pathName.lastIndexOf(File.separator));
			if (path != null && !path.equals("")) {
				// 对于删除�?�根止录下的文件，应该先转到相应的目录下�?�?�以删除文件
				ftpClient.changeWorkingDirectory(new String(path.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
			}
		}
		if (ftpClient.deleteFile(new String(pathName.getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING))) {
			System.out.println("delete ftp file: " + pathName + "  -- passed");
		} else {
			System.err.println("delete ftp file: " + pathName + "  -- No pass");
			System.exit(1);
		}
	}

	/**
	 * 上传文件到ftp�?务器 在进行上传和下载文件的时候，设置文件的类型最好是：
	 * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
	 * 
	 * @param localFilePath
	 *            本地文件路径和�??称
	 * @param remoteFileName
	 *            �?务器文件�??称
	 * @param isRename
	 *            是�?��?命�??
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(String localFilePath, String remoteFileName, boolean isRename) throws IOException {
		boolean flag = false;
		InputStream iStream = null; // 建立一个文件输出�?对象
		localFilePath = replace2Fileseparator(localFilePath); // 预处�?�路径
		remoteFileName = replace2Fileseparator(remoteFileName);
		try {
			if (remoteFileName == null || remoteFileName.equals("")) {
				// ftpClient.changeWorkingDirectory("/");
				ftpClient.changeWorkingDirectory(File.separator);
				remoteFileName = localFilePath.substring(localFilePath.lastIndexOf(File.separator));
			} else if (!remoteFileName.contains(File.separator)) {
				ftpClient.changeWorkingDirectory(File.separator);
			} else {
				if(new File(remoteFileName).isDirectory()){
					ftpClient.changeWorkingDirectory(new String(remoteFileName.getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING));
				}else{
					ftpClient.changeWorkingDirectory(
							new String(remoteFileName.substring(0, remoteFileName.lastIndexOf(File.separator))
									.getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING));
				}
			}
			
			iStream = new FileInputStream(localFilePath);
			// 我们�?�以使用BufferedInputStream进行�?装
			BufferedInputStream bis = new BufferedInputStream(iStream);
			// utf-8,这个�?是固定的值，是根�?�系统本身默认的编�?�?�定的
			flag = ftpClient.storeFile(
					new String(remoteFileName.getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING), bis); // 将Bis输入�?的东西上传到ftp�?务器的fileName文件中，在这里我们对fileName文件�??进行了转�?,解决了中文文件�??�?能上传的问题
			if (flag) {
				if (!isRename) {
					System.out.println("upload file to new server: " + remoteFileName + "  -- passed");
				} else {
					System.out.println("upload and rename ftp file name , from: "
							+ localFilePath.substring(localFilePath.lastIndexOf(File.separator)+1) + " To "
							+ remoteFileName + "  -- passed");
				}
			} else {
				if (!isRename) {
					System.err.println("upload file to new server: " + remoteFileName + "  -- NO pass");
				} else {
					System.err.println("upload and rename ftp file name, from : "
							+ localFilePath.substring(localFilePath.lastIndexOf(File.separator)+1) + " To "
							+ remoteFileName + "  -- No pass");
				}
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println(localFilePath+"localFilePath to upload not exist!  --No pass");
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
	 * 从ftp�?务器上下载文件到本地
	 * 
	 * @param fileDirPath
	 *            �?务器文件所在路径
	 * @param remoteFileName
	 *            下载文件�??
	 * @param localFileName
	 *            本地存放路径
	 * @return
	 * @throws IOException
	 */
	public boolean download(String fileDirPath, String remoteFileName, String localFileName) throws IOException {
		boolean flag = false;
		if (localFileName != null && !localFileName.equals("")) {
			fileDirPath = replace2Fileseparator(fileDirPath);
			localFileName = replace2Fileseparator(localFileName);
			// FileOutputStream fos1=new FileOutputStream(localFileName,false);
			// true表示如果文件已�?存在，�?执行程�?一次便往文件追加一次内容，�?�则会�?一次的执行结果会覆盖文件上一次执行结果
			File outfile = new File(localFileName);
			if (outfile.isDirectory() && localFileName.endsWith(File.separator)) {
				localFileName = localFileName + remoteFileName;
				outfile = new File(localFileName);
			} else if (outfile.isDirectory() && !localFileName.endsWith(File.separator)) {
				localFileName = localFileName + File.separator + remoteFileName;
				outfile = new File(localFileName);
			}
			OutputStream oStream = null;
			try {
				// 在对ftp�?务器�?�根目录下的文件进行�?作的时候一定�?先跳转到相应的工作目录下，�?然retrieveFile()返回的总是false，如果�?作的文件是在根目下就�?�以�?略这一步
				ftpClient.changeWorkingDirectory(new String(fileDirPath.getBytes(local_fileEncoding), "ISO-8859-1"));
				oStream = new FileOutputStream(outfile);
				// 我们�?�以使用BufferedOutputStream进行�?装,�?装�?具有缓冲功能的文件输出�?
				BufferedOutputStream bos = new BufferedOutputStream(oStream);
				flag = ftpClient.retrieveFile(
						new String(remoteFileName.getBytes(local_fileEncoding), FTP.DEFAULT_CONTROL_ENCODING), bos); // 从�?务器检索命�??文件并将其写入给定的OutputStream中,这里必须对remoteFileName进行转�?，�?然处�?��?了中文文件�??
				bos.flush(); // 当我们使用输出�?�?��?数�?�时，当数�?��?能填满输出�?的缓冲区时，这时，数�?�就会被存储在输出�?的缓冲区中;如果，我们这个时候调用关闭(close)输出�?，存储在输出�?的缓冲区中的数�?�就会丢失。所以说，关闭(close)输出�?时，应先刷新(flush)�?�冲的输出�?
				if (flag) {
					if(fileDirPath.endsWith(File.separator)){
					    System.out.println("download ftp file: " + fileDirPath + remoteFileName + "   -- passed");
					}else{
						System.out.println("download ftp file: " + fileDirPath + File.separator + remoteFileName + "   -- passed");
					}
				} else {
					if(fileDirPath.endsWith(File.separator)){
					    System.err.println("download ftp file: " + fileDirPath + remoteFileName + "  -- No pass");
					}else{
						System.err.println("download ftp file: " + fileDirPath + File.separator + remoteFileName + "  -- No pass");
					}
					System.exit(1);
				}
			} catch (IOException e) {
				flag = false;
				return flag;
			} finally {
				oStream.close(); // 关闭文件输出�?
			}
		} else {
			System.out.println("localpath cannot is empty!");
		}
		return flag;
	}

	/**
	 * 
	 * @param path
	 *            需�?进行replace的路径
	 * @return
	 */
	public String replace2Fileseparator(String path) {
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
	 * 
	 * @param filePathOld   本地file path
	 * @param filePathNew   从server上download 到本地的file path
	 * @return
	 * @throws Exception
	 */
	public boolean getFileMD5(String filePathOld, String filePathNew) throws Exception {

		File fileOld = new File(filePathOld);
		File fileNew = new File(filePathNew);
		InputStream inputStreamOld = new FileInputStream(fileOld);
		InputStream inputStreamNew = new FileInputStream(fileNew);
		MessageDigest messageDigestOld = MessageDigest.getInstance("MD5");
		MessageDigest messageDigestNew = MessageDigest.getInstance("MD5");
		byte bufferOld[] = new byte[1024];
		int len;
		while ((len = inputStreamOld.read(bufferOld)) != -1) {
			messageDigestOld.update(bufferOld, 0, len);

		}
		while ((len = inputStreamNew.read(bufferOld)) != -1) {
			messageDigestNew.update(bufferOld, 0, len);

		}
		BigInteger bigIntegerOld = new BigInteger(1, messageDigestOld.digest());
		BigInteger bigIntegerNew = new BigInteger(1, messageDigestNew.digest());
		if (bigIntegerOld.toString().equals(bigIntegerNew.toString())) {
			System.out.println("compare file context : -- passed");
			return true;
		}
		System.err.println("compare file context : -- No pass");
		System.exit(1);
		return false;

	}

	/**
	 * compare the context structure if is same completed in old and new
	 * 
	 * @param ftpClientOld
	 * @param ftpClientNew
	 * @return
	 * @throws Exception
	 */

	List<String> retList = new ArrayList<String>();

	public List<String> getFileList(String path) throws IOException {
		retList.add(path);
		FTPFile[] ftpFiles = ftpClient.listFiles(path);
		if (ftpFiles == null || ftpFiles.length == 0) {
			return retList;
		}
		for (FTPFile ftpFile : ftpFiles) {
			if (ftpFile.isDirectory()) {
				if (path.endsWith(File.separator) || path.equals("/")) {
					getFileList(path + ftpFile.getName());
				} else {
					getFileList(path + File.separator + ftpFile.getName());
				}
			}
		}
		return retList;
	}

}
