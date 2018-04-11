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

public class PurchaseDataCreationSampler extends AbstractJavaSamplerClient {
	
	Logger logger = getNewLogger();
	User user;
	GlobalParametersSingleton controller;
	
	public SampleResult runTest(JavaSamplerContext jsc) {
		SampleResult result = new SampleResult();
        JMeterVariables threadVars = JMeterContextService.getContext().getVariables();

        result.sampleStart(); // start stopwatch

        user = (User)threadVars.getObject(GlobalConstants.USER);
        
        if (user == null) {
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(false);
            result.setSampleLabel("FAILURE: PurchaseDataCreationSampler usr null");
		} else {
        	int[] prod_id_in = new int[GlobalConstants.MAX_ROWS];
        	int[] qty_in = new int[GlobalConstants.MAX_ROWS];

        	for ( int i = 0 ; i < GlobalConstants.MAX_ROWS ; i++ ) {
        		prod_id_in[i] = 0;
        		qty_in[i] = 0;
        	}
        	
        	// Randomize number of cart items with average n_line_items
        	int cart_items = 1 + user.nextInt ( 2 * user.getN_line_items() - 1 );

        	// For each cart item randomly select product_id using weighted prod_array
        	int[] prod_array = GlobalParametersSingleton.getProd_array(); 
        	int   prod_array_size = GlobalParametersSingleton.getProd_array_size();
        	for ( int i = 0 ; i < cart_items ; i++ ) {
        		prod_id_in[i] = prod_array[user.nextInt ( prod_array_size )];
        		qty_in[i] = 1 + user.nextInt ( 3 );  // qty (1, 2 or 3)
        	}

        	StringBuffer sb = new StringBuffer(256);
        	for ( int i = 0 ; i < cart_items ; i++ ) {
        		sb.append("&item=" + Integer.toString(prod_id_in[i]));
        		sb.append("&quan=" + Integer.toString(qty_in[i]));
        	}
        	
        	threadVars.put("purchaseitemsqty", sb.toString());
        	
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(true);
            result.setSampleLabel("SUCCESS: PurchaseDataCreationSampler");
        }
        
        return result;
	}
}
