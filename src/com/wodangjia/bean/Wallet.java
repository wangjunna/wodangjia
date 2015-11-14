package com.wodangjia.bean;

public class Wallet {
	int user_id;
	String relname;
	double wallet_balance;
	String wallet_password;
	String wallet_bank;
	String wallet_bank_no;
	public Wallet(int user_id, String relname, double wallet_balance,
			String wallet_password, String wallet_bank, String wallet_bank_no) {
		super();
		this.user_id = user_id;
		this.relname = relname;
		this.wallet_balance = wallet_balance;
		this.wallet_password = wallet_password;
		this.wallet_bank = wallet_bank;
		this.wallet_bank_no = wallet_bank_no;
	}
	
	public Wallet(int user_id, String relname, double wallet_balance,
			String wallet_bank, String wallet_bank_no) {
		super();
		this.user_id = user_id;
		this.relname = relname;
		this.wallet_balance = wallet_balance;
		this.wallet_bank = wallet_bank;
		this.wallet_bank_no = wallet_bank_no;
	}

	public Wallet(int user_id) {
		super();
		this.user_id = user_id;
	}

	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getRelname() {
		return relname;
	}
	public void setRelname(String relname) {
		this.relname = relname;
	}
	public double getWallet_balance() {
		return wallet_balance;
	}
	public void setWallet_balance(double wallet_balance) {
		this.wallet_balance = wallet_balance;
	}
	public String getWallet_password() {
		return wallet_password;
	}
	public void setWallet_password(String wallet_password) {
		this.wallet_password = wallet_password;
	}
	public String getWallet_bank() {
		return wallet_bank;
	}
	public void setWallet_bank(String wallet_bank) {
		this.wallet_bank = wallet_bank;
	}
	public String getWallet_bank_no() {
		return wallet_bank_no;
	}
	public void setWallet_bank_no(String wallet_bank_no) {
		this.wallet_bank_no = wallet_bank_no;
	}
	
}
