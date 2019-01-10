package com.example.volansys.retrofitdemo;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//todo change here!
public class User {


        private String _id;

        private String email;

        private String name;

        private String password;

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getPassword ()
        {
            return password;
        }

        public void setPassword (String password)
        {
            this.password = password;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [_id = "+_id+", email = "+email+", name = "+name+", password = "+password+"]";
        }

}
