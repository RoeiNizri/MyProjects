package logic;

import enums.RegionEnum;
import enums.RoleEnum;
import enums.StatusEnum;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a User, saves all the data from the DB relevant to a
 * user in this class And its data is used in various controllers
 */
public class User implements Serializable {
	public User(String username, String password, RoleEnum role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	private int id;
    private String username;
    private String password;
    private boolean isLoggedIn;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private RoleEnum role;
    private RegionEnum region;
    private StatusEnum status;
 
	/**
     * Constructor
     * @param id
     * @param username
     * @param password
     * @param isLoggedIn
     * @param firstName
     * @param lastName
     * @param email
     * @param telephone
     * @param role
     */
    public User(int id, String username, String password, boolean isLoggedIn, String firstName, String lastName, String email, String telephone, RoleEnum role,StatusEnum status,RegionEnum region) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.status=status;
        this.region=region;
    }
    
	public User(int id, String firstName, String lastName, String email, String telephone, RegionEnum region) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephone = telephone;
		this.region = region;
	}
	
	
	
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public RegionEnum getRegion() {
		return region;
	}

	public void setRegion(RegionEnum region) {
		this.region = region;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "User [username=" + username + "]";
	}
    
    /**
     * Creates a new list of User with a given ResultSet
     * @param rs ResultSet
     * @return List of User
     */
    public static List<User> createUserListFromResultSet(ResultSet rs){
        List<User> users = new ArrayList<>();
        try{
            while(rs.next()){
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("is_logged_in") == 1,
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        RoleEnum.valueOf(rs.getString("role")),
                        StatusEnum.valueOf(rs.getString("status")),
                        RegionEnum.valueOf(rs.getString("region"))
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}