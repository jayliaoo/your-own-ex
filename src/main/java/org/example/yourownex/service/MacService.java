package org.example.yourownex.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class MacService {

    private final ThreadLocal<Mac> holder;
    private final SecretKeySpec key;

    public MacService() {
        key = new SecretKeySpec(Base64.getDecoder()
                .decode("Rck231HeNucTr1xQx/Kr05Jfic1ZNr6lLmR5ITgm62VSI1WB7/ONgm0nGAjxoaO5"), "HmacSHA3-384");
        this.holder = ThreadLocal.withInitial(() -> {
            try {
                Mac mac = Mac.getInstance("HmacSHA3-384");
                mac.init(key);
                return mac;
            } catch (InvalidKeyException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String mac(String raw) {
        return Base64.getEncoder()
                .encodeToString(holder.get().doFinal(raw.getBytes(StandardCharsets.UTF_8)));
    }
}
