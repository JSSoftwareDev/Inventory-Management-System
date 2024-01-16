package myapp.inventorysystem;

/**
 * RunTime ERROR: Logical error where the stock parameter was not validated.
 * Resolution: The issue was resolved by adding the isValidStock method to validate the stock value
 * for the outsourced part, ensuring it is a non-negative value.
 * FUTURE ENHANCEMENT: Consider extending the validation in isValidStock to include more
 * specific business rules or constraints related to stock levels for outsourced parts.



 * This class represents an outsourced part in the inventory system.
 */
public class OutSourced extends Part {

    private String companyName;

    /**
     * Constructor to create a new outsourced part.
     *
     * @param id          The ID of the part.
     * @param name        The name of the part.
     * @param price       The price of the part.
     * @param stock       The stock quantity of the part.
     * @param min         The minimum stock level for the part.
     * @param max         The maximum stock level for the part.
     * @param companyName The name of the company that supplies the part.
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Get the name of the company that supplies the outsourced part.
     *
     * @return The name of the company.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set the name of the company that supplies the outsourced part.
     *
     * @param companyName The name of the company.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
