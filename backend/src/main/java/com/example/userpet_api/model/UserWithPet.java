package com.example.userpet_api.model;

public class UserWithPet {
    private String id;
    private String gender;
    private String country;
    private String name;
    private String email;
    private Dob dob;
    private String phone;
    private String petImage;

    public static class Dob {
        private String date;
        private int age;

        public Dob() {}
        public Dob(String date, int age) {
            this.date = date;
            this.age = age;
        }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    public UserWithPet() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Dob getDob() { return dob; }
    public void setDob(Dob dob) { this.dob = dob; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPetImage() { return petImage; }
    public void setPetImage(String petImage) { this.petImage = petImage; }
}
