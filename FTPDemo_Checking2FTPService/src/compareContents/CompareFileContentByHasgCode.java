package compareContents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * compare both file if is same completed by MD5
 * @author Administrator
 *
 */
public class CompareFileContentByHasgCode {

	public static void main(String[] args) throws Throwable{
//		File fileold=new File("C:/Users/Administrator/Desktop/test/account.txt");
//		File fileNew=new File("C:/Users/Administrator/Desktop/test/account1.txt");
//		if(fileold.hashCode()==fileNew.hashCode()){
//			System.out.println("两文件内容完全一致");
//		}else{
//			System.out.println("文件内容�?一致");
//		}
		
		
//		String fileold=getFileMD5("C:/Users/Administrator/Desktop/test/account.txt");
//		String filenew=getFileMD5("C:/Users/Administrator/Desktop/test/account1.txt");
//		String fileold=getFileMD5("C:/Users/Administrator/Desktop/test/account2.xml");
//		String filenew=getFileMD5("C:/Users/Administrator/Desktop/test/account.xml");
//		System.out.println(fileold.equals(filenew)+"         "+filenew+" == "+fileold);
		
		
//		String string1="123456zxc";
//		String string2="123456zxcqw";
//		if(string1.hashCode()==string2.hashCode()){
//			System.out.println("内容一致�?");
//		}else{
//			System.out.println("内容�?一致�?�?");
//		}
		
		
		
		
		/*
		 * test path structure
		 */
//		File file=new File("C:/Users/ZHENGNI2/Desktop/ftp_source/harry");
//		Document doc = DocumentHelper.createDocument();
//        Element el = doc.addElement(file.getName());
//		el=getFile(file,el);
//		File d = new File("C:/Users/ZHENGNI2/Desktop/ftp_source/test.xml");
//        if(d.exists()){
////            d.createNewFile();
//            FileOutputStream fos = new FileOutputStream(d);
//            fos.write(doc.asXML().getBytes());
//            fos.flush();
//            fos.close();
//        }
		
		MethodToCompareFileContent methodToCompareFileContent=new MethodToCompareFileContent();
		MethodToCompareFileContent methodToCompareFileContent2=new MethodToCompareFileContent();
		List pathList1=new ArrayList<String>();
		List pathList2=new ArrayList<String>();
		pathList1=methodToCompareFileContent.traverseContents("C:/Users/ZHENGNI2/Desktop/test",false);
		pathList2=methodToCompareFileContent.traverseContents("C:/Users/ZHENGNI2/Desktop/test",false);
//		pathList2=methodToCompareFileContent2.traverseContents("C:/Users/ZHENGNI2/Desktop/ARTIFURN",false);
		for(int i=0;i<pathList1.size();i++){
			System.out.println("path===="+pathList1.get(i));
		}
		for(int i=0;i<pathList2.size();i++){
			System.out.println("path2===="+pathList2.get(i));
		}
			if (pathList1.hashCode()==pathList2.hashCode()) {
				System.out.println("compare ftp folder structure: passed");
			}else{
				System.err.println("compare ftp folder structure: NO pass");
			}
		
		
		
		

	}
	
	public static String getFileMD5 (String filePath) throws Exception{
		File file=new File(filePath);
		InputStream inputStream=new FileInputStream(file);
		MessageDigest messageDigest=MessageDigest.getInstance("MD5");
		byte buffer[]=new byte[1024];
		int len;
		while ((len=inputStream.read(buffer))!=-1) {
			messageDigest.update(buffer,0,len);
			
		}
		BigInteger bigInteger=new BigInteger(1,messageDigest.digest());
		return bigInteger.toString();
		
	}
	
	/**
	 * get path structure return xml
	 * @param file
	 * @param el
	 * @return
	 */
	public static Element getFile(File file, Element el){
        try {
            File[] list = file.listFiles();
            if(list != null && list.length > 0){
                for(File f : list){
                    if(f.isDirectory()){//目录
                        Element e = el.addElement("Folder");
                        e.addAttribute("name", f.getName());
                        getFile(f, e);
                    }else{//文件
                        Element e = el.addElement("File");
                        e.addAttribute("name", f.getName());
                    }
                    System.out.println(el.asXML());
                }
            }
            return el;
        } catch (Exception e) {
            System.err.println("异常"+e);
        }
        return null;
    }
	
//	 List allPathList=new ArrayList<String>();
//	//traverse filepath,and can check contents structure 
//	 public List traverseContents(String path,boolean isRecursion){
//		 if (!isRecursion) {
//			allPathList.clear();
//		}
//	    	File file=new File(path);
//	    	List pathList=new ArrayList<String>();
//	    	if(file.exists()){
//	    		File[] files=file.listFiles();
//	    		if(files.length==0){
//	    			System.err.println("the folder is empty!");
//	    			return null;
//	    		}else{
//	    			for(File file2 : files){
//	    				if(file2.isDirectory()){
//	    					traverseContents(file2.getAbsolutePath(),true);
//	    				}else{
////	    					System.out.println(file2.getAbsolutePath());
//	    					pathList.add(file2.getAbsolutePath());
//	    				}
//	    				
//	    			}
//	    			for(int i=0;i<pathList.size();i++){
////	    				System.out.println("path===="+pathList.get(i));
//	    				allPathList.add(pathList.get(i));
//	    			}
//	    			return allPathList;
//	    		}
//	    	}else{
//	    		System.err.println("the path not exist!");
//	    		return null;
//	    	}
//	    	
//	    }
	
	

}
