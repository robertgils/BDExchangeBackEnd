package backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
public class User {

    @Id @GeneratedValue
    private int id;

    @Email(message = "Email must be valid")
    @Column(unique=true)
    private String emailaddress;

    private String password;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String livingAddress;

    private boolean pickupHome;
    private boolean pickupWarehouse;
    private boolean payInAdvance;
    private boolean cashOnDelivery;

    public User() { }
    public User(String emailaddress, String password) {
        this(emailaddress, password, "", false, false, false, false);
    }
    public User(String emailaddress, String password, String livingAddress,
                boolean pickupHome, boolean pickupWarehouse, boolean payInAdvance, boolean cashOnDelivery) {
        this.emailaddress = emailaddress;
        this.password = password;
        this.livingAddress = livingAddress;
        this.pickupHome = pickupHome;
        this.pickupWarehouse = pickupWarehouse;
        this.payInAdvance = payInAdvance;
        this.cashOnDelivery = cashOnDelivery;
    }

    @Override
    public String toString() {
        return "Username = " + emailaddress +
                ", password =" + password;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getLivingAddress() { return livingAddress; }

    public boolean isPickupHome() { return pickupHome; }

    public boolean isPickupWarehouse() { return pickupWarehouse; }

    public boolean isPayInAdvance() { return payInAdvance; }

    public boolean isCashOnDelivery() { return cashOnDelivery; }
}
