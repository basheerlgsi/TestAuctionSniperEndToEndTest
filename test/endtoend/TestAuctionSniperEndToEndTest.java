package endtoend;

import org.junit.After;
import org.junit.Test;

public class TestAuctionSniperEndToEndTest {
	private final FakeAuctionServer auction = new FakeAuctionServer(
			"item-54321");
	private final ApplicationRunner application = new ApplicationRunner();

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem(); // Step 1
		application.startBiddingIn(auction); // Step 2
		auction.hasReceivedJoinRequestFromSniper(); // Step 3
		auction.announceClosed(); // Step 4
		application.showsSniperHasLostAuction(); // Step 5
	}
	
	//2nd End-End test
	@Test 
	public void sniperMakesAHigherBidButLoses() throws Exception {
	auction.startSellingItem();
	application.startBiddingIn(auction);
	auction.hasReceivedJoinRequestFromSniper();
	auction.reportPrice(1000, 98, "other bidder");
	application.hasShownSniperIsBidding();
	auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
	auction.announceClosed();
	application.showsSniperHasLostAuction();
	}

	// Additional cleanup
	@After
	public void stopAuction() {
		auction.stop();
	}

	@After
	public void stopApplication() throws InterruptedException {
		application.stop();
	}
}
