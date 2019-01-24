package com.cdr.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Used mainly for obtaining a random phone call type given the probabilities
 * set in the configuration file.
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public class CallDistribution {
    private final Map<String, Double> weights;
    private final List<String> weightKeys;
    private final List<Double> weightVals;

    private final Random random;

    public CallDistribution(Map<String, Object> config) {
        List<String> callTypes = (List<String>) config.get("callTypes");
        Map<String, Object> outgoingCallParams = (Map<String, Object>) config.get("outgoingCallParams");
    
        weights = new HashMap<>(callTypes.size());
        weightKeys = new ArrayList<>(callTypes.size());
        weightVals = new ArrayList<>(callTypes.size());
        
        for (String callType : callTypes) {
            Map<String, Object> params = (Map<String, Object>) outgoingCallParams.get(callType);
            weights.put(callType, (Double) params.get("callProb"));
            weightKeys.add(callType);
            weightVals.add((Double) params.get("callProb"));
        }
        
        random = new Random(System.currentTimeMillis());
    }
    
    /**
     * @return A randomly selected phone call type
     */
    public String getRandomCallType() {
        double tmpRnd = 1;

        while (tmpRnd > 0) {
            tmpRnd = random.nextDouble();
            for (int k = 0; k < size(); k++) {
                if (tmpRnd - getVal(k) > 0) {
                    tmpRnd = tmpRnd - getVal(k);
                } else {
                    return getKey(k);
                }
            }
        }
        
        return null;
    }
    
    public int size() {
        return weights.size();
    }

    public Map<String, Double> getWeights() {
        return weights;
    }

    public List<String> getKeys() {
        return weightKeys;
    }
    
    public String getKey(int i) {
        return weightKeys.get(i);
    }

    public List<Double> getVals() {
        return weightVals;
    }
    
    public Double getVal(int i) {
        return weightVals.get(i);
    }
}
