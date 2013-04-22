import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main
{
	public static ServerSocket serverSocket;
	public static BufferedReader bReader;
	public static PrintWriter pWriter;
	public static String clientMessage;
	
	public static int userId = 0;
	
	public static ArrayList<User> userList = new ArrayList<User>();

	public static void main(String[] args)
	{
		try
		{
			serverSocket = new ServerSocket(Integer.parseInt(Resource.PORT));
		}
		catch (Exception e) { e.printStackTrace(); }
		
		System.out.println("TCPServer Listening on Port: " + Resource.PORT);

		while(true) 
		{
			try
			{
				Socket connected = serverSocket.accept();

				bReader = new BufferedReader(new InputStreamReader (connected.getInputStream()));
				pWriter = new PrintWriter(connected.getOutputStream(), true);
				
				while (true)
				{
					clientMessage = bReader.readLine();
					
					if(clientMessage.contains("/connected"))
					{
						userList.add(new User(userId++, clientMessage.substring(11)));
						pWriter.println("/userlist " + getUserList());
						
						System.out.println(connected.getInetAddress().toString().substring(1) + ":" + connected.getPort() + " connected!");
					}
					else if(clientMessage.contains("/name"))
					{
						pWriter.println("/msg ** " + getUserFromId(Integer.parseInt((clientMessage.split(" ")[1]).split("\\\\")[0])) + " CHANGED THEIR NAME TO " + (clientMessage.split(" ")[1]).split("\\\\")[1].substring(1, (clientMessage.split(" ")[1]).split("\\\\")[1].length()-1) + " **");
						pWriter.println("/update " + updateUser(clientMessage));
					}
					else if(clientMessage.contains("/disconnect"))
						pWriter.println("/remove " + removeUser(clientMessage));
					else
						pWriter.println("/msg " + clientMessage);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public static String getUserList()
	{
		String userStr = "";
		
		for(int i = 0; i < userList.size(); i++)
		{
			int id = userList.get(i).getId();
			String name = userList.get(i).getName();

			userStr = userStr + id + "\\" + name;
			if((i+1) < userList.size())
				userStr = userStr + "\\";
		}

		return userStr;
	}
	
	public static String updateUser(String userString)
	{
		userString = userString.substring(6);
		int id = Integer.parseInt(userString.split("\\\\")[0]);
		String username = userString.split("\\\\")[1];
		if(username.charAt(0) == '"')
			username = username.substring(1, username.length()-1);

		String outputStr = "";

		for(int i = 0; i < userList.size(); i++)
		{
			if(userList.get(i).getId() == id)
			{
				userList.get(i).setName(username);
				outputStr = userList.get(i).getId() + "\\" + userList.get(i).getName();
				break;
			}
		}

		return outputStr;
	}
	
	public static String removeUser(String id)
	{
		for(int i = 0; i < userList.size(); i++)
		{
			if(userList.get(i).getId() == Integer.parseInt(id.split(" ")[1]))
			{
				userList.remove(i);
				break;
			}
		}
		
		return id.split(" ")[1];
	}
	
	public static String getUserFromId(int id)
	{
		for(int i = 0; i < userList.size(); i++)
		{
			if(userList.get(i).getId() == id)
				return userList.get(i).getName();
		}
		
		return null;
	}
}