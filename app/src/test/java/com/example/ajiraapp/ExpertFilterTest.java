package com.example.ajiraapp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ExpertFilterTest {
    @Test
    public void testBudgetFilter() {
        ArrayList<Expert> testList = new ArrayList<>();

        // Create test experts
        testList.add(new Expert("John", "Moving", "500-1000"));
        testList.add(new Expert("Jane", "Moving", "1000-1500"));
        testList.add(new Expert("Ali", "Moving", "1500-2000"));
        testList.add(new Expert("Invalid", "Moving", "bad-range"));

        // Case: Budget is 1200
        ArrayList<Expert> result = ExpertFilter.filterByBudget(testList, 1200);

        // Print result for debugging
        System.out.println("Result for budget 1200:");
        for (Expert expert : result) {
            System.out.println(expert.getFirstname());
        }

        // Assert that it should return John and Jane
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstname());
        assertEquals("Jane", result.get(1).getFirstname());

        // Case: Budget is 600
        result = ExpertFilter.filterByBudget(testList, 600);

        // Print result for debugging
        System.out.println("Result for budget 600:");
        for (Expert expert : result) {
            System.out.println(expert.getFirstname());
        }

        // Assert that it should return John
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstname());

        // Case: Budget is 2000 (should include all except invalid)
        result = ExpertFilter.filterByBudget(testList, 2000);

        // Print result for debugging
        System.out.println("Result for budget 2000:");
        for (Expert expert : result) {
            System.out.println(expert.getFirstname());
        }

        // Assert that it should include John, Jane, and Ali
        assertEquals(3, result.size()); // Should exclude invalid expert
        assertEquals("John", result.get(0).getFirstname());
        assertEquals("Jane", result.get(1).getFirstname());
        assertEquals("Ali", result.get(2).getFirstname());
    }
}
