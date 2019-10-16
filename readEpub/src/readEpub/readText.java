package readEpub;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class readText {

	public static void main(String[] args) {
		int cnt =0;
		try{
            //파일 객체 생성
			int flag = 2;
			String outPut ="";
			File folder;
			File file;
			if(flag==1){//개역개정 1, 새번역 2
				outPut = "/Users/JinhoLee/Downloads/개역개정.sql";
				folder = new File("/Users/JinhoLee/Documents/Project/wildbluffers/문서/file/개역개정/OEBPS/Text/");
			}else{
				outPut = "/Users/JinhoLee/Downloads/새번역.sql";
				folder = new File("/Users/JinhoLee/Documents/Project/wildbluffers/문서/file/새번역/OEBPS/Text/");
			}		
			String[] gospels = {"창세기","출애굽기","레위기","민수기","신명기","여호수아","사사기","룻기","사무엘상","사무엘하","열왕기상","열왕기하","역대상","역대하","에스라","느헤미야","에스더","욥기","시편","잠언","전도서","아가","예레미야","예레미야애가","에스겔","다니엘","호세아","요엘","아모스","오바댜","요나","미가","나훔","하박국","스바냐","학개","스가랴","말라기"};
			
			Scanner in = null;
	        PrintWriter out = null;
	        
            String a = new String();
            int b = 0;
            File[] listOfFiles = folder.listFiles();
            int NumberOfFiles = folder.listFiles().length;
            String FilesName[] = new String[NumberOfFiles]; 
            String version = ""; //  새번역
            String type    = ""; // 구약 신약
            //String seq 	   = "wildbluffers"; 
            String gospel  = ""; //복음서
            String subtitle = ""; //소제목
            String chapter = ""; // 
            String verse   = "";
            String lang = "KR";
            String contents = ""; // 내용 
            Pattern p;
            Matcher m;
            
            //System.out.println(NumberOfFiles);
            
          
            for (File file1 : listOfFiles) {
            	if (file1.isFile()) {            		
                	FilesName[cnt] = file1.getName();
                	cnt++;        	
                }
            }
  
           Arrays.sort(FilesName);
           //System.out.println(Arrays.toString(FilesName));
            out = new PrintWriter(outPut);
            for(int j =0; j<cnt;j++){
            b=0;
            //System.out.println("1");
            if(flag==1){
            	file = new File("/Users/JinhoLee/Documents/Project/wildbluffers/문서/file/개역개정/OEBPS/Text/"+FilesName[j]);
            	//System.out.println(FilesName[j]);
            	
            }else{
            	file = new File("/Users/JinhoLee/Documents/Project/wildbluffers/문서/file/새번역/OEBPS/Text/"+FilesName[j]);
            }

            in = new Scanner(file);
            if(FilesName[j].indexOf("Section") == -1){
            	continue;
            }
            //System.out.println(file);
            
            
            while(in.hasNextLine()){
            	b++;
            	a= "[Line : "+b+"]"+in.nextLine();
            	if(b==1)verse = "1";
            	
           //System.out.println(a);
            	
            	if(b<4){
            		
            		continue;
            	
            	}else if(b==4){
            		//<title>VERSION        </title>
            		p = Pattern.compile("<title>(.*?)<\\/title>");
            		m = p.matcher(a);
            		while(m.find()){
            			//System.out.println(m.group(1));
            			version = m.group(1) ;
            			break;
            		}
            	continue;
            	}else if(4<b && b<14){
            		
            		continue;
            		
            	}else if(b==14){
            		
            		p = Pattern.compile(".xhtml\">(.*?)<\\/a><\\/p>");
            		m = p.matcher(a);
            		while(m.find()){
            			//System.out.println(m.group(1));
            			gospel = m.group(1) ;
            			break;
            		}
            	//System.out.println(gospel);
            	continue;
            	
            	}else if(b==15){

            		continue;
            	
            	}else if(b==16){

            		p = Pattern.compile(".xhtml\">(.*?)<span class");
            		m = p.matcher(a);
            		while(m.find()){
            			//System.out.println(m.group(1));
            			chapter = m.group(1) ;
            			break;
            		}

            	continue;
            	}else if(16<b && b<19){

            		continue;
            		
            	}else {
            		if(a.indexOf("<p")<0 || a.indexOf("end")>0) continue;
            		if(a.indexOf("subtitle")>0){
            		p = Pattern.compile("subtitle\">(.*?)&#160;<\\/p>");
            		m = p.matcher(a);
            		while(m.find()){
            			//System.out.println(m.group(1));
            			subtitle = m.group(1) ;
            			break;
            		}
            		continue;
	            	}else{

	                		p = Pattern.compile("\">(.*?)&#160");
	                		m = p.matcher(a);
	                		while(m.find()){
	                			//System.out.println(m.group(1));
	                			verse = m.group(1) ;
	                			break;
	                		}
	                		if(verse.equals("")) continue;
	                		if(flag==1){
	                			p = Pattern.compile("<\\/a>(.*?)</span");
	                		}else{
	                			p = Pattern.compile("<\\/a>(.*?)</p");
	                		}              		
	                		m = p.matcher(a);
	                		while(m.find()){
	                			//System.out.println(m.group(1));
	                			contents = m.group(1) ;
	                			contents = contents.replaceAll("\\(없음\\)", "");
	                			contents = contents.replaceAll("<span class=\"nothing\">", "");
	                			contents = contents.replaceAll("</span>", "");
	                			contents = contents.replaceAll("'", "\"");
	                			
	                			break;
	                		}
	            		
	            		}
            	}
            	//System.out.println(a);
            	//System.out.println(version+type+seq+gospel+subtitle+chapter+verse+lang+contents);
            	for(int i = 0; i < gospels.length; i++){
	            	if(gospels[i].equals(gospel)){
	            		type = "BC";
	            		break;
	            	}else{
	            		type = "AD";
	            	}
            	}

            	System.out.println(String.format("insert into TB_BIBLE (VERSION,TYPE,GOSPEL,SUBTITLE,CHAPTER,VERSE,LANG,CONTENTS) values('%s','%s','%s','%s','%s','%s','%s','%s')", version,type,gospel,subtitle,chapter,verse,lang,contents));
            	out.write(String.format("insert into TB_BIBLE (VERSION,TYPE,GOSPEL,SUBTITLE,CHAPTER,VERSE,LANG,CONTENTS) values('%s','%s','%s','%s','%s','%s','%s','%s');\n", version,type,gospel,subtitle,chapter,verse,lang,contents));
            }
            
            
            }
             in.close();
            out.close();  
            
        }catch (Exception e){
        	e.printStackTrace();
        }


		
	}

}

