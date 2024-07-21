package logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import enums.RegionEnum;
/**
 * Represents a vending machine with a code, name, region, and city.
 */
public class Machine {
	private String machine_code;
	private String machine_name;
	private RegionEnum region;
	private String city;
	/**
	 * Constructs a new Machine object with the given code, name, region, and city.
	 * 
	 * @param machine_code the code of the machine
	 * @param machine_name the name of the machine
	 * @param region       the region of the machine
	 * @param city         the city of the machine
	 */
	public Machine(String machine_code, String machine_name, RegionEnum region, String city) {
		super();
		this.machine_code = machine_code;
		this.machine_name = machine_name;
		this.region = region;
		this.city = city;
	}
	/**
	 * Creates a list of Machine objects from a ResultSet of query results.
	 * 
	 * @param rs the ResultSet to create the list of machines from
	 * @return a list of Machine objects
	 */
    public static List<Machine> createMachineListFromResultSet(ResultSet rs){
        List<Machine> machines = new ArrayList<>();
        try{
            while(rs.next()){
                machines.add(new Machine(rs.getString("machine_code"),
                						rs.getString("machine_name"),
                						RegionEnum.valueOf(rs.getString("region")),
                						rs.getString("city")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return machines;
    }
    
	public String getMachine_code() {
		return machine_code;
	}


	public void setMachine_code(String machine_code) {
		this.machine_code = machine_code;
	}


	public String getMachine_name() {
		return machine_name;
	}


	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}


	public RegionEnum getRegion() {
		return region;
	}


	public void setRegion(RegionEnum region) {
		this.region = region;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}

}
