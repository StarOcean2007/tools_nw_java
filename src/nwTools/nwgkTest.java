package nwTools;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class nwgkTest {
	NWGK nwgk ;
	@Test
	public void test() {
		nwgk=new NWGK();
		nwgk.init();
		List<String> files=  nwgk.listOneGKFiles("D:/mpt/流量/400/");
		for(String f:files){
			System.out.println(f);
		}
		System.out.println(" SORT------------------------");
		Collections.sort(files);
		for(String f:files){
			System.out.println(f);
		}
		System.out.println(" SORT2------------------------");
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
		for(String f:files){
			System.out.println(f);
		}
	}

}
