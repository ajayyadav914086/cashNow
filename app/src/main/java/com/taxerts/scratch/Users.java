package com.taxerts.scratch;

class Users {
    public long Cash;
    public String Country;
    public String Currency;
    public String Email;
    public long Invites;
    public String Name;
    public String PhotoURL;

    public Users(String name, String photoURL, String email, long invites) {
        this.Name = name;
        this.PhotoURL = photoURL;
        this.Email = email;
        this.Invites = invites;
    }

    public Users(String email, String name, String photoURL, String country, String currency, long cash, long invites) {
        this.Email = email;
        this.Name = name;
        this.PhotoURL = photoURL;
        this.Country = country;
        this.Currency = currency;
        this.Cash = cash;
        this.Invites = invites;
    }
}
