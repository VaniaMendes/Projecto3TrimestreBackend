package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents a supplier entity extending the properties of Interest.
 * This class includes details specific to suppliers, such as contact information.
 */
public class Supplier extends Interest{

    /**
     * Contact information for the supplier.
     */
    @XmlElement
    private String contact;

    /**
     * Default constructor for creating a Supplier instance without setting properties.
     */
    public Supplier() {
    }

    /**
     * Retrieves the contact information of the supplier.
     *
     * @return A string representing the contact information of the supplier.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact information for the supplier.
     *
     * @param contact A string containing the contact information to be set for the supplier.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }
}
