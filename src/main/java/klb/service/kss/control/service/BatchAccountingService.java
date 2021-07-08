package klb.service.kss.control.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
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
public class BatchAccountingService {

	@Value("${batchInputDir}")
	String batchInputDir;

	@Value("${batchInputBkDir}")
	String batchInputBkDir;

	@Value("${batchOutputDir}")
	String batchOutputDir;

	@Value("${batchOutputBkDir}")
	String batchOutputBkDir;

	@Value("${checksumFileKey}")
	private String saltkey;

	@Value("${batchPayToKLBPref}")
	private String batchPayToKLBPref;

	@Value("${batchPayToOthPref}")
	private String batchPayToOthPref;

	private SignAndChecksum sign;

	public void batchProcess() {
		this.sign = new SignAndChecksum(saltkey);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println("batchProcess: " + dtf.format(now));

		File dir = new File(batchInputDir);
		for (File file : Files.fileTreeTraverser().breadthFirstTraversal(dir)) {
			if (file.isFile()) {
				readAndProcess(file);
			}
		}
	}

	public void readAndProcess(File file) {
		if (sign.checksumFile(file.getPath())) {
			String fileName = file.getName();

			try {
				List<String> lines = Files.readLines(file, Charsets.UTF_8); // Lines to List String
				boolean isPayToKLB = fileName.indexOf(batchPayToKLBPref) != -1 ? true : false; // true
				boolean isPayToOth = fileName.indexOf(batchPayToOthPref) != -1 ? true : false; // true

				// Bo qua dong dau tien, doc tu dong thu 2 (index 1)
				for (int i = 1; i < lines.size(); i++) {
					String[] stringParts = lines.get(i).split(",");
					String requestId = fileName + "_" + i;
					if (isPayToKLB) {
						payToKLB(stringParts, requestId);
					}
					if (isPayToOth) {
						payToOthBank(stringParts, requestId);
					}
				}

				// Tao file backup, xoa file o thu muc input
				String filePathBk = batchInputBkDir + "\\" + file.getName() + ".bk";
				com.google.common.io.Files.copy(file, new File(filePathBk));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			file.delete();
		} else {
			writeErrorFile(file);
		}

	}

	public void payToKLB(String[] stringParts, String requestId) {
		String transRef = stringParts[0];
		String amount = stringParts[1];
		String currency = stringParts[2];
		String creditAccountNo = stringParts[3];
		String debitAccountNo = stringParts[4];
		String narratives = stringParts[5];
		// GOI HAM CHUYEN TIEN NOI BO
	}

	public void payToOthBank(String[] stringParts, String requestId) {
		String transRef = stringParts[0];
		String Channel = stringParts[1];
		String Amount = stringParts[2];
		String Currency = stringParts[3];
		String Narratives = stringParts[4];
		String CertType = stringParts[5];
		String CertNo = stringParts[6];
		String CertDate = stringParts[7];
		String CertUnit = stringParts[8];
		String BenAccount = stringParts[9];
		String BenBankName = stringParts[10];
		String BenBankCode = stringParts[11];
		String BenBranch = stringParts[12];
		String BenName = stringParts[13];
		String DebitAccount = stringParts[14];
		// GOI HAM CHUYEN TIEN LIEN NGAN HANG
	}

	public void writeErrorFile(File file) {
		// EXPORT FILE bi loi checksum
		String content = "ERROR!@FILE KHONG HOP LE";
		writeResultFile(content, file);
	}

	public void writeResultFile(String content, File file) {
		String fileName = file.getName() + ".resp";
		String path1 = batchOutputDir + "\\" + fileName;
		String path2 = batchOutputBkDir + "\\" + fileName;
		File file1 = new File(path1); // File for FTP
		File file2 = new File(path2); // File for Backup
		try {
			Files.write(content, file1, Charsets.UTF_8);
			Files.write(content, file2, Charsets.UTF_8);
			// Ky vao file
			sign.signToFile(path1);
			sign.signToFile(path2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
