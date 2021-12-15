package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.view.ConsoleService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View current balance";
	private static final String MAIN_MENU_OPTION_LOOK_UP_TRANSFER = "View a Transfer";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";

	//commented out unimplemented features
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS/*, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS*/, MAIN_MENU_OPTION_LOOK_UP_TRANSFER, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private final ConsoleService console;
    private final AuthenticationService authenticationService;
	private final TenmoService tenmoService;



    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.tenmoService = new TenmoService();
//		this.restTemplate = new RestTemplate();
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			System.out.println("*********************");
			System.out.println("****  Main Menu  ****");
			System.out.println("*********************");
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			/*} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();*/
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			/*} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();*/
			} else if(MAIN_MENU_OPTION_LOOK_UP_TRANSFER.equals(choice)) {
				lookUpTransfer();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
			System.out.println("***********************");
		}
	}

	private void lookUpTransfer() {

		//Get and parse user input, then search database.
		long transferId = Long.parseLong(console.getUserInput("Enter transfer Id"));
		Transfer result = tenmoService.lookUpTransfer(transferId);

		//if database gives no result
		if(result == null){
			System.out.println("----------------");
			System.out.println("There is no Transfer with this ID");
			System.out.println("----------------");
		}

		String type = Transfer.Type.values()[result.getTypeId().ordinal()].toString();

		String status = Transfer.Status.values()[result.getStatusId().ordinal()].toString();

		System.out.println("----------------");
		System.out.println("ID: "+result.getId());
		System.out.println("Transfer Type: " +type );
		System.out.println("Transfer Status: " +status);
		System.out.println("Account From: "+ result.getAccountFrom());
		System.out.println("Account To: "+ result.getAccountTo());
		System.out.println("Amount: "+ result.getAmount());
		System.out.println("----------------");
	}

	private void viewCurrentBalance() {
		System.out.println("----------------");
		System.out.println("Your current balance of TE bucks is: " + tenmoService.getBalance());
		System.out.println("----------------");
		console.getUserInput("Press 'Enter' To continue");
	}

	private void viewTransferHistory() {
		Transfer[] transferArray = tenmoService.getTransferHistory();
		for(Transfer transfer:transferArray){
			String transferType = transfer.getTypeId() == Transfer.Type.Send ? "-" : "+";
			System.out.println(transfer.getId() + ": "+ transferType + transfer.getAmount());
		}
		console.getUserInput("Press 'Enter' To continue");
	}

	//commented out unimplemented features
	/*private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}*/

	private void sendBucks() {

		var users = tenmoService.listUsers();
		System.out.println("List of all Tenmo Users");
		System.out.println("----------------");
		System.out.println("id : username" );
		System.out.println("================");
		for (User user: users) {
			System.out.println(user.getId() +  " : " + user.getUsername());
		}

		System.out.println("----------------");
		System.out.println();
		try {
			long receiverID = 0;
			do  {
				String input = console.getUserInput("Enter the Receiver's User ID");
				receiverID = Integer.parseInt(input);
				if (receiverID == currentUser.getUser().getId()) {

					System.out.println("\nYou can not send bucks to yourself.");
					continue;
				}
				break;
			} while (true);

			double amount = Double.parseDouble( console.getUserInput("Enter the amount you would like to transfer"));
			Transfer result = tenmoService.createTransferRequest(receiverID,amount);
			System.out.println();

			if(result.getStatusId() == Transfer.Status.Rejected) {
				System.out.println("Transaction FAILED: Insufficient funds");
			}else if (result != null){
				System.out.println("Transaction SUCCESSFUL: You transferred " + result.getAmount() + " TE bucks --> " + result.getUserIdTo());
			 }

		}catch ( NumberFormatException e){
			System.err.println(e.getMessage());
		}
		console.getUserInput("Press 'Enter' To continue");
	}

	//commented out unimplemented features
	/*private void requestBucks() {
		// TODO Auto-generated method stub
		
	}*/
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				tenmoService.setUser(currentUser);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
	private HttpEntity<Object> makeHttpEntityWithToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		return new HttpEntity<>(headers);
	}

}
