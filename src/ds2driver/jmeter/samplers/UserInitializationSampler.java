package ds2driver.jmeter.samplers;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;

import ds2driver.jmeter.beans.GlobalConstants;
import ds2driver.jmeter.beans.GlobalParametersSingleton;
//import ds2driver.jmeter.beans.Singleton;
import ds2driver.jmeter.beans.User;

public class UserInitializationSampler extends AbstractJavaSamplerClient {

	Logger logger = getNewLogger();

	// set up default arguments for the JMeter GUI
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(GlobalConstants.USERID, "${__counter(FALSE,)}");
        return defaultParameters;
    }
	
	public SampleResult runTest(JavaSamplerContext jsc) {
		
		SampleResult result = new SampleResult();
        JMeterVariables threadVars = JMeterContextService.getContext().getVariables();
        int userid = 0; //default value
        String val;

        result.sampleStart(); // start stopwatch
        
        val = jsc.getParameter(GlobalConstants.USERID);
        if (val != null) {
        	userid = Integer.parseInt(val);
        }

        User user = new User(userid);

        // pct_newcustomers is a JMeter User Defined Variable
        val = threadVars.get(GlobalConstants.PCT_NEWCUSTOMER);
        user.setPct_newcustomers(Integer.parseInt(val));
		logger.debug("UserInitializationSampler: pctNewCustomers " + val);

        // n_searches is a JMeter User Defined Variable
        val = threadVars.get(GlobalConstants.N_SEARCHES);
        user.setN_searches(Integer.parseInt(val));
		logger.debug("UserInitializationSampler: n_searches " + val);

        // search_batch_size is a JMeter User Defined Variable
        val = threadVars.get(GlobalConstants.SEARCH_BATCH_SIZE);
        user.setSearch_batch_size(Integer.parseInt(val));
		logger.debug("UserInitializationSampler: search_batch_size " + val);
		
        // search_batch_size is a JMeter User Defined Variable
        val = threadVars.get(GlobalConstants.SEARCH_BATCH_SIZE);
        user.setN_line_items(Integer.parseInt(val));
		logger.debug("UserInitializationSampler: n_line_items " + val);
		
        // max_customer is a JMeter property
        val = JMeterUtils.getProperty(GlobalConstants.MAX_CUSTOMER);
		logger.debug("UserInitializationSampler: max_customer " + val);
        user.setMax_customer(Integer.parseInt(val));

		logger.debug("UserInitializationSampler: prod_array_size " + GlobalParametersSingleton.getProd_array_size());

        threadVars.putObject(GlobalConstants.USER, user);
        
        result.sampleEnd(); // stop stopwatch
        result.setSuccessful(true);
        result.setSampleLabel("SUCCESS: UserInitializationSampler");
        
        return result;
	}
	
}
