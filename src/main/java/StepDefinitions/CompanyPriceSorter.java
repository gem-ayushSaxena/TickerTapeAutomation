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
import java.util.Comparator;
import java.util.List;

public class CompanyPriceSorter {

    public static void main(String[] args) {
        String filePath = "data.json";
        try {
            // Replace the jsonString with your JSON data
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            // Parse JSON data
            JSONArray jsonArray = new JSONArray(jsonString);

            // Create a list to hold Company objects
            List<Company> companies = new ArrayList<>();

            // Iterate through the JSON array and create Company objects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String companyName = jsonObject.getString("Companies Name");
                String currentPrice = jsonObject.getString("Current Price").replaceAll("[\\u20b9,]", "");
                double price = Double.parseDouble(currentPrice);
                companies.add(new Company(companyName, price));
            }

            // Sort the list of companies by descending order of price
            Collections.sort(companies, Comparator.comparingDouble(Company::getPrice).reversed());

            // Print the sorted list
            for (Company company : companies) {
                System.out.println(company.getCompanyName() + ": " + company.getPrice());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Company class to hold company details
    private static class Company {
        private String companyName;
        private double price;

        public Company(String companyName, double price) {
            this.companyName = companyName;
            this.price = price;
        }

        public String getCompanyName() {
            return companyName;
        }
        public double getPrice() {
            return price;
        }
    }
}


