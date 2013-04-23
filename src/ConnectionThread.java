import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionThread
{
	Thread thread;
	BufferedReader bReader;
	PrintWriter pWriter;
	
	public ConnectionThread(final int id, final Socket socket)
	{
		try
		{
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pWriter = new PrintWriter(socket.getOutputStream(), true);
		}
		catch(Exception e) { e.printStackTrace(); }

		thread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					System.out.println(socket.getInetAddress() + " " + socket.getPort());
					//Socket newConnection = new Socket(socket.getInetAddress().toString(),(Integer.parseInt(Resource.PORT) + id));
					
					while(true)
					{
						String clientMessage = bReader.readLine();
						
						if(clientMessage.contains("/connected"))
						{
							Main.userList.add(new User(id, clientMessage.substring(11)));
							Main.writeToAll("/userlist " + Main.getUserList());
						}
						else if(clientMessage.contains("/name"))
						{
							Main.writeToAll("/console ** " + Main.getUserFromId(Integer.parseInt((clientMessage.split(" ")[1]).split("\\\\")[0])) + " CHANGED THEIR NAME TO " + Main.parseName(clientMessage) + " **");
							Main.writeToAll("/update " + Main.updateUser(clientMessage));
						}
						else if(clientMessage.contains("/file"))
							Main.receiveAndBounceMessage(clientMessage, socket);
						else if(clientMessage.contains("/disconnect"))
							Main.writeToAll("/remove " + Main.removeUser(clientMessage));
						else
							Main.writeToAll("/msg " + clientMessage);
					}
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		};
		thread.start();
	}
	
	public void writeToClient(String message)
	{
		pWriter.println(message);
	}
}