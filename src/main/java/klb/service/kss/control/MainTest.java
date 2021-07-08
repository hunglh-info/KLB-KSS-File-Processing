package klb.service.kss.control;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import klb.service.kss.control.sign.SignAndChecksum;

public class MainTest {

	public static SignAndChecksum sign = new SignAndChecksum("KIENLONGBANK@2021");
	public static String batchInputDir = "D:\\batchInputDir";
	public static String batchInputBkDir = "D:\\batchInputBkDir";
	public static String batchOutputDir = "D:\\batchOutputDir";
	public static String batchOutputBkDir = "D:\\batchOutputBkDir";

	public static String batchPayToKLBPref = "KSS_INT_";
	public static String batchPayToOthPref = "KSS_EXT_";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		 * File dir = new File(batchInputDir); for (File file :
		 * Files.fileTreeTraverser().breadthFirstTraversal(dir)) { if (file.isFile()) {
		 * sign.signToFile(file.getPath()); } }
		 */
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

	public static void readAndProcess(File file) {
		if (sign.checksumFile(file.getPath())) {
			String fileName = file.getName();

			try {
				List<String> lines = Files.readLines(file, Charsets.UTF_8); // Lines to List String
				boolean isPayToKLB = fileName.indexOf(batchPayToKLBPref) != -1 ? true : false; // true
				boolean isPayToOth = fileName.indexOf(batchPayToOthPref) != -1 ? true : false; // true

				writeSuccessFile("TransRef,Status,Reason,BankTransRef", file);
				// Bo qua dong dau tien, doc tu dong thu 2 (index 1)
				for (int i = 1; i < lines.size(); i++) {
					String[] stringParts = lines.get(i).split(",");
					String requestId = fileName + "_" + i;
					String result = null;
					if (isPayToKLB) {
						result = payToKLB(stringParts, requestId);
					}
					if (isPayToOth) {
						result = payToOthBank(stringParts, requestId);
					}
					if (result != null) {
						writeSuccessFile(result, file);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createSuccessRespFile(file);
		} else {
			writeErrorFile(file);
		}

		// Tao file backup, xoa file o thu muc input
		String filePathBk = batchInputBkDir + "\\" + file.getName() + ".bk";
		try {
			com.google.common.io.Files.copy(file, new File(filePathBk));
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String payToKLB(String[] stringParts, String requestId) {
		String transRef = stringParts[0];
		String amount = stringParts[1];
		String currency = stringParts[2];
		String creditAccountNo = stringParts[3];
		String debitAccountNo = stringParts[4];
		String narratives = stringParts[5];
		// GOI HAM CHUYEN TIEN NOI BO
		// INPUT: requestId, amount, currency, creditAccountNo, creditAccountNo,
		// debitAccountNo, narratives
		// OUTPUT:TransRef,Status,Reason,BankTransRef
		return "TransRef,Status,Reason,BankTransRef"; // CAN TRA VE OUTPUT o cho nay
	}

	public static String payToOthBank(String[] stringParts, String requestId) {
		String transRef = stringParts[0];
		String channel = stringParts[1];
		String amount = stringParts[2];
		String currency = stringParts[3];
		String narratives = stringParts[4];
		String certType = stringParts[5];
		String certNo = stringParts[6];
		String certDate = stringParts[7];
		String certUnit = stringParts[8];
		String benAccount = stringParts[9];
		String benBankName = stringParts[10];
		String benBankCode = stringParts[11];
		String benBranch = stringParts[12];
		String benName = stringParts[13];
		String debitAccount = stringParts[14];
		// GOI HAM CHUYEN TIEN LIEN NGAN HANG
		// INPUT:
		// transRef,channel,amount,currency,narratives,certType,certNo,certDate,certUnit
		// benAccount,benBankName,benBankCode,benBranch,benName,debitAccount
		// OUTPUT: TransRef,Status,Reason,BankTransRef

		return "TransRef,Status,Reason,BankTransRef"; // CAN TRA VE OUTPUT o cho nay
	}

	public static void writeErrorFile(File file) {
		// EXPORT FILE bi loi checksum
		String content = "ERROR!@FILE KHONG HOP LE";
		writeResultFile(content, file);
	}

	public static void writeSuccessFile(String line, File file) {
		String fileName = file.getName() + ".resp";
		// GHI VAO THU MUC BACKUP tRUOC DE TRANH BI FTP SERVICE QUET
		String path1 = batchOutputBkDir + "\\" + fileName;
		File file1 = new File(path1);
		CharSink chs = Files.asCharSink(file1, Charsets.UTF_8, FileWriteMode.APPEND);
		try {
			chs.write(line + "\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createSuccessRespFile(File file) {
		String fileName = file.getName() + ".resp";
		String path1 = batchOutputDir + "\\" + fileName;
		String path2 = batchOutputBkDir + "\\" + fileName;
		File file1 = new File(path1); // File for FTP
		File file2 = new File(path2); // File for Backup
		sign.signToFile(path2);
		try {
			com.google.common.io.Files.copy(file2, file1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeResultFile(String content, File file) {
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
