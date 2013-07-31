package auctionsniper;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class Main {
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final int ARG_ITEM_ID = 3;
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;

	public static final String STATUS_JOINING = "Joining";

	public static final String STATUS_LOST = "Lost";

	private MainWindow ui;

	public Main() throws Exception {
		startUserInterface();
	}

	@SuppressWarnings("unused")
	private Chat notToBeGCd;

	

	private void joinAuction(XMPPConnection connection, String itemId)
			throws XMPPException {
		final Chat chat = connection.getChatManager().createChat(
				auctionId(itemId, connection), new MessageListener() {
					public void processMessage(Chat aChat, Message message) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								ui.showStatus(Main.STATUS_LOST);
							}
						});
					}
				});

		this.notToBeGCd = chat;
		chat.sendMessage(new Message());
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow();
			}
		});

	}

	private static XMPPConnection connection(String hostname, String username,
			String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		return connection;
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		
		return String.format(AUCTION_ID_FORMAT, itemId, connection
				.getServiceName());
	}
	
	public static void main(String... args) throws Exception {
		
		Main main = new Main();
	
		main.joinAuction(connection(args[ARG_HOSTNAME],
				args[ARG_USERNAME], args[ARG_PASSWORD]), args[ARG_ITEM_ID]);
	}
}