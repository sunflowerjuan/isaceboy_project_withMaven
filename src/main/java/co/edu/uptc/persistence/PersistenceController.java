package co.edu.uptc.persistence;

public class PersistenceController {

    CustomerJpaController customerJpaController;

    public PersistenceController() {
        customerJpaController = new CustomerJpaController();
    }
}
