package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

// Đánh dấu lớp này là 1 entity JPA
@Entity
// Chỉ định bảng trong csdl là "users"
@Table(name = "users")
public class User {

	// Đánh dấu thuộc tính id là khóa chính
	@Id
	// Sử dụng chiến lược tự động tăng
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Định nghĩa các thuộc tính của côt trong bảng
	// name: Tên của cột trong csdl
	// lenght : Chiều dài tối đa của cột
	// nullable: xác định cột này có chứa giá trị null hay không
	// unique: Xác định cột này có giá trị duy nhất không
	@Column(length = 128, nullable = false, unique = true)
	private String email;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;

	@Column(length = 64)
	private String photos;

	private boolean enabled;

	// @ManyToMany thiết lập mối quan hệ nhiều-nhiều
	@ManyToMany(fetch = FetchType.EAGER)
	// @JoinTable Định nghĩa bản trung gian "users_roles" để lưu các liên kết giữa users và roles
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	// Constructor
	public User() {

	}

	public User(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// Getter and Setter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", roles=" + roles + "]";
	}

	@Transient
	public String getPhotosImagePath() {
		if(id == null || photos == null) return "/images/default-user.png";
		return "/user-photos/" + this.id + "/" + this.photos;
	}

}
