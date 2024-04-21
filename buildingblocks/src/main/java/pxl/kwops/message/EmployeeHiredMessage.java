package pxl.kwops.message;

import pxl.kwops.domain.models.Message;

public class EmployeeHiredMessage extends Message {
    private String number;
    private String firstName;
    private String lastName;

    public EmployeeHiredMessage() {
    }

    public EmployeeHiredMessage(String number, String firstName, String lastName) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
