package StepDefinitions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DividendYieldMedian {

    public static void main(String[] args) throws IOException {
        String filePath = "data.json";
        String jsonString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        List<Company> companies = parseJsonData(jsonString);
        double medianPrice = calculateMedianByDividendYield(companies);
        System.out.println("Median Price of Dividend Yield: " + medianPrice);
    }

    private static List<Company> parseJsonData(String jsonString) {
        List<Company> companies = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String companyName = jsonObject.getString("Companies Name");
                String currentPrice = jsonObject.getString("Current Price").replaceAll("[\\u20b9,]", "");
                String dividendYield = jsonObject.getString("Dividend Yeild").replaceAll("[%,\u2014]", "");

                double price = Double.parseDouble(currentPrice);
                double yield = 0;
                if (!dividendYield.isEmpty()) {
                    try {
                        yield = Double.parseDouble(dividendYield);
                    } catch (NumberFormatException e) {
                        // Handle invalid dividend yield value (if needed)
                        // You can log a warning or take other appropriate action here
                    }
                }

                companies.add(new Company(companyName, price, yield));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return companies;
    }

    private static double calculateMedianByDividendYield(List<Company> companies) {
        List<Double> dividendYields = new ArrayList<>();
        for (Company company : companies) {
            dividendYields.add(company.getDividendYield());
        }
        Collections.sort(dividendYields);
        int size = dividendYields.size();
        if (size % 2 == 0) {
            int middle = size / 2;
            return (dividendYields.get(middle - 1) + dividendYields.get(middle)) / 2.0;
        } else {
            int middle = (size - 1) / 2;
            return dividendYields.get(middle);
        }
    }

    private static class Company {
        private String companyName;
        private double price;
        private double dividendYield;

        public Company(String companyName, double price, double dividendYield) {
            this.companyName = companyName;
            this.price = price;
            this.dividendYield = dividendYield;
        }

        public double getDividendYield() {
            return dividendYield;
        }
    }
}
