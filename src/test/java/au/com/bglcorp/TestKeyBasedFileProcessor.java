package au.com.bglcorp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.util.io.Streams;
import org.junit.Test;

/**
 * @author VEpa
 * @since 2019-03-25
 */
public class TestKeyBasedFileProcessor {

    private String RESOURCE_TEST_PATH = "src/test/resources";

    @Test
    public void testEncryptWithSampleKey() throws NoSuchProviderException, IOException, PGPException {
        String publicKey = RESOURCE_TEST_PATH + "/sample/keys/" + "public.key";
        String decryptedFile = RESOURCE_TEST_PATH + "/sample/" + "book.xml";
        String outFile = RESOURCE_TEST_PATH + "/sample/out/" + "book-encoded" + getDateTime() + ".gpg";

        KeyBasedFileProcessor.encryptFile(outFile,
                                          decryptedFile,
                                          publicKey,
                                          true,
                                          true);
    }

    @Test
    public void testDecryptWithSampleKey() throws IOException, NoSuchProviderException {
        String encryptedFile = RESOURCE_TEST_PATH + "/sample/" + "book-encoded.gpg";
        String privateKey = RESOURCE_TEST_PATH + "/sample/keys/" + "private.key";
        String passphrase = "1Qaz2Wsx@";
        String outFile = RESOURCE_TEST_PATH + "/sample/out/" + "book-decoded" + getDateTime() + ".xml";

        InputStream in = new BufferedInputStream(new FileInputStream(encryptedFile));
        InputStream keyIn = new BufferedInputStream(new FileInputStream(privateKey));

        InputStream decryptedStream = KeyBasedFileProcessor.decryptFile(in, keyIn, passphrase.toCharArray());

        FileOutputStream fileOutputStream = new FileOutputStream(outFile);

        OutputStream fOut = new BufferedOutputStream(fileOutputStream);
        Streams.pipeAll(decryptedStream, fOut);

        fOut.close();
        fileOutputStream.close();
        keyIn.close();
        in.close();

    }

    private final String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        df.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        return (df.format(new Date()));
    }

}
