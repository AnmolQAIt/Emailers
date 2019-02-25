import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

public class CheckEmail implements Runnable{
	
	public static String link;
	File out; 
	Set<String> links=new HashSet<String>();
	public CheckEmail(String link,File out)
	{
		this.out=out;
		this.link=link;
	}
	 public static Set <String> URLStringlist=new HashSet<String>();
	public static Set<String> check(String host,String mailtype,String uname,String pswd)
	{
		try {
		
		Address match=new InternetAddress("dena.bachman@cengage.com");
		Properties properties = new Properties();
          
	      properties.put("mail.imaps.host", host);
	      properties.put("mail.imaps.port", "993");
	      properties.put("mail.imaps.starttls.enable", "true");
	      Session emailSession = Session.getDefaultInstance(properties);
	      
	        Store store;
			store = emailSession.getStore("imaps");
			store.connect(host, uname, pswd);
			Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      Message[] messages= emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN),false));
		     
		      System.out.println("messages.length---" + messages.length);

		      for (int i = 0; i<messages.length; i++) {
		         Message message = messages[i];
		         if((message.getSubject().contains("QA Status Final || IWB || Life AmE 2e CPT Level 4 || Round 1 || Day 3/3 || 02202019")))
		         { System.out.println("---------------------------------");
		         System.out.println(message.getContent().toString());
		         System.out.println("Email Number " + (i+1));
		   
		         writePart(message);
		         System.out.println("Text: " + message.getContent().toString());
		         }
		      }

		      
		      emailFolder.close(false);
		      store.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		System.out.println("The URL STRINGS LIST");
		for(String s:URLStringlist)
			System.out.println("URL STRING LIST:::"+s);
		return URLStringlist;
		   	      
	}
	
	public static void printdetails(Message message) throws MessagingException
	{
		 System.out.println("Subject: " + message.getSubject());
         System.out.println("From: " + message.getFrom()[0]);
	}
	
	public static Set<String> match_regex(String str) throws Exception
	{   BufferedWriter bw;
	File fout = new File("C:\\Users\\anmolaggarwal\\Desktop\\Test\\out.txt");
	FileOutputStream fos = new FileOutputStream(fout);
	 
	 bw = new BufferedWriter(new OutputStreamWriter(fos));
		Set<String> stri = new HashSet<String>();
		Pattern pattern = Pattern.compile("https:\\/\\/(.*?)Flash.zip");   
		Matcher matcher = pattern.matcher(str);
		int i=0;
		
		while (matcher.find())
		{
			str=matcher.group(0);
			stri.add(str);
			i++;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\anmolaggarwal\\Desktop\\Test\\out.txt") ))) {
		    String line;
		    while ((line = br.readLine()) != null)
		    {
		    for(String s:stri)
		    {
		    	if(s!=line)
		    		
		    	{ bw.newLine();
		    		bw.write(s);}
		    }
		    }
		}

		    for(String s:stri){
			bw.write(s);
			bw.newLine();
			}
		    bw.close();
		
		System.out.println("the matching regex count is::::::::::"+i);
		for(String s:stri)
		{  
			System.out.println("The link of .zip files are ::::::::"+s);
		}
			return stri;
			
		
	}
	
	public static void writePart(Part p) throws Exception {
	      if (p instanceof Message)
	         

	      System.out.println("----------------------------");
	      System.out.println("CONTENT-TYPE: " + p.getContentType());

	      //if normal text
	      if (p.isMimeType("text/plain")) {
	         System.out.println("This is plain text");
	         System.out.println("---------------------------");
	         String str=((String) p.getContent());
	         
              URLStringlist=match_regex(str);
	         //System.out.println(URLString);
	         
	      } 
	      //Now I am checking the attachment
	      else if (p.isMimeType("multipart/*")) {
	         System.out.println("This is a Multipart");
	         System.out.println("---------------------------");
	         Multipart mp = (Multipart) p.getContent();
	         int count = mp.getCount();
	         for (int i = 0; i < count; i++)
	            writePart(mp.getBodyPart(i));
	      } 
	      
	    /*  else if (p.isMimeType("message/rfc822")) {
	         System.out.println("This is a Nested Message");
	         System.out.println("---------------------------");
	         writePart((Part) p.getContent());
	      } 
	      //Now inline image
	     else if (p.isMimeType("image/jpeg")) {
	         System.out.println("--------> image/jpeg");
	         Object o = p.getContent();

	         InputStream x = (InputStream) o;
	         // Construct the required byte array
	         System.out.println("x.length = " + x.available());
	         while ((i = (int) ((InputStream) x).available()) > 0) {
	            int result = (int) (((InputStream) x).read(bArray));
	            if (result == -1)
	         int i = 0;
	         byte[] bArray = new byte[x.available()];

	            break;
	         }
	         FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
	         f2.write(bArray);
	      } */
	      
	      //if image
	      else if (p.getContentType().contains("image/")) {
	         System.out.println("content type" + p.getContentType());
	         File f = new File("image" + new Date().getTime() + ".jpg");
	         DataOutputStream output = new DataOutputStream(
	            new BufferedOutputStream(new FileOutputStream(f)));
	            com.sun.mail.util.BASE64DecoderStream test = 
	                 (com.sun.mail.util.BASE64DecoderStream) p
	                  .getContent();
	         byte[] buffer = new byte[1024];
	         int bytesRead;
	         while ((bytesRead = test.read(buffer)) != -1) {
	            output.write(buffer, 0, bytesRead);
	         }
	      } 
	    /*  else {
	         Object o = p.getContent();
	         if (o instanceof String) {
	            System.out.println("This is a string");
	            System.out.println("---------------------------");
	            System.out.println((String) o);
	         } 
	         else if (o instanceof InputStream) {
	            System.out.println("This is just an input stream");
	            System.out.println("---------------------------");
	            InputStream is = (InputStream) o;
	            is = (InputStream) o;
	            int c;
	            while ((c = is.read()) != -1)
	               System.out.write(c);
	         } 
	         else {
	            System.out.println("This is an unknown type");
	            System.out.println("---------------------------");
	            System.out.println(o.toString());
	         }
	      }*/
	}

