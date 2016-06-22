/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.crypto.examples;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.crypto.cipher.CipherTransformation;
import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.utils.Utils;

public class CipherByteArrayExample {

    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes("1234567890123456"),"AES");
        final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));
        Properties properties = new Properties();
        //Creates a CryptoCipher instance with the transformation and properties.
        final CipherTransformation transform = CipherTransformation.AES_CBC_PKCS5PADDING;
        CryptoCipher encipher = Utils.getCipherInstance(transform, properties);

        final String sampleInput = "hello world!";
        System.out.println("input:  " + sampleInput);

        byte[] input = getUTF8Bytes(sampleInput);
        byte[] output = new byte[32]; 

        //Initializes the cipher with ENCRYPT_MODE, key and iv.
        encipher.init(Cipher.ENCRYPT_MODE, key, iv);
        //Continues a multiple-part encryption/decryption operation for byte array.
        int updateBytes = encipher.update(input, 0, input.length, output, 0);
        System.out.println(updateBytes);
        //We should call do final at the end of encryption/decryption.
        int finalBytes = encipher.doFinal(input, 0, 0, output, updateBytes);
        System.out.println(finalBytes);
        //Closes the cipher.
        encipher.close();

        System.out.println(Arrays.toString(Arrays.copyOf(output, updateBytes+finalBytes)));

        // Now reverse the process
        CryptoCipher decipher = Utils.getCipherInstance(transform, properties);
        decipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte [] decoded = new byte[32];
        decipher.doFinal(output, 0, updateBytes + finalBytes, decoded, 0);

        System.out.println("output: " + new String(decoded, StandardCharsets.UTF_8));
    }

}
