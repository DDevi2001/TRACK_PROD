package model;

import java.util.ArrayList;

public class Quotation {
    private final ArrayList<IndividualDetails> individualDetails = new ArrayList<>();
    private ArrayList<String> termsAndCondition = new ArrayList<>();
    {
        termsAndCondition.add("This Quotation is issued subject to availability of material.");
        termsAndCondition.add("Supplier warrants that the Products manufactured by Supplier shall be free from defects.");
        termsAndCondition.add("Prices mentioned in the Quotation are fixed.");
    }

    public ArrayList<IndividualDetails> getIndividualDetails() {
        return individualDetails;
    }

    public ArrayList<String> getTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(ArrayList<String> termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }

    public void setIndividualDetails(String productID, String productName, int quantity, AvailabilityStatus status, float cost) {
        individualDetails.add(new IndividualDetails(productID, productName, quantity, status, cost));
    }
}
