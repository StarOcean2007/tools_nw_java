package nwTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

public class NWGK {
	private String[] triNoes;
	private String[] gkPathes;
	public void setGKPath(String path){
		gkPathes=path.split(";");
	//	System.out.println(path);
	}
	public void init(){
		triNoes="17484,18676,20740,22826,33951,12937,6702,7224,8121,14717".split(",");
		StringBuilder sbPath=new StringBuilder();
		int[] iSet={400,600,800,1000,1400};
		for(int i:iSet){
			sbPath.append("D:/mpt/流量/").append(i).append("/").append(";");			
		}
		int[] iSet2={50,100,200,500,1000,2000,5000,10000};
		for(int i:iSet2){
			sbPath.append("D:/mpt/淹没工况/").append(i).append("/").append(";");			
		}		
		sbPath.deleteCharAt(sbPath.length()-1);
		setGKPath(sbPath.toString());
		
	}
	int getGKIndexByFile(String fileName){
		return Integer.parseInt(fileName.substring(0, fileName.length()-4));
	}
	String getGKRQByIndex(int rqIndex){
		try{ 
			StringBuilder rq=new StringBuilder();
			int m=20*rqIndex;				
			if(m>=1440){
				rq.append(m/1440).append("天");
				m=m%1440;
			}
			if(m>=60){
				rq.append(m/60).append("小时");
				m=m%60;
			}
			if(m>0){
				rq.append(m).append("分钟");
			} 
			return rq.toString();
	 		
		}
		catch(Exception e){
			 e.printStackTrace();  
		}
		return null;
			
	}
	public List<Object> getDataByOneFile(String path,String fileName) {  
		List<Object> data=new ArrayList<Object>();  
        InputStream fis = null;
        try {  
            // 读取文件的输入流
        	
            fis = new FileInputStream(path+fileName);  
            // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
            DBFReader reader = new DBFReader(fis);  
            // 调用DBFReader对实例方法得到path文件中字段的个数  
      
            Object[] rowValues;  
            int rqIndex=getGKIndexByFile(fileName);
            String gkRQ=getGKRQByIndex(rqIndex);
            int k=0;
            while ((rowValues = reader.nextRecord()) != null) {
            	String no=rowValues[1].toString().split("\\.")[0];
            	boolean isNotValidData=true;
            	for(String filterNo:triNoes){
            		if(filterNo.equals(no)){
            			isNotValidData=false;
            			break;
            		}
            	}
            	if(isNotValidData){
            		continue;
            	}
            	Map<String,Object> map=new LinkedHashMap();
            	map.put("TRI_NO", no);
            	map.put("IDX", rqIndex);
            	map.put("FSRQ", gkRQ);
            	for (int i = 2; i < rowValues.length; i++) {  
            		  DBFField field = reader.getField(i);  
            		  map.put(field.getName().toUpperCase(), rowValues[i]);
                }            	
            	data.add(map);
            } 
            System.out.println(fileName+":"+data.size());
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                fis.close();  
            } catch (Exception e) {  
            }  
        }  
         return data;
    } 
	private Map<String,Object> generateOneGKInfo(String path){
		Map<String,Object> result =new LinkedHashMap<String,Object>();
		List<Object> oneGKData=new ArrayList<Object>();
		List<String> files=  listOneGKFiles(path);
		Collections.sort(files,new Comparator<String>(){
			@Override
			public int compare(String o1,String o2){
				if(o1==null||o2==null||o1.length()<=4||o2.length()<=4){
					return  -1;
				}
				try{
					int k1=Integer.parseInt(o1.substring(0,o1.length()-4));
					int k2=Integer.parseInt(o2.substring(0,o2.length()-4));
					if(k1>k2){
						return 1;
					}else if(k1<k2){
						return -1;
					}
					else
						return 0;
				}
				catch(Exception e){
					
				}
				return 0;
				
			}
		});
		// System.out.println(path+":"+files.size());
		for(String f:files){			 
			 oneGKData.addAll(getDataByOneFile(path,f));
		}
		 result.put("key", path);
		 result.put("data", oneGKData);
		 return result;
	}
	public List<Object> generateGKInfo(){
		List<Object> dataList=new ArrayList<Object>();
		for(String p:gkPathes){
			
			dataList.add(generateOneGKInfo(p));
		}
		return dataList;
	}
	public String  exportTo(){
		List<Object> data=generateGKInfo();
		System.out.println("data:"+data.size());
		Gson gson = new Gson(); 
        return gson.toJson(data);
	}
	 
	   public boolean isValidGKFile(String fileName){
		  return Pattern.compile("[1-9][0-9]*.DBF").matcher(fileName).matches();  
	   }
	   public  List<String>  listOneGKFiles(String path){ 
		   
		   List<String> fileList=new ArrayList<String>();
		   File file=new File(path);
		   if(file.exists()){
			   File[] files=file.listFiles();
			   for(File f:files){
				   if(f.isFile()){
					   if(isValidGKFile(f.getName().toUpperCase())){					    
					   	   fileList.add(f.getName());
					   }
				   }
			   }
		   }
		  
		   return fileList;
	   }
	   
	public static void main(String[] args) {
		NWGK nwgk=new NWGK();
		nwgk.init(); 
		System.out.println(nwgk.exportTo()); 

	}

}
