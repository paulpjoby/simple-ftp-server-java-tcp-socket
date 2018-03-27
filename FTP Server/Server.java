import java.io.*;
import java.net.*;
import java.util.*;

class Server{
        ServerSocket server = null;
	Socket client = null;
	public static void main(String[] arg)
	{
		Server s = new Server();
		s.doConnections();
	}
	public void doConnections(){
		
		try{
			server = new ServerSocket(8888);
			while(true)
			{
			  client = server.accept();
			  ClientThread ct = new ClientThread(client);
			  ct.start();
			}
		}
		catch(Exception e)
		{
		}
	}
}	

class ClientThread extends Thread{
        public Socket client = null;
	public DataInputStream dis = null;
	public DataOutputStream dos = null;
	public FileInputStream fis = null;
	public FileOutputStream fos = null;
	public BufferedReader br = null;
	public String inputFromUser = "";
	public File file = null;

	public ClientThread(Socket c)
	{	try{
		     client = c;
	             dis =new DataInputStream(c.getInputStream());
		     dos = new DataOutputStream(c.getOutputStream());

		}
		catch(Exception e)
		{
			
		}
	}
	public void run()
	{ 
		while(true){
		try{
		       String input = dis.readUTF();
			String filename = "",filedata ="";
			byte[] data;
		        if(input.equals("FILE_SEND_FROM_CLIENT"))
		        {
				filename = dis.readUTF();
				filedata = dis.readUTF();
				fos = new FileOutputStream(filename);
				fos.write(filedata.getBytes());
				fos.close();
		        }
			else if(input.equals("DOWNLOAD_FILE"))
 			{
				filename = dis.readUTF();
				file = new File(filename);
				if(file.isFile())
				{
				       fis = new FileInputStream(file);
				       data = new byte[fis.available()];
					fis.read(data);
					filedata = new String(data);
					fis.close();
					dos.writeUTF(filedata);
				}
				else
				{
					dos.writeUTF(""); // NO FILE FOUND
				}
			}	
			else
			{
				System.out.println("Error at Server");
			}
		}
		catch(Exception e)
		{

		}	
	     }
	}
}
