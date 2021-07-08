package klb.service.kss.control.sign;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

public class SignAndChecksum {

	private String saltkey;
	// private String filePath;

	public SignAndChecksum(String saltkey) {
		this.saltkey = saltkey;
		// this.filePath = filePath;
	}

	public Boolean checksumFile(String filePath) {
		File file = new File(filePath);
		String fileName = file.getName();
		try {
			String fileAsString = Files.toString(file, Charsets.UTF_8);
			String[] stringParts = fileAsString.split("!@");
			String hashString = stringParts[0];
			String origContent = stringParts[1];
			String hashContent = fileName + "|" + origContent + "|" + this.saltkey;
			String hashTest = hashing(hashContent);
			if (String.valueOf(hashTest).equals(String.valueOf(hashString))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void signToFile(String filePath) {
		File file = new File(filePath);
		String fileName = file.getName();
		try {
			String fileAsString = Files.toString(file, Charsets.UTF_8);
			String hashContent = fileName + "|" + fileAsString + "|" + this.saltkey;
			String hasgStr = hashing(hashContent);
			Files.write(hasgStr + "!@" + fileAsString, file, Charsets.UTF_8);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String hashing(String hashcontent) {

		String testHash = "";
		try {
			testHash = DigestUtils.sha256Hex(hashcontent);
			// testHash = "";
			return testHash;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return testHash;
		}
	}

}
