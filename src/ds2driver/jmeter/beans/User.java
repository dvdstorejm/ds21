package ds2driver.jmeter.beans;

import java.util.Random;

//This class is equivalent to public class User
//in ds2xdriver.cs
public class User {

	private int userid;
	private Random r = null;
	
	//can move to Singleton if it works
	private int max_customer;
	private int pct_newcustomers;
	private int n_searches;
	private int search_batch_size;
	private int n_line_items;
	
	public int getN_line_items() {
		return n_line_items;
	}

	public void setN_line_items(int n_line_items) {
		this.n_line_items = n_line_items;
	}

	public int getSearch_batch_size() {
		return search_batch_size;
	}

	public void setSearch_batch_size(int search_batch_size) {
		this.search_batch_size = search_batch_size;
	}

	public int getN_searches() {
		return n_searches;
	}

	public void setN_searches(int n_searches) {
		this.n_searches = n_searches;
	}

	public int getMax_customer() {
		return max_customer;
	}

	public void setMax_customer(int max_customer) {
		this.max_customer = max_customer;
	}

	public int getPct_newcustomers() {
		return pct_newcustomers;
	}

	public void setPct_newcustomers(int pct_newcustomers) {
		this.pct_newcustomers = pct_newcustomers;
	}

	public User(int userid) {
		this.userid = userid;
		r = new Random();
	}
	
	public int nextInt(int i) {
		return r.nextInt (i);
	}
	
	public double nextDouble() {
		return r.nextDouble();
	}

	public int getUserid() {
		return this.userid;
	}
}
