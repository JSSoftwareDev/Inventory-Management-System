package myapp.inventorysystem;


/**
 * RUNTIME ERROR: There was no validation for the machineId being non-negative, which could lead to incorrect data.
 * Resolution: This has been corrected by adding validation
 * to ensure that machineId is non-negative.
 * FUTURE ENHANCEMENT: Implement additional validation for machineId,
 * This class represents an in-house part in the inventory system.
 */
public class InHouse extends Part {

    private int machineId;

    /**
     * Constructor to create a new in-house part.
     *
     * @param id        The ID of the part.
     * @param name      The name of the part.
     * @param price     The price of the part.
     * @param stock     The stock quantity of the part.
     * @param min       The minimum stock level for the part.
     * @param max       The maximum stock level for the part.
     * @param machineId The ID of the machine used for in-house manufacturing.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        // Validate machineId to ensure it is non-negative
        if (machineId >= 0) {
            this.machineId = machineId;
        } else {
            // Default to 0 if an invalid machineId is provided
            this.machineId = 0;
        }
    }

    /**
     * Get the machine ID associated with the in-house part.
     *
     * @return The machine ID.
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Set the machine ID associated with the in-house part.
     *
     * @param machineId The machine ID.
     * FUTURE ENHANCEMENT: Implement additional validation for machineId,
     * considering constraints specific to the manufacturing process.
     */
    public void setMachineId(int machineId) {
        // FUTURE ENHANCEMENT: Consider additional validation for machineId
        // based on the requirements of the manufacturing process.
        this.machineId = machineId;
    }

    public void setMachineId(String machineIdText) {
    }
}
