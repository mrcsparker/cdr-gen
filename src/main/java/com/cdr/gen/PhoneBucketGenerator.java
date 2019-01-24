package com.cdr.gen;

import com.cdr.gen.util.RandomGaussian;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates a set of phone numbers for which a person can call.
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public class PhoneBucketGenerator {
    private final Map<String, Object> outgoingNumberDist;

    public PhoneBucketGenerator(Map<String, Object> config) {
        outgoingNumberDist = (Map<String, Object>) config.get("outgoingNumberDistribution");
        
    }
    
    /**
     * Create the set of random phone number based on the frequency of each call type.
     * @param p The person for which the phone numbers will be generated.
     * @param callTypeSummary The frequency of each call type
     * @return The generated numbers, separated by type of call
     */
    public Map<String, List<String>> createPhoneBucket(Person p, Map<String, Integer> callTypeSummary) {
        Map<String, Long> params;
        Map<String, List<String>> destPhoneNumbers = new HashMap<>(callTypeSummary.size());
        
        for (Map.Entry<String, Integer> e : callTypeSummary.entrySet()) {
            params = (Map<String, Long>) outgoingNumberDist.get(e.getKey());
            
            RandomGaussian gauss = RandomGaussian.generate(
                    params.get("stdDev"), params.get("mean"));
            
            Double count = Math.ceil((e.getValue() / 100.0) * Math.abs(gauss.getValueOne()));
            int phoneCount = (count > 0) ? count.intValue() : 1;
            
            if (!destPhoneNumbers.containsKey(e.getKey())) {
                destPhoneNumbers.put(e.getKey(), new ArrayList<>(phoneCount));
            }
            
            String phoneNumber;
            String code = p.getPhoneNumber().substring(0, 4);
            
            for (int i=0; i<phoneCount; i++) {
                if (e.getKey().equals("Local")) {
                    phoneNumber = code + PhoneNumberGenerator.getRandomNumber(7);
                } else {
                    String destCode = PhoneNumberGenerator.getRandomPhoneCode(e.getKey(), code);
                    assert destCode != null;
                    phoneNumber = destCode + PhoneNumberGenerator.getRandomNumber(11 - destCode.length());
                }

                destPhoneNumbers.get(e.getKey()).add(phoneNumber);
            }
        }
        
        return destPhoneNumbers;
    }
}
