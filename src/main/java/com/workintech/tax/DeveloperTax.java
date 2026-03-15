package com.workintech.tax;

import org.springframework.stereotype.Component;

@Component
public class DeveloperTax implements Taxable {
    public double getSimpleTaxRate() { return 15d; }
    public double getMiddleTaxRate() { return 25d; }
    public double getUpperTaxRate() { return 35d; }
}
