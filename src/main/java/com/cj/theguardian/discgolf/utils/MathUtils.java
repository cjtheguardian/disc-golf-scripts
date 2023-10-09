package com.cj.theguardian.discgolf.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MathUtils {

    public static BigDecimal calcAverage(List<BigDecimal> numbers) {
        return BigDecimal.valueOf(numbers.stream().collect(Collectors.averagingDouble(bd -> bd.doubleValue())));
    }

    public static BigDecimal calcMedian(List<BigDecimal> numbers) {
        numbers = numbers.stream().sorted().collect(Collectors.toList());
        return numbers.get(numbers.size() / 2);
    }

}
