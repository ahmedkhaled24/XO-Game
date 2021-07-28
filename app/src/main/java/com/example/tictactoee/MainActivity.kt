package com.example.tictactoee

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("Users")
    var myEmail:String?=null
    var s:String?=null

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        var x = intent.extras!!

        s = x.getString("state")
        if (s=="1"){
            linearOne.visibility = View.GONE
        } else {
            myEmail = x.getString("email")
            Toast.makeText(this,myEmail,Toast.LENGTH_LONG).show()

            btn_req.setOnClickListener {
                var userDemail = edt.text.toString()
                myRef.child(SplitStrint(userDemail)).child("Request").push().setValue(myEmail)
                PlayerOnline(SplitStrint(myEmail!!) + SplitStrint(userDemail)) // 999999999999
                playerSymbol ="X" // 999999999999
            }


            btn_acc.setOnClickListener {
                var userDemail = edt.text.toString()
                myRef.child(SplitStrint(userDemail)).child("Request").push().setValue(myEmail)
                PlayerOnline(SplitStrint(userDemail) + SplitStrint(myEmail!!)) // 999999999999
                playerSymbol = "O" // 999999999999
            }
            incommingCalls()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var x = intent.extras!!
        s = x.getString("state")
        if (s=="2"){
            menuInflater.inflate(R.menu.menu,menu)
        } else{}

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.send_id -> linearOne.visibility = View.VISIBLE
            R.id.logOut_id -> {
                FirebaseAuth.getInstance().signOut()
                var intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("state","2")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    // اضغط مرتين للخروج
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            finish()
            var intent = Intent(this,SplashActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this,"Press back again to exit page",Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    fun buSelect(view: View) {
        val btnChoise = view as Button
        var cellID = 0
        when (btnChoise.id) {
            R.id.btn1 -> cellID = 1
            R.id.btn2 -> cellID = 2
            R.id.btn3 -> cellID = 3
            R.id.btn4 -> cellID = 4
            R.id.btn5 -> cellID = 5
            R.id.btn6 -> cellID = 6
            R.id.btn7 -> cellID = 7
            R.id.btn8 -> cellID = 8
            R.id.btn9 -> cellID = 9
        }

        try{
            if (s=="1"){
                AutoPlayOnline(cellID) //dooooooooo
            }
            else{
                myRef.child("PlayerOnline").child(sessionID!!).child(cellID.toString()).setValue(myEmail) // 999999999999
            }
        } catch (e:Exception){
            Toast.makeText(this,"Please send a request to your friend first",Toast.LENGTH_SHORT).show()
        }

//         PlayGame(cellID, btnChoise) //dooooooooo

    }


    var playerOne = ArrayList<Int>()
    var playerTwo = ArrayList<Int>()
    var playerActive = 1


    fun PlayGame(cellID: Int, btnChoise: Button) {
        try {
            if(playerActive==1){
                btnChoise.text = "X"
                btnChoise.setBackgroundResource(R.color.playerone)
                playerOne.add(cellID)
                playerActive = 2
                try{
                    if (s=="1"){
                        AutoPlay() //dooooooooo
                    }
                } catch (e:Exception){
                    Toast.makeText(this,"PlayGame",Toast.LENGTH_SHORT).show()
                }
//                AutoPlay()
            } else{
                btnChoise.text = "O"
                btnChoise.setBackgroundResource(R.color.playertwo)
                playerTwo.add(cellID)
                playerActive = 1
            }
            btnChoise.isEnabled = false
            checkWinner()

        }catch (e: Exception){
//            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
        }

    }

    fun checkWinner (){
        //row1
        if(playerOne.contains(1) && playerOne.contains(2) && playerOne.contains(3)){
            animationResult(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9)
        }
        if(playerTwo.contains(1) && playerTwo.contains(2) && playerTwo.contains(3)){
            animationResult(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9)
        }

        //row2
        if(playerOne.contains(4) && playerOne.contains(5) && playerOne.contains(6)){
            animationResult(btn4,btn5,btn6,btn1,btn2,btn3,btn7,btn8,btn9)
        }
        if(playerTwo.contains(4) && playerTwo.contains(5) && playerTwo.contains(6)){
            animationResult(btn4,btn5,btn6,btn1,btn2,btn3,btn7,btn8,btn9)
        }

        //row3
        if(playerOne.contains(7) && playerOne.contains(8) && playerOne.contains(9)){
            animationResult(btn7,btn8,btn9,btn1,btn2,btn3,btn4,btn5,btn6)
        }
        if(playerTwo.contains(7) && playerTwo.contains(8) && playerTwo.contains(9)){
            animationResult(btn7,btn8,btn9,btn1,btn2,btn3,btn4,btn5,btn6)
        }

        //col1
        if(playerOne.contains(1) && playerOne.contains(4) && playerOne.contains(7)){
            animationResult(btn1,btn4,btn7,btn2,btn3,btn5,btn6,btn8,btn9)
        }
        if(playerTwo.contains(1) && playerTwo.contains(4) && playerTwo.contains(7)){
            animationResult(btn1,btn4,btn7,btn2,btn3,btn5,btn6,btn8,btn9)
        }

        //col2
        if(playerOne.contains(2) && playerOne.contains(5) && playerOne.contains(8)){
            animationResult(btn2,btn5,btn8,btn1,btn3,btn4,btn6,btn7,btn9)
        }
        if(playerTwo.contains(2) && playerTwo.contains(5) && playerTwo.contains(8)){
            animationResult(btn2,btn5,btn8,btn1,btn3,btn4,btn6,btn7,btn9)
        }

        //col3
        if(playerOne.contains(3) && playerOne.contains(6) && playerOne.contains(9)){
            animationResult(btn3,btn6,btn9,btn1,btn2,btn4,btn5,btn7,btn8)
        }
        if(playerTwo.contains(3) && playerTwo.contains(6) && playerTwo.contains(9)){
            animationResult(btn3,btn6,btn9,btn1,btn2,btn4,btn5,btn7,btn8)
        }

        //X1
        if(playerOne.contains(1) && playerOne.contains(5) && playerOne.contains(9)){
            animationResult(btn1,btn5,btn9,btn2,btn3,btn4,btn6,btn7,btn8)
        }
        if(playerTwo.contains(1) && playerTwo.contains(5) && playerTwo.contains(9)){
            animationResult(btn1,btn5,btn9,btn2,btn3,btn4,btn6,btn7,btn8)
        }

        //X2
        if(playerOne.contains(3) && playerOne.contains(5) && playerOne.contains(7)){
            animationResult(btn3,btn5,btn7,btn1,btn2,btn4,btn6,btn8,btn9)
        }
        if(playerTwo.contains(3) && playerTwo.contains(5) && playerTwo.contains(7)){
            animationResult(btn3,btn5,btn7,btn1,btn2,btn4,btn6,btn8,btn9)
        }
    }

    fun animationResult(btn1Win:Button ,btn2Win:Button ,btn3Win:Button ,btn4Lose:Button ,
                        btn5Lose:Button ,btn6Lose:Button ,btn7Lose:Button ,btn8Lose:Button ,btn9Lose:Button){

        btnsDisenable()

        btn1Win.animate().alpha(0.7f).duration = 2000
        btn2Win.animate().alpha(0.7f).duration = 2000
        btn3Win.animate().alpha(0.7f).duration = 2000

        btn4Lose.animate().rotation(180f).duration = 2000
        btn5Lose.animate().rotation(180f).duration = 2000
        btn6Lose.animate().rotation(180f).duration = 2000
        btn7Lose.animate().rotation(180f).duration = 2000
        btn8Lose.animate().rotation(180f).duration = 2000
        btn9Lose.animate().rotation(180f).duration = 2000

        btn4Lose.setBackgroundResource(R.drawable.design_items)
        btn5Lose.setBackgroundResource(R.drawable.design_items)
        btn6Lose.setBackgroundResource(R.drawable.design_items)
        btn7Lose.setBackgroundResource(R.drawable.design_items)
        btn8Lose.setBackgroundResource(R.drawable.design_items)
        btn9Lose.setBackgroundResource(R.drawable.design_items)
    }



    fun btnReset(view: View) {

        implementBtnReset()
    }

    fun implementBtnReset(){
        playerOne.clear()
        playerTwo.clear()

        btnsEnable()

        btn1.animate().rotation(0f).duration = 2000
        btn2.animate().rotation(00f).duration = 2000
        btn3.animate().rotation(0f).duration = 2000
        btn4.animate().rotation(0f).duration = 2000
        btn5.animate().rotation(0f).duration = 2000
        btn6.animate().rotation(0f).duration = 2000
        btn7.animate().rotation(0f).duration = 2000
        btn8.animate().rotation(0f).duration = 2000
        btn9.animate().rotation(0f).duration = 2000

        btn1.animate().alpha(1f).duration = 2000
        btn2.animate().alpha(1f).duration = 2000
        btn3.animate().alpha(1f).duration = 2000
        btn4.animate().alpha(1f).duration = 2000
        btn5.animate().alpha(1f).duration = 2000
        btn6.animate().alpha(1f).duration = 2000
        btn7.animate().alpha(1f).duration = 2000
        btn8.animate().alpha(1f).duration = 2000
        btn9.animate().alpha(1f).duration = 2000

        btn1.text=""
        btn1.setBackgroundResource(R.drawable.design_items)

        btn2.text=""
        btn2.setBackgroundResource(R.drawable.design_items)

        btn3.text=""
        btn3.setBackgroundResource(R.drawable.design_items)

        btn4.text=""
        btn4.setBackgroundResource(R.drawable.design_items)

        btn5.text=""
        btn5.setBackgroundResource(R.drawable.design_items)

        btn6.text=""
        btn6.setBackgroundResource(R.drawable.design_items)

        btn7.text=""
        btn7.setBackgroundResource(R.drawable.design_items)

        btn8.text=""
        btn8.setBackgroundResource(R.drawable.design_items)

        btn9.text=""
        btn9.setBackgroundResource(R.drawable.design_items)
    }

    fun btnsEnable(){
        btn1.isEnabled = true
        btn2.isEnabled = true
        btn3.isEnabled = true
        btn4.isEnabled = true
        btn5.isEnabled = true
        btn6.isEnabled = true
        btn7.isEnabled = true
        btn8.isEnabled = true
        btn9.isEnabled = true
    }

    fun btnsDisenable(){
        btn1.isEnabled = false
        btn2.isEnabled = false
        btn3.isEnabled = false
        btn4.isEnabled = false
        btn5.isEnabled = false
        btn6.isEnabled = false
        btn7.isEnabled = false
        btn8.isEnabled = false
        btn9.isEnabled = false
    }

    fun AutoPlay(){
        //scan
        var emptyCell = ArrayList<Int>()
        for(cellID in 1..9){
            if (!(playerOne.contains(cellID) || playerTwo.contains(cellID))){
                emptyCell.add(cellID)
            }
        }

        //select rand
        val RundIndex = Random.nextInt(emptyCell.size-0)+0
        var c = emptyCell[RundIndex]

        //convert index to button
        var buselecttt:Button?
        when(c){
            1-> buselecttt=btn1
            2-> buselecttt=btn2
            3-> buselecttt=btn3
            4-> buselecttt=btn4
            5-> buselecttt=btn5
            6-> buselecttt=btn6
            7-> buselecttt=btn7
            8-> buselecttt=btn8
            9-> buselecttt=btn9
            else-> {
                buselecttt=btn1
            }
        }

        //play
        PlayGame(c,buselecttt)

    }

    fun AutoPlayOnline(c: Int){

        //convert index to button
        var buselecttt:Button?
        when(c){
            1-> buselecttt=btn1
            2-> buselecttt=btn2
            3-> buselecttt=btn3
            4-> buselecttt=btn4
            5-> buselecttt=btn5
            6-> buselecttt=btn6
            7-> buselecttt=btn7
            8-> buselecttt=btn8
            9-> buselecttt=btn9
            else-> {
                buselecttt=btn1
            }
        }

        //play
        PlayGame(c,buselecttt)
    }


    var num = 0

    fun incommingCalls(){
        myRef.child(SplitStrint(myEmail!!)).child("Request")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        val td = snapshot.value as HashMap<String,Any>
                        if (td!=null){
                            var value:String
                            for (key in td.keys){
                                value = td[key] as String
                                edt.setText(value)
                                val notifyme = Notifications()
                                notifyme.Notify(applicationContext, value + ": want to play tic tac toe", num)
                                num++
                                myRef.child(SplitStrint(myEmail!!)).child("Request").setValue(true)
                                break
                            }
                        }
                    }catch (e:Exception){

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


    var sessionID:String?=null
    var playerSymbol:String?=null


    fun PlayerOnline(sessionID:String){
        this.sessionID = sessionID
        myRef.child("PlayerOnline").removeValue()
        myRef.child("PlayerOnline").child(sessionID)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        playerOne.clear()
                        playerTwo.clear()
                        val td = snapshot.value as HashMap<String,Any>
                        if (td!=null){
                            var value:String
                            for (key in td.keys){
                                value = td[key] as String
                                Toast.makeText(this@MainActivity,"$td",Toast.LENGTH_SHORT).show()
                                if (value!=myEmail){

//                                    if (playerSymbol=="X"){
//                                        playerActive = 1
//                                    }
//                                    else{
//                                        playerActive = 2
//                                    }
                                    playerActive = if (playerSymbol==="X") 1 else 2
                                } else{
                                    playerActive = if (playerSymbol==="X") 2 else 1
                                }
                                AutoPlayOnline(key.toInt())
                            }
                        }
                    }catch (ex:Exception){
                        Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun SplitStrint(str:String):String{
        var split = str.split("@")
        return split[0]
    }


}
