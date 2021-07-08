package klb.service.kss.control.dataset;

import java.util.List;

import klb.service.kss.control.dataline.DataLineCollect;

public class DataSetCollect {
	private String labelNo = "No";
	private String labelRequestId = "RequestId";
	private String labelTransactionReference = "TransactionReference";
	private String labelTransDateTime = "TransDateTime";
	private String labelCompanyId = "CompanyId";
	private String labelCustomerName = "CustomerName";
	private String labelCustomerAccount = "CustomerAccount";
	private String labelAmount = "Amount";
	private String labelFee = "Fee";
	private String labelTax = "Tax";
	private String labelNarratives = "Narratives";
	private String labelCollectAccount = "CollectAccount";
	private String labelStatus = "Status";
	private String labelReason = "Reason";
	private List<DataLineCollect> ds;

	public String getHeaderLine() {
		StringBuilder buildStr = new StringBuilder();
		buildStr.append(labelNo);
		buildStr.append("," + labelRequestId);
		buildStr.append("," + labelTransactionReference);
		buildStr.append("," + labelTransDateTime);
		buildStr.append("," + labelCompanyId);
		buildStr.append("," + labelCustomerName);
		buildStr.append("," + labelCustomerAccount);
		buildStr.append("," + labelAmount);
		buildStr.append("," + labelFee);
		buildStr.append("," + labelTax);
		buildStr.append("," + labelNarratives);
		buildStr.append("," + labelCollectAccount);
		buildStr.append("," + labelStatus);
		buildStr.append("," + labelReason + "\n");
		return buildStr.toString();
	}

	public String genDataFile() {
		StringBuilder strResult = new StringBuilder();
		for (int i = 0; i < ds.size(); i++) {
			DataLineCollect obj = ds.get(i);
			strResult.append(i + 1);
			strResult.append("," + obj.getRequestId());
			strResult.append("," + obj.getTransactionReference());
			strResult.append("," + obj.getTransDateTime());
			strResult.append("," + obj.getCompanyId());
			strResult.append("," + obj.getCustomerName());
			strResult.append("," + obj.getCustomerAccount());
			strResult.append("," + obj.getAmount());
			strResult.append("," + obj.getFee());
			strResult.append("," + obj.getTax());
			strResult.append("," + obj.getNarratives());
			strResult.append("," + obj.getCollectAccount());
			strResult.append("," + obj.getStatus());
			strResult.append("," + obj.getReason() + "\n");
		}
		return getHeaderLine() + strResult.toString();
	}

	public List<DataLineCollect> getDs() {
		return ds;
	}

	public void setDs(List<DataLineCollect> ds) {
		this.ds = ds;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public String getLabelRequestId() {
		return labelRequestId;
	}

	public String getLabelTransactionReference() {
		return labelTransactionReference;
	}

	public String getLabelTransDateTime() {
		return labelTransDateTime;
	}

	public String getLabelCompanyId() {
		return labelCompanyId;
	}

	public String getLabelCustomerName() {
		return labelCustomerName;
	}

	public String getLabelCustomerAccount() {
		return labelCustomerAccount;
	}

	public String getLabelAmount() {
		return labelAmount;
	}

	public String getLabelFee() {
		return labelFee;
	}

	public String getLabelTax() {
		return labelTax;
	}

	public String getLabelNarratives() {
		return labelNarratives;
	}

	public String getLabelCollectAccount() {
		return labelCollectAccount;
	}

	public String getLabelStatus() {
		return labelStatus;
	}

	public String getLabelReason() {
		return labelReason;
	}

}
