package datastorage;

/**
 * The DAOFactory class is responsible for creating instances of different Data Access Object (DAO) classes.
 * It follows the Singleton pattern to ensure only one instance of the factory is created.
 */
public class DAOFactory {

    private static DAOFactory instance;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private DAOFactory() {

    }

    /**
     * Returns the instance of the DAOFactory.
     * If the instance is not already created, it creates a new instance.
     *
     * @return The instance of DAOFactory.
     */
    public static DAOFactory getDAOFactory() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    /**
     * Creates and returns an instance of the TreatmentDAO.
     *
     * @return The TreatmentDAO instance.
     */
    public TreatmentDAO createTreatmentDAO() {
        return new TreatmentDAO(ConnectionBuilder.getConnection());
    }

    /**
     * Creates and returns an instance of the PatientDAO.
     *
     * @return The PatientDAO instance.
     */
    public PatientDAO createPatientDAO() {
        return new PatientDAO(ConnectionBuilder.getConnection());
    }

    /**
     * Creates and returns an instance of the CaregiverDAO.
     *
     * @return The CaregiverDAO instance.
     */
    public CaregiverDAO createCaregiverDAO() {
        return new CaregiverDAO(ConnectionBuilder.getConnection());
    }

    /**
     * Creates and returns an instance of the LoginDAO.
     *
     * @return The LoginDAO instance.
     */
    public LoginDAO createLoginDAO() {
        return new LoginDAO(ConnectionBuilder.getConnection());
    }
}