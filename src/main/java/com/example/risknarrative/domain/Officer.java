package com.example.risknarrative.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
@Table(name = "OFFICERS")
public class Officer {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @Column
    private String name;
    @Column
    private String officerRole;
    @Column
    private String appointedOn;
    @Column
    @JsonProperty(access = WRITE_ONLY)
    private String resignedOn;
    @JoinColumn()
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    public Officer() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficerRole() {
        return officerRole;
    }

    public void setOfficerRole(String officerRole) {
        this.officerRole = officerRole;
    }

    public String getAppointedOn() {
        return appointedOn;
    }

    public void setAppointedOn(String appointedOn) {
        this.appointedOn = appointedOn;
    }

    public String getResignedOn() {
        return resignedOn;
    }

    public void setResignedOn(String resignedOn) {
        this.resignedOn = resignedOn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Officer officer = (Officer) o;
        return Objects.equals(id, officer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Officer[" +
                "name=" + name + ", " +
                "officerRole=" + officerRole + ", " +
                "appointedOn=" + appointedOn + ", " +
                "resignedOn=" + resignedOn + ", " +
                "address=" + address + ']';
    }

}
