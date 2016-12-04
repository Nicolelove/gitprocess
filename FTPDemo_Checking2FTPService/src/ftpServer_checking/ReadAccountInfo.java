package ftpServer_checking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ReadAccountInfo {

//	public static void main(String[] args) {
//		
//		readAccountInfoFromTXT("C:/Users/ZHENGNI2/Desktop/ftp_source/ftp_accountInfo.txt");
//	}
	
	public static Map<String,String> readAccountInfoFromTXT(String filePath) {
		String encording="utf-8";
		File pathFile=new java.io.File(filePath);
		Map<String,String> accountMap=new HashMap<String,String>();
		if(pathFile.exists()){
			if(pathFile.isFile()){
				try {
					InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(pathFile), encording);
					BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
					String txtLine="";
					while ((txtLine = bufferedReader.readLine()) != null) {
						if (!txtLine.trim().equals("==") || !txtLine.trim().equals("")) {
							String[] splitUsernamePassword = txtLine.split("==");
							String username = splitUsernamePassword[0];
							String password = splitUsernamePassword[1];
							accountMap.put(username, password);    //if have the same username,map will cover the pre one in last one
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println("ftp account file can not found!");
			}
		}else{
			System.out.println("ftp account file can not found!");
		}
		
		return accountMap;
		
	}

}
