package libs;
public class ItemInfo {
	
	String name = null;
	private String interactSetting = null;
	private String depositSetting = "Deposit-All";
	String withdrawSetting = "Withdraw";

	int id = 0;
	int withdrawAmount = 0;
	int depositAmount = 0;

	public ItemInfo(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setWithdrawSetting(String set) {
		this.withdrawSetting = set;
	}

	public void setWithdrawAmount(int num) {
		this.withdrawAmount = num;
	}

	public void setDepositAmount(int num) {
		this.depositAmount = num;
	}

	public String getName() {
		return name;
	}

	public int getDepositAmount() {
		return depositAmount;
	}

	public int getWithdrawAmount() {
		return withdrawAmount;
	}

	public String getWithdrawSetting() {
		return withdrawSetting;
	}

	public String getDepositSetting() {
		return depositSetting;
	}

	public void setDepositSetting(String depositSetting) {
		this.depositSetting = depositSetting;
	}

	public String getInteractSetting() {
		return interactSetting;
	}

	public void setInteractSetting(String interactSetting) {
		this.interactSetting = interactSetting;
	}

}
