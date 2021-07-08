package klb.service.kss.control.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import klb.service.kss.control.dataline.DataLineCollect;
import klb.service.kss.control.dataline.DataLinePay;
import klb.service.kss.control.dataset.DataSetCollect;
import klb.service.kss.control.dataset.DataSetPay;
import klb.service.kss.control.sign.SignAndChecksum;

@Service
public class ExportControlFileService {

	@Value("${exportControlFileDir}")
	String exportDir;

	@Value("${exportControlFileBkDir}")
	String exportControlFileBkDir;
	
	@Value("${exportDTPattern}")
	String exportDTPattern;

	@Value("${collectFilePrefix}")
	private String collectFilePrefix;

	@Value("${payFilePrefix}")
	private String payFilePrefix;

	@Value("${checksumFileKey}")
	private String saltkey;

	private String dtNow;
	private SignAndChecksum sign;
	
	public void exportControlFile() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(exportDTPattern);
		LocalDateTime now = LocalDateTime.now();
		this.dtNow = dtf.format(now);
		System.out.println("ExportControlFileService: " + dtNow);
		this.sign = new SignAndChecksum(saltkey);
		
		mockTest();

	}

	public String exportCollectFile(DataSetCollect dsData) {
		String fileName = collectFilePrefix + dtNow + ".csv";
		String filePath = exportDir + "\\" + fileName;
		String filePathBk = exportControlFileBkDir + "\\" + fileName;
		try {
			File file = new File(filePath);
			Files.write(dsData.genDataFile(), file, Charsets.UTF_8);
			sign.signToFile(filePath);
			com.google.common.io.Files.copy(file, new File(filePathBk));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	public String exportPayFile(DataSetPay dsData) {
		String fileName = payFilePrefix + dtNow + ".csv";
		String filePath = exportDir + "\\" + fileName;
		String filePathBk = exportControlFileBkDir + "\\" + fileName;
		try {
			File file = new File(filePath);
			Files.write(dsData.genDataFile(), file, Charsets.UTF_8);
			sign.signToFile(filePath);
			com.google.common.io.Files.copy(file, new File(filePathBk));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
	
	private void mockTest() {
		// Tao hau to file bang chuoi thoi gian

		SignAndChecksum sign = new SignAndChecksum(saltkey);

		// FILE DOI SOAT THU HO
		List<DataLineCollect> ds = new ArrayList<DataLineCollect>();
		for (int i = 1; i <= 100; i++) {
			DataLineCollect obj = new DataLineCollect();
			obj.setRequestId("request" + i);
			obj.setTransactionReference("transref" + i);
			obj.setTransDateTime("NOW");
			obj.setCompanyId("KSS");
			obj.setCustomerName("CONG TY CHUNG KHOAN KSS");
			obj.setCustomerAccount("10099669966");
			obj.setAmount("100000000");
			obj.setFee("0");
			obj.setTax("0");
			obj.setNarratives("TT KSS LE HUY HUNG");
			obj.setCollectAccount("10099669966");
			obj.setStatus("SUCCESS");
			obj.setReason("");
			ds.add(obj);
		}
		DataSetCollect coll = new DataSetCollect();
		coll.setDs(ds);
		String filePath = exportCollectFile(coll);
		
		// FILE DOI SOAT CHI HO
		List<DataLinePay> ds2 = new ArrayList<DataLinePay>();
		for (int i = 1; i <= 100; i++) {
			DataLinePay obj = new DataLinePay();
			obj.setRequestId("request" + i);
			obj.setTransactionReference("transref" + i);
			obj.setTransDateTime("NOW");
			obj.setCompanyId("KSS");
			obj.setBenAccount("19025530012345");
			obj.setBenName("LE HUY HUNG");
			obj.setBenBank("TECHCOMBANK");
			obj.setAmount("100000000");
			obj.setFee("0");
			obj.setTax("0");
			obj.setNarratives("TT KSS LE HUY HUNG");
			obj.setPayAccount("10099669966");
			obj.setStatus("SUCCESS");
			obj.setReason("");
			ds2.add(obj);
		}
		DataSetPay pay = new DataSetPay();
		pay.setDs(ds2);
		String filePath2 = exportPayFile(pay);
	}

}
