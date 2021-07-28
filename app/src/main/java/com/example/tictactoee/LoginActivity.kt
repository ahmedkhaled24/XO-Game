package com.example.tictactoee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth : FirebaseAuth? = null

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("Users")
    var state:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        var x = intent.extras!!
        state = x.getString("state")
    }


    fun signUp_idEvent(view: View) {
        var intent = Intent(this,RegisterActivity::class.java)
        intent.putExtra("state",state)
        startActivity(intent)
    }

    fun btnLoginEvent(view: View) {

        if(etEmail_id.text.isEmpty() && etPassword_id.text.isEmpty()){
            Toast.makeText(this,getString(R.string.Enter_your_email_and_password_text), Toast.LENGTH_SHORT).show()
            return
        }

        else if(etEmail_id.text.isEmpty() || etPassword_id.text.isEmpty()){
            Toast.makeText(this,getString(R.string.CompleteYourData_text), Toast.LENGTH_SHORT).show()
            return
        } else{
            LoginToFirebase(etEmail_id.text.toString(), etPassword_id.text.toString())
        }


    }


    fun LoginToFirebase(email:String, password:String){

        mAuth!!.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{

                if (it.isSuccessful){
                    progress_bar.visibility = View.VISIBLE
                    image_lock.visibility=View.GONE
                    image_lock_open.visibility=View.VISIBLE

                    var currentUser = mAuth!!.currentUser

                    // save in database realtime
                    if (currentUser!=null){
                        myRef.child(SplitStrint(currentUser.email.toString())).setValue(currentUser.uid)
                    }
                    LoadMain()
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,getString(R.string.Error_text) + it.message,Toast.LENGTH_SHORT).show()
            }
    }


    override fun onStart() {
        super.onStart()
        LoadMain()
    }


    fun LoadMain(){

        var currentUser = mAuth!!.currentUser
        if (currentUser!=null){
            var intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("email",currentUser.email)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("state",state)
            startActivity(intent)
            finish()
        }
    }


    fun SplitStrint(str:String):String{
        var split = str.split("@")
        return split[0]
    }



}
