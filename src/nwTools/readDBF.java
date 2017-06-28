package nwTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileInputStream;  
import java.io.InputStream;  
  







import com.google.gson.Gson;
import com.linuxense.javadbf.DBFField;  
import com.linuxense.javadbf.DBFReader; 

public class readDBF {
	   List<Object> data=new ArrayList<Object>();

	   public static void readDBF(String path) {  
	        InputStream fis = null;  
	        try {  
	            // 读取文件的输入流  
	            fis = new FileInputStream(path);  
	            // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
	            DBFReader reader = new DBFReader(fis);  
	            // 调用DBFReader对实例方法得到path文件中字段的个数  
	            int fieldsCount = reader.getFieldCount();  
	            // 取出字段信息  
	            for (int i = 0; i < fieldsCount; i++) {  
	                DBFField field = reader.getField(i);  
	                System.out.println(field.getName());  
	            }  
	            Object[] rowValues;  
	            // 一条条取出path文件中记录  
	            int k=0;
	            while ((rowValues = reader.nextRecord()) != null) {
	            	if(!rowValues[1].toString().startsWith("8121.")){
	            		//System.out.println(rowValues[1].toString());
	            		continue;
	            	}
	            	System.out.println();
	            	System.out.println("第"+(++k)+"条记录");
	            	for (int i = 0; i < rowValues.length; i++) {  
	                	System.out.print(i>0?",":""+i+":");
	                	System.out.print(rowValues[i]);
	                    	                      
	                }	                
	            }  
	            for (int i = 0; i < fieldsCount; i++) {  
	                DBFField field = reader.getField(i);  
	                System.out.println(field.getName());  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                fis.close();  
	            } catch (Exception e) {  
	            }  
	        }  
	    }
	   
	   public static boolean isValidFile(String fileName){
		  return Pattern.compile("[1-9][0-9]*.DBF").matcher(fileName).matches(); 
		 /*  Pattern pattern=Pattern.compile("[1-9][0-9]*.DBF");
		   
		   Matcher isValid=pattern.matcher(fileName);
		   if(isValid.matches()){
			   return true;
		   }
		   return false;
		   */
	   }
	   public static Map<String,Object> getFiles(String path){
		   Map<String,Object> result =new HashMap<String,Object>();
		   result.put("key", path);
		   
		   List<String> fileList=new ArrayList<String>();
		   File file=new File(path);
		   if(file.exists()){
			   File[] files=file.listFiles();
			   for(File f:files){
				   if(f.isFile()){
					   if(isValidFile(f.getName().toUpperCase())){
					   	   System.out.println(f.getAbsolutePath());
					   	   fileList.add(f.getName());
					   }
				   }
			   }
		   }
		   result.put("files", fileList);
		   return result;
	   }
	   public static void readDBF(Map map) {  
	       
	        for (String f :(List<String>) map.get("files")){
	        	 String path=map.get("key").toString()+File.separatorChar+f;
	        
		        InputStream fis = null;
		        try {  
		            // 读取文件的输入流
		        	
		            fis = new FileInputStream(path);  
		            // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
		            DBFReader reader = new DBFReader(fis);  
		            // 调用DBFReader对实例方法得到path文件中字段的个数  
		            int fieldsCount = reader.getFieldCount();  
		            // 取出字段信息  
		            for (int i = 0; i < fieldsCount; i++) {  
		                DBFField field = reader.getField(i);  
		                System.out.println(field.getName());  
		            }  
		            Object[] rowValues;  
		            // 一条条取出path文件中记录  
		            int k=0;
		            while ((rowValues = reader.nextRecord()) != null) {
		            	if(!rowValues[1].toString().startsWith("8121.")){
		            		//System.out.println(rowValues[1].toString());
		            		continue;
		            	}
		            	System.out.println();
		            	System.out.println("第"+(++k)+"条记录");
		            	for (int i = 0; i < rowValues.length; i++) {  
		                	System.out.print(i>0?",":""+i+":");
		                	System.out.print(rowValues[i]);
		                    	                      
		                }	                
		            }  
		            for (int i = 0; i < fieldsCount; i++) {  
		                DBFField field = reader.getField(i);  
		                System.out.println(field.getName());  
		            }  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        } finally {  
		            try {  
		                fis.close();  
		            } catch (Exception e) {  
		            }  
		        }  
	        }
	    } 
	    public static void main(String[] args) {  

	        //readDBF.readDBF("D:/mpt/流量/400/1.DBF");
	    	System.out.println();
	    	Gson gson = new Gson();
	    	Map<String,Object> obj=getFiles("D:/mpt/流量/400");
	    	readDBF(obj);
	        String str = gson.toJson(obj);
	    	System.out.println(str);
	    	
	    	System.out.println(File.separatorChar);
	    }  


}
