package ds2driver.jmeter.samplers;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;

import ds2driver.jmeter.beans.GlobalConstants;
import ds2driver.jmeter.beans.GlobalParametersSingleton;

import org.slf4j.Logger;

public class ControllerInitializationSampler extends AbstractJavaSamplerClient {

	Logger logger = getNewLogger();
	
	public SampleResult runTest(JavaSamplerContext jsc) {
		SampleResult result = new SampleResult();
        JMeterVariables threadVars = JMeterContextService.getContext().getVariables();

        result.sampleStart();

        boolean res = init(threadVars);
        result.sampleEnd();
        result.setSuccessful(res);
        result.setSampleLabel(res == true ? "SUCCESS: ": "FAILURE: " + "ControllerInitializationSampler");
        return result;
	}
	
	private boolean init(JMeterVariables vars) {

		int val1;
		int i_db_custom_size = 0;
		int mult_cust_rows   = 0;
		int mult_ord_rows    = 0;
		int mult_prod_rows   = 0;
		double ratio         = 0;

		String val;
		String str_db_size   = "";
		String db_custom_size= "";
		String str_is_mb_gb  = "";

		// user defined variable		
		val = vars.get(GlobalConstants.TARGET);
		if (val != null) {
			logger.info("ControllerInitializationSampler: target: " + val);
		} else {
			return false;
		}

		// user defined variable		
		val = vars.get(GlobalConstants.PORT);
		if (val != null) {
			logger.info("ControllerInitializationSampler: port: " + val);
		} else {
			return false;
		}
		
		// user defined variable 
		val = vars.get(GlobalConstants.N_THREADS);
		if (val != null) {
			val1 = Integer.parseInt(val);
			logger.info("ControllerInitializationSampler: n_threads: " + val1);
		} else {
			return false;
		}

		// user defined variable 
		val = vars.get(GlobalConstants.SEARCH_BATCH_SIZE);
		if (val != null) {
			val1 = Integer.parseInt(val);
			logger.info("ControllerInitializationSampler: search_batch_size: " + val1);
		} else {
			return false;
		}

		// user defined variable		
		val = vars.get(GlobalConstants.N_SEARCHES);
		if (val != null) {
			val1 = Integer.parseInt(val);
			logger.info("ControllerInitializationSampler: n_searches: " + val1);
		} else {
			return false;
		}

		// user defined variable
		val = vars.get(GlobalConstants.N_LINE_ITEMS);
		if (val != null) {
			val1 = Integer.parseInt(val);
			logger.info("ControllerInitializationSampler: n_line_items: " + val1);
		} else {
			return false;
		}

		// user defined variable
		val = vars.get(GlobalConstants.PCT_NEWCUSTOMER);
		if (val != null) {
			val1 = Integer.parseInt(val);
			logger.info("ControllerInitializationSampler: pct_newcustomers: " + val1);
		} else {
			return false;
		}

		// user defined variable		
		val = vars.get(GlobalConstants.DB_SIZE);
		if (val != null) {
			str_db_size = val;
			logger.info("ControllerInitializationSampler: db_size: " + str_db_size);
		} else {
			return false;
		}
		
		if ( str_db_size.toUpperCase() == "S" ) {
			db_custom_size = "10mb";
		} else if ( str_db_size.toUpperCase() == "M" ) {
			db_custom_size = "1gb";
		} else if ( str_db_size.toUpperCase() == "L" ) {
			db_custom_size = "100gb";
		} else {
			db_custom_size = str_db_size;
			db_custom_size = db_custom_size.toLowerCase ( );  //For case insensitivity
		}
		
		if ( db_custom_size.indexOf ( "mb" ) != -1 ) {
			str_is_mb_gb = db_custom_size.substring (db_custom_size.indexOf("mb"));
			logger.info("db_custom_size mb: " + db_custom_size + " str_is_mb_gb: " + str_is_mb_gb);
			try {
				i_db_custom_size = Integer.parseInt (db_custom_size.substring (0 , db_custom_size.indexOf("mb")));
				logger.info("i_db_custom_size mb: " + i_db_custom_size);
			} catch (NumberFormatException nfe) {
				logger.error("NumberFormatException1:" + nfe.getMessage());
				return false;
			}
		} else if ( db_custom_size.indexOf ( "gb" ) != -1 ) {
			str_is_mb_gb = db_custom_size.substring (db_custom_size.indexOf("gb"));
			logger.info("db_custom_size gb: " + db_custom_size + " str_is_mb_gb: " + str_is_mb_gb);
			try {
				i_db_custom_size = Integer.parseInt (db_custom_size.substring (0 , db_custom_size.indexOf("gb")));
				logger.info("i_db_custom_size gb: " + i_db_custom_size);
			} catch (NumberFormatException nfe) {
				logger.error("NumberFormatException2:" + nfe.getMessage());
				return false;
			}
		} else {
			logger.error("Invalid db_custom_size:" + db_custom_size);
			return false;
		}

		//Size is in MB  (Database can be only in range 1 mb to 1024 mb - Small instance S)
		if ( str_is_mb_gb.compareToIgnoreCase ("mb") == 0 ) {
		   ratio = ( double ) ( i_db_custom_size / 10.0 );
		   mult_cust_rows = 20000;
		   mult_ord_rows = 1000;
		   mult_prod_rows = 10000;
		   logger.info("Assign values mb");
		} else if ( str_is_mb_gb.compareToIgnoreCase ("gb" ) == 0 ) {
		//Size is in GB (database can be 1 GB (Medium instance M) or > 1 GB (Larger instance L)
			if ( i_db_custom_size == 1 ) {
				//Medium M size 1 GB database
				ratio = ( double ) ( i_db_custom_size / 1.0 );
				mult_cust_rows = 2000000;
				mult_ord_rows = 100000;
				mult_prod_rows = 100000;
				logger.info("Assign values gb = 1 GB");
			} else {
				//Size > 1 GB Large L size database
				ratio = ( double ) ( i_db_custom_size / 100.0 );
				mult_cust_rows = 200000000;
				mult_ord_rows = 10000000;
				mult_prod_rows = 1000000;
				logger.info("Assign values gb > 1 GB");
			}
		}
		
		//Initialize number of rows in table according to ratio calculated for custom database size
		int customer_rows = ( int ) ( ratio * mult_cust_rows );
		int order_rows = ( int ) ( ratio * mult_ord_rows );
		int product_rows = ( int ) ( ratio * mult_prod_rows );
		int max_customer = customer_rows;
		int max_product = product_rows;
		
		// these variables are required in the second thread group
		JMeterUtils.setProperty(GlobalConstants.CUSTOMER_ROWS, Integer.toString(customer_rows));
		JMeterUtils.setProperty(GlobalConstants.ORDER_ROWS, Integer.toString(order_rows));
		JMeterUtils.setProperty(GlobalConstants.PRODUCT_ROWS, Integer.toString(product_rows));
		JMeterUtils.setProperty(GlobalConstants.MAX_CUSTOMER, Integer.toString(max_customer));
		JMeterUtils.setProperty(GlobalConstants.MAX_PRODUCT, Integer.toString(max_product));

        GlobalParametersSingleton.getInstance(product_rows, max_product);

		logger.info("ControllerInitializationSampler: customer_rows: " + customer_rows);
		logger.info("ControllerInitializationSampler: order_rows: " + order_rows);
		logger.info("ControllerInitializationSampler: product_rows: " + product_rows);
		logger.info("ControllerInitializationSampler: max_customer: " + max_customer);
		logger.info("ControllerInitializationSampler: max_product: " + customer_rows);
		
		return true;
	}
}
