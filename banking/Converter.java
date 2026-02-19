package org.poo.banking;

import org.poo.fileio.ExchangeInput;
import java.util.HashMap;
import java.util.Map;

public final class Converter {
    //Singleton
    private static Converter instance = null;
    private final Map<String, Map<String, Double>> EXCHANGE_RATES = new HashMap<>();

    /**
     *
     * @return
     */
    public static Converter getInstance() {
        if (instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    /**
     *
     * @param rates
     */
    public void initializare(final ExchangeInput[] rates) {
        EXCHANGE_RATES.clear();
        // adaugam ratele
        for (ExchangeInput rate : rates) {
            String from = rate.getFrom();
            String to = rate.getTo();
            double rateValue = rate.getRate();

            if (!EXCHANGE_RATES.containsKey(from)) {
                EXCHANGE_RATES.put(from, new HashMap<String, Double>());
            }
            EXCHANGE_RATES.get(from).put(to, rateValue);
            if (!EXCHANGE_RATES.containsKey(to)) {
                EXCHANGE_RATES.put(to, new HashMap<String, Double>());
            }
            EXCHANGE_RATES.get(to).put(from, 1.0 / rateValue);
        }

        // calculez rate pt valute ce nu au conversie directa
        for (String from : EXCHANGE_RATES.keySet()) {
            for (String to : EXCHANGE_RATES.keySet()) {
                for (String intermediate : EXCHANGE_RATES.keySet()) {
                    if (EXCHANGE_RATES.get(from).containsKey(intermediate)
                            && EXCHANGE_RATES.get(intermediate).containsKey(to)) {
                        double rate = EXCHANGE_RATES.get(from).get(intermediate)
                                * EXCHANGE_RATES.get(intermediate).get(to);
                        EXCHANGE_RATES.get(from).put(to, rate);
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     * @param fromCurrency
     * @param toCurrency
     * @param amount
     * @return
     */
    public double convert(final String fromCurrency,
                          final String toCurrency,
                          final double amount) {
        Map<String, Double> fromRates = EXCHANGE_RATES.get(fromCurrency);
        if (fromRates != null && fromRates.containsKey(toCurrency)) {
            return amount * fromRates.get(toCurrency);
        }
        return amount;
    }
}