public static void main(String[] args) {
	String host = "webmail.qainfotech.com";
    String mailtype = "imaps";
    String uname = "anmolaggarwal@qainfotech.com";
    String pswd = "Anmolkiet@18";
	
	 File out=new File("C:\\Users\\anmolaggarwal\\Desktop\\Test\\ABC"+System.currentTimeMillis()+".zip");
	 Set<String> links=check(host,mailtype,uname,pswd);
	 for(String Unique_Link:links)
	 new Thread(new CheckEmail(Unique_Link, out)).start();
	
}

@Override
public void run() 
{
	synchronized (URLStringlist) {
		
	
	for(String newstring:URLStringlist)
	{
	try {		
		URL url=new URL(newstring);
		HttpURLConnection http=(HttpURLConnection)url.openConnection();
		double filesize=(double)http.getContentLengthLong();
		BufferedInputStream in=new BufferedInputStream(http.getInputStream());
		FileOutputStream fos=new FileOutputStream(this.out);
		BufferedOutputStream bout=new BufferedOutputStream(fos, 1024);
		byte[] buffer=new byte[1024];
		double downloaded=0.00;
		int read=0;
		double percentdownloaded=0.00;
		while((read=in.read(buffer, 0, 1024))>=0)
		{
			bout.write(buffer, 0, read);
			downloaded+=read;
			percentdownloaded=(downloaded*100)/filesize;
			String percent=String.format("%.4f",percentdownloaded);
			System.out.println("Downloaded"+percent+"% of file");
		}
		bout.close();
		in.close();
		System.out.println("Download complete");
	} catch (Exception e) {
		e.printStackTrace();
	}}
}}
}
