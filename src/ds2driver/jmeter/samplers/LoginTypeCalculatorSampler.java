package ds2driver.jmeter.samplers;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;

import ds2driver.jmeter.beans.GlobalConstants;
import ds2driver.jmeter.beans.GlobalParametersSingleton;
import ds2driver.jmeter.beans.User;

public class LoginTypeCalculatorSampler extends AbstractJavaSamplerClient {

	Logger logger = getNewLogger();
	User user;
	
	public SampleResult runTest(JavaSamplerContext jsc) {
		
		SampleResult result = new SampleResult();
        JMeterVariables vars = JMeterContextService.getContext().getVariables();
        int isLogin = 0;
        
        result.sampleStart(); // start stopwatch

        user = (User)vars.getObject(GlobalConstants.USER);
        
        if (user == null) {
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(false);
            result.setSampleLabel("FAILURE: LoginTypeCalculatorSampler usr null");
        } else {
        	double user_type = user.nextDouble ( );
            if ( user_type >= user.getPct_newcustomers() / 100.0 ) {
            	// If this is true we have a returning customer
            	isLogin = GlobalConstants.EXISTING_CUSTOMER;
            	CreateLoginData();
            } else {
            	isLogin = GlobalConstants.NEW_CUSTOMER;
            	CreateUserData();
            }
            
            int n_searches = user.getN_searches();
            int n_browse = 1 + user.nextInt ( 2 * n_searches - 1 );   // Perform average of n_searches searches
            
            vars.put("n_browse", Integer.toString(n_browse));
            vars.put("islogin", Integer.toString(isLogin));
            
    		logger.debug("LoginTypeCalculatorSampler: isLogin " + isLogin);
    		logger.debug("LoginTypeCalculatorSampler: n_browse " + n_browse);
    		logger.debug("LoginTypeCalculatorSampler: prod_array_size " + GlobalParametersSingleton.getProd_array_size());

            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(true);
            result.setSampleLabel("SUCCESS: LoginTypeCalculatorSampler");
        }
        
        return result;
	}

	private void CreateLoginData() {
        JMeterVariables vars = JMeterContextService.getContext().getVariables();
		int i_user = 1 + user.nextInt ( user.getMax_customer() );

		String username_in = "user" + i_user;
		String password_in = "password";
		vars.put("username_in", username_in);
		vars.put("password_in", password_in);

		return;
	}
	
	private void CreateUserData() {
        JMeterVariables vars = JMeterContextService.getContext().getVariables();
		String[] states = new String[] {"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", 
				   "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", 
				   "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", 
				   "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};

		String[] countries = new String[] {"Australia", "Canada", "Chile", "China", "France", "Germany", "Japan", 
		      "Russia", "South Africa", "UK"};

		int j;

		int i_user = 1 + user.nextInt ( user.getMax_customer() );

		//added some more randomization with alphabets to avoid the logic in
		//existing workload generator which keeps generating new customer
		//until find a userid that doesn't exist
		String username_in = "newuser" + ( char ) ( 97 + user.nextInt ( 26 ) ) + ( char ) ( 97 + user.nextInt ( 26 ) ) + i_user;
		String password_in = "password";

		String firstname_in = ""; for ( j = 0 ; j < 6 ; j++ ) { firstname_in = firstname_in + ( char ) ( 65 + user.nextInt ( 26 ) ); }
		String lastname_in = ""; for ( j = 0 ; j < 10 ; j++ ) { lastname_in = lastname_in + ( char ) ( 65 + user.nextInt ( 26 ) ); }
		String city_in = ""; for ( j = 0 ; j < 7 ; j++ ) { city_in = city_in + ( char ) ( 65 + user.nextInt ( 26 ) ); }
			      
		String zip_in, state_in, country_in;
		if ( user.nextInt ( 2 ) == 1 ) // Select region (US or ROW)
		{ //ROW    
			zip_in = "";
			state_in = "";
			country_in = countries[user.nextInt ( 10 )];
		}
		else //US
		{
			zip_in = ( Integer.toString((user.nextInt ( 100000 ) )) );
			state_in = states[user.nextInt ( 50 )];
			country_in = "US";
		} //End Else

		//to generate random number between a and b, use r.nextInt((max - min) + 1) + min
		String phone_in = "" + (user.nextInt((1000 - 100) + 1) + 100) + user.nextInt ( 10000000 );
		int    creditcardtype_in = 1 + user.nextInt ( 5 );
		String creditcard_in = "" + (user.nextInt((100000000 - 10000000) + 1) + 10000000) + (user.nextInt((100000000 - 10000000) + 1) + 10000000);
		int    ccexpmon_in = 1 + user.nextInt ( 12 );
		int    ccexpyr_in = 2008 + user.nextInt ( 5 );
		String address1_in = phone_in + " Dell Way";
		String address2_in = "";
		String email_in = lastname_in + "@dell.com";
		int    age_in = user.nextInt((100 - 18) + 1) + 18;
		int    income_in = 20000 * (user.nextInt((6 - 1) + 1) + 1); // >$20,000, >$40,000, >$60,000, >$80,000, >$100,000
		String gender_in = ( user.nextInt ( 2 ) == 1 ) ? "M" : "F";
		
		vars.put("username_in", username_in);
		vars.put("password_in", password_in);
		vars.put("firstname_in", firstname_in);
		vars.put("lastname_in", lastname_in);
		vars.put("city_in", city_in);
		vars.put("zip_in", zip_in);
		vars.put("state_in", state_in);
		vars.put("country_in", country_in);
		vars.put("phone_in", phone_in);
		vars.put("creditcardtype_in", Integer.toString(creditcardtype_in));
		vars.put("creditcard_in", creditcard_in);
		vars.put("ccexpmon_in", Integer.toString(ccexpmon_in));
		vars.put("ccexpyr_in", Integer.toString(ccexpyr_in));
		vars.put("address1_in", address1_in);
		vars.put("address2_in", address2_in);
		vars.put("email_in", email_in);
		vars.put("age_in", Integer.toString(age_in));
		vars.put("income_in", Integer.toString(income_in));
		vars.put("gender_in", gender_in);

		return;
	}
}
