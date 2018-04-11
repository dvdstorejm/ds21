package ds2driver.jmeter.beans;

// This class is equivalent to public class GlobalConstants
// in ds2xdriver.cs. This class also has other constants
// used in the JMeter driver.
public class GlobalConstants {
	
	// constants used in the driver
	public static final int MAX_USERS = 1000;
    public static final int MAX_CATEGORY = 16;
    public static final int MAX_ROWS = 100;
    public static final int LAST_N = 100;
    
    // user defined variables - key names
    public static final String TARGET = "target";
    public static final String PORT = "port";
	public static final String N_THREADS = "n_threads";
	public static final String SEARCH_BATCH_SIZE = "search_batch_size";
	public static final String N_SEARCHES = "n_searches";
	public static final String N_LINE_ITEMS = "n_line_items";
	public static final String PCT_NEWCUSTOMER = "pct_newcustomers";
	public static final String DB_SIZE = "db_size";
	
	// derived JMeter properties to be shared across thread groups - key names
	public static final String CUSTOMER_ROWS = "customer_rows";
	public static final String ORDER_ROWS = "order_rows";
	public static final String PRODUCT_ROWS = "product_rows";
	public static final String MAX_CUSTOMER = "max_customer";
	public static final String MAX_PRODUCT = "max_product";
	
	// internally used user defined variables - key values
    public static final int EXISTING_CUSTOMER = 0;
    public static final int NEW_CUSTOMER = 1;

    // internally used user defined variables - key names
	public static final String USER = "user";
	public static final String USERID = "userid";
	public static final String CONTROLLER = "controller";
	
}
