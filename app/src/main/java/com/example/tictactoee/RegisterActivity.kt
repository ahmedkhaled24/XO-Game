package com.example.tictactoee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("Users")

    var state:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var x = intent.extras!!
        state = x.getString("state")
    }

    fun btnRegisterEvent(view: View) {

        if(name_id.text.isEmpty() && email_id.text.isEmpty() && password_id.text.isEmpty() && passwordConfirm_id.text.isEmpty()){
            Toast.makeText(this,getString(R.string.PleaseEnterYourInformation_text), Toast.LENGTH_SHORT).show()
            return
        }

        else if(name_id.text.isEmpty() || email_id.text.isEmpty() || password_id.text.isEmpty() || passwordConfirm_id.text.isEmpty()){
            Toast.makeText(this,getString(R.string.PleaseCompleteYourInformation_text), Toast.LENGTH_SHORT).show()
            return
        } else if (password_id.text.length <= 7){
            Toast.makeText(this,getString(R.string.PasswordsMustBeAtLeast8Digits_text), Toast.LENGTH_SHORT).show()
            return
        } else if (password_id.text.toString() != passwordConfirm_id.text.toString()){
            Toast.makeText(this,getString(R.string.ThePasswordConfirmationIsIncorrect_text), Toast.LENGTH_SHORT).show()
            return
        } else {

            var email = email_id.text.toString()
            var pass = password_id.text.toString()
            mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        progress_barr.visibility = View.VISIBLE
                        var intent = Intent(this,LoginActivity::class.java)
                        intent.putExtra("state",state)
                        startActivity(intent)
                        Toast.makeText(this,getString(R.string.Successful_text), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,getString(R.string.Error_text) ,Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,getString(R.string.FailedToCreateUser_text) + it.message,Toast.LENGTH_SHORT).show()
                }
        }

    }


}
