package ds2driver.jmeter.beans;

public class GlobalParametersSingleton {

	private static volatile GlobalParametersSingleton instance = null;

	private static int prod_array_size;
	private static int[] prod_array;
	
	private static int max_customer;
	private static int pct_newcustomers;
	private static int n_searches;
	private static int search_batch_size;
	private static int n_line_items;
	  
	public static GlobalParametersSingleton getInstance() {
		return instance;
	}
	
	private GlobalParametersSingleton() {
	}

	public static GlobalParametersSingleton getInstance(int product_rows, int max_product) {
	if (instance == null) {
		synchronized (GlobalParametersSingleton.class){
	    if (instance == null) {
	        instance = new GlobalParametersSingleton();
			//Reason : Every 10000th product wil be popular and will have 10 entries in list
			//Set up array to choose product ids from, weighted with more entries for popular products
			//Popular products (in this case every 10,000th) will have 10 entries in list, others just 1
				int prod_arr_size = product_rows + 100000;
				prod_array = new int[prod_arr_size];
				int i = 0;
				for ( int j = 1 ; j <= max_product ; j++ ) {
					if ( ( j % 10000 ) == 0 ) for ( int k = 0 ; k < 10 ; k++ ) prod_array[i++] = j;
					else prod_array[i++] = j;
				}
				prod_array_size = i;
	        }
	      }
	    }
	    return instance ;
	  }

	public static int getProd_array_size() {
		return prod_array_size;
	}
	
	public static int[] getProd_array() {
		return prod_array;
	}
	
	public static int getMax_customer() {
		return max_customer;
	}

	public static void setMax_customer(int max_customerz) {
		max_customer = max_customerz;
	}

	public static int getPct_newcustomers() {
		return pct_newcustomers;
	}

	public static void setPct_newcustomers(int pct_newcustomersz) {
		pct_newcustomers = pct_newcustomersz;
	}

	public static int getN_searches() {
		return n_searches;
	}

	public static void setN_searches(int n_searchesz) {
		n_searches = n_searchesz;
	}

	public static int getSearch_batch_size() {
		return search_batch_size;
	}

	public static void setSearch_batch_size(int search_batch_sizez) {
		search_batch_size = search_batch_sizez;
	}

	public static int getN_line_items() {
		return n_line_items;
	}

	public static void setN_line_items(int n_line_itemsz) {
		n_line_items = n_line_itemsz;
	}


}