package musichub.server;

public class MusicHubServer {
	private ServerInstance serv;
	
	public MusicHubServer() {
		int port = 2109;
		this.serv = new ServerInstance(port);
	}
}
