package com.example.ajiraapp;

import java.util.ArrayList;

public class ExpertFilter {
    public static ArrayList<Expert> filterByBudget(ArrayList<Expert> experts, double maxBudget) {
        ArrayList<Expert> filtered = new ArrayList<>();

        for (Expert expert : experts) {
            String[] rangeParts = expert.getServicecharge().split("-");

            if (rangeParts.length == 2) {
                try {
                    double min = Double.parseDouble(rangeParts[0].trim());
                    double max = Double.parseDouble(rangeParts[1].trim());

                    if ((maxBudget >= min && maxBudget <= max) || max <= maxBudget) {
                        filtered.add(expert);
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid entries
                    continue;
                }
            }
        }

        return filtered;
    }
}
