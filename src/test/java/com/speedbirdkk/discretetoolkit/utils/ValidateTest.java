package com.speedbirdkk.discretetoolkit.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class ValidateTest {
    @Test
    public void givenASubsetOfB_whenValidateSubset_thenReturnTrue() {
        Set<Integer> A = Set.of(0, 1);
        Set<Integer> B = Set.of(0, 1, 2);

        boolean actualValidation = Validate.subset(A, B);
        boolean expectedValidation = true;

        assertEquals(expectedValidation, actualValidation);
    }
}
