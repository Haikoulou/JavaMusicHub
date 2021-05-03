package musichub.tests;

import musichub.server.*;
import musichub.business.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MusicHubServerTest {
	
	private MusicHubServer theHubServer;
	private MusicHub theHub;

    //@BeforeEach                                        
    public void setUp() throws Exception {
    	theHubServer = new MusicHubServer();
    	theHubServer.launch(6667);
    }
    
    @Test                                             
    //@DisplayName("Simple multiplication should work")
    public void testLaunch() {
    	//theHub = new MusicHub(); 
    	Throwable exception = assertThrows(ConnectionFailureException.class, () -> theHub = new MusicHub());
        assertEquals("Age must be an Integer.", exception.getMessage());
    }
}
