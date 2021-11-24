package be.azsa.tracable.model;

public class HistoryOrder {
    private long idHistory;
    private Order order;
    private be.azsa.aztaxi_v3.model.Car car;
    private Driver driver;

    //Constructeur vide
    public HistoryOrder(){}

    //Constructeur avec paramètres
    public HistoryOrder(Order order, be.azsa.aztaxi_v3.model.Car car, Driver driver) {
        this.order = order;
        this.car = car;
        this.driver = driver;
    }

    //Getters&Setters
    public long getIdHistory() { return idHistory; }
    public void setIdHistory(int idHistory) { this.idHistory = idHistory; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public be.azsa.aztaxi_v3.model.Car getCar() { return car; }
    public void setCar(be.azsa.aztaxi_v3.model.Car car) { this.car = car; }
    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    //ToString
    @Override
    public String toString() {
        return "{" +
                "idHistory=" + idHistory +
                ", order=" + order +
                ", car=" + car +
                ", driver=" + driver +
                '}';
    }
}
