import java.time.LocalDate;

public class EmployePojo {
	 
    public String firstName;
    public String lastName;
    private LocalDate startDate;
 
    public EmployePojo(String firstName, String lastName, LocalDate startDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.startDate = startDate;
    }
 
    public String name() {
        return this.firstName + " " + this.lastName;
    }
 
    public LocalDate getStart() {
        return this.startDate;
    }
}